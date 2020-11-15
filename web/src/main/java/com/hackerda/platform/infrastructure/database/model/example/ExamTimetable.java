package com.hackerda.platform.infrastructure.database.model.example;

import java.util.Date;

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

    private Integer day;

    private Integer schoolWeek;

    private Date gmtCreate;

    private Date gmtModify;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName == null ? null : roomName.trim();
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName == null ? null : courseName.trim();
    }

    public String getTermOrder() {
        return termOrder;
    }

    public void setTermOrder(String termOrder) {
        this.termOrder = termOrder == null ? null : termOrder.trim();
    }

    public String getTermYear() {
        return termYear;
    }

    public void setTermYear(String termYear) {
        this.termYear = termYear == null ? null : termYear.trim();
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getSchoolWeek() {
        return schoolWeek;
    }

    public void setSchoolWeek(Integer schoolWeek) {
        this.schoolWeek = schoolWeek;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }
}