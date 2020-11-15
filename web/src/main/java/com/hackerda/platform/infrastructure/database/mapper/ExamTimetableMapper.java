package com.hackerda.platform.infrastructure.database.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.hackerda.platform.infrastructure.database.model.example.ExamTimetable;
import com.hackerda.platform.infrastructure.database.model.example.ExamTimetableExample;

public interface ExamTimetableMapper {
    long countByExample(ExamTimetableExample example);

    int deleteByExample(ExamTimetableExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ExamTimetable record);

    int insertSelective(ExamTimetable record);

    List<ExamTimetable> selectByExample(ExamTimetableExample example);

    ExamTimetable selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ExamTimetable record, @Param("example") ExamTimetableExample example);

    int updateByExample(@Param("record") ExamTimetable record, @Param("example") ExamTimetableExample example);

    int updateByPrimaryKeySelective(ExamTimetable record);

    int updateByPrimaryKey(ExamTimetable record);
}