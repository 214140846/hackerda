package com.hackerda.platform.domain.course.timetable;

import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.time.Term;

import java.util.List;

public interface CourseTimetableRepository {

    CourseTimeTableOverview getByAccount(StudentUserBO wechatStudentUserBO, Term term);

    CourseTimeTableOverview getByClassId(String classId, Term term);

    void saveByStudent(List<CourseTimetableBO> tableList, StudentUserBO wechatStudentUserBO);

    void saveByClass(List<CourseTimetableBO> tableList, String classId);
}
