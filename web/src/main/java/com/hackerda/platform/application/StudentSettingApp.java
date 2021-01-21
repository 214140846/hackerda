package com.hackerda.platform.application;

import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentSettingRepository;
import com.hackerda.platform.domain.student.StudentSettingsBO;
import com.hackerda.platform.domain.student.StudentUserBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentSettingApp {

    @Autowired
    private StudentSettingRepository studentSettingRepository;

    public StudentSettingsBO getSettings(StudentAccount account) {

        StudentSettingsBO settingsBO = studentSettingRepository.get(account);
        if (settingsBO == null) {
            StudentSettingsBO studentSettingsBO = StudentSettingsBO.create(account);
            studentSettingRepository.save(studentSettingsBO);
            return studentSettingsBO;
        }

        return settingsBO;
    }


    public void updateFiled(StudentAccount account, String updateFiled, boolean value) {

        StudentSettingsBO settingsBO = studentSettingRepository.get(account);

        if(settingsBO == null) {
           throw new UnsupportedOperationException("account:" + account +  "has`t setting");
        }

        settingsBO.updateSwitch(updateFiled, value);
        studentSettingRepository.update(settingsBO);

    }



}
