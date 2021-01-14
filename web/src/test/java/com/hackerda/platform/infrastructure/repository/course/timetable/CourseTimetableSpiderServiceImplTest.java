package com.hackerda.platform.infrastructure.repository.course.timetable;

import com.hackerda.platform.domain.course.timetable.CourseTimeTableOverview;
import com.hackerda.platform.domain.course.timetable.CourseTimetableBO;
import com.hackerda.platform.domain.time.Term;
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
@ActiveProfiles("test")
@SpringBootTest
public class CourseTimetableSpiderServiceImplTest {

    @Autowired
    private CourseTimetableSpiderServiceImpl courseTimetableSpiderService;


    @Test
    public void fetchByClassId() {


        CourseTimeTableOverview courseTimeTableOverview = courseTimetableSpiderService.fetchByClassId("2019100024", new Term(2020, 2021, 2));

        for (CourseTimetableBO bo : courseTimeTableOverview.getCourseTimetableBOList()) {
            System.out.println(bo);
        }

    }
}