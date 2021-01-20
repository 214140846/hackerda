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


    public CourseTimetable selectByPrimaryKey(Integer id) {
        return courseTimetableExtMapper.selectByPrimaryKey(id);
    }

    public List<CourseTimetable> selectByTime (int order, int datOfWeek, String termYear, int termOrder) {
        CourseTimetableExample example = new CourseTimetableExample();
        example.createCriteria().andClassOrderEqualTo(order)
                .andClassDayEqualTo(datOfWeek)
                .andTermYearEqualTo(termYear)
                .andTermOrderEqualTo(termOrder);

        return courseTimetableExtMapper.selectByExample(example);
    }

    public CourseTimetable selectUniqueCourse(CourseTimetable courseTimetable) {
        CourseTimetableExample example = new CourseTimetableExample();
        CourseTimetableExample.Criteria criteria = example.createCriteria();

        getUniqueExample(courseTimetable, example, criteria);

        return courseTimetableExtMapper.selectByExample(example).stream().findFirst().orElse(null);

    }

    private CourseTimetableExample getUniqueExample(CourseTimetable courseTimetable, CourseTimetableExample example, CourseTimetableExample.Criteria criteria) {

        if (courseTimetable.getCourseId() != null) {
            criteria.andCourseIdEqualTo(courseTimetable.getCourseId());
        }
        if (courseTimetable.getCourseSequenceNumber() != null) {
            criteria.andCourseSequenceNumberEqualTo(courseTimetable.getCourseSequenceNumber());
        }
        if (courseTimetable.getClassDay() != null) {
            criteria.andClassDayEqualTo(courseTimetable.getClassDay());
        }
        if (courseTimetable.getClassOrder() != null) {
            criteria.andClassOrderEqualTo(courseTimetable.getClassOrder());
        }
        if (courseTimetable.getTermYear() != null) {
            criteria.andTermYearEqualTo(courseTimetable.getTermYear());
        }
        if (courseTimetable.getTermOrder() != null) {
            criteria.andTermOrderEqualTo(courseTimetable.getTermOrder());
        }
        if (courseTimetable.getStartWeek() != null) {
            criteria.andStartWeekEqualTo(courseTimetable.getStartWeek());
        }
        if (courseTimetable.getEndWeek() != null) {
            criteria.andEndWeekEqualTo(courseTimetable.getEndWeek());
        }

        return example;
    }

    public List<CourseTimetable> selectByClassRelative(ClassCourseTimetable relative) {

        return courseTimetableExtMapper.selectByClassRelative(relative);
    }

    public List<CourseTimetable> selectByIdList(List<Integer> idList) {
        CourseTimetableExample example = new CourseTimetableExample();
        CourseTimetableExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(idList);

        return courseTimetableExtMapper.selectByExample(example);
    }

    public void insertSelective(CourseTimetable courseTimetable) {
        courseTimetableExtMapper.insertSelective(courseTimetable);
    }

    public void insertBatch(List<CourseTimetable> list) {
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        courseTimetableExtMapper.insertBatch(list);
    }

    public void updateByPrimaryKeySelective(CourseTimetable courseTimetable) {
        courseTimetableExtMapper.updateByPrimaryKeySelective(courseTimetable);
    }

    public void updateByUniqueKey(CourseTimetable courseTimetable) {
        CourseTimetableExample example = new CourseTimetableExample();
        CourseTimetableExample.Criteria criteria = example.createCriteria();
        getUniqueExample(courseTimetable, example, criteria);

        courseTimetableExtMapper.updateByExampleSelective(courseTimetable, example);
    }

    public List<CourseTimetable> selectBatchByKey(List<CourseTimetable> list){
        if(CollectionUtils.isEmpty(list)){
            return list;
        }
        return courseTimetableExtMapper.selectBatch(list);
    }

    public List<CourseTimetable> getCurrentTermTableByAccount(Integer account){
        SchoolTime schoolTime = DateUtils.getCurrentSchoolTime();

        StudentCourseTimeTable table = new StudentCourseTimeTable()
                .setStudentId(account)
                .setTermOrder(schoolTime.getTerm().getOrder())
                .setTermYear(schoolTime.getTerm().getTermYear());
        return courseTimetableExtMapper.selectByStudentRelative(table);
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
