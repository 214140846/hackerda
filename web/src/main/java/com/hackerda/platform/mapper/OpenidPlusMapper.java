package com.hackerda.platform.mapper;

import java.util.List;

import com.hackerda.platform.pojo.example.OpenidExample;
import com.hackerda.platform.pojo.wechat.Openid;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OpenidPlusMapper {
    int countByExample(OpenidExample example);

    int deleteByExample(OpenidExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Openid record);

    int insertSelective(Openid record);

    List<Openid> selectByExample(OpenidExample example);

    Openid selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Openid record, @Param("example") OpenidExample example);

    int updateByExample(@Param("record") Openid record, @Param("example") OpenidExample example);

    int updateByPrimaryKeySelective(Openid record);

    int updateByPrimaryKey(Openid record);

    List<Openid> getOpenIdsByAccount(@Param("accounts") List<Integer> accounts);

    List<Openid> getOpenIdsByOpenIds(@Param("openids") List<String> openids);

    String isOpenidExist(String openid);

    int openidUnbind(String openid);

    int isOpenidBind(String openid);

    List<String> getAllOpenidsFromOneClass(@Param("classId") int classId, @Param("openid") String openid);
}