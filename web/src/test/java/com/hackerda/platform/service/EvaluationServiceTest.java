package com.hackerda.platform.service;

import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.StudentUserBO;
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
public class EvaluationServiceTest {

    @Autowired
    private EvaluationService evaluationService;
    @Autowired
    private StudentRepository studentRepository;


    @Test
    public void test() {

        StudentUserBO studentUserBO = studentRepository.findWetChatUser(new StudentAccount(2017023115));

        System.out.println(studentUserBO.getEnablePassword());

        evaluationService.evaluate(studentUserBO);
    }
}