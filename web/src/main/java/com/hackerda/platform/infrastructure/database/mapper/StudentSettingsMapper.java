package com.hackerda.platform.infrastructure.database.mapper;

import com.hackerda.platform.infrastructure.database.model.StudentSettings;
import com.hackerda.platform.infrastructure.database.model.StudentSettingsExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StudentSettingsMapper {
    long countByExample(StudentSettingsExample example);

    int deleteByExample(StudentSettingsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StudentSettings record);

    int insertSelective(StudentSettings record);

    List<StudentSettings> selectByExample(StudentSettingsExample example);

    StudentSettings selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StudentSettings record, @Param("example") StudentSettingsExample example);

    int updateByExample(@Param("record") StudentSettings record, @Param("example") StudentSettingsExample example);

    int updateByPrimaryKeySelective(StudentSettings record);

    int updateByPrimaryKey(StudentSettings record);
}