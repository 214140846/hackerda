package com.hackerda.platform.domain.course.timetable;

import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.time.Term;

public interface CourseTimetableSpiderService {

    CourseTimeTableOverview fetchByClassId(String classId, Term term);

    CourseTimeTableOverview fetchByStudent(StudentUserBO student);

}
