package com.hackerda.platform.infrastructure.database.dao;


import com.hackerda.platform.infrastructure.database.mapper.ext.WechatOpenIdExtMapper;
import com.hackerda.platform.infrastructure.database.model.WechatOpenid;
import com.hackerda.platform.infrastructure.database.model.example.WechatOpenidExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WechatOpenIdDao {
    @Autowired
    private WechatOpenIdExtMapper wechatOpenIdExtMapper;


    public void insertSelective(WechatOpenid wechatOpenid){
        wechatOpenIdExtMapper.insertSelective(wechatOpenid);
    }

    public void updateByPrimaryKeySelective(WechatOpenid wechatOpenid){
        wechatOpenIdExtMapper.updateByPrimaryKeySelective(wechatOpenid);
    }

    public List<WechatOpenid> getOpenid(String openid) {
        WechatOpenidExample wechatOpenidExample = new WechatOpenidExample();
        wechatOpenidExample.createCriteria().andOpenidEqualTo(openid);
        return wechatOpenIdExtMapper.selectByExample(wechatOpenidExample);
    }


    public void openIdUnbind(String openid, String appid) {
        WechatOpenidExample example = new WechatOpenidExample();
        example.createCriteria()
                .andAppidEqualTo(appid)
                .andOpenidEqualTo(openid);

        wechatOpenIdExtMapper.updateByExampleSelective(new WechatOpenid().setIsBind(false), example);
    }

    public void saveOrUpdate(WechatOpenid wechatOpenid){
        wechatOpenIdExtMapper.saveOrUpdate(wechatOpenid);
    }
}
