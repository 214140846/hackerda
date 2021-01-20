package com.hackerda.platform.task;

import com.google.common.collect.Lists;
import com.hackerda.platform.MDCThreadPool;
import com.hackerda.platform.application.GradeQueryApp;
import com.hackerda.platform.domain.constant.ErrorCode;
import com.hackerda.platform.domain.grade.GradeFetchTask;
import com.hackerda.platform.domain.grade.GradeOverviewBO;
import com.hackerda.platform.domain.grade.GradeUpdateMessage;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.StudentSettingRepository;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.wechat.WechatMessageSender;
import com.hackerda.platform.domain.wechat.WechatTemplateMessage;
import com.hackerda.platform.infrastructure.AntiDuplicateLinkedBlockingQueue;
import com.hackerda.platform.infrastructure.database.dao.UrpClassDao;
import com.hackerda.platform.infrastructure.database.model.UrpClass;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Yuki
 * @date 2019/6/6 10:55
 */
@Slf4j
@Service
public class GradeAutoUpdateScheduled implements Runnable{

    private final ExecutorService gradeAutoUpdatePool = new MDCThreadPool(8, 8,
            0L, TimeUnit.MILLISECONDS, new AntiDuplicateLinkedBlockingQueue<>(10240), r -> new Thread(r, "gradeUpdate"));

    private final ExecutorService classExecutor = new MDCThreadPool(2, 2,
            0L, TimeUnit.MILLISECONDS, new AntiDuplicateLinkedBlockingQueue<>(1024), r -> new Thread(r, "classGradeUpdateCheck"));


    @Autowired
    private GradeQueryApp gradeQueryApp;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private BlockingQueue<GradeFetchTask> gradeFetchQueue;
    @Autowired
    private WechatMessageSender wechatMessageSender;
    @Autowired
    private UrpClassDao urpClassDao;
    @Autowired
    private StudentSettingRepository studentSettingRepository;

    @Value("${scheduled.gradeUpdate:false}")
    private boolean autoStart;

    @Getter
    private volatile boolean start = false;

    @PostConstruct
    public void initMethod(){
        if (!start && autoStart) {
            CompletableFuture.runAsync(this, gradeAutoUpdatePool);
            CompletableFuture.runAsync(this::simulation, gradeAutoUpdatePool);
        }
    }


    @Scheduled(cron = "0 0/20 * * * ? ")
    public void simulation() {
        if(!autoStart || !start) {
            return;
        }

        List<String> prefixList = Lists.newArrayList("2017", "2018", "2019", "2020");

        for (String prefix : prefixList) {
            List<String> classNumList = urpClassDao.selectByNumPrefix(prefix).stream().map(UrpClass::getClassNum).collect(Collectors.toList());
            for (String classNum : classNumList) {
                classExecutor.submit(new ClassFetchTask(Integer.parseInt(classNum)));
            }
        }
    }


    public void run() {
        log.info("GradeAutoUpdateScheduled run");
        start = true;
        GradeFetchTask task;
        try {
            while (start && (task = gradeFetchQueue.take()) != null) {
                StudentUserBO tigerStudent = task.getTigerStudent();
                List<WechatStudentUserBO> userList = getFetchList(tigerStudent.getUrpClassNum())
                        .stream()
                        .filter(x -> x.getAccount().equals(tigerStudent.getAccount()))
                        .collect(Collectors.toList());

                for (WechatStudentUserBO user : userList) {
                    gradeAutoUpdatePool.submit(new StudentFetchTask(user));
                }
            }
        } catch (Exception e) {
            log.error("get task exception", e);
        } finally {
            shutdown();
        }

    }

    public synchronized void start() {
        if(!start) {
            log.info("evaluate task start");
            CompletableFuture.runAsync(this);
        }
    }

    public synchronized void shutdown() {
        this.start = false;
    }

