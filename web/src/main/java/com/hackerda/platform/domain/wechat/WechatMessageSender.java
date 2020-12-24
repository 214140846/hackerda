package com.hackerda.platform.domain.wechat;

import java.util.List;

public interface WechatMessageSender {

    void sendTemplateMessageAsync(WechatTemplateMessage wechatTemplateMessage);

    void sendTemplateMessageAsync(List<WechatTemplateMessage> wechatTemplateMessageList);

    void sendTemplateMessage(WechatTemplateMessage wechatTemplateMessage);

    void sendTemplateMessage(List<WechatTemplateMessage> wechatTemplateMessageList);
}
