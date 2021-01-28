package com.hackerda.platform.domain.grade;

import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GradeOverviewBO {

    private TermGradeViewBO termGradeViewBO = TermGradeViewBO.ofEmpty();

    private double gpa;

    private int gpaRank;

    private int gpaRankSize;

    /**
     * 选修课分
     */
    private double optionalCourseCredit;

    private int errorCode;

    private String errorMsg;

    private GradeOverviewBO (TermGradeViewBO termGradeViewBO) {
        setTermGradeViewBO(termGradeViewBO);
    }


    private GradeOverviewBO() {

    }

    public void setTermGradeViewBO (TermGradeViewBO termGradeViewBO) {
        this.termGradeViewBO = termGradeViewBO;

        double sumGradePoint = 0.0;
        double sumCredit = 0.0;
        double sumOptionalCredit = 0.0;

        List<GradeBO> collect = this.termGradeViewBO.getTermGradeBOList().stream()
                .flatMap(x -> x.getGradeList().stream())
                .filter(GradeBO::hasScore)
                .collect(Collectors.toList());

        // count gpa
        for (GradeBO grade : collect) {
            double credit = grade.getCourseCredit();
            sumCredit += credit;
            sumGradePoint += grade.getCreditGradePoint();
            if(grade.isOptional()){
                sumOptionalCredit += credit;
            }
        }
        this.setOptionalCourseCredit(sumOptionalCredit);

        if(sumCredit != 0){
            double f = sumGradePoint / sumCredit;
            BigDecimal b = new BigDecimal(f);
            this.setGpa(b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
        }

    }


    public List<GradeBO> getUpdateGrade(){
        return this.termGradeViewBO.getUpdateGrade();
    }

    public List<GradeBO> getNewGrade(){
        return this.termGradeViewBO.getNewGrade();
    }

    public boolean fetchSuccess(){
        return this.termGradeViewBO.isFetchSuccess();
    }

    public List<GradeBO> getNeedToSendGrade(){
        return this.termGradeViewBO.getNeedToSendGrade();
    }

    /**
     * 以往的成绩是否已经完成抓取
     * @return 完成抓取则返回true
     */
    public boolean isFinishFetch(){
        List<TermGradeBO> termGradeList = this.termGradeViewBO.getTermGradeBOList();
        if (!CollectionUtils.isEmpty(termGradeList)) {
            for (TermGradeBO grade : termGradeList) {
                if(!grade.isFinishFetch()){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public List<TermGradeBO> getTermGradeList() {
        return this.termGradeViewBO.getTermGradeBOList();
    }


    public static GradeOverviewBO create(TermGradeViewBO termGradeViewBO) {

        GradeOverviewBO gradeOverviewBO = new GradeOverviewBO(termGradeViewBO);

        gradeOverviewBO.errorCode = termGradeViewBO.getErrorCode();
        gradeOverviewBO.errorMsg = termGradeViewBO.getErrorMsg();

        return gradeOverviewBO;
    }


    public static GradeOverviewBO ofFetchFail(int errorCode, String msg) {

        GradeOverviewBO gradeOverviewBO = new GradeOverviewBO();

        gradeOverviewBO.setErrorCode(errorCode);
        gradeOverviewBO.setErrorMsg(msg);

        return gradeOverviewBO;
    }

    public static GradeOverviewBO ofEmpty() {
        return new GradeOverviewBO();
    }


    public boolean currentTermGradeUpdate() {
        return !CollectionUtils.isEmpty(getNeedToSendGrade());
    }

}
