package com.hackerda.platform.task;

import com.hackerda.platform.MDCThreadPool;
import com.hackerda.platform.domain.grade.*;
import com.hackerda.platform.application.GradeQueryApp;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.wechat.WechatMessageSender;
import com.hackerda.platform.domain.wechat.WechatTemplateMessage;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    //这里设置拒绝策略为调用者运行，这样可以降低产生任务的速率
    private final ExecutorService gradeAutoUpdatePool = new MDCThreadPool(8, 8,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), r -> new Thread(r, "gradeUpdate"));

    @Autowired
    private GradeQueryApp gradeQueryApp;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private BlockingQueue<GradeFetchTask> gradeFetchQueue;
    @Autowired
    private WechatMessageSender wechatMessageSender;

    private final Set<GradeFetchTask> taskSet = Collections.synchronizedSet(new HashSet<>());

    @Value("${scheduled.gradeUpdate:false}")
    private boolean autoStart;

    @Getter
    private volatile boolean start = false;

    @PostConstruct
    public void initMethod(){
        if (!start && autoStart) {
            CompletableFuture.runAsync(this);
        }
    }

    //    @Async
//    @Scheduled(cron = "0 0/20 * * * ? ")

    public void run() {
        start = true;
        GradeFetchTask task;
        try {
            while (start && (task = gradeFetchQueue.take()) != null) {
                if(!taskSet.contains(task)) {
                    taskSet.add(task);

                    CompletableFuture<Void> future = CompletableFuture.runAsync(new GradeFetchTaskWrapper(task), gradeAutoUpdatePool);

                    GradeFetchTask finalTask = task;
                    future.whenComplete((x, y)-> taskSet.remove(finalTask));

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

    @Data
    private class GradeFetchTaskWrapper implements Runnable{
        private int timeoutCount;
        private GradeFetchTask gradeFetchTask;

        GradeFetchTaskWrapper(GradeFetchTask gradeFetchTask) {
            this.gradeFetchTask = gradeFetchTask;
        }

        @Override
        public void run() {
            UUID uuid = UUID.randomUUID();
            MDC.put("traceId", "gradeUpdateTask-" + uuid.toString());
            List<WechatStudentUserBO> wetChatUser = studentRepository
                    .findWetChatUser(gradeFetchTask.getTigerStudent().getUrpClassNum())
                    .stream().filter(x-> x.hasBindApp("wx541fd36e6b400648"))
                    .collect(Collectors.toList());

            if (gradeFetchTask.isTigerByUser()) {
                wetChatUser =
                        wetChatUser.stream()
                                .filter(x-> !x.getAccount().equals(gradeFetchTask.getTigerStudent().getAccount()))
                                .collect(Collectors.toList());
            }

            for (WechatStudentUserBO wechatStudentUserBO : wetChatUser) {
                try {
                    GradeOverviewBO gradeOverview = gradeQueryApp.getGradeOverview(wechatStudentUserBO, false);
                    List<WechatTemplateMessage> messageList = gradeOverview.getNeedToSendGrade().stream()
                            .map(x -> new GradeUpdateMessage(wechatStudentUserBO.getUnionId().getWechatUser(
                                    "wx541fd36e6b400648"), x, wechatStudentUserBO))
                            .collect(Collectors.toList());
                    wechatMessageSender.sendTemplateMessageAsync(messageList);
                } catch (Exception e) {
                    log.info("{} fetch grade error", wechatStudentUserBO.getAccount(), e);
                }
            }
        }
    }


}