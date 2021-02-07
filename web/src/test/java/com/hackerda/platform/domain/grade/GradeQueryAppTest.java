package com.hackerda.platform.domain.grade;

import com.hackerda.platform.application.GradeQueryApp;
import com.hackerda.platform.domain.SpiderSwitch;
import com.hackerda.platform.domain.constant.ErrorCode;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.infrastructure.database.model.Grade;
import com.hackerda.platform.infrastructure.repository.grade.GradeSpiderFacade;
import com.hackerda.platform.infrastructure.repository.student.StudentRepositoryImpl;
import com.hackerda.spider.exception.PasswordUnCorrectException;
import com.hackerda.spider.exception.UrpEvaluationException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GradeQueryAppTest {
    @Autowired
    private GradeQueryApp gradeQueryApp;
    @Autowired
    private StudentRepositoryImpl studentUserRepository;
    @MockBean
    private GradeSpiderFacade gradeSpiderFacade;
    @MockBean
    private SpiderSwitch spiderSwitch;

    private List<Grade> currentGradeList;

    private List<Grade> schemeGradeList;

    @Before
    public void init() {

        currentGradeList = getGradeList("/currentGrade");
        schemeGradeList = getGradeList("/everGrade");
    }

    @SneakyThrows
    private List<Grade> getGradeList(String fileName) {

        try (ObjectInputStream out = new ObjectInputStream(new FileInputStream(this.getClass().getResource(fileName).getPath()))) {
            //执行反序列化读取
            Grade[] obj = (Grade[]) out.readObject();
            //将数组转换成List
            return Lists.newArrayList(obj);
        }

    }

    @Test
    public void testFetchSuccess(){
        doReturn(true).when(spiderSwitch).fetchUrp();
        doReturn(currentGradeList).when(gradeSpiderFacade).getCurrentTermGrade(any());
        doReturn(schemeGradeList).when(gradeSpiderFacade).getSchemeGrade(any());

        WechatStudentUserBO user = studentUserRepository.findWetChatUser(new StudentAccount(2017025838));

        GradeOverviewBO bo = gradeQueryApp.getGradeOverview(user);

        assertThat(bo.fetchSuccess()).isTrue();
        assertThat(bo.getUpdateGrade().size()).isEqualTo(0);
        assertThat(bo.getNewGrade().size()).isGreaterThan(0);


        GradeOverviewBO bo1 = gradeQueryApp.getGradeOverview(user);
        assertThat(bo1.fetchSuccess()).isTrue();
        assertThat(bo1.getUpdateGrade().size()).isEqualTo(0);
        assertThat(bo1.getTermGradeList().size()).isGreaterThan(0);
    }

    @Test
    public void testUpdateAfterFetchSuccess(){
        doReturn(true).when(spiderSwitch).fetchUrp();
        doReturn(currentGradeList).when(gradeSpiderFacade).getCurrentTermGrade(any());
        doReturn(schemeGradeList).when(gradeSpiderFacade).getSchemeGrade(any());

        WechatStudentUserBO user = studentUserRepository.findWetChatUser(new StudentAccount(2017025838));

        GradeOverviewBO bo = gradeQueryApp.getGradeOverview(user);

        assertThat(bo.fetchSuccess()).isTrue();
        assertThat(bo.getNewGrade().size()).isGreaterThan(0);


        List<Grade> currentGradeList = getGradeList("/currentGrade");
        currentGradeList.remove(0);
        doReturn(currentGradeList).when(gradeSpiderFacade).getCurrentTermGrade(any());
        GradeOverviewBO bo1 = gradeQueryApp.getGradeOverview(user);
        assertThat(bo1.fetchSuccess()).isTrue();
        assertThat(bo1.getUpdateGrade().size()).isEqualTo(1);
        assertThat(bo1.getUpdateGrade().stream().findAny().get().isShow()).isFalse();
    }

    @Test
    public void testFailAfterFetchSuccess(){
        doReturn(true).when(spiderSwitch).fetchUrp();
        doReturn(currentGradeList).when(gradeSpiderFacade).getCurrentTermGrade(any());
        doReturn(schemeGradeList).when(gradeSpiderFacade).getSchemeGrade(any());

        WechatStudentUserBO user = studentUserRepository.findWetChatUser(new StudentAccount(2017025838));

        GradeOverviewBO bo = gradeQueryApp.getGradeOverview(user);

        assertThat(bo.fetchSuccess()).isTrue();

        doThrow(new UrpEvaluationException("")).when(gradeSpiderFacade).getCurrentTermGrade(any());
        doReturn(schemeGradeList).when(gradeSpiderFacade).getSchemeGrade(any());

        GradeOverviewBO bo1 = gradeQueryApp.getGradeOverview(user);
        assertThat(bo1.fetchSuccess()).isFalse();
        assertThat(bo1.getTermGradeList().size()).isGreaterThan(0);
    }

    @Test
    public void testSwitch(){
        doReturn(true).when(spiderSwitch).fetchUrp();
        doReturn(currentGradeList).when(gradeSpiderFacade).getCurrentTermGrade(any());
        doReturn(schemeGradeList).when(gradeSpiderFacade).getSchemeGrade(any());

        WechatStudentUserBO user = studentUserRepository.findWetChatUser(new StudentAccount(2017025838));

        GradeOverviewBO bo = gradeQueryApp.getGradeOverview(user);

        assertThat(bo.fetchSuccess()).isTrue();

        doReturn(false).when(spiderSwitch).fetchUrp();

        GradeOverviewBO bo1 = gradeQueryApp.getGradeOverview(user);
        assertThat(bo1.fetchSuccess()).isFalse();
        assertThat(bo1.getTermGradeList().size()).isGreaterThan(0);
    }

    @Test
    public void testPasswordUnCorrect(){
        doReturn(true).when(spiderSwitch).fetchUrp();
        doThrow(new PasswordUnCorrectException()).when(gradeSpiderFacade).getCurrentTermGrade(any());
        doReturn(schemeGradeList).when(gradeSpiderFacade).getSchemeGrade(any());

        WechatStudentUserBO user = studentUserRepository.findWetChatUser(new StudentAccount(2017025838));

        assertThatThrownBy(() -> gradeQueryApp.getGradeOverview(user)).isInstanceOf(PasswordUnCorrectException.class);
    }

    @Test
    public void testUrpException(){
        doReturn(true).when(spiderSwitch).fetchUrp();
        doThrow(new UrpEvaluationException("")).when(gradeSpiderFacade).getCurrentTermGrade(any());
        doReturn(schemeGradeList).when(gradeSpiderFacade).getSchemeGrade(any());

        WechatStudentUserBO user = studentUserRepository.findWetChatUser(new StudentAccount(2017025838));

        GradeOverviewBO bo = gradeQueryApp.getGradeOverview(user);

        assertThat(bo.fetchSuccess()).isFalse();
        assertThat(bo.getErrorCode()).isEqualTo(ErrorCode.Evaluation_ERROR.getErrorCode());
    }

    @Test
    public void testMockStudent(){
        doReturn(true).when(spiderSwitch).fetchUrp();
        doThrow(new UrpEvaluationException("")).when(gradeSpiderFacade).getCurrentTermGrade(any());
        doReturn(schemeGradeList).when(gradeSpiderFacade).getSchemeGrade(any());

        WechatStudentUserBO user = studentUserRepository.findWetChatUser(new StudentAccount(2017025838));
        user.setMsgHasCheck(false);

        GradeOverviewBO bo = gradeQueryApp.getGradeOverview(user);

        assertThat(bo.fetchSuccess()).isFalse();
    }

}