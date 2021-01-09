package com.hackerda.platform.service.wechat.handler.messageHandler;


import com.hackerda.platform.builder.TextBuilder;
import com.hackerda.platform.domain.SpiderSwitch;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.service.EvaluationService;
import com.hackerda.platform.task.EvaluateTask;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class EvaluationHandler implements WxMpMessageHandler {

    @Autowired
    private EvaluationService evaluationService;
    @Autowired
    private TextBuilder textBuilder;
    @Autowired
    private EvaluateTask evaluateTask;
    @Autowired
    private SpiderSwitch spiderSwitch;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {

        StudentUserBO student = (StudentUserBO) context.get("student");

        if (evaluationService.hasFinish(student.getAccount())) {
            return textBuilder.build("评估已完成", wxMpXmlMessage, wxMpService);
        }

        if (!spiderSwitch.fetchUrp()) {
            return textBuilder.build("教务网已关闭，服务已暂停", wxMpXmlMessage, wxMpService);
        }


        evaluationService.push(student.getAccount());

        if(!evaluateTask.isStart()) {
            evaluateTask.start();
        }

        return textBuilder.build("评估中，自动教评完成后会通知你哦", wxMpXmlMessage, wxMpService);
    }

}
