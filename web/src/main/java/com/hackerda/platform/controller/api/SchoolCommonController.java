package com.hackerda.platform.controller.api;


import com.hackerda.platform.controller.WebResponse;
import com.hackerda.platform.domain.constant.ErrorCode;
import com.hackerda.platform.controller.vo.CourseTimetableOverviewVO;
import com.hackerda.platform.controller.vo.GradeResultVo;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.infrastructure.database.model.Exam;
import com.hackerda.platform.service.CourseTimeTableService;
import com.hackerda.platform.service.ExamTimeTableService;
import com.hackerda.platform.service.GradeService;
import com.hackerda.platform.service.rbac.UserAuthorizeService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

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

    @RequiresAuthentication
    @RequestMapping(value = "/grade")
    public WebResponse getNowGradeV2() {
        GradeResultVo grade = gradeService.getGrade();

        if (grade.getErrorCode() == ErrorCode.ACCOUNT_OR_PASSWORD_INVALID.getErrorCode()){
            return WebResponse.fail(ErrorCode.ACCOUNT_OR_PASSWORD_INVALID.getErrorCode(), "账号或密码错误");
        }

        return WebResponse.success(grade);
    }

    @RequiresAuthentication
    @RequestMapping(value = "/timetable")
    public WebResponse getTimeTableV2() {

        CourseTimetableOverviewVO vo = courseTimeTableService.getCurrentTermCourseTimeTableByStudent();
        if (vo.getErrorCode() == ErrorCode.ACCOUNT_OR_PASSWORD_INVALID.getErrorCode()){
            return WebResponse.fail(ErrorCode.ACCOUNT_OR_PASSWORD_INVALID.getErrorCode(), "账号或密码错误");
        }
        return WebResponse.success(vo.getCourseTimetableVOList());
    }

    @RequiresAuthentication
    @RequestMapping(value = "/unbind")
    public WebResponse appUnbind(@RequestParam(value = "account", required = false) String account,
                                 @RequestParam(value = "appId") String appId) {

        userAuthorizeService.appStudentRevokeAuthorize(account, appId);
        return WebResponse.success("success");
    }

    @RequiresAuthentication
    @GetMapping("/exam")
    public WebResponse getExamTimeTableByStudent() {
        StudentUserBO wechatStudentUserBO = (StudentUserBO) SecurityUtils.getSubject().getPrincipal();

        List<Exam> examTimeList = examTimeTableService.getExamTimeList(wechatStudentUserBO);
        return WebResponse.success(examTimeList);
    }
}
