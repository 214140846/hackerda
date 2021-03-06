package com.hackerda.platform.controller.api;

import com.hackerda.platform.controller.WebResponse;
import com.hackerda.platform.controller.request.CreateCommentRequest;
import com.hackerda.platform.controller.request.CreatePostRequest;
import com.hackerda.platform.controller.request.LikeRequest;
import com.hackerda.platform.controller.request.ModifyUserInfoRequest;
import com.hackerda.platform.controller.vo.*;
import com.hackerda.platform.domain.constant.ErrorCode;
import com.hackerda.platform.domain.user.AppUserBO;
import com.hackerda.platform.service.community.AppMessageService;
import com.hackerda.platform.service.community.CommunityCommentService;
import com.hackerda.platform.service.community.CommunityPostService;
import com.hackerda.platform.service.community.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/community")
public class CommunityController {

    @Autowired
    private CommunityPostService communityPostService;
    @Autowired
    private CommunityCommentService communityCommentService;
    @Autowired
    private AppMessageService appMessageService;
    @Autowired
    private UserService userService;

    @RequiresAuthentication
    @GetMapping("/getPostIdentityByStudent")
    public WebResponse<?> getPostIdentityByStudent(@RequestParam(value = "studentAccount") String account){
        return WebResponse.success(communityPostService.getPostIdentityByStudent(account));
    }

    @RequiresAuthentication
    @PostMapping("/createPost")
    public WebResponse<?> createPost(@RequestBody CreatePostRequest createPostRequest){
        AppUserBO appUserBO = (AppUserBO)SecurityUtils.getSubject().getPrincipal();

        return WebResponse.success(communityPostService.cratePost(appUserBO.getUserName(), createPostRequest));
    }

    @GetMapping("/getPostById")
    public WebResponse<PostVO> getPost(@RequestParam(value = "postId") int postId,
                                       @RequestParam(value = "appId", required = false) String appId,
                                       @RequestParam(value = "openid", required = false) String openid){

        AppUserBO appUserBO = (AppUserBO)SecurityUtils.getSubject().getPrincipal();
        String username = appUserBO.getUserName();
        return WebResponse.success(communityPostService.getPostById(username, postId, appId, openid));
    }

    @RequiresAuthentication
    @GetMapping("/deletePostById")
    public WebResponse<CreateCommentResultVO> deletePostById(@RequestParam(value = "postId") int postId){
        AppUserBO appUserBO = (AppUserBO)SecurityUtils.getSubject().getPrincipal();
        String username = appUserBO.getUserName();
        return WebResponse.success(communityPostService.deletePostById(username, postId));
    }

    @GetMapping("/getPost")
    public WebResponse<PostDetailVO> getPost(@RequestParam(value = "startId", required = false) Integer startId,
                                             @RequestParam(value = "count") int count,
                                             @RequestParam(value = "appId", required = false) String appId,
                                             @RequestParam(value = "openid", required = false) String openid){
        AppUserBO appUserBO = (AppUserBO)SecurityUtils.getSubject().getPrincipal();
        String username = appUserBO.getUserName();
        return WebResponse.success(communityPostService.getPostDetail(username, startId, count, appId, openid));
    }

    @RequiresAuthentication
    @GetMapping("/getRecentlyPost")
    public WebResponse<PostDetailVO> getRecentlyPost(@RequestParam(value = "timestamp", required = false) Long timestamp,
                                             @RequestParam(value = "count") int count,
                                             @RequestParam(value = "appId", required = false) String appId,
                                             @RequestParam(value = "openid", required = false) String openid){
        AppUserBO appUserBO = (AppUserBO)SecurityUtils.getSubject().getPrincipal();
        String username = appUserBO.getUserName();
        return WebResponse.success(communityPostService.getRecentlyPost(username, timestamp, count, appId, openid));
    }

    @GetMapping("/getRecommendPost")
    public WebResponse<PostDetailVO> getRecommendPost(@RequestParam(value = "appId", required = false) String appId,
                                                      @RequestParam(value = "openid", required = false) String openid){
        AppUserBO appUserBO = (AppUserBO)SecurityUtils.getSubject().getPrincipal();
        String username = appUserBO.getUserName();
        return WebResponse.success(communityPostService.getRecommendPost(username,  appId, openid));
    }

