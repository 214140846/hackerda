package com.hackerda.platform.service.wechat.handler.messageHandler;


import com.hackerda.platform.builder.TextBuilder;
import com.hackerda.platform.domain.constant.RedisKeys;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.service.EvaluationService;
import com.hackerda.platform.utils.DateUtils;
import com.hackerda.platform.utils.Term;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class EvaluationHandler implements WxMpMessageHandler {

    @Autowired
    private EvaluationService evaluationService;
    @Autowired
    private TextBuilder textBuilder;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {

        StudentUserBO student = (StudentUserBO) context.get("student");

        if (evaluationService.hasFinish(student.getAccount())) {
            return textBuilder.build("评估已完成", wxMpXmlMessage, wxMpService);
        }


        return textBuilder.build("评估中，评估完成后会我们会提醒你", wxMpXmlMessage, wxMpService);
    }

}
