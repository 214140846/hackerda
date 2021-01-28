package com.hackerda.platform.infrastructure.spider.grade;

import com.hackerda.platform.domain.constant.ErrorCode;
import com.hackerda.platform.domain.grade.GradeOverviewBO;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.infrastructure.database.model.Grade;
import com.hackerda.platform.infrastructure.repository.grade.GradeSpiderFacade;
import com.hackerda.platform.infrastructure.repository.student.StudentRepositoryImpl;
import com.hackerda.spider.exception.PasswordUnCorrectException;
import com.hackerda.spider.exception.UrpEvaluationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest
@TestPropertySource(properties = {
        "spider.grade.timeout=500",
})
public class GradeFetchServiceImplTest {

    @Autowired
    private StudentRepositoryImpl studentUserRepository;
    @Autowired
    private GradeFetchServiceImpl gradeFetchService;
    @MockBean
    private GradeSpiderFacade gradeSpiderFacade;

    private List<Grade> currentGradeList;

    private List<Grade> schemeGradeList;

    @Before
    public void init() throws IOException, ClassNotFoundException {

        try (ObjectInputStream out = new ObjectInputStream(new FileInputStream(new File(this.getClass().getResource("/currentGrade").getPath())))) {
            //执行反序列化读取
            Grade[] obj = (Grade[]) out.readObject();
            //将数组转换成List
            currentGradeList = Arrays.asList(obj);
        }

        try (ObjectInputStream out = new ObjectInputStream(new FileInputStream(new File(this.getClass().getResource(
                "/everGrade").getPath())))) {
            //执行反序列化读取
            Grade[] obj = (Grade[]) out.readObject();
            //将数组转换成List
            schemeGradeList = Arrays.asList(obj);
        }

    }


    @Test
    public void testFetchSuccess() {
        doReturn(currentGradeList).when(gradeSpiderFacade).getCurrentTermGrade(any());
        doReturn(schemeGradeList).when(gradeSpiderFacade).getSchemeGrade(any());

        WechatStudentUserBO user = studentUserRepository.findWetChatUser(new StudentAccount(2017025838));
        GradeOverviewBO overviewBO = gradeFetchService.getAllByStudent(user);

        assertThat(overviewBO.fetchSuccess()).isTrue();


        doReturn(Collections.emptyList()).when(gradeSpiderFacade).getCurrentTermGrade(any());
        doReturn(Collections.emptyList()).when(gradeSpiderFacade).getSchemeGrade(any());

        GradeOverviewBO emptyView = gradeFetchService.getAllByStudent(user);

        assertThat(emptyView.fetchSuccess()).isTrue();


        doReturn(currentGradeList).when(gradeSpiderFacade).getCurrentTermGrade(any());
        doReturn(Collections.emptyList()).when(gradeSpiderFacade).getSchemeGrade(any());

        GradeOverviewBO emptyView2 = gradeFetchService.getAllByStudent(user);

        assertThat(emptyView2.fetchSuccess()).isTrue();


        doReturn(Collections.emptyList()).when(gradeSpiderFacade).getCurrentTermGrade(any());
        doReturn(schemeGradeList).when(gradeSpiderFacade).getSchemeGrade(any());

        GradeOverviewBO emptyView3 = gradeFetchService.getAllByStudent(user);

        assertThat(emptyView3.fetchSuccess()).isTrue();
    }

    @Test
    public void testFetchPasswordError() {
        WechatStudentUserBO user = studentUserRepository.findWetChatUser(new StudentAccount(2017025838));

        doReturn(currentGradeList).when(gradeSpiderFacade).getCurrentTermGrade(any());
        doThrow(new PasswordUnCorrectException()).when(gradeSpiderFacade).getSchemeGrade(any());

        assertThatExceptionOfType(PasswordUnCorrectException.class).isThrownBy(() -> gradeFetchService.getAllByStudent(user));

        doThrow(new PasswordUnCorrectException()).when(gradeSpiderFacade).getCurrentTermGrade(any());
        doReturn(schemeGradeList).when(gradeSpiderFacade).getSchemeGrade(any());

        assertThatExceptionOfType(PasswordUnCorrectException.class).isThrownBy(() -> gradeFetchService.getAllByStudent(user));

    }


    @Test
    public void testCommonError() {
        WechatStudentUserBO user = studentUserRepository.findWetChatUser(new StudentAccount(2017025838));

        doThrow(new UrpEvaluationException("")).when(gradeSpiderFacade).getCurrentTermGrade(any());
        doReturn(schemeGradeList).when(gradeSpiderFacade).getSchemeGrade(any());

        GradeOverviewBO view = gradeFetchService.getAllByStudent(user);

        assertThat(view.fetchSuccess()).isFalse();
        assertThat(view.getErrorCode()).isEqualTo(ErrorCode.Evaluation_ERROR.getErrorCode());



        doReturn(schemeGradeList).when(gradeSpiderFacade).getCurrentTermGrade(any());
        doThrow(new UrpEvaluationException("")).when(gradeSpiderFacade).getSchemeGrade(any());

        GradeOverviewBO view2 = gradeFetchService.getAllByStudent(user);

        assertThat(view2.fetchSuccess()).isFalse();
        assertThat(view2.getErrorCode()).isEqualTo(ErrorCode.Evaluation_ERROR.getErrorCode());
    }


    @Test
    public void testTimeout() {
        WechatStudentUserBO user = studentUserRepository.findWetChatUser(new StudentAccount(2017025838));

        doAnswer((x) -> {
            Thread.sleep(700);
            return currentGradeList;
        }).when(gradeSpiderFacade).getCurrentTermGrade(any());
        doReturn(schemeGradeList).when(gradeSpiderFacade).getSchemeGrade(any());

        GradeOverviewBO view = gradeFetchService.getAllByStudent(user);

        assertThat(view.fetchSuccess()).isFalse();
        assertThat(view.getErrorCode()).isEqualTo(ErrorCode.SYSTEM_ERROR.getErrorCode());

    }


}