package com.hackerda.platform.controller.api;


import com.hackerda.platform.aggregator.UserInfoAggregator;
import com.hackerda.platform.controller.WebResponse;
import com.hackerda.platform.controller.vo.*;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.infrastructure.database.model.Exam;
import com.hackerda.platform.service.*;
import com.hackerda.platform.service.rbac.UserAuthorizeService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SchoolCommonController {

    @Autowired
    private CourseTimeTableService courseTimeTableService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private UserAuthorizeService userAuthorizeService;
    @Autowired
    private ExamTimeTableService examTimeTableService;
    @Autowired
    private UserInfoAggregator userInfoAggregator;
    @Autowired
    private UserExtInfoService userExtInfoService;
    @Autowired
    private EvaluationService evaluationService;

    @RequiresAuthentication
    @RequestMapping(value = "/grade")
    public WebResponse<GradeResultVO> getNowGradeV2() {

        return WebResponse.success(gradeService.getGrade());
    }

    @RequiresAuthentication
    @RequestMapping(value = "/timetableV2")
    public WebResponse<CourseTimetableOverviewVO> getTimeTableV2() {

        CourseTimetableOverviewVO vo = courseTimeTableService.getCurrentTermCourseTimeTableByStudent();

        return WebResponse.success(vo);
    }

    @RequiresAuthentication
    @RequestMapping(value = "/unbind")
    public WebResponse<String> appUnbind(@RequestParam(value = "account", required = false) String account,
                                 @RequestParam(value = "appId") String appId) {

        userAuthorizeService.appStudentRevokeAuthorize(account, appId);
        return WebResponse.success("success");
    }

    @RequiresAuthentication
    @GetMapping("/exam")
    public WebResponse<List<Exam>> getExamTimeTableByStudent() {
        StudentUserBO wechatStudentUserBO = (StudentUserBO) SecurityUtils.getSubject().getPrincipal();

        List<Exam> examTimeList = examTimeTableService.getExamTimeList(wechatStudentUserBO);
        return WebResponse.success(examTimeList);
    }

    @RequiresAuthentication
    @GetMapping("/updateStudentInfo")
    public WebResponse<UserInfoVO> updateStudentInfo() {
        WechatStudentUserBO wechatStudentUserBO = (WechatStudentUserBO) SecurityUtils.getSubject().getPrincipal();

        return WebResponse.success(userInfoAggregator.updateStudentInfo(wechatStudentUserBO));
    }

    @RequiresAuthentication
    @GetMapping("/userExtInfo")
    public WebResponse<UserExtInfoVO> getUserExtInfo() {
        WechatStudentUserBO wechatStudentUserBO = (WechatStudentUserBO) SecurityUtils.getSubject().getPrincipal();

        return WebResponse.success(userExtInfoService.getUserExtInfo(wechatStudentUserBO));
    }


    @RequiresAuthentication
    @GetMapping("/evaluate")
    public WebResponse<CreateCommentResultVO> evaluate() {
        WechatStudentUserBO wechatStudentUserBO = (WechatStudentUserBO) SecurityUtils.getSubject().getPrincipal();
        if(evaluationService.hasFinish(wechatStudentUserBO)) {

            return WebResponse.success(new CreateCommentResultVO(false, "评估已完成"));
        }

        evaluationService.push(wechatStudentUserBO.getAccount());
        return WebResponse.success(new CreateCommentResultVO(true, ""));
    }

    @RequiresAuthentication
    @GetMapping("/getStudentSetting")
    public WebResponse<StudentSettingVO> getStudentSetting() {
        WechatStudentUserBO wechatStudentUserBO = (WechatStudentUserBO) SecurityUtils.getSubject().getPrincipal();

        return WebResponse.success(userExtInfoService.getStudentSetting(wechatStudentUserBO));
    }

    @RequiresAuthentication
    @PostMapping("/updateStudentSetting")
    public WebResponse<CreateCommentResultVO> updateStudentSetting(@RequestParam(value = "key") String key,
                                                              @RequestParam(value = "value") String value) {
        WechatStudentUserBO wechatStudentUserBO = (WechatStudentUserBO) SecurityUtils.getSubject().getPrincipal();

        return WebResponse.success(userExtInfoService.updateSetting(wechatStudentUserBO, key, value));
    }
}
