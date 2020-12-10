package com.hackerda.platform.task;

import com.hackerda.platform.config.wechat.WechatMpPlusProperties;
import com.hackerda.platform.domain.message.EvaluateFinishMessage;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.wechat.WechatMessageSender;
import com.hackerda.platform.service.EvaluationService;
import com.hackerda.spider.exception.PasswordUnCorrectException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class EvaluateTask {

    @Autowired
    private EvaluationService evaluationService;
    @Autowired
    private StudentRepository studentRepository;
    @Value("wechat.mp.plus.templateId.evaluate")
    private String templateId;
    @Autowired
    private WechatMessageSender wechatMessageSender;
    @Autowired
    private WechatMpPlusProperties wechatMpPlusProperties;



    public void run() {

        StudentAccount account;
        while ((account = evaluationService.pop()) != null) {
            try {
                WechatStudentUserBO user = studentRepository.findWetChatUser(account);
                evaluationService.evaluate(user);

                if (evaluationService.hasFinish(user)) {
                    evaluationService.addFinish(account);

                    sendMessage(user);

                } else {
                    evaluationService.push(account);
                }
            } catch (PasswordUnCorrectException ignored) {

            } catch (Exception e) {
                evaluationService.push(account);
                log.error("{} evaluate error", account, e);
            }

        }


    }

    void sendMessage(WechatStudentUserBO user) {
        String appId = wechatMpPlusProperties.getAppId();
        EvaluateFinishMessage message = new EvaluateFinishMessage(user.getUnionId().getWechatUser(appId),
                templateId, user);

        wechatMessageSender.sendTemplateMessage(message);
    }

}
