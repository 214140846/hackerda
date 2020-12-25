package com.hackerda.platform.controller.auth;

import com.hackerda.platform.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
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
public class StudentJWTRealmTest {

    @Autowired
    private StudentJWTRealm studentJWTRealm;

    @Test
    public void verifyToken() {
        String s = JwtUtils.signForWechatStudent("2014025838", "wx05f7264e83fa40e9", "oCxRO1G9N755dOY5dwcT5l3IlS3Y");
        AuthenticationInfo token = studentJWTRealm.verifyToken("2014025838", "wx05f7264e83fa40e9", s);
        System.out.println(token);
    }
}