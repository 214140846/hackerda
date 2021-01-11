package com.hackerda.platform.domain.course.timetable;

import com.hackerda.platform.application.CourseTimetableQueryApp;
import com.hackerda.platform.domain.time.Term;
import com.hackerda.platform.infrastructure.database.mapper.ext.TruncateMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class CourseTimetableQueryAppTest {

    @Autowired
    private CourseTimetableQueryApp courseTimetableQueryApp;


    @Test
    public void getByAccount() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);


        CompletableFuture.runAsync(() -> {
            try {
                CourseTimeTableOverview view1 = courseTimetableQueryApp.getByClassId("2020010012", new Term("2020-2021",
                        2));
                System.out.println(view1.isFetchSuccess());
                System.out.println(view1.isEmpty());
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                countDownLatch.countDown();
            }



        });

        CompletableFuture.runAsync(() -> {
            try {
                CourseTimeTableOverview view2 = courseTimetableQueryApp.getByClassId("2020010012", new Term("2020-2021",
                        2));
                System.out.println(view2.isFetchSuccess());
                System.out.println(view2.isEmpty());
            } catch (Exception e) {
                e.printStackTrace();
            }

            finally {
                countDownLatch.countDown();
            }

        });

        CompletableFuture.runAsync(() -> {
            try {
                CourseTimeTableOverview view2 = courseTimetableQueryApp.getByClassId("2020010013", new Term("2020-2021",
                        2));
                System.out.println(view2.isFetchSuccess());
                System.out.println(view2.isEmpty());
            } catch (Exception e) {
                e.printStackTrace();
            }

            finally {
                countDownLatch.countDown();
            }

        });



        countDownLatch.await();

    }
}