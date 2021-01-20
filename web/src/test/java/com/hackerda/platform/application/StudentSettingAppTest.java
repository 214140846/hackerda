package com.hackerda.platform.application;

import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentSettingsBO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;


@Slf4j
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentSettingAppTest {

    @Autowired
    private StudentSettingApp studentSettingApp;


    @Test
    public void getSettings() {

        StudentSettingsBO settings = studentSettingApp.getSettings(new StudentAccount("2014025838"));

        studentSettingApp.updateFiled(new StudentAccount("2014025838"), "comment", false);

        StudentSettingsBO settings1 = studentSettingApp.getSettings(new StudentAccount("2014025838"));

        assertThat(settings1.isCommentPushSwitch()).isFalse();

    }

    @Test
    public void updateFiled() {
    }
}