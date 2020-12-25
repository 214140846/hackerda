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
    public void test() {
        StudentAccount account = new StudentAccount("2020026251");
        UnionId ofNew = UnionId.ofNew("test");
        WechatUser wechatUser = new WechatUser("wx541fd36e6b400648", "oCxRO1G9N755dOY5dwcT5l3IlS3Y");
        ofNew.bindOpenid(wechatUser);
        unionIdRepository.save(ofNew);

        WechatStudentUserBO wechatStudentUserBO = studentBindApp.bindByUnionId(account, "1", ofNew, wechatUser);

        StudentAccount account2 = new StudentAccount("2020026252");
        UnionId ofNew2 = UnionId.ofNew("test2");

        ofNew2.bindOpenid(wechatUser);
        unionIdRepository.save(ofNew2);
        WechatStudentUserBO wechatStudentUserBO2 = studentBindApp.bindByUnionId(account2, "1", ofNew2, wechatUser);

        StudentUserBO userBO = new StudentUserBO();
        userBO.setUrpClassNum(wechatStudentUserBO.getUrpClassNum());
        gradeQueryApp.getGradeOverview(wechatStudentUserBO2);
//        gradeFetchQueue.offer(new GradeFetchTask(true, userBO));

//        gradeQueryApp.getGradeOverview(wechatStudentUserBO);
        gradeAutoUpdateScheduled.run();

//        System.out.println(gradeFetchQueue.size());

    }

    @Test
    public void test2() throws InterruptedException {
        StudentAccount account = new StudentAccount("2020026251");
        UnionId ofNew = UnionId.ofNew("test");
        WechatUser wechatUser = new WechatUser("wx541fd36e6b400648", "oCxRO1G9N755dOY5dwcT5l3IlS3Y");
        ofNew.bindOpenid(wechatUser);
        unionIdRepository.save(ofNew);

        WechatStudentUserBO wechatStudentUserBO = studentBindApp.bindByUnionId(account, "1", ofNew, wechatUser);

        StudentAccount account2 = new StudentAccount("2020026252");
        UnionId ofNew2 = UnionId.ofNew("test2");

        ofNew2.bindOpenid(wechatUser);
        unionIdRepository.save(ofNew2);
        WechatStudentUserBO wechatStudentUserBO2 = studentBindApp.bindByUnionId(account2, "1", ofNew2, wechatUser);

        StudentUserBO userBO = new StudentUserBO();
        userBO.setUrpClassNum(wechatStudentUserBO.getUrpClassNum());
        gradeAutoUpdateScheduled.addClassTask(wechatStudentUserBO.getUrpClassNum().toString());
//        gradeQueryApp.getGradeOverview(wechatStudentUserBO2);
        Thread.sleep(1000L);
        gradeFetchQueue.offer(new GradeFetchTask(true, userBO));

//        gradeQueryApp.getGradeOverview(wechatStudentUserBO);
        gradeAutoUpdateScheduled.run();

//        System.out.println(gradeFetchQueue.size());


    }

    @Test
    public void test3() {


        List<String> prefixList = Lists.newArrayList("2017", "2018", "2019", "2020");

        for (String prefix : prefixList) {
            List<String> classNumList = urpClassDao.selectByNumPrefix(prefix).stream().map(UrpClass::getClassNum).collect(Collectors.toList());
            System.out.println(classNumList);
        }

    }
}
