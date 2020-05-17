package com.hackerda.platform.spider.newmodel.searchclass;

import com.hackerda.platform.pojo.UrpCourse;
import lombok.AllArgsConstructor;

import java.util.Set;

@lombok.Data
@AllArgsConstructor
public class ClassCourseHour {
    public ClassCourseHour(){

    }
    private ClassInfoSearchResult classInfo;
    private int courseHourCount;
    private Set<UrpCourse> urpCourseSet;
}