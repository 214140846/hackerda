package com.hackerda.platform.infrastructure.repository.grade;

import com.hackerda.platform.MDCThreadPool;
import com.hackerda.platform.domain.SpiderSwitch;
import com.hackerda.platform.domain.constant.ErrorCode;
import com.hackerda.platform.domain.grade.GradeBO;
import com.hackerda.platform.domain.grade.GradeRepository;
import com.hackerda.platform.domain.grade.TermGradeBO;
import com.hackerda.platform.domain.grade.TermGradeViewBO;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.time.SchoolTimeManager;
import com.hackerda.platform.infrastructure.database.dao.GradeDao;
import com.hackerda.platform.infrastructure.database.model.Grade;
import com.hackerda.platform.infrastructure.repository.FetchScene;
import com.hackerda.platform.infrastructure.repository.FetchStatusRecorder;
import com.hackerda.platform.utils.DateUtils;
import com.hackerda.platform.domain.time.SchoolTime;
import com.hackerda.platform.domain.time.Term;
import com.hackerda.spider.exception.PasswordUnCorrectException;
import com.hackerda.spider.exception.UrpEvaluationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.ResourceAccessException;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class GradeRepositoryImpl implements GradeRepository {

    @Autowired
    private GradeDao gradeDao;
    @Autowired
    private GradeAdapter gradeAdapter;


    @Override
    public void save(List<GradeBO> gradeList) {
        if(CollectionUtils.isEmpty(gradeList)){
            return;
        }

        List<Grade> list = gradeList.stream().map(x -> gradeAdapter.toDO(x)).collect(Collectors.toList());

        if(CollectionUtils.isEmpty(list)){
            return;
        }

        gradeDao.insertBatch(list);
    }

    @Override
    public void update(List<GradeBO> gradeList) {
        if(CollectionUtils.isEmpty(gradeList)){
            return;
        }

        List<Grade> list = gradeList.stream().map(x -> gradeAdapter.toDO(x)).collect(Collectors.toList());

        for (Grade grade : list) {
            gradeDao.updateByUniqueIndex(grade);
        }
    }

    @Override
    public void delete(GradeBO grade) {
        gradeDao.deleteByUniqueIndex(gradeAdapter.toDO(grade));
    }

    @Override
    public TermGradeViewBO getAllByStudent(StudentUserBO student) {
        List<GradeBO> gradeBOList = gradeDao.getGradeByAccount(student.getAccount().getInt()).stream()
                .map(x -> gradeAdapter.toBO(x)).collect(Collectors.toList());

        return TermGradeViewBO.ofRepository(gradeBOList);
    }
}
