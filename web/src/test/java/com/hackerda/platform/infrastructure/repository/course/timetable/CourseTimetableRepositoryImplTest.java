package com.hackerda.platform.infrastructure.repository.course.timetable;

import com.hackerda.platform.application.CourseTimetableQueryApp;
import com.hackerda.platform.domain.course.timetable.CourseTimeTableOverview;
import com.hackerda.platform.domain.course.timetable.CourseTimetableRepository;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.time.Term;
import com.hackerda.platform.infrastructure.database.mapper.ext.TruncateMapper;
import com.hackerda.platform.infrastructure.repository.student.StudentRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class CourseTimetableRepositoryImplTest {
    @Autowired
    private CourseTimetableRepository courseTimetableRepository;
    @Autowired
    private StudentRepositoryImpl studentUserRepository;
    @Autowired
    private CourseTimetableQueryApp courseTimetableQueryApp;

    @Test
    public void getCurrentTermTable() {

        WechatStudentUserBO account = studentUserRepository.findWetChatUser(new StudentAccount(2017025838));

        CourseTimeTableOverview account1 = courseTimetableQueryApp.getByStudent(account, new Term("2020-2021", 2));

        assertThat(account1.isFetchSuccess()).isTrue();

        CourseTimeTableOverview account2 = courseTimetableQueryApp.getByStudent(account, new Term("2020-2021", 2));

        assertThat(account2.isFetchSuccess()).isFalse();
        assertThat(account2.getNewList().size() == 0).isTrue();

    }

    @Test
    public void getCurrentTermByClassId() {
    }

    @Test
    public void saveByStudent() {
    }

    @Test
    public void saveByClass() {

        WechatStudentUserBO account = studentUserRepository.findWetChatUser(new StudentAccount(2017025838));

        CourseTimeTableOverview account1 = courseTimetableRepository.getByClassId(account.getUrpClassNum().toString(), new Term("2020-2021", 2));
        courseTimetableRepository.saveByClass(account1.getNewList(), account.getUrpClassNum().toString());

        assertThat(account1.isFetchSuccess()).isTrue();

        CourseTimeTableOverview account2 = courseTimetableRepository.getByClassId(account.getUrpClassNum().toString(), new Term("2020-2021", 2));

        assertThat(account2.isFetchSuccess()).isFalse();
        assertThat(account2.getNewList().size() == 0).isTrue();


    }
}