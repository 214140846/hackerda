package com.hackerda.platform.domain.grade;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class TermGradeViewBO {

    private List<TermGradeBO> termGradeBOList;

    private int errorCode;

    private String errorMsg;


    public static TermGradeViewBO ofEmpty() {
        TermGradeViewBO termGradeViewBO = new TermGradeViewBO();
        termGradeViewBO.setTermGradeBOList(Collections.emptyList());
        return termGradeViewBO;
    }
}
