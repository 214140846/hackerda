package com.hackerda.platform.infrastructure.community;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("prod")
@SpringBootTest
public class RecommendPostRedisRecorderScript {

    @Autowired
    private RecommendPostRedisRecorder recommendPostRedisRecorder;


    @Test
    public void add() {
        recommendPostRedisRecorder.add(95, new Date());
    }

}