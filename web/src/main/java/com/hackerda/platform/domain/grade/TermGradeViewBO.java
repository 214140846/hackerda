package com.hackerda.platform.domain.grade;

import com.hackerda.platform.domain.time.Term;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class TermGradeViewBO {

    private List<TermGradeBO> termGradeBOList;

    private int errorCode;

    private String errorMsg;

    private boolean fetchSuccess;

    private boolean fromRepository;


    public TermGradeViewBO combine(TermGradeViewBO other) {
        termGradeBOList.addAll(other.termGradeBOList);

        this.fetchSuccess = other.fetchSuccess && this.fetchSuccess;
        this.fromRepository = other.fromRepository && this.fromRepository;

        return this;
    }

    public List<GradeBO> getUpdateGrade(){
        if(!CollectionUtils.isEmpty(termGradeBOList)) {
            return termGradeBOList.stream().map(TermGradeBO::getGradeList)
                    .flatMap(Collection::stream)
                    .filter(GradeBO::isUpdate)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<GradeBO> getNewGrade(){
        if(!CollectionUtils.isEmpty(termGradeBOList)) {
            return termGradeBOList.stream().map(TermGradeBO::getGradeList)
                    .flatMap(Collection::stream)
                    .filter(GradeBO::isNewGrade)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<GradeBO> getScoreUpdateGrade(){

        if(!CollectionUtils.isEmpty(termGradeBOList)) {
            return termGradeBOList.stream().map(TermGradeBO::getGradeList)
                    .flatMap(Collection::stream)
                    .filter(GradeBO::isScoreUpdate)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<GradeBO> getNeedToSendGrade(){

        if(!CollectionUtils.isEmpty(termGradeBOList)){
            return termGradeBOList.get(0).getGradeList()
                    .stream()
                    .filter(GradeBO::hasScore)
                    .filter(x-> x.isScoreUpdate() || x.isNewGrade())
                    .filter(GradeBO::isTodayUpdate)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public int size() {
        return termGradeBOList.stream().mapToInt(TermGradeBO ::size).sum();
    }



    /**
     * factory method
     */

    public static TermGradeViewBO ofEmpty() {
        TermGradeViewBO termGradeViewBO = new TermGradeViewBO();
        termGradeViewBO.setTermGradeBOList(Collections.emptyList());
        termGradeViewBO.setFetchSuccess(false);
        termGradeViewBO.setFromRepository(false);

        return termGradeViewBO;
    }

    public static TermGradeViewBO ofFetchSuccess(List<GradeBO> gradeList) {
        TermGradeViewBO termGradeViewBO = new TermGradeViewBO();

        List<TermGradeBO> termGradeBOList = getTermGradeList(gradeList);

        termGradeViewBO.setTermGradeBOList(termGradeBOList);
        termGradeViewBO.setErrorCode(0);
        termGradeViewBO.setErrorMsg("");
        termGradeViewBO.setFetchSuccess(true);
        termGradeViewBO.setFromRepository(false);

        return termGradeViewBO;
    }

    public static TermGradeViewBO ofRepository(List<GradeBO> gradeList) {
        TermGradeViewBO termGradeViewBO = new TermGradeViewBO();

        List<TermGradeBO> termGradeBOList = getTermGradeList(gradeList);

        termGradeViewBO.setTermGradeBOList(termGradeBOList);
        termGradeViewBO.setErrorCode(0);
        termGradeViewBO.setErrorMsg("");
        termGradeViewBO.setFetchSuccess(false);
        termGradeViewBO.setFromRepository(true);

        return termGradeViewBO;

    }

    @NotNull
    private static List<TermGradeBO> getTermGradeList(List<GradeBO> gradeList) {
        return gradeList.stream()
                .collect(Collectors.groupingBy(x -> new Term(x.getTermYear(), x.getTermOrder())))
                .entrySet().stream()
                .map(x -> new TermGradeBO()
                        .setTerm(new Term(x.getKey().getTermYear(), x.getKey().getOrder()))
                        .setGradeList((new ArrayList<>(x.getValue())))
                        .setFetchSuccess(true)
                        .setFinishFetch(false)
                )
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }



}
