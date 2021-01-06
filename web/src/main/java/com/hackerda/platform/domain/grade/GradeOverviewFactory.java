package com.hackerda.platform.domain.grade;

import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.service.GpaRanker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeOverviewFactory {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private GpaRanker gpaRanker;


    public GradeOverviewBO create(StudentUserBO student) {


        TermGradeViewBO termGradeViewBO = gradeRepository.getAllByStudent(student);

        GradeOverviewBO bo = new GradeOverviewBO(termGradeViewBO);

        if(bo.fetchSuccess()){
            bo.setErrorCode(0);
        }

        GpaRanker.RankResult rankResult = gpaRanker.rank(student, bo.getGpa());

        bo.setGpaRank(rankResult.getRank());
        bo.setGpaRankSize(rankResult.getSize());

        return bo;
    }






}
