package com.hackerda.platform.service;

import com.hackerda.platform.controller.vo.CourseTimeTableVO;
import com.hackerda.platform.controller.vo.CourseTimetableOverviewVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CourseTimeTableServiceTest {

    @Autowired
    private CourseTimeTableService courseTimeTableService;

    @Test
    public void getCurrentTermCourseTimeTableByStudent() {

        CourseTimetableOverviewVO table = courseTimeTableService.getCurrentTermCourseTimeTableByStudent(2017026003);

        for (CourseTimeTableVO tableVo : table.getCourseTimetableVOList()) {
            System.out.println(tableVo);
        }

    }
}