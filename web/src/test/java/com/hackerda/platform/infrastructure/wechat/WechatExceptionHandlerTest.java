package com.hackerda.platform.infrastructure.wechat;

import com.hackerda.platform.domain.wechat.UnionId;
import com.hackerda.platform.domain.wechat.UnionIdRepository;
import com.hackerda.platform.domain.wechat.WechatUser;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ActiveProfiles("test")
public class WechatExceptionHandlerTest {

    @Autowired
    private UnionIdRepository unionIdRepository;
    @Autowired
    private WechatExceptionHandler wechatExceptionHandler;

    @Test
    public void handle() {


        UnionId testUnionId = UnionId.ofNew("test_unionId");
        testUnionId.bindOpenid(new WechatUser("appId1", "openId1"));
        unionIdRepository.save(testUnionId);

        // test other
        WxError build = WxError.builder().errorCode(43005).build();
        wechatExceptionHandler.handle(new WxErrorException(build), new WechatUser("appId1", "openId1"));

        assertThat(unionIdRepository.find("test_unionId").getWechatUser("appId1").isSubscribe()).isTrue();

        //test subscribe
        WxError build1 = WxError.builder().errorCode(43004).build();
        wechatExceptionHandler.handle(new WxErrorException(build1), new WechatUser("appId1", "openId1"));

        assertThat(unionIdRepository.find("test_unionId").getWechatUser("appId1").isSubscribe()).isFalse();

    }
}