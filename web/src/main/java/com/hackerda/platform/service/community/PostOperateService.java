package com.hackerda.platform.service.community;

import com.hackerda.platform.application.CommunityPostApp;
import com.hackerda.platform.controller.vo.CreateCommentResultVO;
import com.hackerda.platform.domain.community.PostDetailBO;
import com.hackerda.platform.domain.community.PosterRepository;
import com.hackerda.platform.domain.user.AppUserBO;
import com.hackerda.platform.domain.user.action.ActionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostOperateService {

    @Autowired
    private CommunityPostApp communityPostApp;
    @Autowired
    private PosterRepository posterRepository;

    public CreateCommentResultVO topPost(AppUserBO appUserBO, Long postId, boolean revoke) {

        PostDetailBO post = posterRepository.findByPostById(postId);

        ActionResult actionResult = communityPostApp.topPost(appUserBO.getUserName(), post, revoke);

        return new CreateCommentResultVO(actionResult.isSuccess(), actionResult.getMsg());

    }

    public CreateCommentResultVO recommendPost(AppUserBO appUserBO, Long postId, boolean revoke) {

        PostDetailBO post = posterRepository.findByPostById(postId);

        ActionResult actionResult = communityPostApp.recommendPost(appUserBO.getUserName(), post, revoke);

        return new CreateCommentResultVO(actionResult.isSuccess(), actionResult.getMsg());

    }



}
