package com.hackerda.platform.application;

import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentFactory;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.user.Gender;
import com.hackerda.platform.domain.wechat.UnionId;
import com.hackerda.platform.domain.wechat.WechatUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class CreateStudentAppTest {

    @Autowired
    private StudentFactory studentFactory;
    @Autowired
    private CreateStudentApp createStudentApp;
    @Autowired
    private UnionIdApp unionIdApp;
    @Autowired
    private StudentBindApp studentBindApp;


    @Test
    public void updateStudentInfo() {


        WechatStudentUserBO create = studentFactory.create(new StudentAccount(2020025838), "1", "test",
                Gender.Man, "2020060012");

        UnionId unionId = unionIdApp.getUnionId("test_unionId", new WechatUser("wx05f7264e83fa40e9", "openid"));

        studentBindApp.bindUnionId(create, unionId);

        System.out.println(create);

        createStudentApp.updateStudentInfo(create);

        System.out.println(create);


    }
}