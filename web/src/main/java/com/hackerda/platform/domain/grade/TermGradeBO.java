package com.hackerda.platform.domain.grade;

import com.hackerda.platform.domain.time.Term;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class TermGradeBO implements Comparable<TermGradeBO> {
    private List<GradeBO> gradeList = Collections.emptyList();

    private Term term;

    private boolean fetchSuccess;

    private boolean finishFetch;

    private String errorMsg;

    private int errorCode;

    public String getExecutiveEducationPlanNum() {
        return this.term.getExecutiveEducationPlanNum();
    }

    @Override
    public int compareTo(TermGradeBO o) {
        return this.getExecutiveEducationPlanNum().compareTo(o.getExecutiveEducationPlanNum());
    }

    public List<GradeBO> getUpdate(){
        return gradeList.stream().filter(GradeBO::isUpdate).collect(Collectors.toList());
    }

    public List<GradeBO> getNew(){
        return gradeList.stream().filter(GradeBO::isNewGrade).collect(Collectors.toList());
    }

}
