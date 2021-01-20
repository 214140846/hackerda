package com.hackerda.platform.domain.course.timetable;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class CourseTimetableBOTest {

    @Test
    public void isHoldWeek() {

        CourseTimetableBO courseTimetableBO = new CourseTimetableBO();

        courseTimetableBO.setClassInSchoolWeek("11111111111111111100");

        assertThat(courseTimetableBO.isHoldWeek(1)).isTrue();
        assertThat(courseTimetableBO.isHoldWeek(20)).isFalse();
        assertThat(courseTimetableBO.isHoldWeek(-1)).isFalse();
    }
}