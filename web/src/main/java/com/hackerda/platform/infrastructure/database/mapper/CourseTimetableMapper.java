package com.hackerda.platform.infrastructure.database.mapper;

import com.hackerda.platform.infrastructure.database.model.CourseTimetable;
import com.hackerda.platform.infrastructure.database.model.example.CourseTimetableExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CourseTimetableMapper {
    int countByExample(CourseTimetableExample example);

    int deleteByExample(CourseTimetableExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CourseTimetable record);

    int insertSelective(CourseTimetable record);

    List<CourseTimetable> selectByExample(CourseTimetableExample example);

    CourseTimetable selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CourseTimetable record, @Param("example") CourseTimetableExample example);

    int updateByExample(@Param("record") CourseTimetable record, @Param("example") CourseTimetableExample example);

    int updateByPrimaryKeySelective(CourseTimetable record);

    int updateByPrimaryKey(CourseTimetable record);
}