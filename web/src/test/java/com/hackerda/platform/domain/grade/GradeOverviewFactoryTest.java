package com.hackerda.platform.domain.grade;

import com.google.common.collect.Lists;
import com.hackerda.platform.infrastructure.database.model.Grade;
import com.hackerda.platform.infrastructure.repository.grade.GradeAdapter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@Slf4j
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class GradeOverviewFactoryTest {

    @Autowired
    private GradeOverviewFactory gradeOverviewFactory;
    @Autowired
    private GradeAdapter gradeAdapter;

    private List<GradeBO> currentGradeList;



    @Before
    public void init() {

        currentGradeList = getGradeList("/currentGrade");
    }

    @SneakyThrows
    private List<GradeBO> getGradeList(String fileName) {

        try (ObjectInputStream out = new ObjectInputStream(new FileInputStream(new File(this.getClass().getResource(fileName).getPath())))) {
            //执行反序列化读取
            Grade[] obj = (Grade[]) out.readObject();
            //将数组转换成List
            return Arrays.stream(obj).map(x-> gradeAdapter.toBO(x)).collect(Collectors.toList());
        }

    }

    private GradeBO randomGet(List<GradeBO> list) {

        int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());

        return list.get(randomNum);
    }


    @Test
    public void checkUpdate() {

        List<GradeBO> copy = getGradeList("/currentGrade");

        GradeBO gradeBO = randomGet(copy);
        gradeBO.setScore(gradeBO.getScore() + 1);


        // 1.1 成绩更新 与数据库对比结果
        TermGradeViewBO termGradeViewBO = gradeOverviewFactory.checkUpdate(TermGradeViewBO.ofFetchSuccess(currentGradeList),
                TermGradeViewBO.ofRepository(copy));
        assertThat(termGradeViewBO.getUpdateGrade().size()).isEqualTo(1);
        assertThat(termGradeViewBO.getScoreUpdateGrade().size()).isEqualTo(1);

        // 1.2 成绩更新 与数据库对比结果
        List<GradeBO> copy1 = getGradeList("/currentGrade");

        GradeBO gradeBO1 = randomGet(copy1);
        gradeBO1.setRank(gradeBO.getRank() + 1);

        TermGradeViewBO termGradeViewBO1 =
                gradeOverviewFactory.checkUpdate(TermGradeViewBO.ofFetchSuccess(currentGradeList),
                TermGradeViewBO.ofRepository(copy1));
        assertThat(termGradeViewBO1.getUpdateGrade().size()).isEqualTo(1);
        assertThat(termGradeViewBO1.getScoreUpdateGrade().size()).isEqualTo(0);


        // 2.数据库没有数据 全部存入
        TermGradeViewBO termGradeViewBO2 =
                gradeOverviewFactory.checkUpdate(TermGradeViewBO.ofFetchSuccess(currentGradeList),
                TermGradeViewBO.ofEmpty());
        assertThat(termGradeViewBO2.getNewGrade().size()).isGreaterThan(0);


        // 3.抓取结果比数据多一条新的数据
        List<GradeBO> copy2 = getGradeList("/currentGrade");
        copy2.remove(0);
        TermGradeViewBO termGradeViewBO3 =
                gradeOverviewFactory.checkUpdate(TermGradeViewBO.ofFetchSuccess(currentGradeList),
                        TermGradeViewBO.ofRepository(copy2));
        assertThat(termGradeViewBO3.getNewGrade().size()).isGreaterThan(0);
    }

    @Test
    public void checkHide() {

        List<GradeBO> copy = getGradeList("/currentGrade");

        GradeBO gradeBO = randomGet(copy);
        gradeBO.setScore(gradeBO.getScore() + 1);


        // 3.抓取结果比数据多一条新的数据
        List<GradeBO> copy2 = getGradeList("/currentGrade");
        copy2.remove(0);
        TermGradeViewBO termGradeViewBO3 =
                gradeOverviewFactory.checkUpdate(TermGradeViewBO.ofFetchSuccess(copy2),
                        TermGradeViewBO.ofRepository(currentGradeList));
        assertThat(termGradeViewBO3.getNewGrade().size()).isEqualTo(0);
        assertThat(termGradeViewBO3.getUpdateGrade().size()).isEqualTo(1);
    }


}