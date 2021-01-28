package com.hackerda.platform.application;

import com.hackerda.platform.domain.SpiderSwitch;
import com.hackerda.platform.domain.grade.*;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.application.event.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Queue;

@Slf4j
@Service
public class GradeQueryApp {

    @Autowired
    private GradeOverviewFactory factory;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private Queue<GradeFetchTask> gradeFetchQueue;
    @Autowired
    private SpiderSwitch spiderSwitch;

    public GradeOverviewBO getGradeOverview(StudentUserBO studentUser) {
        return getGradeOverview(studentUser, true);
    }


    public GradeOverviewBO getGradeOverview(StudentUserBO studentUser, boolean isFormUser) {

        if(!studentUser.isMsgHasCheck()) {
            return GradeOverviewBO.ofEmpty();
        }

        if(!spiderSwitch.fetchUrp()) {
            return factory.createFromRepo(studentUser);
        }

        GradeOverviewBO gradeOverviewBO = factory.create(studentUser);

        List<GradeBO> updateGrade = gradeOverviewBO.getUpdateGrade();
        gradeRepository.update(updateGrade);

        List<GradeBO> newGrade = gradeOverviewBO.getNewGrade();
        gradeRepository.save(newGrade);

        if(isFormUser && gradeOverviewBO.currentTermGradeUpdate()) {
            gradeFetchQueue.offer(new GradeFetchTask(true, studentUser));
        }

        return gradeOverviewBO;

    }
}
