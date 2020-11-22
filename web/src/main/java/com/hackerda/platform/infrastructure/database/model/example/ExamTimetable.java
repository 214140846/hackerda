package com.hackerda.platform.infrastructure.database.model.example;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;
@Data
@Accessors(chain = true)
public class ExamTimetable {

    private Integer id;

    private String name;

    private String roomName;

    private String courseName;

    private String termOrder;

    private String termYear;

    private Date examDate;

    private Date startTime;

    private Date endTime;

    private String day;

    private String schoolWeek;

    private Date gmtCreate;

    private Date gmtModify;

}