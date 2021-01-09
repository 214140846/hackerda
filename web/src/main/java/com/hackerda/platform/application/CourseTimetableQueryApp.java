package com.hackerda.platform.application;

import com.hackerda.platform.domain.course.timetable.CourseTimeTableOverview;
import com.hackerda.platform.domain.course.timetable.CourseTimetableRepository;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.StudentUserBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseTimetableQueryApp {
    @Autowired
    private CourseTimetableRepository courseTimetableRepository;

    public CourseTimeTableOverview getByStudent(StudentUserBO studentUserBO, String termYear, int termOrder){

        CourseTimeTableOverview timeTableOverview = null;

        if(studentUserBO.isMsgHasCheck()) {
            timeTableOverview = courseTimetableRepository.getByAccount(studentUserBO, termYear, termOrder);
        }

        if(timeTableOverview == null ||timeTableOverview.isEmpty() || !timeTableOverview.isCurrentTerm()){
            timeTableOverview = courseTimetableRepository.getByClassId(studentUserBO.getUrpClassNum().toString(), termYear,
                    termOrder);
        }

        if(timeTableOverview.isCurrentTerm()) {
            if(timeTableOverview.isPersonal()){
                courseTimetableRepository.saveByStudent(timeTableOverview.getNewList(), studentUserBO);
            }else {
                courseTimetableRepository.saveByClass(timeTableOverview.getNewList(), studentUserBO.getUrpClassNum().toString());
            }
        }



        return timeTableOverview;
    }

    public CourseTimeTableOverview getByClassId(String classId, String termYear, int termOrder) {

        CourseTimeTableOverview timeTableOverview = courseTimetableRepository.getByClassId(classId,
                termYear, termOrder);

        courseTimetableRepository.saveByClass(timeTableOverview.getNewList(), classId);

        return timeTableOverview;
    }
}
