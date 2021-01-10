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

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("beta")
@SpringBootTest
public class CourseTimetableQueryAppTest {

    @Autowired
    private CourseTimetableQueryApp courseTimetableQueryApp;


    @Test
    public void getByAccount() {

        CourseTimeTableOverview view = courseTimetableQueryApp.getByClassId("2020010012", new Term("2020-2021", 2));
        System.out.println(view);

    }
}