    @GetMapping("/getTopPost")
    public WebResponse<PostDetailVO> getTopPost(@RequestParam(value = "appId", required = false) String appId,
                                                      @RequestParam(value = "openid", required = false) String openid){
        AppUserBO appUserBO = (AppUserBO)SecurityUtils.getSubject().getPrincipal();
        String username = appUserBO.getUserName();
        return WebResponse.success(communityPostService.getTopPost(username,  appId, openid));
    }

    @GetMapping("/checkPostUpdate")
    public WebResponse<CheckPostUpdateVO> checkPostUpdate(@RequestParam(value = "maxPostId") long maxPostId){

        return WebResponse.success(communityPostService.checkUpdate(maxPostId));
    }


    @GetMapping("/getPostByUserName")
    public WebResponse<PostDetailVO> getPostByUserName(@RequestParam(value = "userName") String postUserName,
                                                       @RequestParam(value = "startId", required = false) Integer startId,
                                                       @RequestParam(value = "count") int count){
        AppUserBO appUserBO = (AppUserBO)SecurityUtils.getSubject().getPrincipal();
        String username = appUserBO.getUserName();
        return WebResponse.success(communityPostService.getPostByUserName(username, postUserName, startId, count));
    }

    @RequiresAuthentication
    @PostMapping("/createComment")
    public WebResponse<CreateCommentResultVO> createComment(@RequestBody CreateCommentRequest createCommentRequest){
        AppUserBO appUserBO = (AppUserBO)SecurityUtils.getSubject().getPrincipal();
        String username = appUserBO.getUserName();
        return WebResponse.success(communityCommentService.addComment(username, createCommentRequest));
    }

    @RequiresAuthentication
    @PostMapping("/deleteCommentById")
    public WebResponse<CreateCommentResultVO> deleteCommentById(@RequestParam(value = "commentId") int commentId){
        AppUserBO appUserBO = (AppUserBO)SecurityUtils.getSubject().getPrincipal();
        String username = appUserBO.getUserName();
        return WebResponse.success(communityCommentService.deleteCommentById(username, commentId));
    }

    @GetMapping("/getCommentByPostId")
    public WebResponse<CommentDetailVO> getDetailByPostId(@RequestParam(value = "postId") Long postId){
        AppUserBO appUserBO = (AppUserBO)SecurityUtils.getSubject().getPrincipal();
        String username = appUserBO.getUserName();
        return WebResponse.success(communityCommentService.findByPostId(username, postId));
    }

    @RequiresAuthentication
    @GetMapping("/addLike")
    public WebResponse<CreateCommentResultVO> addLike(LikeRequest likeRequest){
        AppUserBO appUserBO = (AppUserBO)SecurityUtils.getSubject().getPrincipal();
        String username = appUserBO.getUserName();
        return WebResponse.success(communityCommentService.addLike(likeRequest, username));
    }

    @RequiresAuthentication
    @GetMapping("/getNotice")
    public WebResponse<AppMessageOverviewVO> getNotice(@RequestParam(value = "startId", required = false) Integer startId,
                                                    @RequestParam(value = "count") int count, @RequestParam(value =
            "markAsRead") boolean markAsRead){
        AppUserBO appUserBO = (AppUserBO)SecurityUtils.getSubject().getPrincipal();
        String username = appUserBO.getUserName();
        return WebResponse.success(appMessageService.getAppNotice(username, startId, count, markAsRead));
    }

    @RequiresAuthentication
    @GetMapping("/getMessageCount")
    public WebResponse<MessageCountVO> getMessageCount(){
        AppUserBO appUserBO = (AppUserBO)SecurityUtils.getSubject().getPrincipal();
        String username = appUserBO.getUserName();
        return WebResponse.success(appMessageService.messageCount(username));
    }

    @PostMapping("/modifyUserInfo")
    @RequiresAuthentication
    public WebResponse<?> modifyUserInfo(@Valid ModifyUserInfoRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {

            List<String> list = bindingResult.getFieldErrors().stream()
                    .map(x -> x.getField() + x.getDefaultMessage())
                    .collect(Collectors.toList());

            return WebResponse.fail(ErrorCode.DATA_NOT_VALID.getErrorCode(), String.join(",", list));
        }

        AppUserBO appUserBO = (AppUserBO)SecurityUtils.getSubject().getPrincipal();
        String username = appUserBO.getUserName();
        return WebResponse.success(userService.modifyUserData(username, request));

    }
}
