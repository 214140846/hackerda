package com.hackerda.platform.application;

import com.hackerda.platform.domain.wechat.WechatUser;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
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
public class UnionIdAppScript {

    @Autowired
    private WxMpService wxPlusService;
    @Autowired
    private UnionIdApp unionIdApp;

    @Test
    public void saveUnionId() throws WxErrorException {

        WxMpUserList wxMpUserList = wxPlusService.getUserService().userList(null);

        for (String openid : wxMpUserList.getOpenids()) {
            unionIdApp.saveUnionId(new WechatUser("wx541fd36e6b400648", openid));
        }

    }
}