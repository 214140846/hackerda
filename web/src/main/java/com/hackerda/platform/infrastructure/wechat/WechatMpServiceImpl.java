package com.hackerda.platform.infrastructure.wechat;

import com.hackerda.platform.config.wechat.WechatMpConfiguration;
import com.hackerda.platform.domain.wechat.WechatMpService;
import com.hackerda.platform.domain.wechat.WechatUser;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WechatMpServiceImpl implements WechatMpService {

    @Autowired
    private WechatMpConfiguration wechatMpConfiguration;


    @Override
    public WxMpUser getUserInfo(WechatUser wechatUser) {
        try {
            return wechatMpConfiguration.getMpService(wechatUser.getAppId()).getUserService().userInfo(wechatUser.getOpenId());
        } catch (WxErrorException e) {
            log.error("get wechat userinfo error", e);
        }

        return null;
    }
}
