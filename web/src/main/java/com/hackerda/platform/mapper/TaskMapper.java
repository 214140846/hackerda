package com.hackerda.platform.mapper;

import com.hackerda.platform.pojo.wechat.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TaskMapper {
    String isTaskBinding(@Param("openid") String openid, @Param("updateType") int updateType);

    int taskBinding(Task task);

    int taskCountUpdate(@Param("openid") String openid, @Param("updateType") int updateType);
}
