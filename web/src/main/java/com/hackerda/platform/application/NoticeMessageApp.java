package com.hackerda.platform.application;

import com.hackerda.platform.domain.community.CommentBO;
import com.hackerda.platform.domain.message.AppNoticeMessageBO;
import com.hackerda.platform.domain.message.CommentNoticeMessage;
import com.hackerda.platform.domain.message.MessageFactory;
import com.hackerda.platform.domain.message.MessageRepository;
import com.hackerda.platform.domain.wechat.UnionId;
import com.hackerda.platform.domain.wechat.UnionIdRepository;
import com.hackerda.platform.domain.wechat.WechatMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class NoticeMessageApp {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MessageFactory messageFactory;
    @Autowired
    private WechatMessageSender wechatMessageSender;
    @Autowired
    private UnionIdRepository unionIdRepository;

    public void readPostMessage(String userName) {
        messageRepository.updateHasRead(userName);
    }


    public List<AppNoticeMessageBO> createAppNoticeByReceiver(String userName, Integer startId, int count,
                                                              boolean markAsRead) {

        List<AppNoticeMessageBO> messageList = messageFactory.createAppNoticeByReceiver(userName, startId, count);

        if(markAsRead) {
            messageRepository.updateHasRead(userName);
        }

        return messageList;
    }

}
