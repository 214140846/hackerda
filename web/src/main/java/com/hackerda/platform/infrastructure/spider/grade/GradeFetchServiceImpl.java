package com.hackerda.platform.infrastructure.spider.grade;

import com.hackerda.platform.MDCThreadPool;
import com.hackerda.platform.domain.constant.ErrorCode;
import com.hackerda.platform.domain.grade.*;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.infrastructure.repository.grade.GradeAdapter;
import com.hackerda.platform.infrastructure.repository.grade.GradeSpiderFacade;
import com.hackerda.spider.exception.PasswordUnCorrectException;
import com.hackerda.spider.exception.UrpEvaluationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GradeFetchServiceImpl implements GradeFetchService {

    @Autowired
    private GradeSpiderFacade gradeSpiderFacade;
    @Autowired
    private GradeAdapter gradeAdapter;
    @Value("${spider.grade.timeout: 5000}")
    private int getGradeTimeout;

    private final ExecutorService gradeAutoUpdatePool = new MDCThreadPool(8, 8,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), r -> new Thread(r, "gradeSearch"));

    @Override
    public GradeOverviewBO getAllByStudent(StudentUserBO student) {
        CompletableFuture<TermGradeViewBO> currentFuture =
                CompletableFuture.supplyAsync(() -> {
                            List<GradeBO> gradeBOList =
                                    gradeSpiderFacade.getCurrentTermGrade(student).stream().map(grade -> gradeAdapter.toBO(grade)).collect(Collectors.toList());

                            return TermGradeViewBO.ofFetchSuccess(gradeBOList);
                        },
                        gradeAutoUpdatePool);

        CompletableFuture<TermGradeViewBO> schemeFuture = getSchemeFuture(student);

        CompletableFuture<GradeOverviewBO> completableFuture = currentFuture.thenCombine(schemeFuture,
                (x, y) -> GradeOverviewBO.create(x.combine(y)));
        try {
            return completableFuture.get(getGradeTimeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return handleException(e);
        }
    }


    private CompletableFuture<TermGradeViewBO> getSchemeFuture(StudentUserBO student) {
        return CompletableFuture.supplyAsync(() -> {
            List<GradeBO> gradeBOList = gradeSpiderFacade.getSchemeGrade(student).stream().map(grade -> gradeAdapter.toBO(grade)).collect(Collectors.toList());

            return TermGradeViewBO.ofFetchSuccess(gradeBOList);

        }, gradeAutoUpdatePool);
    }


    private GradeOverviewBO handleException(Exception exception) {
        int errorCode;
        String msg;

        if(exception instanceof ExecutionException) {
            Throwable cause = exception.getCause();
            if (cause instanceof PasswordUnCorrectException) {
                throw (PasswordUnCorrectException) cause;
            }
            else if (cause instanceof UrpEvaluationException) {
                errorCode = ErrorCode.Evaluation_ERROR.getErrorCode();
                msg = "评估未完成，无法查看成绩";
            } else {
                errorCode = ErrorCode.SYSTEM_ERROR.getErrorCode();
                msg = exception.getMessage();
                log.error("get grade error", exception);
            }

        }else if(exception instanceof TimeoutException) {
            errorCode = ErrorCode.SYSTEM_ERROR.getErrorCode();
            msg = "抓取超时";
        }else {
            errorCode = ErrorCode.SYSTEM_ERROR.getErrorCode();
            msg = exception.getMessage();
            log.error("get grade error", exception);
        }

        return GradeOverviewBO.ofFetchFail(errorCode, msg);

    }
}
