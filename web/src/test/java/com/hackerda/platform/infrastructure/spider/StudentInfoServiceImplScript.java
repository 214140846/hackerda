package com.hackerda.platform.infrastructure.spider;

import com.google.common.io.CharSource;
import com.google.common.io.Files;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.infrastructure.database.model.Grade;
import com.hackerda.platform.infrastructure.database.model.UrpClass;
import com.hackerda.platform.infrastructure.repository.grade.GradeSpiderFacade;
import com.hackerda.platform.infrastructure.repository.student.StudentRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class StudentInfoServiceImplScript {


    @Autowired
    private StudentRepositoryImpl studentUserRepository;
    @Autowired
    private GradeSpiderFacade gradeSpiderFacade;


    @Test
    public void saveGrade() {
        StudentUserBO studentUserBO = studentUserRepository.find(new StudentAccount(2017025838));

        List<Grade> currentTermGrade = gradeSpiderFacade.getSchemeGrade(studentUserBO);
        File currentGrade = new File("everGrade");

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(currentGrade)))
        {
            //将List转换成数组
            Grade[] grades = currentTermGrade.toArray(new Grade[0]);

            //执行序列化存储
            out.writeObject(grades);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        try (ObjectInputStream out = new ObjectInputStream(new FileInputStream(currentGrade)))
        {
            //执行反序列化读取
            Grade[] obj = (Grade[]) out.readObject();
            //将数组转换成List
            List<Grade> listObject = Arrays.asList(obj);

            System.out.println(listObject);
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }



    }
}