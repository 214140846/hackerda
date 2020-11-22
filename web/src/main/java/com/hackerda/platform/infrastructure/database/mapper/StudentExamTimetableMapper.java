package com.hackerda.platform.infrastructure.database.mapper;

import com.hackerda.platform.infrastructure.database.model.StudentExamTimetable;
import com.hackerda.platform.infrastructure.database.model.StudentExamTimetableExample;
import com.hackerda.platform.infrastructure.database.model.example.ExamTimetable;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
@Mapper
@Repository
public interface StudentExamTimetableMapper {
    long countByExample(StudentExamTimetableExample example);

    int deleteByExample(StudentExamTimetableExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StudentExamTimetable record);

    int insertSelective(StudentExamTimetable record);

    List<StudentExamTimetable> selectByExample(StudentExamTimetableExample example);

    StudentExamTimetable selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StudentExamTimetable record, @Param("example") StudentExamTimetableExample example);

    int updateByExample(@Param("record") StudentExamTimetable record, @Param("example") StudentExamTimetableExample example);

    int updateByPrimaryKeySelective(StudentExamTimetable record);

    int updateByPrimaryKey(StudentExamTimetable record);
    
    int batchInsert(@Param("list") List<StudentExamTimetable> list);
}