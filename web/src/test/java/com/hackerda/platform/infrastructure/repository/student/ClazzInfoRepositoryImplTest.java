package com.hackerda.platform.infrastructure.repository.student;

import com.hackerda.platform.domain.student.ClazzInfoBO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * @author JR Chan
 */
@Slf4j
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ClazzInfoRepositoryImplTest {

    @Autowired
    private ClazzInfoRepositoryImpl clazzInfoRepository;

    @Test
    public void findClassByNum() {

        ClazzInfoBO classByNum = clazzInfoRepository.findClassByNum("2016010011");
        assertThat(classByNum).isNotNull();

        System.out.println(classByNum);

        ClazzInfoBO nullAble = clazzInfoRepository.findClassByNum("");
        assertThat(nullAble).isNull();
    }
}