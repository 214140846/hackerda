package com.hackerda.platform.domain.grade;

import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.StudentUserBO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author JR Chan
 */
@Slf4j
@ActiveProfiles("prod")
@RunWith(SpringRunner.class)
@SpringBootTest
public class GradeOverviewFactoryScript {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private GradeOverviewFactory gradeOverviewFactory;

    @Test
    public void create() {

        StudentUserBO studentUserBO = studentRepository.find(new StudentAccount(2019026116));

        GradeOverviewBO gradeOverviewBO = gradeOverviewFactory.create(studentUserBO);

        System.out.println(gradeOverviewBO.getGpa());

    }
}