package com.hackerda.platform.domain.wechat;

public interface WechatMessageSender {

    void sendTemplateMessage(WechatTemplateMessage wechatTemplateMessage);
}
