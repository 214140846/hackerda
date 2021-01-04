package com.hackerda.platform.task;

import com.hackerda.platform.application.StudentBindApp;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.wechat.UnionId;
import com.hackerda.platform.domain.wechat.UnionIdRepository;
import com.hackerda.platform.domain.wechat.WechatUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@Slf4j
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class GradeAutoUpdateScheduledTest {

    @Autowired
    private GradeAutoUpdateScheduled gradeAutoUpdateScheduled;
    @Autowired
    private UnionIdRepository unionIdRepository;
    @Autowired
    private StudentBindApp studentBindApp;

    @Test
    public void simulation() {

//        gradeAutoUpdateScheduled.simulation();

        StudentAccount account = new StudentAccount("2020026251");
        UnionId ofNew = UnionId.ofNew("test");
        WechatUser wechatUser = new WechatUser("wx541fd36e6b400648", "oCxRO1G9N755dOY5dwcT5l3IlS3Y");
        ofNew.bindOpenid(wechatUser);
        unionIdRepository.save(ofNew);

        WechatStudentUserBO wechatStudentUserBO = studentBindApp.bindByUnionId(account, "1", ofNew, wechatUser);

        GradeAutoUpdateScheduled.ClassFetchTask classFetchTask =
                gradeAutoUpdateScheduled.new ClassFetchTask(wechatStudentUserBO.getUrpClassNum());

        classFetchTask.run();

        gradeAutoUpdateScheduled.run();

    }
}