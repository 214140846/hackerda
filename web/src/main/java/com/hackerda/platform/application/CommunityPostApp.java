package com.hackerda.platform.application;

import com.hackerda.platform.MDCThreadPool;
import com.hackerda.platform.domain.community.*;
import com.hackerda.platform.domain.user.action.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommunityPostApp {

    private ContentSecurityCheckService contentSecurityCheckService;
    @Autowired
    private PosterRepository posterRepository;
    @Autowired
    private RecommendPostRecorder recommendPostRecorder;
    @Autowired
    private UserActionRecordRepository userActionRecordRepository;


    private final ExecutorService imageSecCheckPool = new MDCThreadPool(8, 8,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), r -> new Thread(r, "imageSecCheck"));

    private final RestTemplate template = new RestTemplate();

    public void createPost(PostBO postBO) {

        if(!contentSecurityCheckService.isSecurityContent(postBO.getContent())) {
            postBO.setStatus(RecordStatus.UnderReview);
        }else {
            postBO.setStatus(RecordStatus.Release);
        }

        if(postBO.hasImage()) {
            CountDownLatch latch = new CountDownLatch(postBO.getImageInfoList().size());
            for (ImageInfo image : postBO.getImageInfoList()) {
                imageSecCheckPool.submit(() -> {
                    try {
                        boolean isSec = imageSecCheck(image);
                        image.setRecordStatus(isSec ? RecordStatus.Release :  RecordStatus.UnderReview);
                    }finally {
                        latch.countDown();
                    }
                });
            }
            try {
                latch.await(3000L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                log.error("latch await error", e);
            }
        }
        posterRepository.save(postBO);
    }

    public boolean deletePost(PostBO postBO, String userName) {
        if(postBO.canDeleteByUser(userName)) {
            postBO.delete();
            posterRepository.update(postBO);
            return true;
        } else {
            log.warn("user {} no access to delete post {}", userName, postBO);
            return false;
        }
    }

    /**
     * 将post置顶或者取消置顶
     * @param operateUser 操作的用户名
     * @param postBO 需要被更换状态的post对象
     * @param revoke 是否移除置顶，是则为ture
     * @return 是否置顶成功
     */
    @Transactional
    public synchronized ActionResult topPost(String operateUser, PostBO postBO, boolean revoke) {
        Date operatorTime = new Date();
        if (revoke) {
            if(postBO.isTop()) {
                postBO.statusToRelease();

                posterRepository.update(postBO);
                recordStatusChange(operateUser, postBO, operatorTime);
                return ActionResult.success();
            }
            return ActionResult.fail(postBO.getId() +"无法取消置顶");
        }

        List<PostDetailBO> topPostList = posterRepository.findByStatus(Collections.singletonList(RecordStatus.TOP));

        if (!topPostList.isEmpty()) {
            PostDetailBO top = topPostList.stream().findFirst().orElse(null);
            top.statusToRelease();
            posterRepository.update(top);

            recordStatusChange(operateUser, postBO, operatorTime);
        }

        postBO.top();
        posterRepository.update(postBO);

        recordStatusChange(operateUser, postBO, operatorTime);
        return ActionResult.success();
    }



    /**
     * 将post置顶或者取消置顶
     * @param operateUser 操作的用户名
     * @param postBO 需要被更换状态的post对象
     * @param revoke 是否移除置顶，是则为ture
     * @return 是否置顶成功
     */
    @Transactional
    public synchronized ActionResult recommendPost(String operateUser, PostBO postBO, boolean revoke) {
        if (revoke) {
            if(postBO.getStatus() == RecordStatus.Featured) {
                postBO.statusToRelease();

                posterRepository.update(postBO);
                recommendPostRecorder.remove(postBO.getId(), getTomorrow(new Date()));
                recordStatusChange(operateUser, postBO, new Date());
                return ActionResult.success();
            }
            return ActionResult.fail(postBO.getId() +"无法取消精选");
        }

        if (recommendPostRecorder.getPostIdList(new Date()).size() < 2) {
            postBO.feature();

            posterRepository.update(postBO);
            recommendPostRecorder.add(postBO.getId(), getTomorrow(new Date()));

            recordStatusChange(operateUser, postBO, new Date());
            return ActionResult.success();
        }

        return ActionResult.fail("今日精选帖子以达上限");
    }

    private boolean imageSecCheck(ImageInfo imageInfo) {
        try {
            String url = imageInfo.getPath();

            String decode = URLDecoder.decode(url, StandardCharsets.UTF_8.name());

            ResponseEntity<byte[]> entity = template.getForEntity(decode, byte[].class);

            return contentSecurityCheckService.isSecurityImage(entity.getBody());
        }catch (Throwable throwable) {
            log.error("check imageSec error", throwable);
            return true;
        }


    }

    public List<PostDetailBO> getRecommendPost() {
        List<Long> idList = recommendPostRecorder.getPostIdList(new Date());
        return posterRepository.findByIdList(idList)
                .stream().filter(PostBO::isShow)
                .sorted(Comparator.comparing(PostBO::getId).reversed()).collect(Collectors.toList());
    }

    private void recordStatusChange(String operator, PostBO postBO, Date operatorTime) {
        UserActionRecordBO record = new UserActionRecordBO(operator, UserAction.CHANGE_POST_STATUS,
                ActionTarget.POST, postBO.getId().toString(), String.valueOf(postBO.getStatus().getCode()),
                operatorTime);

        userActionRecordRepository.store(record);

    }

    private Date getTomorrow(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,1);

        return calendar.getTime();
    }

    @Autowired
    public void setContentSecurityCheckService(ContentSecurityCheckService contentSecurityCheckService) {
        this.contentSecurityCheckService = contentSecurityCheckService;
    }

}
