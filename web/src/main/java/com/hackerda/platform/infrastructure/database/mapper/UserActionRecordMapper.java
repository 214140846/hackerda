package com.hackerda.platform.infrastructure.database.mapper;

import com.hackerda.platform.infrastructure.database.model.UserActionRecord;
import com.hackerda.platform.infrastructure.database.model.UserActionRecordExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserActionRecordMapper {
    long countByExample(UserActionRecordExample example);

    int deleteByExample(UserActionRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserActionRecord record);

    int insertSelective(UserActionRecord record);

    List<UserActionRecord> selectByExample(UserActionRecordExample example);

    UserActionRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserActionRecord record, @Param("example") UserActionRecordExample example);

    int updateByExample(@Param("record") UserActionRecord record, @Param("example") UserActionRecordExample example);

    int updateByPrimaryKeySelective(UserActionRecord record);

    int updateByPrimaryKey(UserActionRecord record);
}