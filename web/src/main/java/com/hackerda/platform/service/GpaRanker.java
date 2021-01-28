package com.hackerda.platform.service;

import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.constant.RedisKeys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GpaRanker {

    private final StringRedisTemplate stringRedisTemplate;

    private final String profiles;


    public GpaRanker(StringRedisTemplate stringRedisTemplate, @Value("${spring.profiles.active}") String profiles) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.profiles = profiles;
    }

    public RankResult rank(StudentUserBO studentUser, double gpa){
        ZSetOperations<String, String> zSet = stringRedisTemplate.opsForZSet();
        zSet.add(getKey(studentUser), studentUser.getAccount().toString(), gpa);

        return getRank(studentUser);
    }


    public RankResult getRank(StudentUserBO studentUser){
        ZSetOperations<String, String> zSet = stringRedisTemplate.opsForZSet();
        String key = getKey(studentUser);
        Long rank = zSet.reverseRank(key, studentUser.getAccount().toString());
        Long size = zSet.size(key);

        return new RankResult(size, rank);
    }

    private String getKey(StudentUserBO studentUser){
        return RedisKeys.GPA_RANK.genKey(profiles, studentUser.getGrade(), studentUser.getSubjectName());
    }


    public static class RankResult{

        private final int size;
        private final int rank;

        private RankResult(Long size, Long rank){
            this.size = Optional.ofNullable(size).orElse(0L).intValue();
            this.rank = Optional.ofNullable(rank).orElse(0L).intValue() + 1;
        }

        public int getSize() {
            return size;
        }

        public int getRank() {
            return rank;
        }
    }
}
