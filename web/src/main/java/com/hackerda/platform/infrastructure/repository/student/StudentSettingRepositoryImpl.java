package com.hackerda.platform.infrastructure.repository.student;

import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentSettingRepository;
import com.hackerda.platform.domain.student.StudentSettingsBO;
import com.hackerda.platform.infrastructure.database.mapper.StudentSettingsMapper;
import com.hackerda.platform.infrastructure.database.model.StudentSettings;
import com.hackerda.platform.infrastructure.database.model.StudentSettingsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class StudentSettingRepositoryImpl implements StudentSettingRepository {

    @Autowired
    private StudentSettingsMapper studentSettingsMapper;

    @Override
    public StudentSettingsBO get(StudentAccount studentAccount) {

        StudentSettingsExample example = new StudentSettingsExample();
        example.createCriteria().andAccountEqualTo(studentAccount.getAccount());

        return studentSettingsMapper.selectByExample(example).stream().map(x-> StudentSettingsBO.builder()
                .studentAccount(studentAccount)
                .commentPushSwitch(x.getCommentPushSwitch())
                .coursePushSwitch(x.getCoursePushSwitch())
                .examPushSwitch(x.getExamPushSwitch())
                .gradePushSwitch(x.getGradePushSwitch())
                .build()).findFirst().orElse(null);

    }

    @Override
    public void save(StudentSettingsBO studentSettingsBO) {
        StudentSettings record = new StudentSettings();

        record.setAccount(studentSettingsBO.getStudentAccount().getAccount());
        record.setCommentPushSwitch(studentSettingsBO.isCommentPushSwitch());
        record.setCoursePushSwitch(studentSettingsBO.isCoursePushSwitch());
        record.setExamPushSwitch(studentSettingsBO.isExamPushSwitch());
        record.setGradePushSwitch(studentSettingsBO.isGradePushSwitch());

        studentSettingsMapper.insertSelective(record);

    }

    @Override
    public void update(StudentSettingsBO studentSettingsBO) {

        if(!studentSettingsBO.isModify()) {
            return;
        }

        StudentSettings record = new StudentSettings();

        StudentSettingsExample example = new StudentSettingsExample();
        example.createCriteria().andAccountEqualTo(studentSettingsBO.getStudentAccount().getAccount());

        record.setCommentPushSwitch(studentSettingsBO.isCommentPushSwitch());
        record.setCoursePushSwitch(studentSettingsBO.isCoursePushSwitch());
        record.setExamPushSwitch(studentSettingsBO.isExamPushSwitch());
        record.setGradePushSwitch(studentSettingsBO.isGradePushSwitch());

        studentSettingsMapper.updateByExampleSelective(record, example);

    }
}
