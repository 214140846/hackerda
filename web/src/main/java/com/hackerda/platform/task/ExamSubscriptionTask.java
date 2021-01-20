package com.hackerda.platform.task;

import com.google.common.collect.Lists;
import com.hackerda.platform.builder.TemplateBuilder;
import com.hackerda.platform.config.wechat.WechatMpConfiguration;
import com.hackerda.platform.config.wechat.WechatMpPlusProperties;
import com.hackerda.platform.config.wechat.WechatTemplateProperties;
import com.hackerda.platform.domain.message.ExamRemindMessage;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.StudentSettingRepository;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.wechat.WechatMessageSender;
import com.hackerda.platform.domain.wechat.WechatTemplateMessage;
import com.hackerda.platform.infrastructure.database.dao.ExamTimetableDao;
import com.hackerda.platform.infrastructure.database.dao.StudentExamTimeTableDao;
import com.hackerda.platform.infrastructure.database.model.ScheduleTask;
import com.hackerda.platform.infrastructure.database.model.example.ExamTimetable;
import com.hackerda.platform.service.ExamTimeTableService;
import com.hackerda.platform.service.ScheduleTaskService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 1.通过定时任务的表把对应场景值中所有订阅状态可用的用户查询出来
 * 2.按照openid和appid属性去查找对应用户的考试时间
 * 3.根据具体的逻辑去处理上面的数据。
 * 通过schedule_task(openid, appid)的这两个属性 -> 在student表中找到学生对应的class_id  -> 然后用这个class_id
 * 去class_exam_timetable中找到所有关联的本学期的timetable_id -> 前面的timetable_id对应exam_timetable的主键id。
 *
 * @author FMC
 * @date 2019/6/22 12:36
 */

@Slf4j
@Service
public class ExamSubscriptionTask {

    @Value("scheduled.gradeUpdate")
    private String updateSwitch;
    @Autowired
    private ExamTimetableDao examTimetableDao;
    @Autowired
    private StudentExamTimeTableDao studentExamTimeTableDao;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private WechatMpPlusProperties wechatMpPlusProperties;
    @Autowired
    private WechatMessageSender wechatMessageSender;
    @Autowired
    private StudentSettingRepository studentSettingRepository;


    private final List<Integer> remindPeriodList = Lists.newArrayList(0, 1, 3, 7, 14, 30, 50);


    @Scheduled(cron = "0 0 9 ? * MON-FRI")      //这个cron表达式的意思是星期一到星期五的晚上8点执行一次
    void autoUpdateExam() {
        //执行前，检查定时任务的可用性
        if (isTaskEnable()){  return; }
        // 返回classes班级信息、examTimeTables明天的考试信息、scheduleTasks定时任务信息

        for (Integer integer : remindPeriodList) {
            Calendar now = getNow();
            now.add(Calendar.DAY_OF_MONTH, integer);

            Calendar end = getNow();
            end.add(Calendar.DAY_OF_MONTH, integer + 1);

            List<ExamTimetable> examTimetableList = examTimetableDao.getExamByTimePeriod(now.getTime(), end.getTime());

            for (ExamTimetable examTimetable : examTimetableList) {
                List<StudentAccount> accountList = studentExamTimeTableDao.selectStudentAccountByExamId(examTimetable.getId()).stream()
                        .map(StudentAccount::new)
                        .distinct()
                        .collect(Collectors.toList());

                List<WechatStudentUserBO> wechatStudentUserBOList =
                        studentRepository.getByAccountList(accountList).stream()
                                .filter(x-> x.hasBindApp(wechatMpPlusProperties.getAppId()))
                                .filter(x-> studentSettingRepository.get(x.getAccount()).isExamPushSwitch())
                                .collect(Collectors.toList());


                List<WechatTemplateMessage> messageList = wechatStudentUserBOList.stream()
                        .map(x -> new ExamRemindMessage(x, x.getWechatUser(wechatMpPlusProperties.getAppId()), examTimetable, integer))
                        .collect(Collectors.toList());


                wechatMessageSender.sendTemplateMessageAsync(messageList);
            }

        }


    }

    private Calendar getNow() {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.set(Calendar.HOUR, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);

        return now;

    }


    /**
     * 根据配置文件中的属性，判断该定时任务是否可用
     *
     * @return 可用结果
     */
    private boolean isTaskEnable() {
        return BooleanUtils.toBoolean(updateSwitch);
    }

}
