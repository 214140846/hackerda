package com.hackerda.platform.application;

import com.hackerda.platform.application.event.EventPublisher;
import com.hackerda.platform.domain.community.*;
import com.hackerda.platform.domain.message.CommentNoticeMessage;
import com.hackerda.platform.domain.message.MessageBO;
import com.hackerda.platform.domain.message.MessageRepository;
import com.hackerda.platform.domain.message.MessageTriggerSource;
import com.hackerda.platform.domain.student.StudentSettingRepository;
import com.hackerda.platform.domain.wechat.UnionId;
import com.hackerda.platform.domain.wechat.UnionIdRepository;
import com.hackerda.platform.domain.wechat.WechatMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommunityCommentApp {

    @Autowired
    private ContentSecurityCheckService contentSecurityCheckService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private PosterRepository posterRepository;
    @Autowired
    private CommentCountService commentCountService;
    @Autowired
    private LikeCountService likeCountService;
    @Autowired
    private EventPublisher eventPublisher;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UnionIdRepository unionIdRepository;
    @Autowired
    private WechatMessageSender wechatMessageSender;


    public void addComment(CommentBO commentBO) {

        if (!contentSecurityCheckService.isSecurityContent(commentBO.getContent())) {
            commentBO.setStatus(RecordStatus.UnderReview);
        } else {
            commentBO.setStatus(RecordStatus.Release);
        }

        commentRepository.save(commentBO);

        if(commentBO.isRelease()) {
            eventPublisher.addCommentEvent(commentBO, true);
            posterRepository.updateLastReplyTime(commentBO.getPostId(), new Date());

            if (!commentBO.isTriggerBySelf()) {
                sendCommentMessage(commentBO.getReplyUserName(), commentBO);
            }
        }

    }

    public void addLike(LikeBO likeBO) {
        LikeBO like = likeRepository.find(likeBO.getUserName(), likeBO.getLikeType(), likeBO.getTypeContentId());

        if (like != null) {
            like.setShow(likeBO.isShow());
            like.setLikeTime(likeBO.getLikeTime());
            likeRepository.update(like);
        } else if (likeBO.isShow()) {
            likeRepository.save(likeBO);
            eventPublisher.addLikeEvent(likeBO);
            if(likeBO.getLikeType() == LikeType.Post) {
                posterRepository.updateLastReplyTime(likeBO.getTypeContentId(), new Date());
            }

        }
    }


    /**
     * 这里用用户id，而不直接指定微信appId的原因，后面这个逻辑可能兼容发送小程序消息和服务号消息
     *  @param receiverUserName 接收者用户的id
     * @param commentBO 提醒的评论消息
     * @return
     */
    public CompletableFuture<Void> sendCommentMessage(String receiverUserName, CommentBO commentBO) {
        UnionId unionId = unionIdRepository.findByUserName(receiverUserName);

        if (unionId.hasApp("wx541fd36e6b400648") && unionId.getWechatUser("wx541fd36e6b400648").isSubscribe()) {
            CommentNoticeMessage message = new CommentNoticeMessage(unionId.getWechatUser("wx541fd36e6b400648"), commentBO);
            return wechatMessageSender.sendTemplateMessageAsync(message);
        }

        return CompletableFuture.runAsync(() -> {});

    }

    public boolean deleteComment(String userName, CommentBO commentBO) {

        if(commentBO.canDeleteByUser(userName)) {
            commentBO.delete();
            commentRepository.update(commentBO);

            List<MessageBO> messageList = messageRepository.find(MessageTriggerSource.Comment, commentBO.getId());
            messageList.forEach(MessageBO::delete);

            for (MessageBO message : messageList) {
                messageRepository.update(message);
            }

            return true;
        }

        return false;
    }

    public void countSynchronize() {

        long maxId = posterRepository.findShowPost(null, 1).stream().findFirst().get().getId() + 1;

        while (true) {
            List<PostDetailBO> showPost = posterRepository.findShowPost(Math.toIntExact(maxId), 100);
            if(showPost.size() == 0) {
                break;
            }

            maxId = showPost.stream()
                    .map(PostDetailBO::getId)
                    .min(Long::compareUnsigned)
                    .orElse(-1L);


            for (PostDetailBO detailBO : showPost) {
                List<CommentDetailBO> detailBOList = commentRepository.findByPost(RecordStatus.Release, detailBO.getId());
                commentCountService.setCount(detailBO.getId(), detailBOList.size());

                List<LikeBO> show = likeRepository.findShow(LikeType.Post, detailBO.getId());

                likeCountService.setCount(LikeType.Post, detailBO.getId(),
                        show.stream().map(LikeBO::getUserName).collect(Collectors.toSet()));

                detailBO.setLikeCount(show.size());
                detailBO.setCommentCount(detailBOList.size());

                posterRepository.update(detailBO);

                for (CommentDetailBO commentDetailBO : detailBOList) {
                    List<LikeBO> commentLike = likeRepository.findShow(LikeType.Comment, commentDetailBO.getId());

                    likeCountService.setCount(LikeType.Comment, detailBO.getId(),
                            commentLike.stream().map(LikeBO::getUserName).collect(Collectors.toSet()));
                }
            }
        }
    }


}
