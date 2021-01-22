package com.hackerda.platform.infrastructure.wechat;


import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.hackerda.platform.config.wechat.MiniProgramProperties;
import com.hackerda.platform.domain.community.ContentSecurityCheckService;
import com.hackerda.platform.domain.constant.RedisKeys;
import com.hackerda.platform.domain.constant.SubscribeScene;
import com.hackerda.platform.domain.wechat.WechatAuthService;
import com.hackerda.platform.infrastructure.wechat.model.AccessTokenResponse;
import com.hackerda.platform.infrastructure.wechat.model.AuthResponse;
import com.hackerda.platform.infrastructure.wechat.model.Response;
import com.hackerda.platform.infrastructure.wechat.model.SubscribeMessage;
import com.hackerda.platform.utils.WXBizDataCrypt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class MiniProgramService implements WechatAuthService, ContentSecurityCheckService {
    @Autowired
    private MiniProgramProperties miniProgramProperties;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String root = "https://api.weixin.qq.com";
    private static final String authString = root + "/sns/jscode2session?appid=%s&secret=%s&js_code" +
            "=%s&grant_type=authorization_code";

    private static final String accessToken = root + "/cgi-bin/token?grant_type=client_credential&appid=%s&secret" +
            "=%s";

    private static final String SEND_SUBSCRIBE = root+ "/cgi-bin/message/subscribe/send?access_token=%s";

    private static final String msg_check = root + "/wxa/msg_sec_check?access_token=%s";

    private static final String img_check = root + "/wxa/img_sec_check?access_token=%s";

    public AuthResponse auth(String code) {

        String url = String.format(authString, miniProgramProperties.getAppId(), miniProgramProperties.getSecret(), code);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);

        return parseResponse(entity.getBody(), AuthResponse.class);

    }

    public void subscribe(String templateId, String openid){
        SubscribeScene scene = SubscribeScene.getByMiniProgramTemplateId(templateId);
        if(scene == null){
            log.warn("templateId {} scene is null", templateId);
            return;
        }

    }

    @Retryable(value = Exception.class)
    public String getAccessToken() {
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        if(isAccessTokenExpire()){
            String url = String.format(accessToken, miniProgramProperties.getAppId(), miniProgramProperties.getSecret());
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
            AccessTokenResponse response = parseResponse(entity.getBody(), AccessTokenResponse.class);
            if(response.getErrcode() == 0){
                opsForValue.set(RedisKeys.MINI_PROGRAM_ACCESS_TOKEN.getName(), response.getAccessToken(),
                        response.getExpiresIn(), TimeUnit.SECONDS);
                return response.getAccessToken();
            }else {
                log.error("get access token error {}", response);
                throw new RuntimeException();
            }

        }else {
            return opsForValue.get(RedisKeys.MINI_PROGRAM_ACCESS_TOKEN.getName());
        }

    }



    public void sendSubscribeMessage(SubscribeMessage<?> message) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
        headers.setContentType(mediaType);


        String url = String.format(SEND_SUBSCRIBE, getAccessToken());

        HttpEntity<String> request = new HttpEntity<>(JSON.toJSONString(message), headers);
        log.info("send mini app subscribe message data {}", message);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request , String.class);
            log.info("send mini app subscribe message success response {}", response.getBody());
        }catch (Exception e){
            log.error("send mini app subscribe message error", e);
        }


    }

    boolean isAccessTokenExpire(){

        return !BooleanUtils.toBoolean(stringRedisTemplate.hasKey(RedisKeys.MINI_PROGRAM_ACCESS_TOKEN.getName()));
    }

    <T extends Response> T parseResponse(String result, Class<T> clazz){
        Gson gson = new Gson();
        T t = gson.fromJson(result, clazz);
        if (t == null) {
            throw new RuntimeException("parse object error " + clazz.toString());
        }
        if (t.getErrcode() != 0) {
            log.error("request fail code:{} msg:{}  body:{}", t.getErrcode(), t.getErrMsg(), t);
        }

        return t;
    }

    @Override
    public String appCodeToOpenId(String code) {
        return auth(code).getOpenid();
    }

    @Override
    public AuthResponse authCode(String code) {
        return auth(code);
    }

    @Override
    public String getUserInfo(String code, String encryptedData, String iv) {
        AuthResponse auth = auth(code);
        WXBizDataCrypt biz = new WXBizDataCrypt(miniProgramProperties.getAppId(), auth.getSessionKey());

        return biz.decryptData(encryptedData, iv);
    }

    @Override
    public boolean isSecurityContent(String content) {

        if(StringUtils.isEmpty(content)) {
            return true;
        }
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> map = new HashMap<>();
        map.put("content", content);

        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
        headers.setContentType(mediaType);
        HttpEntity<String> request = new HttpEntity<>(JSON.toJSONString(map), headers);
        try {
            String url = String.format(msg_check, getAccessToken());
            ResponseEntity<String> entity = restTemplate.postForEntity(url, request, String.class);

            Response response = parseResponse(entity.getBody(), Response.class);

            if(response.getErrcode() == 0){
                return true;
            }
            else if(response.getErrcode() == 87014) {
                return false;
            }
            else {
                log.info("check msg security error response {}", response);
                return true;
            }
        } catch (Throwable throwable) {
            log.error("check msg security error", throwable);
            return true;
        }



    }

    @Override
    public boolean isSecurityImage(byte[] image) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        HttpEntity<byte[]> request = new HttpEntity<>(image, headers);

        try {
            String url = String.format(img_check, getAccessToken());
            ResponseEntity<String> entity = restTemplate.postForEntity(url, request, String.class);

            Response response = parseResponse(entity.getBody(), Response.class);

            if(response.getErrcode() == 0){
                return true;
            }
            else if(response.getErrcode() == 87014) {
                return false;
            }
            else {
                log.info("check security error response {}", response);
                return true;
            }
        } catch (Throwable throwable) {
            log.error("check security error", throwable);
            return true;
        }
    }
}
