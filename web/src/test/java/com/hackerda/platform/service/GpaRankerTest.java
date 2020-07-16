package com.hackerda.platform.service;

import com.hackerda.platform.dao.StudentUserDao;
import com.hackerda.platform.pojo.constant.RedisKeys;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class GpaRankerTest {


    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private StudentUserDao studentUserDao;


    @Test
    public void test(){

        ZSetOperations<String, String> zSet = redisTemplate.opsForZSet();

        Set<String> set = zSet.reverseRange(RedisKeys.GPA_RANK.genKey("2017", "会计学"), 0, 10);

        int x = 0;
        for (String s : set) {

            System.out.println(x++ + " " + studentUserDao.selectStudentByAccount(Integer.parseInt(s)).getName());
        }


    }

}