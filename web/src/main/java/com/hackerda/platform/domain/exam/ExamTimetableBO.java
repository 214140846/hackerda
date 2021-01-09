package com.hackerda.platform.domain.exam;

import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.time.Term;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ExamTimetableBO {

    private Integer id;

    private String name;

    private String roomName;

    private String courseName;

    private Date examDate;

    private Date startTime;

    private Date endTime;

    private String day;

    private String schoolWeek;

    private Term term;

    private List<StudentAccount> studentAccountList;

    public boolean isExpire() {
        return new Date().after(startTime);
    }
}
