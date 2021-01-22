package com.hackerda.platform.infrastructure.spider.grade;

import com.hackerda.platform.MDCThreadPool;
import com.hackerda.platform.domain.constant.ErrorCode;
import com.hackerda.platform.domain.grade.GradeFetchService;
import com.hackerda.platform.domain.grade.TermGradeBO;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.time.SchoolTimeManager;
import com.hackerda.platform.infrastructure.database.dao.GradeDao;
import com.hackerda.platform.infrastructure.database.model.Grade;
import com.hackerda.platform.infrastructure.repository.FetchScene;
import com.hackerda.platform.infrastructure.repository.FetchStatusRecorder;
import com.hackerda.platform.infrastructure.repository.grade.GradeAdapter;
import com.hackerda.platform.infrastructure.repository.grade.GradeSpiderFacade;
import com.hackerda.platform.utils.DateUtils;
import com.hackerda.platform.domain.time.SchoolTime;
import com.hackerda.platform.domain.time.Term;
import com.hackerda.spider.exception.PasswordUnCorrectException;
import com.hackerda.spider.exception.UrpEvaluationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GradeFetchServiceImpl implements GradeFetchService {

    @Autowired
    private GradeSpiderFacade gradeSpiderFacade;
    @Autowired
    private FetchStatusRecorder recorder;
    @Autowired
    private GradeAdapter gradeAdapter;
    @Autowired
    private GradeDao gradeDao;
    @Value("${spider.grade.timeout: 5000}")
    private int getGradeTimeout;
    @Autowired
    private SchoolTimeManager schoolTimeManager;

    private final ExecutorService gradeAutoUpdatePool = new MDCThreadPool(8, 8,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), r -> new Thread(r, "gradeSearch"));

    @Override
    public List<TermGradeBO> getAllByStudent(StudentUserBO student) {
        CompletableFuture<List<TermGradeBO>> currentFuture =
                CompletableFuture.supplyAsync(() -> {
                            List<TermGradeBO> gradeBOList = gradeToTermGradeList(gradeSpiderFacade.getCurrentTermGrade(student));
                            gradeBOList.forEach(x-> x.setFetchSuccess(true));
                            return gradeBOList;
                        },
                        gradeAutoUpdatePool);

        CompletableFuture<List<TermGradeBO>> schemeFuture = getSchemeFuture(student);

        CompletableFuture<List<TermGradeBO>> completableFuture = currentFuture.thenCombine(schemeFuture,
                (x, y) -> {
                    x.addAll(y);
                    return x;
                });


        try {
            return completableFuture.get(getGradeTimeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("get grade error", e);
            throw new RuntimeException(e);
        }
    }


    private CompletableFuture<List<TermGradeBO>> getSchemeFuture(StudentUserBO student) {
        if (recorder.needToFetch(FetchScene.EVER_GRADE, student.getAccount().toString())) {
            return CompletableFuture.supplyAsync(() -> {
                List<TermGradeBO> gradeBOList = gradeToTermGradeList(gradeSpiderFacade.getSchemeGrade(student));
                gradeBOList.forEach(x-> x.setFetchSuccess(true));
                return gradeBOList;
            }, gradeAutoUpdatePool);
        }else {
            List<TermGradeBO> termGradeBOList =
                    gradeToTermGradeList(gradeDao.getEverTermGradeByAccount(student.getAccount().getInt()));
            termGradeBOList.forEach(x-> x.setFinishFetch(true));
            return CompletableFuture.completedFuture(termGradeBOList);
        }
    }


    private List<TermGradeBO> gradeToTermGradeList(List<Grade> gradeList) {
        SchoolTime schoolTime = schoolTimeManager.getCurrentSchoolTime();

        return gradeList.stream()
                .collect(Collectors.groupingBy(x -> new Term(x.getTermYear(), x.getTermOrder())))
                .entrySet().stream()
                .map(x -> new TermGradeBO()
                        .setTermYear(x.getKey().getTermYear())
                        .setTermOrder(x.getKey().getOrder())
                        .setGradeList((x.getValue().stream()
                                .map(grade -> gradeAdapter.toBO(grade))
                                .collect(Collectors.toList())))
                        .setCurrentTerm(schoolTime.getTerm().equals(x.getKey()))
                )
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }


    private void handleException(List<TermGradeBO> termGradeList, Exception exception) {
        int errorCode;
        String msg;

        if(exception instanceof ExecutionException) {
            Throwable cause = exception.getCause();
            if (cause instanceof PasswordUnCorrectException) {
                errorCode = ErrorCode.ACCOUNT_OR_PASSWORD_INVALID.getErrorCode();
                msg = "账号或密码错误";
            }
            else if (cause instanceof UrpEvaluationException) {
                errorCode = ErrorCode.Evaluation_ERROR.getErrorCode();
                msg = "评估未完成，无法查看成绩";
            } else {
                errorCode = ErrorCode.SYSTEM_ERROR.getErrorCode();
                msg = exception.getMessage();
                log.error("get grade error", exception);
            }

        }else if(exception instanceof  TimeoutException) {
            errorCode = ErrorCode.SYSTEM_ERROR.getErrorCode();
            msg = "抓取超时";
        }else {
            errorCode = ErrorCode.SYSTEM_ERROR.getErrorCode();
            msg = exception.getMessage();
            log.error("get grade error", exception);
        }

        termGradeList.forEach(x-> {
            x.setErrorCode(errorCode);
            x.setFetchSuccess(false);
            x.setErrorMsg(msg);
        });

    }
}
