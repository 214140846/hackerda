package com.hackerda.platform.domain.student;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class StudentSettingsBOTest {

    @Test
    public void updateSwitch() {

        StudentSettingsBO build = StudentSettingsBO.builder().commentPushSwitch(true).build();
        build.updateSwitch("comment", false);

        assertThat(build.isCommentPushSwitch()).isFalse();
        assertThat(build.isModify()).isTrue();

    }
}