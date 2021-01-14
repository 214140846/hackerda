package com.hackerda.platform.service.wechat.handler.messageHandler;


import com.hackerda.platform.application.UnionIdApp;
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
    private StudentRepository studentRepository;
    @Autowired
    private UnionIdApp unionIdApp;


    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {
        String appId = wxMpService.getWxMpConfigStorage().getAppId();
        String fromUser = wxMessage.getFromUser();
        StringBuffer buffer = new StringBuffer();

        UnionId unionId = unionIdApp.saveUnionId(new WechatUser(appId, fromUser));

        WechatStudentUserBO wetChatUser = studentRepository.findWetChatUser(unionId);

        buffer.append("感谢你的关注，我们在这已经等候多时了，很高兴遇见你~");
        if (wetChatUser == null) {
            buffer.append("为了更好的为你提供服务，请先点击下方的菜单的用户【用户绑定】");
        } else {
            buffer.append("以后成绩更新以后，我们会第一时间通知你。");
        }

        buffer.append("\n\n").append("使用过程中有问题在后台留言，我们会尽快解决的");

        return textBuilder.build(new String(buffer), wxMessage, wxMpService);
    }

}
