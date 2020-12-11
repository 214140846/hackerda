package com.hackerda.platform.task;

import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.service.EvaluationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@ActiveProfiles("prod")
@RunWith(SpringRunner.class)
@SpringBootTest
public class EvaluateTaskTest {

    @Autowired
    private EvaluateTask evaluateTask;
    @Autowired
    private EvaluationService evaluationService;
    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void sendMessage() {

//        WechatStudentUserBO wetChatUser = studentRepository.findWetChatUser(new StudentAccount(2014025838));
//        evaluateTask.sendMessage(wetChatUser);
        evaluationService.push(new StudentAccount(2014025838));
        evaluateTask.start();
    }
}