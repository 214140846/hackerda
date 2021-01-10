package com.hackerda.platform.service;

import com.hackerda.platform.domain.course.timetable.CourseTimeTableOverview;
import com.hackerda.platform.application.CourseTimetableQueryApp;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.time.SchoolTimeManager;
import com.hackerda.platform.domain.time.Term;
import com.hackerda.platform.controller.vo.CourseTimeTableVo;
import com.hackerda.platform.controller.vo.CourseTimetableOverviewVO;
import com.hackerda.platform.controller.vo.CourseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yuki
 * @date 2019/8/29 21:40
 */
@Slf4j
@Service
public class CourseTimeTableService {

    @Autowired
    private CourseTimetableQueryApp courseTimetableQueryApp;
    @Autowired
    private SchoolTimeManager schoolTimeManager;
    @Autowired
    private StudentRepository studentRepository;


    public CourseTimetableOverviewVO getCurrentTermCourseTimeTableByStudent() {

        StudentUserBO wechatStudentUserBO = (StudentUserBO) SecurityUtils.getSubject().getPrincipal();

        Term term = schoolTimeManager.getCourseTimeTableTerm();

        CourseTimeTableOverview timeTableOverview = courseTimetableQueryApp.getByStudent(wechatStudentUserBO, term);

        return toVO(timeTableOverview);
    }


    public CourseTimetableOverviewVO getCurrentTermCourseTimeTableByStudent(int account) {

        StudentUserBO studentUserBO = studentRepository.find(new StudentAccount(account));

        Term term = schoolTimeManager.getCourseTimeTableTerm();

        CourseTimeTableOverview timeTableOverview = courseTimetableQueryApp.getByStudent(studentUserBO, term);

        return toVO(timeTableOverview);
    }

    public CourseTimetableOverviewVO toVO(CourseTimeTableOverview timeTableOverview){

        CourseTimetableOverviewVO vo = new CourseTimetableOverviewVO();
        vo.setErrorCode(timeTableOverview.getErrorCode());
        vo.setErrorMsg(timeTableOverview.getErrorMsg());

        if(timeTableOverview.isEmpty()) {
            return vo;
        }

        List<CourseTimeTableVo> collect = timeTableOverview.getCourseTimetableBOList().stream().map(x -> {

            CourseTimeTableVo tableVo = new CourseTimeTableVo();

            tableVo.setRoomName(x.getRoomName());
            tableVo.setRoomNumber(x.getRoomNumber());

            tableVo.setAttendClassTeacher(x.getAttendClassTeacher());
            tableVo.setCampusName(x.getCampusName());
            tableVo.setClassDay(x.getClassDay());
            tableVo.setClassOrder(x.getClassOrder());
            tableVo.setClassInSchoolWeek(x.getClassInSchoolWeek());
            tableVo.setContinuingSession(x.getContinuingSession());
            tableVo.setEndWeek(x.getEndWeek());
            tableVo.setStartWeek(x.getStartWeek());

            tableVo.setTermOrder(x.getTermOrder());
            tableVo.setTermYear(x.getTermYear());
            tableVo.setWeekDescription(x.getWeekDescription());
            tableVo.setStudentCount(x.getStudentCount());

            CourseVO courseVO = new CourseVO();

            courseVO.setNum(x.getCourseBO().getNum());
            courseVO.setCourseOrder(x.getCourseBO().getCourseOrder());
            courseVO.setCredit(x.getCourseBO().getCredit());
            courseVO.setName(x.getCourseBO().getName());

            tableVo.setCourse(courseVO);

            return tableVo;
        }).collect(Collectors.toList());

        vo.setCourseTimetableVOList(collect);

        return vo;

    }

}
