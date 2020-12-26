package com.hackerda.platform.task;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.hackerda.platform.MDCThreadPool;
import com.hackerda.platform.domain.grade.*;
import com.hackerda.platform.application.GradeQueryApp;
import com.hackerda.platform.domain.student.StudentRepository;
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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author Yuki
 * @date 2019/6/6 10:55
 */
@Slf4j
@Service
public class GradeAutoUpdateScheduled implements Runnable{
    private final BlockingQueue<Runnable> queue = new AntiDuplicateLinkedBlockingQueue<>(1024);

    private final ExecutorService gradeAutoUpdatePool = new MDCThreadPool(8, 8,
            0L, TimeUnit.MILLISECONDS, queue, r -> new Thread(r, "gradeUpdate"));

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

    private final Set<String> taskSet = Collections.synchronizedSet(new HashSet<>());

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
        if(!autoStart) {
            return;
        }
        log.info("fetch task queue size {}", queue.size());

        List<String> prefixList = Lists.newArrayList("2017", "2018", "2019", "2020");

        for (String prefix : prefixList) {
            List<String> classNumList = urpClassDao.selectByNumPrefix(prefix).stream().map(UrpClass::getClassNum).collect(Collectors.toList());
            for (String classNum : classNumList) {
                addClassTask(classNum);
            }
        }
    }

    @VisibleForTesting
    public void addClassTask(String classNum) {
        List<WechatStudentUserBO> fetchList = getFetchList(Integer.parseInt(classNum));
        if (!CollectionUtils.isEmpty(fetchList)) {
            Random r = new Random();
            WechatStudentUserBO studentUserBO = fetchList.get(r.nextInt(fetchList.size()));
            if(handleMessage(studentUserBO)) {
                GradeFetchTaskWrapper task = new GradeFetchTaskWrapper(fetchList.stream()
                        .filter(x -> !x.getAccount().equals(studentUserBO.getAccount()))
                        .collect(Collectors.toList()), classNum);

                CompletableFuture<Void> future = CompletableFuture.runAsync(task, gradeAutoUpdatePool);
                future.whenComplete((x,y) -> taskSet.remove(classNum));
            }
        }
    }

    public void run() {
        log.info("GradeAutoUpdateScheduled run");
        start = true;
        GradeFetchTask task;
        try {
            while (start && (task = gradeFetchQueue.take()) != null) {
                CompletableFuture.runAsync(new GradeFetchTaskWrapper(task), gradeAutoUpdatePool);
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

    private boolean handleMessage(WechatStudentUserBO wechatStudentUserBO) {
        try {
            GradeOverviewBO gradeOverview = gradeQueryApp.getGradeOverview(wechatStudentUserBO, false);
            List<WechatTemplateMessage> messageList = gradeOverview.getNeedToSendGrade().stream()
                    .map(x -> new GradeUpdateMessage(wechatStudentUserBO.getUnionId().getWechatUser(
                            "wx541fd36e6b400648"), x, wechatStudentUserBO))
                    .collect(Collectors.toList());
            wechatMessageSender.sendTemplateMessageAsync(messageList);

            return !CollectionUtils.isEmpty(messageList);
        } catch (Exception e) {
            log.info("{} fetch grade error", wechatStudentUserBO.getAccount(), e);
        }

        return false;
    }

    private List<WechatStudentUserBO> getFetchList(Integer urpClassNum) {
        return studentRepository
                .findWetChatUser(urpClassNum)
                .stream().filter(x-> x.hasBindApp("wx541fd36e6b400648"))
                .collect(Collectors.toList());
    }

    @Data
    private class GradeFetchTaskWrapper implements Runnable{
        private int timeoutCount;
        private GradeFetchTask gradeFetchTask;
        private List<WechatStudentUserBO> wetChatUserList = Collections.emptyList();
        private String urpClassNum;

        GradeFetchTaskWrapper(GradeFetchTask gradeFetchTask) {
            this.gradeFetchTask = gradeFetchTask;
            this.urpClassNum = gradeFetchTask.getTigerStudent().getUrpClassNum().toString();
        }

        GradeFetchTaskWrapper(List<WechatStudentUserBO> wetChatUserList, String urpClassNum) {
            this.wetChatUserList = wetChatUserList;
            this.urpClassNum = urpClassNum;

        }

        @Override
        public void run() {
            UUID uuid = UUID.randomUUID();
            MDC.put("traceId", "gradeUpdateTask-" + uuid.toString());
            List<WechatStudentUserBO> wetChatUser;

            if (wetChatUserList.isEmpty()) {
                wetChatUser =
                        getFetchList(gradeFetchTask.getTigerStudent().getUrpClassNum()).stream()
                                .filter(x-> !x.getAccount().equals(gradeFetchTask.getTigerStudent().getAccount()))
                                .collect(Collectors.toList());
            } else {
                wetChatUser = wetChatUserList;
            }

            log.info("classNum {} fetch start, size {}", this.urpClassNum, wetChatUser.size());
            for (WechatStudentUserBO wechatStudentUserBO : wetChatUser) {
                handleMessage(wechatStudentUserBO);
            }

            log.info("classNum {} fetch finish, size {}", this.urpClassNum, wetChatUser.size());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            GradeFetchTaskWrapper that = (GradeFetchTaskWrapper) o;

            return new EqualsBuilder()
                    .append(urpClassNum, that.urpClassNum)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(urpClassNum)
                    .toHashCode();
        }
    }


}