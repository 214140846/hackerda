package com.hackerda.platform.domain.grade;

import com.google.common.collect.Lists;
import com.hackerda.platform.application.GradeQueryApp;
import com.hackerda.platform.application.StudentBindApp;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.wechat.*;
import com.hackerda.platform.infrastructure.database.dao.UrpClassDao;
import com.hackerda.platform.infrastructure.database.model.UrpClass;
import com.hackerda.platform.task.GradeAutoUpdateScheduled;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

@Slf4j
@ActiveProfiles("prod")
@RunWith(SpringRunner.class)
@SpringBootTest
public class GradeQueryAppScript {

    @Autowired
    private GradeQueryApp gradeQueryApp;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentBindApp studentBindApp;
    @Autowired
    private WechatMessageSender wechatMessageSender;
    @Autowired
    private BlockingQueue<GradeFetchTask> gradeFetchQueue;
    @Autowired
    private GradeAutoUpdateScheduled gradeAutoUpdateScheduled;
    @Autowired
    private UnionIdRepository unionIdRepository;
    @Autowired
    private UrpClassDao urpClassDao;


    @Test
    public void test3() {


        List<String> prefixList = Lists.newArrayList("2018");

        for (String prefix : prefixList) {
            List<String> classNumList = urpClassDao.selectByNumPrefix(prefix).stream()
                    .filter(x-> x.getSubjectNum().equals("080601"))
                    .map(UrpClass::getClassNum).collect(Collectors.toList());
            for (String s : classNumList) {
                for (WechatStudentUserBO studentUserBO : studentRepository.findWetChatUser(Integer.parseInt(s))) {
                    System.out.println(studentUserBO.getAccount());
                    gradeQueryApp.getGradeOverview(studentUserBO);
                }
            }

        }

    }
}
