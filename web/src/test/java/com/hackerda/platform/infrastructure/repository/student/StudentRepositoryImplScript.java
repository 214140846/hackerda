package com.hackerda.platform.infrastructure.repository.student;

import com.hackerda.platform.domain.student.FindMessage;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.wechat.WechatMessageSender;
import com.hackerda.platform.domain.wechat.WechatTemplateMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@Slf4j
@ActiveProfiles("prod")
@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentRepositoryImplScript {

    @Autowired
    private StudentRepositoryImpl studentRepository;
    @Autowired
    private WechatMessageSender wechatMessageSender;

    @Test
    public void findWetChatUser() {
        List<WechatStudentUserBO> wetChatUser = studentRepository.findWetChatUser(2017100014);

        List<WechatStudentUserBO> collect = wetChatUser.stream().filter(x -> x.hasBindApp("wx541fd36e6b400648"))
                .collect(Collectors.toList());

        List<WechatTemplateMessage> message = collect.stream().map(x -> new FindMessage(x.getUnionId().getWechatUser(
                "wx541fd36e6b400648"))).collect(Collectors.toList());

        wechatMessageSender.sendTemplateMessage(message);

    }
}