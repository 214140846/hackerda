package com.hackerda.platform.domain.course.timetable;

import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.repository.student.StudentUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseTimetableQueryService {

    @Autowired
    private StudentUserRepository studentUserRepository;
    @Autowired
    private CourseTimetableFactory courseTimetableFactory;
    @Autowired
    private CourseTimetableRepository courseTimetableRepository;

    public CourseTimeTableOverview getByAccount(int account){

        StudentUserBO studentUserBO = studentUserRepository.getByAccount(account);
        CourseTimeTableOverview current = courseTimetableFactory.createCurrent(studentUserBO);

        if(current.isEmpty()){
            courseTimetableRepository.getCurrentTermByClassId(studentUserBO);
        }

        List<CourseTimetableBO> newList = current.getNewList();
        courseTimetableRepository.saveByStudent(newList, studentUserBO);

        return current;
    }
}
