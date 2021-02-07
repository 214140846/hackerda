package com.hackerda.platform.infrastructure.repository.student;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.hackerda.platform.domain.student.*;
import com.hackerda.platform.infrastructure.database.mapper.ext.UrpClassExtMapper;
import com.hackerda.platform.infrastructure.database.model.UrpClass;
import com.hackerda.platform.infrastructure.database.model.example.UrpClassExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@Slf4j
@Repository
public class ClazzInfoRepositoryImpl implements ClazzInfoRepository {

    @Autowired
    private UrpClassExtMapper urpClassExtMapper;

    private final Cache<String, List<AcademyBO>> academyCache = CacheBuilder.newBuilder().build();
    private final Cache<String, List<SubjectBO>> subjectCache = CacheBuilder.newBuilder().build();
    private final Cache<String, List<ClazzBO>> clazzCache = CacheBuilder.newBuilder().build();
    private final Cache<String, Optional<UrpClass>> clazzInfoCache = CacheBuilder.newBuilder().build();

    @Override
    public List<AcademyBO> findAcademy(String grade) {

        UrpClassExample example = new UrpClassExample();
        example.createCriteria().andAdmissionGradeEqualTo(grade);
        try {
            return academyCache.get(grade, () -> urpClassExtMapper.selectByExample(example).stream()
                    .map(x-> new AcademyBO(grade, x.getAcademyName(), x.getAcademyNum()))
                    .distinct()
                    .sorted(Comparator.comparing(x-> Integer.parseInt(x.getNum())))
                    .collect(Collectors.toList()));
        } catch (ExecutionException e) {
            log.error("findAcademy grade {} cache error", grade, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<SubjectBO> findSubject(String grade, String academyNum) {
        UrpClassExample example = new UrpClassExample();
        example.createCriteria().andAdmissionGradeEqualTo(grade)
        .andAcademyNumEqualTo(academyNum);
        try {
            return subjectCache.get(grade+ academyNum, () -> urpClassExtMapper.selectByExample(example).stream()
                    .map(x-> new SubjectBO(grade, x.getSubjectName(), x.getSubjectNum()))
                    .distinct()
                    .sorted(Comparator.comparing(SubjectBO::getNum))
                    .collect(Collectors.toList()));
        } catch (ExecutionException e) {
            log.error("findSubject grade {} cache error", grade, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<ClazzBO> findClazz(String grade, String academyNum, String subjectNum) {
        UrpClassExample example = new UrpClassExample();
        example.createCriteria().andAdmissionGradeEqualTo(grade)
                .andAcademyNumEqualTo(academyNum)
                .andSubjectNumEqualTo(subjectNum);
        try {
            return clazzCache.get(grade+ academyNum+ subjectNum,
                    () -> urpClassExtMapper.selectByExample(example).stream()
                    .map(x-> new ClazzBO(grade, x.getClassName(), x.getClassNum()))
                    .distinct()
                    .sorted(Comparator.comparing(x-> Integer.parseInt(x.getNum())))
                    .collect(Collectors.toList()));
        } catch (ExecutionException e) {
            log.error("findClazz grade {} cache error", grade, e);
            return Collections.emptyList();
        }
    }

    @Override
    public ClazzInfoBO findClassByNum(String classNum) {

        UrpClassExample example = new UrpClassExample();
        example.createCriteria().andClassNumEqualTo(classNum);

        try {
            Optional<UrpClass> optional = clazzInfoCache.get(classNum, () -> urpClassExtMapper.selectByExample(example).stream().findFirst());

            UrpClass urpClass = optional.orElse(null);

            if(urpClass == null) {
                return null;
            }

            SubjectBO subjectBO = new SubjectBO(urpClass.getAdmissionGrade(), urpClass.getSubjectName(), urpClass.getSubjectNum());
            ClazzBO clazzBO = new ClazzBO(urpClass.getAdmissionGrade(), urpClass.getClassName(), urpClass.getClassNum());
            AcademyBO academyBO = new AcademyBO(urpClass.getAdmissionGrade(), urpClass.getAcademyName(), urpClass.getAcademyNum());

            return new ClazzInfoBO(academyBO, subjectBO, clazzBO);

        } catch (ExecutionException e) {
            log.error("findAcademy urpClass {} cache error", classNum, e);
            return null;
        }
    }
}
