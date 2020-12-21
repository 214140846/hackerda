package com.hackerda.platform.domain.exam;

import com.hackerda.platform.utils.Term;

import java.util.List;

public interface ExamRepository {

    List<ExamTimetableBO> findTimetable(String courseName, Term term);

}
