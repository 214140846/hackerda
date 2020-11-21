package com.hackerda.platform.infrastructure.database.mapper;

import com.hackerda.platform.infrastructure.database.model.example.ExamTimetable;
import com.hackerda.platform.infrastructure.database.model.example.ExamTimetableExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
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
    
    int batchInsert(@Param("list") List<ExamTimetable> list);
}