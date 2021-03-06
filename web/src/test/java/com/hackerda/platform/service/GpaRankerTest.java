package com.hackerda.platform.service;

import com.hackerda.platform.infrastructure.database.dao.StudentUserDao;
import com.hackerda.platform.domain.constant.RedisKeys;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

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
        String key = RedisKeys.GPA_RANK.genKey("2017", "土木工程");
        Set<String> set = zSet.reverseRange(key, 0, 10);

        int x = 1;
        for (String s : set) {

            System.out.println(x++ + " " + studentUserDao.selectStudentByAccount(Integer.parseInt(s)).getName() +" " + zSet.score(key, s ));
        }


    }

}