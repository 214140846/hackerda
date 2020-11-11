package com.hackerda.platform.service;

import com.hackerda.platform.infrastructure.database.model.Exam;
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
@RunWith(SpringRunner.class)
@ActiveProfiles("prod")
@SpringBootTest
public class ExamTimeTableServiceTest {

    @Autowired
    private ExamTimeTableService examTimeTableService;

    @Test
    public void getExamTimeListFromSpider() {

        List<Exam> examList = examTimeTableService.getExamTimeListFromSpider(2017026003);


        for (Exam exam : examList) {
            System.out.println(exam);
        }

    }

}