package com.hackerda.platform.config.wechat;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@Slf4j
@ActiveProfiles("prod")
@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatMpConfigurationTest {

    @Autowired
    private WechatMpConfiguration wechatMpConfiguration;
    @Autowired
    private WxMpService wxPlusService;

    @Test
    public void setPlusMenu() throws WxErrorException {
//        wechatMpConfiguration.setPlusMenu();

        WxMpUser wxMpUser = wxPlusService.getUserService().userInfo("oCxRO1ECkRD-kxNY0ee07piPznPM");

        String nickname = wxMpUser.getNickname();
        System.out.println(nickname);


        String headImgUrl = wxMpUser.getHeadImgUrl();

        System.out.println(headImgUrl);
    }
}