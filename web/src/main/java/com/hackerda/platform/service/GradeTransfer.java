package com.hackerda.platform.service;

import com.hackerda.platform.domain.grade.GradeBO;
import com.hackerda.platform.domain.grade.GradeOverviewBO;
import com.hackerda.platform.domain.grade.TermGradeBO;
import com.hackerda.platform.controller.vo.GradeResultVO;
import com.hackerda.platform.controller.vo.GradeVO;
import com.hackerda.platform.controller.vo.TermGradeVO;
import com.hackerda.platform.domain.time.SchoolTimeManager;
import com.hackerda.platform.domain.time.Term;
import com.hackerda.platform.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class GradeTransfer {

    @Autowired
    private CourseTransfer courseTransfer;
    @Autowired
    private SchoolTimeManager schoolTimeManager;

    public GradeResultVO adapter2VO(GradeOverviewBO gradeOverviewBO){
        GradeResultVO vo = new GradeResultVO();

        vo.setGpa(gradeOverviewBO.getGpa());
        vo.setGpaRank(gradeOverviewBO.getGpaRank());
        vo.setGpaRankSize(gradeOverviewBO.getGpaRankSize());
        vo.setOptionalCourseCredit(gradeOverviewBO.getOptionalCourseCredit());

        vo.setTermGradeList(gradeOverviewBO.getTermGradeList().stream()
                .map(this::adapter2VO)
                .collect(Collectors.toList()));

        vo.setErrorCode(gradeOverviewBO.getErrorCode());
        vo.setMessage(gradeOverviewBO.getErrorMsg());

        return vo;
    }

    public TermGradeVO adapter2VO(TermGradeBO termGradeBO){
        TermGradeVO vo = new TermGradeVO();
        Term term = termGradeBO.getTerm();
        vo.setCurrentTerm(term.equals(schoolTimeManager.getCurrentSchoolTime().getTerm()));
        vo.setTermOrder(term.getOrder());
        vo.setTermYear(term.getTermYear());
        vo.setGradeList(termGradeBO.getGradeList().stream()
                .filter(GradeBO::isShow)
                .map(this::adapter2VO)
                .sorted()
                .collect(Collectors.toList()));

        return vo;
    }

    public GradeVO adapter2VO(GradeBO gradeBO){
        GradeVO vo = new GradeVO();
        vo.setCourse(courseTransfer.adapter2VO(gradeBO.getCourse()));
        vo.setScore(gradeBO.getScore());
        vo.setExamTime(DateUtils.localDateToDate(gradeBO.getExamTime(), DateUtils.YYYY_MM_DD_PATTERN));
        vo.setOperateTime(gradeBO.getOperateTime());

        vo.setCoursePropertyName(gradeBO.getCoursePropertyName());

        vo.setRank(gradeBO.getRank());
        vo.setTermOrder(gradeBO.getTermOrder());
        vo.setTermYear(gradeBO.getTermYear());
        vo.setGradePoint(gradeBO.getGradePoint());


        return vo;
    }
}
