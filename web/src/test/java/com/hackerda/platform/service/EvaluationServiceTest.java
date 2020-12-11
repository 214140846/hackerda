package com.hackerda.platform.service;

import com.hackerda.platform.domain.constant.RedisKeys;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.utils.DateUtils;
import com.hackerda.platform.utils.Term;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Test
    public void test() {

        StudentUserBO studentUserBO = studentRepository.findWetChatUser(new StudentAccount(2017023115));

        System.out.println(studentUserBO.getEnablePassword());

//        evaluationService.evaluate(studentUserBO);

//        Term term = DateUtils.getCurrentSchoolTime().getTerm();
//        String key = RedisKeys.FINISH_EVALUATION_SET.genKey(term.asKey());
//        stringRedisTemplate.opsForSet().remove(key, new StudentAccount(2014025846).toString());
//
//        boolean b = evaluationService.hasFinish(new StudentAccount(2014025846));
//        System.out.println(b);

    }
}