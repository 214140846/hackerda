package com.hackerda.platform.domain.grade;

import com.hackerda.platform.application.GradeQueryApp;
import com.hackerda.platform.application.StudentBindApp;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.wechat.*;
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
@ActiveProfiles("test")
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


    @Test
    public void test() {
        StudentAccount account = new StudentAccount("2020026251");
        UnionId ofNew = UnionId.ofNew("test");
        WechatUser wechatUser = new WechatUser("wx541fd36e6b400648", "oCxRO1G9N755dOY5dwcT5l3IlS3Y");
        ofNew.bindOpenid(wechatUser);
        WechatStudentUserBO wechatStudentUserBO = studentBindApp.bindByUnionId(account, "1", ofNew, wechatUser);
        unionIdRepository.save(ofNew);


        StudentUserBO userBO = new StudentUserBO();
        userBO.setUrpClassNum(wechatStudentUserBO.getUrpClassNum());
        gradeFetchQueue.add(new GradeFetchTask(true, userBO));

//        gradeQueryApp.getGradeOverview(wechatStudentUserBO);
        gradeAutoUpdateScheduled.run();

//        System.out.println(gradeFetchQueue.size());

    }
}
