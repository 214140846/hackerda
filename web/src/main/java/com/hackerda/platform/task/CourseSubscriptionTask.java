package com.hackerda.platform.task;

import com.hackerda.platform.config.wechat.WechatMpPlusProperties;
import com.hackerda.platform.domain.course.timetable.CourseTimeTableOverview;
import com.hackerda.platform.domain.course.timetable.CourseTimetableBO;
import com.hackerda.platform.domain.course.timetable.CourseTimetableRepository;
import com.hackerda.platform.domain.message.CourseTimetableRemindMessage;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.time.SchoolTime;
import com.hackerda.platform.domain.time.SchoolTimeManager;
import com.hackerda.platform.domain.wechat.WechatMessageSender;
import com.hackerda.platform.domain.wechat.WechatTemplateMessage;
import com.hackerda.platform.service.CourseSubscribeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yuki
 * @date 2018/11/6 20:36
 */
@Slf4j
@Service
public class CourseSubscriptionTask {

    @Value("${scheduled.sendCourse}")
    private String updateSwitch;
    @Autowired
    private CourseTimetableRepository courseTimetableRepository;
    @Autowired
    private SchoolTimeManager schoolTimeManager;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private WechatMpPlusProperties wechatMpPlusProperties;
    @Autowired
    private WechatMessageSender wechatMessageSender;


    @Scheduled(cron = "0 0 8 * * ?")
    //这个cron表达式的意思是星期一到星期五的早上8点执行一次
    void sendCourseRemindMsgForFirstSection() {
        execute(CourseSubscribeService.FIRST_SECTION);
    }

    @Scheduled(cron = "0 50 9 * * ?")
    //这个cron表达式的意思是星期一到星期五的早上9点50分执行一次
    void sendCourseRemindMsgForSecondSection() {
        execute(CourseSubscribeService.SECOND_SECTION);
    }

    @Scheduled(cron = "0 0 13 * * ?")
    //这个cron表达式的意思是星期一到星期五的下午13点执行一次
    void sendCourseRemindMsgForThirdSection() {
        execute(CourseSubscribeService.THIRD_SECTION);
    }

    @Scheduled(cron = "0 50 14 * * ?")
    //这个cron表达式的意思是星期一到星期五的下午14点50分执行一次
    void sendCourseRemindMsgForFourthSection() {
        execute(CourseSubscribeService.FOURTH_SECTION);
    }

    @Scheduled(cron = "0 0 18 * * ?")
    //这个cron表达式的意思是星期一到星期五的晚上6点执行一次
    void sendCourseRemindMsgForFifthSection() {
        execute(CourseSubscribeService.FIFTH_SECTION);
    }

    public void execute(int section) {
        log.info("send course message task switch {}", updateSwitch);

        if (!BooleanUtils.toBoolean(updateSwitch)) {
            return;
        }

        SchoolTime schoolTime = schoolTimeManager.getCurrentSchoolTime();

        CourseTimeTableOverview tableBySection = courseTimetableRepository.getByTime(section, schoolTime.getDay(), schoolTime.getTerm());

        if(tableBySection.isEmpty()) {
            return;
        }

        List<CourseTimetableBO> sendList = tableBySection.getCourseTimetableBOList().stream().filter(x -> x.isHoldWeek(schoolTime.getSchoolWeek())).collect(Collectors.toList());

        for (CourseTimetableBO courseTimetableBO : sendList) {
            List<StudentAccount> accountList = courseTimetableRepository.getStudentById(courseTimetableBO.getId());

            List<WechatStudentUserBO> userList = studentRepository.getByAccountList(accountList).stream()
                    .filter(x-> x.hasBindApp(wechatMpPlusProperties.getAppId()))
                    .collect(Collectors.toList());

            List<WechatTemplateMessage> messageList = userList.stream()
                    .map(x -> new CourseTimetableRemindMessage(x.getWechatUser(wechatMpPlusProperties.getAppId()), courseTimetableBO, x))
                    .collect(Collectors.toList());

            wechatMessageSender.sendTemplateMessageAsync(messageList);
        }


    }



}
