package com.hackerda.platform.controller.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TermGradeVO implements Comparable<TermGradeVO>{
    private List<GradeVO> gradeList;

    private String termYear;

    private int termOrder;

    private boolean isCurrentTerm;

    public String getExecutiveEducationPlanNum() {
        return this.getTermYear() + "-" + this.getTermOrder()+ "-1";
    }

    @Override
    public int compareTo(TermGradeVO o) {

        return this.getExecutiveEducationPlanNum().compareTo(o.getExecutiveEducationPlanNum());
    }
}