    private GradeOverviewBO handleMessage(WechatStudentUserBO wechatStudentUserBO) {
        try {
            GradeOverviewBO gradeOverview = gradeQueryApp.getGradeOverview(wechatStudentUserBO, false);
            List<WechatTemplateMessage> messageList = gradeOverview.getNeedToSendGrade().stream()
                    .map(x -> new GradeUpdateMessage(wechatStudentUserBO.getUnionId().getWechatUser(
                            "wx541fd36e6b400648"), x, wechatStudentUserBO))
                    .collect(Collectors.toList());
            wechatMessageSender.sendTemplateMessageAsync(messageList);

            return gradeOverview;
        } catch (Exception e) {
            log.info("{} fetch grade error", wechatStudentUserBO.getAccount(), e);
        }

        return null;
    }

    private List<WechatStudentUserBO> getFetchList(Integer urpClassNum) {
        List<WechatStudentUserBO> wetChatUser = studentRepository
                .findWetChatUser(urpClassNum);

        return wetChatUser
                .stream()
                .filter(x-> x.hasBindApp("wx541fd36e6b400648"))
                .filter(x-> x.getWechatUser("wx541fd36e6b400648").isSubscribe())
                .filter(WechatStudentUserBO::isPasswordCorrect)
                .filter(x-> studentSettingRepository.get(x.getAccount()).isGradePushSwitch())
                .collect(Collectors.toList());
    }


    @Data
    class ClassFetchTask  implements Runnable{

        private final Integer urpClassNum;
        private int retryCount = 0;

        public ClassFetchTask(Integer urpClassNum) {
            this.urpClassNum = urpClassNum;
        }

        @Override
        public void run() {
            List<WechatStudentUserBO> fetchList = getFetchList(urpClassNum);
            log.info("classNum {} start size {}", urpClassNum, fetchList.size());
            if (CollectionUtils.isNotEmpty(fetchList)) {
                Random r = new Random();
                WechatStudentUserBO studentUserBO = fetchList.get(r.nextInt(fetchList.size()));
                GradeOverviewBO gradeOverviewBO = handleMessage(studentUserBO);

                if(gradeOverviewBO == null || gradeOverviewBO.getErrorCode() == ErrorCode.READ_TIMEOUT.getErrorCode()) {
                    if (retryCount < 2) {
                        log.info("classNum {} retry count {}", urpClassNum, retryCount);
                        classExecutor.submit(this);
                    }
                    retryCount ++;
                }

                else if(CollectionUtils.isNotEmpty(gradeOverviewBO.getNeedToSendGrade())) {
                    List<WechatStudentUserBO> needToFetch = fetchList.stream()
                            .filter(x -> !x.getAccount().equals(studentUserBO.getAccount()))
                            .filter(WechatStudentUserBO :: isPasswordCorrect)
                            .collect(Collectors.toList());
                    for (WechatStudentUserBO user : needToFetch) {
                        gradeAutoUpdatePool.submit(new StudentFetchTask(user));
                    }
                }
            }

        }
    }

    @Data
    class StudentFetchTask implements Runnable {

        private final WechatStudentUserBO wechatStudentUserBO;
        private int retryCount = 0;

        public StudentFetchTask(WechatStudentUserBO student) {
            this.wechatStudentUserBO = student;
        }

        @Override
        public void run() {

            log.info("account {} start", wechatStudentUserBO.getAccount());
            GradeOverviewBO gradeOverviewBO = handleMessage(wechatStudentUserBO);

            if(gradeOverviewBO == null || gradeOverviewBO.getErrorCode() == ErrorCode.READ_TIMEOUT.getErrorCode()) {
                if (retryCount < 2) {
                    gradeAutoUpdatePool.submit(this);
                }
                retryCount++;
            }
            log.info("account {} finish", wechatStudentUserBO.getAccount());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            StudentFetchTask that = (StudentFetchTask) o;

            return new EqualsBuilder()
                    .append(wechatStudentUserBO.getAccount(), that.wechatStudentUserBO.getAccount())
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(wechatStudentUserBO.getAccount())
                    .toHashCode();
        }
    }


}