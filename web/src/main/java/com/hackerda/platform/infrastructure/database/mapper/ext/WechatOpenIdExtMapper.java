package com.hackerda.platform.infrastructure.database.mapper.ext;

import com.hackerda.platform.infrastructure.database.mapper.WechatOpenidMapper;
import com.hackerda.platform.infrastructure.database.model.WechatOpenid;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author JR Chan
 */
@Mapper
@Repository
public interface WechatOpenIdExtMapper extends WechatOpenidMapper {

    void insertBatch(List<WechatOpenid> wechatOpenidList);

    int saveOrUpdate(WechatOpenid wechatOpenid);
}
