package com.hackerda.platform.task;

import lombok.extern.slf4j.Slf4j;
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
public class GradeAutoUpdateScheduledTest {

    @Autowired
    private GradeAutoUpdateScheduled gradeAutoUpdateScheduled;

    @Test
    public void simulation() {

        gradeAutoUpdateScheduled.simulation();
        gradeAutoUpdateScheduled.run();
    }
}