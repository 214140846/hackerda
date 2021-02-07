package com.hackerda.platform.infrastructure.repository;

import com.hackerda.platform.domain.grade.GradeBO;
import com.hackerda.platform.domain.grade.GradeRepository;
import com.hackerda.platform.domain.grade.TermGradeBO;
import com.hackerda.platform.domain.grade.TermGradeViewBO;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.infrastructure.database.mapper.GradeMapper;
import com.hackerda.platform.infrastructure.database.model.Grade;
import com.hackerda.platform.infrastructure.repository.grade.GradeAdapter;
import com.hackerda.platform.infrastructure.repository.grade.GradeSpiderFacade;
import com.hackerda.platform.infrastructure.repository.student.StudentRepositoryImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GradeRepositoryImplTest {

    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private GradeAdapter gradeAdapter;
    @Autowired
    private StudentRepository studentRepository;

    private List<GradeBO> currentGradeList;

    @Before
    public void init() {
        currentGradeList = getGradeList("/currentGrade");
    }

    @SneakyThrows
    private List<GradeBO> getGradeList(String fileName) {

        try (ObjectInputStream out = new ObjectInputStream(new FileInputStream(this.getClass().getResource(fileName).getPath()))) {
            //执行反序列化读取
            Grade[] obj = (Grade[]) out.readObject();
            //将数组转换成List
            return Arrays.stream(obj).map(x-> gradeAdapter.toBO(x)).collect(Collectors.toList());
        }
    }

    @Test
    public void testSave(){

        gradeRepository.save(currentGradeList);

        Integer account = currentGradeList.stream().findAny().get().getAccount();

        TermGradeViewBO all = gradeRepository.getAllByStudent(studentRepository.find(new StudentAccount(account)));

        assertThat(all.size()).isEqualTo(currentGradeList.size());

    }



}