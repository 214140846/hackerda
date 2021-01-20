package com.hackerda.platform.domain.course.timetable;

import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.time.Term;

import java.util.List;

public interface CourseTimetableRepository {

    CourseTimeTableOverview getByAccount(StudentUserBO wechatStudentUserBO, Term term);

    CourseTimeTableOverview getByClassId(String classId, Term term);

    CourseTimeTableOverview getByTime(int order, int datOfWeek, Term term);

    List<StudentAccount> getStudentById(int courseTimeTaleId);

    void saveByStudent(List<CourseTimetableBO> tableList, StudentUserBO wechatStudentUserBO);

    void saveByClass(List<CourseTimetableBO> tableList, String classId);
}
