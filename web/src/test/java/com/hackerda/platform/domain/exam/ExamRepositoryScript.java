package com.hackerda.platform.domain.exam;

import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.wechat.UnionId;
import com.hackerda.platform.domain.wechat.UnionIdRepository;
import com.hackerda.platform.domain.wechat.WechatMessageSender;
import com.hackerda.platform.infrastructure.repository.exam.ExamRepositoryImpl;
import com.hackerda.platform.domain.time.Term;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@ActiveProfiles("prod")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExamRepositoryScript {

    @Autowired
    private ExamRepositoryImpl examRepository;
    @Autowired
    private WechatMessageSender wechatMessageSender;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UnionIdRepository unionIdRepository;

    @Test
    public void findTimetable() {

        for (ExamTimetableBO timetableBO : examRepository.findTimetable("高等数学I(上)", new Term("2020-2021-1"))) {

            for (StudentAccount studentAccount : timetableBO.getStudentAccountList()) {
                UnionId unionId = unionIdRepository.find(studentAccount);
                if (unionId.hasApp("wx541fd36e6b400648")) {
                    ExamRemindMessage examRemindMessage = new ExamRemindMessage(unionId.getWechatUser("wx541fd36e6b400648"), timetableBO, "https" +
                            "://mubu" +
                            ".com/doc/3k7SdHrcW81");

//                    wechatMessageSender.sendTemplateMessage(examRemindMessage);
                    System.out.println(examRemindMessage);
                }
            }
        }

    }
}