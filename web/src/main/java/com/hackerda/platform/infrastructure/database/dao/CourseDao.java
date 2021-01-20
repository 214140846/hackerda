package com.hackerda.platform.infrastructure.database.dao;

import com.hackerda.platform.infrastructure.database.mapper.ext.CourseExtMapper;
import com.hackerda.platform.infrastructure.database.model.Course;
import com.hackerda.platform.infrastructure.database.model.example.CourseExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class CourseDao {
    @Resource
    private CourseExtMapper courseExtMapper;

    public List<Course> getAllCourse(){
        CourseExample example = new CourseExample();
        return courseExtMapper.selectByExample(example);
    }


    public List<Course> selectByCourseList(List<Course> courseList){
        if(CollectionUtils.isEmpty(courseList)){
            return Collections.emptyList();
        }
        return courseExtMapper.selectByCourseList(courseList);
    }

    public void insertSelective(Course course){
        try {
            courseExtMapper.insertSelective(course);
        }catch (Exception e){
            log.error("error data {}", course, e);
        }
    }

    public void insertBatch(List<Course> courseList){
        if(!CollectionUtils.isEmpty(courseList)) {
            courseExtMapper.insertBatch(courseList);
        }
    }
}
