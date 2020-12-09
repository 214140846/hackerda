package com.hackerda.platform.application;

import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentInfoService;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.user.UserRepository;
import com.hackerda.platform.infrastructure.database.mapper.StudentUserMapper;
import com.hackerda.platform.infrastructure.database.model.StudentUser;
import com.hackerda.platform.infrastructure.database.model.example.StudentUserExample;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@Slf4j
@ActiveProfiles("prod")
@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentBindAppScript {


    @Autowired
    private StudentBindApp studentBindApp;
    @Autowired
    private StudentUserMapper studentUserMapper;
    @Autowired
    private StudentRepository userRepository;
    @Autowired
    private StudentInfoService studentInfoService;

    @Test
    public void studentUpdate() {

        StudentUserExample example = new StudentUserExample();
        example.createCriteria().andAccountGreaterThan(2020000000).andHasCheckEqualTo(false);

        List<StudentUser> studentUsers = studentUserMapper.selectByExample(example);

        for (StudentUser studentUser : studentUsers) {
            System.out.println(studentUser.getAccount());

            try {
                WechatStudentUserBO userBO = studentInfoService.getStudentInfo(new StudentAccount(studentUser.getAccount()), "1");
                userRepository.save(userBO);
            } catch (Exception e) {
                System.out.println(studentUser.getAccount() +": " + e.getMessage());
            }
        }

    }

    @Test
    public void studentUpdateTest() {

        StudentUserExample example = new StudentUserExample();
        example.createCriteria().andAccountEqualTo(2020024997);

        List<StudentUser> studentUsers = studentUserMapper.selectByExample(example);

        for (StudentUser studentUser : studentUsers) {
            WechatStudentUserBO userBO = studentInfoService.getStudentInfo(new StudentAccount(studentUser.getAccount()), "1");
            userRepository.save(userBO);
        }

    }
}