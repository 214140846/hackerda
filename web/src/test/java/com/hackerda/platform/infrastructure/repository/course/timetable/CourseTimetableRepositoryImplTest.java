package com.hackerda.platform.infrastructure.repository.course.timetable;

import com.hackerda.platform.application.CourseTimetableQueryApp;
import com.hackerda.platform.domain.course.CourseBO;
import com.hackerda.platform.domain.course.timetable.CourseTimeTableOverview;
import com.hackerda.platform.domain.course.timetable.CourseTimetableBO;
import com.hackerda.platform.domain.course.timetable.CourseTimetableRepository;
import com.hackerda.platform.domain.course.timetable.CourseTimetableSpiderService;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.time.Term;
import com.hackerda.platform.infrastructure.repository.student.StudentRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CourseTimetableRepositoryImplTest {
    @Autowired
    private CourseTimetableRepository courseTimetableRepository;
    @Autowired
    private StudentRepositoryImpl studentUserRepository;
    @Autowired
    private CourseTimetableQueryApp courseTimetableQueryApp;
    @MockBean
    private CourseTimetableSpiderService courseTimetableSpiderService;

    private CourseTimetableBO courseTimetableBO1;

    @Before
    public void mock() {
        CourseBO courseBO = new CourseBO();

        courseBO.setName("测试课程");
        courseBO.setCredit("3");
        courseBO.setNum("test_course_id");
        courseBO.setCourseOrder("01");
        courseBO.setExamType("");
        courseBO.setExamTypeCode("");
        courseBO.setTermOrder(2);
        courseBO.setTermYear("2020-2021");
        courseBO.setAcademyCode("");
        courseBO.setAcademyName("");
        courseBO.setTeacherName("");

        courseTimetableBO1 = new CourseTimetableBO()
                .setCourseBO(courseBO)
                .setRoomName("测试教室")
                .setRoomNumber("test_number")
                .setCampusName("test_campus")
                .setContinuingSession(2)
                .setCourseId("test_course_id")
                .setAttendClassTeacher("")
                .setClassDay(1)
                .setClassOrder(1)
                .setStartWeek(1)
                .setEndWeek(20)
                .setClassInSchoolWeek("11111111111111111100")
                .setCourseSequenceNumber("01")
                .setWeekDescription("week_desc")
                .setClassDistinct(0)
                .setTermOrder(2)
                .setTermYear("2020-2021");

    }

    @Test
    public void getCurrentTermTable() {

        when(courseTimetableSpiderService.fetchByStudent(any()))
                .thenReturn(CourseTimeTableOverview.ofFetchSuccess(Lists.newArrayList(courseTimetableBO1), true));

        when(courseTimetableSpiderService.fetchByClassId(any(), any()))
                .thenReturn(CourseTimeTableOverview.ofFetchSuccess(Collections.emptyList(), false));

        WechatStudentUserBO account = studentUserRepository.findWetChatUser(new StudentAccount(2017025838));

        CourseTimeTableOverview account1 = courseTimetableQueryApp.getByStudent(account, new Term("2020-2021", 2));

        assertThat(account1.isFetchSuccess()).isTrue();

        CourseTimeTableOverview account2 = courseTimetableQueryApp.getByStudent(account, new Term("2020-2021", 2));

        assertThat(account2.isFetchSuccess()).isFalse();
        assertThat(account2.getNewList().size() == 0).isTrue();

    }

    @Test
    public void getByTime() {
        WechatStudentUserBO account = studentUserRepository.findWetChatUser(new StudentAccount(2017025838));
        courseTimetableRepository.saveByClass(Lists.newArrayList(courseTimetableBO1), account.getUrpClassNum().toString());

        assertThat(courseTimetableRepository.getByTime(1, 1 ,new Term("2020-2021", 2)).isEmpty()).isFalse();
    }

    @Test
    public void getStudentById() {
        WechatStudentUserBO student = studentUserRepository.findWetChatUser(new StudentAccount(2017025838));
        courseTimetableRepository.saveByClass(Lists.newArrayList(courseTimetableBO1), student.getUrpClassNum().toString());
        courseTimetableRepository.saveByStudent(Lists.newArrayList(courseTimetableBO1), student);

        CourseTimeTableOverview overview = courseTimetableRepository.getByClassId(student.getUrpClassNum().toString(), new Term("2020-2021", 2));

        Integer id = overview.getCourseTimetableBOList().stream().findFirst().get().getId();


        assertThat(courseTimetableRepository.getStudentById(id).size()).isEqualTo(1);

        WechatStudentUserBO student2 = studentUserRepository.findWetChatUser(new StudentAccount(2014025838));
        courseTimetableRepository.saveByStudent(Lists.newArrayList(courseTimetableBO1), student2);

        assertThat(courseTimetableRepository.getStudentById(id).size()).isEqualTo(2);

    }

    @Test
    public void saveByClass() {

        when(courseTimetableSpiderService.fetchByStudent(any()))
                .thenReturn(CourseTimeTableOverview.ofFetchSuccess(Lists.newArrayList(), true));

        when(courseTimetableSpiderService.fetchByClassId(any(), any()))
                .thenReturn(CourseTimeTableOverview.ofFetchSuccess(Lists.newArrayList(courseTimetableBO1), false));

        WechatStudentUserBO account = studentUserRepository.findWetChatUser(new StudentAccount(2017025838));

        CourseTimeTableOverview account1 = courseTimetableQueryApp.getByStudent(account, new Term("2020-2021", 2));

        assertThat(account1.isFetchSuccess()).isTrue();

        CourseTimeTableOverview account2 = courseTimetableRepository.getByClassId(account.getUrpClassNum().toString(), new Term("2020-2021", 2));

        assertThat(account2.isFetchSuccess()).isFalse();
        assertThat(account2.getCourseTimetableBOList().isEmpty()).isFalse();


    }
}