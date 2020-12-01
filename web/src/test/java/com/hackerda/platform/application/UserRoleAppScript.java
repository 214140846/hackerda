package com.hackerda.platform.application;

import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.user.*;
import com.hackerda.platform.domain.wechat.WechatUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
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
public class UserRoleAppScript {

    @Autowired
    private UserRoleApp userRoleApp;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Before
    public void init() {


    }

    @Test
    public void grantRole() {
        RoleBO roleBO = roleRepository.findByCode("admin");

        AppStudentUserBO userBO = userRepository.findByStudentAccount(new StudentAccount(2014025838));

        userRoleApp.grantRole(userBO, userBO, roleBO);
    }
}