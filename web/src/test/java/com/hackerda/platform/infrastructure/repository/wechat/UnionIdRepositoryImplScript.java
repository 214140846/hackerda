package com.hackerda.platform.infrastructure.repository.wechat;

import com.hackerda.platform.domain.wechat.UnionId;
import com.hackerda.platform.domain.wechat.UnionIdRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("prod")
public class UnionIdRepositoryImplScript {

    @Autowired
    private UnionIdRepository unionIdRepository;

    @Test
    public void findByUserName() {

        UnionId unionId = unionIdRepository.findByUserName("5648f33f-0c6b-4d26-9a75-5ba681293c33");
        System.out.println(unionId);
    }
}