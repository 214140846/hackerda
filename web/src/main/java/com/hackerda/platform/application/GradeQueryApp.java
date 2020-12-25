package com.hackerda.platform.application;

import com.hackerda.platform.domain.grade.*;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.application.event.EventPublisher;
import com.hackerda.platform.infrastructure.database.model.StudentUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class GradeQueryApp {

    @Autowired
    private GradeOverviewFactory factory;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private EventPublisher eventPublisher;
    @Autowired
    private Queue<GradeFetchTask> gradeFetchQueue;

    public GradeOverviewBO getGradeOverview(StudentUserBO studentUser) {
        return getGradeOverview(studentUser, true);
    }


    public GradeOverviewBO getGradeOverview(StudentUserBO studentUser, boolean isFormUser) {

        if(!studentUser.isMsgHasCheck()) {
            return new GradeOverviewBO(Collections.emptyList());
        }

        GradeOverviewBO gradeOverviewBO = factory.create(studentUser);

        List<GradeBO> updateGrade = gradeOverviewBO.getUpdateGrade();
        gradeRepository.update(updateGrade);

        List<GradeBO> newGrade = gradeOverviewBO.getNewGrade();
        gradeRepository.save(newGrade);

        if(gradeOverviewBO.isFinishFetch()){
            eventPublisher.publishGradeFetchFinish(studentUser.getAccount().toString());
        }

        if(isFormUser && gradeOverviewBO.currentTermGradeUpdate()) {
            gradeFetchQueue.offer(new GradeFetchTask(true, studentUser));
        }

        return gradeOverviewBO;

    }





}
