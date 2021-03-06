package com.hackerda.platform.controller;


import com.hackerda.platform.config.wechat.WechatMpConfiguration;
import com.hackerda.platform.domain.constant.ErrorCode;
import com.hackerda.platform.service.rbac.UserAuthorizeService;
import com.hackerda.spider.exception.PasswordUnCorrectException;
import com.hackerda.spider.exception.UrpVerifyCodeException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Slf4j
@Controller
public class UserBindingController {
    @Resource
    private HttpSession httpSession;
    @Resource
    private UserAuthorizeService userAuthorizeService;

    private static final int ACCOUNT_LENGTH = 10;
    private static final String ACCOUNT_PREFIX = "20";

    @RequestMapping(value = "/bind", method = RequestMethod.GET)
    public String loginHtml(@RequestParam(value = "openid", required = false) String openid,
                            @RequestParam(value = "appid", required = false) String appid) {
        httpSession.setAttribute("openid", openid);
        httpSession.setAttribute("appid", appid);
        return "LoginWeb/Login";
    }


    @RequestMapping(value = "/bind/wechat", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse loginHtmlPost(@RequestParam("account") String account,
                                     @RequestParam("password") String password,
                                     @RequestParam(value = "appid", required = false) String appid,
                                     @RequestParam(value = "openid", required = false) String openid
    ) {
        if (appid == null) {
            appid = (String) httpSession.getAttribute("appid");
        }
        if (openid == null) {
            openid = (String) httpSession.getAttribute("openid");
        }

        log.info("student bind start account:{}  appId:{} openid:{}", account, appid, openid);

        if (!isAccountValid(account)) {
            log.info("student getStudentInfo fail--invalid account:{}", account);
            return WebResponse.fail(ErrorCode.ACCOUNT_OR_PASSWORD_INVALID.getErrorCode(), "账号无效");
        }


        try {
            if (!StringUtils.isEmpty(openid)) {
                userAuthorizeService.studentAuthorize(account, password, appid, openid, "");
            } else {
                return WebResponse.fail(ErrorCode.ACCOUNT_OR_PASSWORD_INVALID.getErrorCode(), "账号或者密码错误");
            }
            httpSession.setAttribute("account", account);
        } catch (UrpVerifyCodeException e) {
            log.info("student bind fail verify code error account:{}  openid:{}", account, openid);
            return WebResponse.fail(ErrorCode.VERIFY_CODE_ERROR.getErrorCode(), "验证码错误");
        } catch (PasswordUnCorrectException e) {
            log.info("student bind fail Password not correct account:{}  openid:{}", account,  openid);
            return WebResponse.fail(ErrorCode.ACCOUNT_OR_PASSWORD_INVALID.getErrorCode(), "账号或者密码错误");
        }

        log.info("student bind success account:{} password:{}, appId:{} openid:{}", account, password, appid, openid);
        return WebResponse.success("success");
    }


    @RequestMapping(value = "/autoEvaluate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public WebResponse autoEvaluate(@RequestParam("account") String account,
                                    @RequestParam("password") String password,
                                    @RequestParam(value = "appid", required = false) String appid,
                                    @RequestParam(value = "openid", required = false) String openid
    ) {

        return null;
    }


    private boolean isAccountValid(String account) {
        return !Objects.isNull(account) && account.length() == ACCOUNT_LENGTH && account.startsWith(ACCOUNT_PREFIX);
    }

}
