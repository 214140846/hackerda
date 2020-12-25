package com.hackerda.platform.infrastructure.database.dao;

import com.hackerda.platform.infrastructure.database.mapper.ext.UrpClassExtMapper;
import com.hackerda.platform.infrastructure.database.model.UrpClass;
import com.hackerda.platform.infrastructure.database.model.example.UrpClassExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UrpClassDao {
    @Resource
    private UrpClassExtMapper urpClassExtMapper;

    public List<UrpClass> selectAllClass(){
        return urpClassExtMapper.selectByExample(new UrpClassExample());
    }

    public UrpClass selectByClassNumber(String classNumber){
        UrpClassExample example = new UrpClassExample();
        example.createCriteria()
                .andClassNumEqualTo(classNumber);

        return urpClassExtMapper.selectByExample(example).stream().findFirst().orElse(null);

    }


    public UrpClass selectByName(String name){
        UrpClassExample example = new UrpClassExample();
        example.createCriteria()
                .andClassNameEqualTo(name);

        return urpClassExtMapper.selectByExample(example).stream().findFirst().orElse(null);
    }

    public List<UrpClass> selectByNumPrefix(String prefix){
        UrpClassExample example = new UrpClassExample();
        example.createCriteria()
                .andClassNumLike(prefix+"%");

        return urpClassExtMapper.selectByExample(example);
    }

    public void insertSelective(UrpClass urpClass){
        urpClassExtMapper.insertSelective(urpClass);
    }

    public void insertBatch(List<UrpClass> list){
        urpClassExtMapper.insertBatch(list);
    }

}
