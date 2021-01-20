package com.hackerda.platform.task;

import com.hackerda.platform.domain.course.CourseBO;
import com.hackerda.platform.domain.course.timetable.CourseTimetableBO;
import com.hackerda.platform.domain.message.CourseTimetableRemindMessage;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.wechat.WechatMessageSender;
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
public class CourseSubscriptionTaskTest {

    @Autowired
    private WechatMessageSender wechatMessageSender;
    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void execute() {
        CourseBO courseBO = new CourseBO();

        courseBO.setName("测试课程");
        courseBO.setCredit("3");
        courseBO.setNum("test_course_id");
        courseBO.setCourseOrder("01");
        courseBO.setExamType("");
        courseBO.setExamTypeCode("");
        courseBO.setTermOrder(2);
        courseBO.setTermYear("2020-2021");
        courseBO.setAcademyCode("");
        courseBO.setAcademyName("");
        courseBO.setTeacherName("");

        CourseTimetableBO courseTimetableBO1 = new CourseTimetableBO()
                .setCourseBO(courseBO)
                .setRoomName("测试教室")
                .setRoomNumber("test_number")
                .setCampusName("test_campus")
                .setContinuingSession(2)
                .setCourseId("test_course_id")
                .setAttendClassTeacher("")
                .setClassDay(1)
                .setClassOrder(1)
                .setStartWeek(1)
                .setEndWeek(20)
                .setClassInSchoolWeek("11111111111111111100")
                .setCourseSequenceNumber("01")
                .setWeekDescription("week_desc")
                .setClassDistinct(0)
                .setTermOrder(2)
                .setTermYear("2020-2021");
        StudentUserBO studentUserBO = studentRepository.find(new StudentAccount(2014025838));
        CourseTimetableRemindMessage message = new CourseTimetableRemindMessage(new WechatUser("wx541fd36e6b400648", "oCxRO1G9N755dOY5dwcT5l3IlS3Y"), courseTimetableBO1, studentUserBO);


        wechatMessageSender.sendTemplateMessage(message);


    }
}