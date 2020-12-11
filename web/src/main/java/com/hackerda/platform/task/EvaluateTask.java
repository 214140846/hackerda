package com.hackerda.platform.task;

import com.hackerda.platform.config.wechat.WechatMpPlusProperties;
import com.hackerda.platform.domain.message.EvaluateFinishMessage;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.wechat.WechatMessageSender;
import com.hackerda.platform.service.EvaluationService;
import com.hackerda.spider.exception.PasswordUnCorrectException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class EvaluateTask implements Runnable{

    @Autowired
    private EvaluationService evaluationService;
    @Autowired
    private StudentRepository studentRepository;
    @Value("${wechat.mp.plus.templateId.evaluate}")
    private String templateId;
    @Value("${evaluate.task.start :false}")
    private boolean autoStart;

    @Autowired
    private WechatMessageSender wechatMessageSender;
    @Autowired
    private WechatMpPlusProperties wechatMpPlusProperties;
    @Getter
    private boolean start = false;

    @PostConstruct
    public void initMethod(){
        if (evaluationService.hasTask() && !start && autoStart) {
            start();
        }
    }

    public void run() {

        start = true;
        StudentAccount account;

        try {
            while ((account = evaluationService.pop()) != null) {
                log.info("account {} start evaluate", account);
                try {
                    WechatStudentUserBO user = studentRepository.findWetChatUser(account);
                    if (evaluationService.hasFinish(user)) {
                        evaluationService.addFinish(account);

                        sendMessage(user);
                        log.info("account {} finish evaluate", account);
                        continue;
                    }

                    evaluationService.evaluate(user);

                    if (evaluationService.hasFinish(user)) {
                        evaluationService.addFinish(account);

                        sendMessage(user);
                        log.info("account {} finish evaluate", account);
                    } else {
                        evaluationService.push(account);
                    }
                } catch (PasswordUnCorrectException ignored) {

                } catch (Exception e) {
                    evaluationService.push(account);
                    log.error("{} evaluate error", account, e);
                }
            }
        } catch (QueryTimeoutException exception) {
            log.info("task QueryTimeoutException");

        } finally {
            start = false;
        }

    }

    public synchronized void start() {
        if(!start) {
            log.info("evaluate task start");
            CompletableFuture.runAsync(this);
        }

    }

    void sendMessage(WechatStudentUserBO user) {
        String appId = wechatMpPlusProperties.getAppId();
        EvaluateFinishMessage message = new EvaluateFinishMessage(user.getUnionId().getWechatUser(appId),
                templateId, user);

        wechatMessageSender.sendTemplateMessage(message);
    }

}
