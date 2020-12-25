package com.hackerda.platform.service.wechat.handler.messageHandler;


import com.hackerda.platform.builder.TextBuilder;
import com.hackerda.platform.config.wechat.WechatMpPlusProperties;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.wechat.UnionId;
import com.hackerda.platform.domain.wechat.UnionIdRepository;
import com.hackerda.platform.domain.wechat.WechatUser;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;


/**
 * 用户关注事件发送引导信息
 */
@Slf4j
@Component
public class SubscribeEventHandler implements WxMpMessageHandler {
    @Resource
    private TextBuilder textBuilder;
    @Autowired
    private UnionIdRepository unionIdRepository;
    @Autowired
    private StudentRepository studentRepository;


    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {
        String appId = wxMpService.getWxMpConfigStorage().getAppId();
        String fromUser = wxMessage.getFromUser();
        StringBuffer buffer = new StringBuffer();

        UnionId unionId = unionIdRepository.find(new WechatUser(appId, fromUser));

        WechatStudentUserBO wetChatUser = studentRepository.findWetChatUser(unionId);

        buffer.append("欢迎！\n\n");
        if (wetChatUser == null) {
            buffer.append("为了更好的为你提供服务，请先点击下方的菜单的用户【用户绑定】");
        } else {
            buffer.append("现在你已经拥有了成绩提醒的功能");
        }

        buffer.append("\n\n").append("使用过程中有问题在后台留言，我们会尽快解决的");

        return textBuilder.build(new String(buffer), wxMessage, wxMpService);
    }

}
