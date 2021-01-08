package com.hackerda.platform.domain.wechat;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface WechatMessageSender {

    CompletableFuture<Void> sendTemplateMessageAsync(WechatTemplateMessage wechatTemplateMessage);

    CompletableFuture<Void> sendTemplateMessageAsync(List<WechatTemplateMessage> wechatTemplateMessageList);

    void sendTemplateMessage(WechatTemplateMessage wechatTemplateMessage);

    void sendTemplateMessage(List<WechatTemplateMessage> wechatTemplateMessageList);
}
