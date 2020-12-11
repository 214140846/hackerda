package com.hackerda.platform.service.wechat.interceptor;

import com.hackerda.platform.application.UnionIdApp;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.wechat.UnionId;
import com.hackerda.platform.domain.wechat.UnionIdRepository;
import com.hackerda.platform.domain.wechat.WechatUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("prod")
@SpringBootTest
public class WechatOpenIdInterceptorTest {

    @Autowired
    private UnionIdApp unionIdApp;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UnionIdRepository unionIdRepository;

    @Test
    public void bind() {
        WechatUser wechatUser = new WechatUser("wx541fd36e6b400648", "oCxRO1EJX58bkqdZqwSX4acQEghE");
//        UnionId unionId = unionIdApp.getUnionId("oqxukuHEXe7sXxgGME38phN_jnlc", wechatUser);

//        System.out.println(unionId);

        UnionId id = unionIdRepository.find(wechatUser);

        System.out.println(id);
    }
}