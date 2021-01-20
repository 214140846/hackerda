package com.hackerda.platform.infrastructure.database.dao;

import com.hackerda.platform.domain.time.Term;
import com.hackerda.platform.infrastructure.database.mapper.StudentCourseTimeTableMapper;
import com.hackerda.platform.infrastructure.database.mapper.ext.ClassCourseTimeTableExtMapper;
import com.hackerda.platform.infrastructure.database.mapper.ext.CourseTimetableExtMapper;
import com.hackerda.platform.infrastructure.database.model.*;
import com.hackerda.platform.infrastructure.database.model.example.ClassCourseTimetableExample;
import com.hackerda.platform.infrastructure.database.model.example.CourseTimetableExample;
import com.hackerda.platform.infrastructure.database.model.example.StudentCourseTimeTableExample;
import com.hackerda.platform.utils.DateUtils;
import com.hackerda.platform.domain.time.SchoolTime;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CourseTimeTableDao {
    @Resource
    private CourseTimetableExtMapper courseTimetableExtMapper;
    @Autowired
    private StudentCourseTimeTableMapper studentCourseTimeTableMapper;
    @Autowired
    private ClassCourseTimeTableExtMapper classCourseTimeTableExtMapper;

    public List<CourseTimetableDetailDO> selectByTime (int order, int dayOfWeek, String termYear, int termOrder) {


        return courseTimetableExtMapper.selectDetailTime(order, dayOfWeek, termYear, termOrder);
    }

    public void insertBatch(List<CourseTimetable> list) {
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        courseTimetableExtMapper.insertBatch(list);
    }

    public List<CourseTimetable> selectBatchByKey(List<CourseTimetable> list){
        if(CollectionUtils.isEmpty(list)){
            return list;
        }
        return courseTimetableExtMapper.selectBatch(list);
    }

    public void insertBatchStudentRelative(List<StudentCourseTimeTable> relativeList){
        courseTimetableExtMapper.insertBatchStudentRelative(relativeList);
    }

    public List<CourseTimetableDetailDO> selectDetailByStudent(StudentCourseTimeTable relative){
        if(relative == null){
            return Collections.emptyList();
        }
        return courseTimetableExtMapper.selectDetailByStudentAccount(relative).stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<CourseTimetableDetailDO> selectDetailByClassId(ClassCourseTimetable relative){
        if(relative == null){
            return Collections.emptyList();
        }
        return courseTimetableExtMapper.selectDetailByClassId(relative);
    }


    public List<String> selectAccountById(Integer id){
        StudentCourseTimeTableExample example = new StudentCourseTimeTableExample();
        example.createCriteria().andCourseTimetableIdEqualTo(id);

        return studentCourseTimeTableMapper.selectByExample(example).stream()
                .map(StudentCourseTimeTable::getStudentId)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    public List<Integer> selectClassIdById(Integer id){
        ClassCourseTimetableExample example = new ClassCourseTimetableExample();
        example.createCriteria().andCourseTimetableIdEqualTo(id);

        return classCourseTimeTableExtMapper.selectByExample(example).stream()
                .map(ClassCourseTimetable::getClassId)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
