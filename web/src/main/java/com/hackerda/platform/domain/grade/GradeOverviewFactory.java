package com.hackerda.platform.domain.grade;

import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.service.GpaRanker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GradeOverviewFactory {

    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private GradeFetchService gradeFetchService;
    @Autowired
    private GpaRanker gpaRanker;


    public GradeOverviewBO create(StudentUserBO student) {

        GradeOverviewBO fetchView = gradeFetchService.getAllByStudent(student);

        TermGradeViewBO termGradeViewBO = gradeRepository.getAllByStudent(student);

        if(fetchView.fetchSuccess()) {
            TermGradeViewBO gradeViewBO = checkUpdate(fetchView.getTermGradeViewBO(), termGradeViewBO);
            fetchView.setTermGradeViewBO(gradeViewBO);
        } else {
            fetchView.setTermGradeViewBO(termGradeViewBO);
        }

        GpaRanker.RankResult rankResult = gpaRanker.rank(student, fetchView.getGpa());

        fetchView.setGpaRank(rankResult.getRank());
        fetchView.setGpaRankSize(rankResult.getSize());

        return fetchView;
    }

    public GradeOverviewBO createFromRepo(StudentUserBO student) {

        GradeOverviewBO overview = GradeOverviewBO.create(gradeRepository.getAllByStudent(student));

        GpaRanker.RankResult rankResult = gpaRanker.rank(student, overview.getGpa());

        overview.setGpaRank(rankResult.getRank());
        overview.setGpaRankSize(rankResult.getSize());

        return overview;
    }



    /**
     * 返回新一个新成绩的list，旧的的成绩会被过滤
     */
    TermGradeViewBO checkUpdate(TermGradeViewBO fetchView, TermGradeViewBO fromRepo) {

        List<GradeBO> gradeList = fetchView
                .getTermGradeBOList().stream()
                .filter(x-> !x.isFinishFetch())
                .map(TermGradeBO::getGradeList)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());


        // 如果数据库之前没有保存过该学号成绩，则直接返回抓取结果
        if (CollectionUtils.isEmpty(fromRepo.getTermGradeBOList())) {
            gradeList.forEach(x-> x.setNewGrade(true));
            return fetchView;
        }

        // 课程的唯一id作为map的key
        Function<GradeBO, String> keyMapper =
                grade -> grade.getCourseNumber() + grade.getCourseOrder()+grade.getTermYear()+grade.getTermOrder();

        // 这个逻辑是处理教务网同一个课程返回两个结果，那么选择有成绩的结果
        BinaryOperator<GradeBO> binaryOperator = (oldValue, newValue) -> {
            if (oldValue.getScore() == -1) {
                return newValue;
            } else {
                return oldValue;
            }
        };


        Map<String, GradeBO> spiderGradeMap = gradeList.stream()
                .collect(Collectors.toMap(keyMapper, x -> x, binaryOperator));

        Map<String, GradeBO> dbGradeMap = fromRepo.getTermGradeBOList().stream()
                .flatMap(x-> x.getGradeList().stream())
                .collect(Collectors.toMap(keyMapper, x -> x, binaryOperator));

        List<GradeBO> resultList = spiderGradeMap.entrySet().stream()
                .map(entry -> {
                    GradeBO grade = dbGradeMap.remove(entry.getKey());
                    GradeBO value = entry.getValue();
                    if (grade == null) {
                        value.setNewGrade(true);
                        return value;
                    }

                    if (!Objects.equals(grade, value)) {
                        value.setUpdate(true);
                        value.setScoreUpdate(!value.getScore().equals(grade.getScore()));
                        return value;
                    }
                    grade.setShow(true);
                    return grade;
                }).collect(Collectors.toList());


        for (GradeBO gradeBO : dbGradeMap.values()) {
            gradeBO.setUpdate(gradeBO.isShow());
            gradeBO.setShow(false);
        }

        resultList.addAll(dbGradeMap.values());

        return TermGradeViewBO.ofFetchSuccess(resultList);
    }


}
