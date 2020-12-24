package com.hackerda.platform.infrastructure;

import com.hackerda.platform.domain.grade.GradeFetchTask;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import static org.junit.Assert.*;


import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("prod")
@SpringBootTest
public class AntiDuplicateLinkedBlockingQueueTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void testAdd() throws InterruptedException {

        BlockingQueue<GradeFetchTask> queue = new AntiDuplicateLinkedBlockingQueue<>(5);

        queue.add(new GradeFetchTask(true, studentRepository.find(new StudentAccount(2014025838))));

        queue.add(new GradeFetchTask(true, studentRepository.find(new StudentAccount(2014025846))));

        assertThat(queue.size()).isEqualTo(1);

        GradeFetchTask take = queue.take();

        assertThat(queue.size()).isEqualTo(0);

        queue.add(new GradeFetchTask(true, studentRepository.find(new StudentAccount(2014025846))));

        assertThat(queue.size()).isEqualTo(1);
    }

    @Test
    public void testMultiAdd() throws InterruptedException {

        BlockingQueue<GradeFetchTask> queue = new AntiDuplicateLinkedBlockingQueue<>(5);

        StudentUserBO studentUserBO = studentRepository.find(new StudentAccount(2014025838));

        List<WechatStudentUserBO> wetChatUser = studentRepository.findWetChatUser(studentUserBO.getUrpClassNum());

        System.out.println(wetChatUser.size());

        for (WechatStudentUserBO wechatStudentUserBO : wetChatUser) {
            queue.put(new GradeFetchTask(true, wechatStudentUserBO));
        }

        System.out.println(queue.size());
    }

}