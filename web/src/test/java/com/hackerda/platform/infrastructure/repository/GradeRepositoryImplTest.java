package com.hackerda.platform.infrastructure.repository;

import com.hackerda.platform.domain.grade.GradeBO;
import com.hackerda.platform.domain.grade.GradeRepository;
import com.hackerda.platform.domain.grade.TermGradeBO;
import com.hackerda.platform.domain.grade.TermGradeViewBO;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.infrastructure.database.mapper.GradeMapper;
import com.hackerda.platform.infrastructure.repository.grade.GradeSpiderFacade;
import com.hackerda.platform.infrastructure.repository.student.StudentRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class GradeRepositoryImplTest {

    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private GradeMapper gradeMapper;
    @Autowired
    private StudentRepositoryImpl studentUserRepository;
    @MockBean
    private GradeSpiderFacade gradeSpiderFacade;
    @Autowired
    private FetchStatusRecorder fetchStatusRecorder;


    @Test
    public void testUpdate(){
        gradeMapper.truncate();

        WechatStudentUserBO user = studentUserRepository.findWetChatUser(new StudentAccount(2017025838));

        TermGradeViewBO viewBO = gradeRepository.getAllByStudent(user);

        GradeBO gradeBO = viewBO.getTermGradeBOList().get(0).getGradeList().stream().findAny().orElse(null);

        if(gradeBO != null){
            gradeBO.setScore(-2.0);
            gradeRepository.update(Collections.singletonList(gradeBO));
        }

        TermGradeViewBO viewBO1 = gradeRepository.getAllByStudent(user);
        List<GradeBO> update = viewBO1.getTermGradeBOList().get(0).getUpdate();

        assert update.size() == 1;
    }


    @Test
    public void testNewGrade(){

        WechatStudentUserBO user = studentUserRepository.findWetChatUser(new StudentAccount(2017025838));

        List<TermGradeBO> termGradeBOList = gradeRepository.getAllByStudent(user).getTermGradeBOList();

        termGradeBOList.get(0).getGradeList().stream().findAny()
                .ifPresent(gradeBO -> gradeRepository.delete(gradeBO));

        List<TermGradeBO> termGradeBOList2 = gradeRepository.getAllByStudent(user).getTermGradeBOList();
        List<GradeBO> newGrade = termGradeBOList2.get(0).getNew();

        assert newGrade.size() == 1;
    }

    @Test
    public void testGetGrade(){

        WechatStudentUserBO user = studentUserRepository.findWetChatUser(new StudentAccount(2017025838));

        fetchStatusRecorder.removeRecord(FetchScene.EVER_GRADE, "2017025838");

        when(gradeSpiderFacade.getSchemeGrade(any())).thenReturn(Collections.emptyList());

        gradeRepository.getAllByStudent(user);

        fetchStatusRecorder.recordFinish(FetchScene.EVER_GRADE, "2017025838");

        gradeRepository.getAllByStudent(user);

        verify(gradeSpiderFacade, times(1)).getSchemeGrade(any());

        assertThat(fetchStatusRecorder.needToFetch(FetchScene.EVER_GRADE, "2017025837")).isFalse();
    }

    @Test
    public void clear(){

        fetchStatusRecorder.clearRecord(FetchScene.EVER_GRADE);

    }



}