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
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRoleAppTest {

    @Autowired
    private UserRoleApp userRoleApp;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRegisterApp userRegisterApp;

    @Before
    public void init() {


    }

    @Test
    public void grantRole() {
        StudentAccount studentAccount = new StudentAccount("2014025838");
        PhoneNumber phoneNumber = new PhoneNumber("17301086276");
        WechatUser wechatUser = new WechatUser("test_appId", "test_openid");

        AppStudentUserBO appStudentUserBO = new AppStudentUserBO(studentAccount, "test2", "1", "test_avatarPath",
                phoneNumber, Gender.Woman,
                "test_introduction");

        userRegisterApp.register(appStudentUserBO, wechatUser);


        PermissionBO permissionBO = new PermissionBO("推荐", "recommend", 0);

        roleRepository.store(permissionBO);

        RoleBO roleBO = new RoleBO("超级管理员", "super_admin", 0);
        roleBO.grantPermission(permissionBO);

        roleRepository.store(roleBO);

        userRoleApp.grantRole(appStudentUserBO, appStudentUserBO, roleBO);

    }
}