package com.hackerda.platform.infrastructure.database.dao;

import com.hackerda.platform.domain.message.ExamRemindMessage;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.wechat.WechatMessageSender;
import com.hackerda.platform.domain.wechat.WechatTemplateMessage;
import com.hackerda.platform.domain.wechat.WechatUser;
import com.hackerda.platform.infrastructure.database.model.example.ExamTimetable;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;


@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("beta")
@SpringBootTest
public class ExamTimetableDaoTest {

    @Autowired
    private ExamTimetableDao examTimetableDao;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private WechatMessageSender wechatMessageSender;

    @Test
    public void getExamByTimePeriod() {
        Date date = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);


        System.out.println(calendar.get(Calendar.DAY_OF_MONTH));

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2021, Calendar.MARCH, 3, 0, 0);

        System.out.println(calendar2.getTime());


        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(2021, Calendar.MARCH, 4,0, 0);

        StudentUserBO studentUserBO = studentRepository.find(new StudentAccount(2014025838));
        WechatUser wechatUser = new WechatUser("wx541fd36e6b400648", "oCxRO1G9N755dOY5dwcT5l3IlS3Y");

        for (ExamTimetable examTimetable : examTimetableDao.getExamByTimePeriod(calendar2.getTime(),
                calendar3.getTime())) {
            wechatMessageSender.sendTemplateMessage(new ExamRemindMessage(studentUserBO, wechatUser, examTimetable, 0));
        }


    }
}