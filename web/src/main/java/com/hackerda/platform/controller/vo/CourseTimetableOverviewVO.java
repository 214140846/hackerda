package com.hackerda.platform.controller.vo;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class CourseTimetableOverviewVO {

    private List<CourseTimeTableVO> courseTimetableVOList = Collections.emptyList();

    private List<Integer> termShowList = Collections.emptyList();;

    private List<Integer> termRestList = Collections.emptyList();

    private TermVO term;

    private String errorMsg;

    private int errorCode;
}
