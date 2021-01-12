package com.hackerda.platform.domain.course.timetable;

import com.hackerda.platform.application.CourseTimetableQueryApp;
import com.hackerda.platform.domain.time.Term;
import com.hackerda.platform.infrastructure.database.mapper.ext.TruncateMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class CourseTimetableQueryAppTest {

    @Autowired
    private CourseTimetableQueryApp courseTimetableQueryApp;


    @Test
    public void getByAccount() {



        CourseTimeTableOverview view1 = courseTimetableQueryApp.getByClassId("2020010012", new Term("2020-2021",
                2));


        for (Integer bo : view1.getTermShowList()) {
            System.out.println(view1.getCourseTimetableBOList().get(bo));
        }

        System.out.println("-----------------------");
        for (Integer bo : view1.getTermRestList()) {
            System.out.println(view1.getCourseTimetableBOList().get(bo));
        }


    }
}