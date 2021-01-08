package com.hackerda.platform.infrastructure.database.mapper;

import com.hackerda.platform.infrastructure.database.model.AccountWechatUnionId;
import com.hackerda.platform.infrastructure.database.model.WechatUnionId;
import com.hackerda.platform.infrastructure.database.model.WechatUnionIdExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface WechatUnionIdMapper {
    long countByExample(WechatUnionIdExample example);

    int deleteByExample(WechatUnionIdExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WechatUnionId record);

    int insertSelective(WechatUnionId record);

    List<WechatUnionId> selectByExample(WechatUnionIdExample example);

    WechatUnionId selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WechatUnionId record, @Param("example") WechatUnionIdExample example);

    int updateByExample(@Param("record") WechatUnionId record, @Param("example") WechatUnionIdExample example);

    int updateByPrimaryKeySelective(WechatUnionId record);

    int updateByPrimaryKey(WechatUnionId record);

    List<WechatUnionId> selectByAccount(Integer account);

    List<AccountWechatUnionId> selectByAccountList(List<Integer> accountList);

    List<WechatUnionId> selectByUserName(String userName);
}