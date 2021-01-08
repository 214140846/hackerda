package com.hackerda.platform.infrastructure.wechat;

import com.hackerda.platform.MDCThreadPool;
import com.hackerda.platform.config.wechat.WechatMpConfiguration;
import com.hackerda.platform.domain.wechat.WechatMessageSender;
import com.hackerda.platform.domain.wechat.WechatTemplateMessage;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WechatMessageSenderImpl implements WechatMessageSender {

    @Autowired
    private WechatMpConfiguration wechatMpConfiguration;

    private final ExecutorService wechatMessagePool = new MDCThreadPool(8, 8,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), r -> new Thread(r, "wechatMessagePool"));

    @Override
    public CompletableFuture<Void> sendTemplateMessageAsync(WechatTemplateMessage wechatTemplateMessage) {
        return CompletableFuture.runAsync(() -> sendTemplateMessage(wechatTemplateMessage), wechatMessagePool);

    }

    @Override
    @SuppressWarnings("unchecked")
    public CompletableFuture<Void> sendTemplateMessageAsync(List<WechatTemplateMessage> wechatTemplateMessageList) {
        for (WechatTemplateMessage message : wechatTemplateMessageList) {
            this.sendTemplateMessageAsync(message);
        }

        CompletableFuture<Void>[] completableFutures =
                wechatTemplateMessageList.stream().map(this::sendTemplateMessageAsync).toArray(CompletableFuture[]::new);
        return CompletableFuture.allOf(completableFutures);

    }

    @Override
    public void sendTemplateMessage(WechatTemplateMessage wechatTemplateMessage) {
        String appId = wechatTemplateMessage.getSendApp();
        WxMpService wxService = wechatMpConfiguration.getMpService(appId);
        WxMpTemplateMessage wxMpTemplateMessage = wechatTemplateMessage.genMpTemplateMessage();
        try {
            String msg = wxService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
            log.info("send message {} success  msg {}", wxMpTemplateMessage.toJson(), msg);
        } catch (Exception e) {
            log.error("message {} send error",wxMpTemplateMessage.toJson(), e);
        }

    }

    @Override
    public void sendTemplateMessage(List<WechatTemplateMessage> wechatTemplateMessageList) {
        for (WechatTemplateMessage message : wechatTemplateMessageList) {
            this.sendTemplateMessage(message);
        }
    }
}
