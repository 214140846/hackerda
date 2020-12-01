package com.hackerda.platform.controller.api;

import com.hackerda.platform.controller.WebResponse;
import com.hackerda.platform.controller.vo.CreateCommentResultVO;
import com.hackerda.platform.domain.user.AppUserBO;
import com.hackerda.platform.domain.user.PermissionBO;
import com.hackerda.platform.service.community.PostOperateService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/community")
public class CommunityOperatorController {

    @Autowired
    private PostOperateService postOperateService;

    @RequiresPermissions(PermissionBO.TOP)
    @GetMapping("/topPost")
    public WebResponse<CreateCommentResultVO> topPost(@RequestParam(value = "postId") long postId,
                                                      @RequestParam(value = "revoke") boolean revoke) {

        AppUserBO appUserBO = (AppUserBO) SecurityUtils.getSubject().getPrincipal();
        return WebResponse.success(postOperateService.topPost(appUserBO, postId, revoke));

    }

    @RequiresPermissions(PermissionBO.RECOMMEND)
    @GetMapping("/recommendPost")
    public WebResponse<CreateCommentResultVO> recommendPost(@RequestParam(value = "postId") long postId,
                                                      @RequestParam(value = "revoke") boolean revoke) {

        AppUserBO appUserBO = (AppUserBO) SecurityUtils.getSubject().getPrincipal();
        return WebResponse.success(postOperateService.recommendPost(appUserBO, postId, revoke));

    }

}
