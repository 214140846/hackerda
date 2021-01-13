package com.hackerda.platform.application;

import com.hackerda.platform.domain.constant.ErrorCode;
import com.hackerda.platform.domain.student.Action;
import com.hackerda.platform.domain.student.StudentInfoService;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.wechat.ActionRecord;
import com.hackerda.platform.domain.wechat.WechatActionRecordRepository;
import com.hackerda.platform.domain.wechat.WechatUser;
import com.hackerda.platform.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateStudentApp {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private WechatActionRecordRepository wechatActionRecordRepository;
    @Autowired
    private StudentInfoService studentInfoService;

    public WechatStudentUserBO createStudentUser(WechatStudentUserBO wechatStudentUserBO, WechatUser wechatUser) {


        WechatStudentUserBO studentUserBO = studentRepository.findWetChatUser(wechatStudentUserBO.getAccount());

        if(studentUserBO != null && studentUserBO.hasBindApp(wechatUser.getAppId()) && !studentUserBO.hasBindWechatUser(wechatUser)) {
            wechatActionRecordRepository.save(new ActionRecord(wechatUser, Action.AccountHasBind, wechatStudentUserBO.getAccount()));
            throw new BusinessException(ErrorCode.ACCOUNT_HAS_BIND, wechatStudentUserBO.getAccount() + "该学号已经被绑定");
        }

        wechatStudentUserBO.bindWechatUser(wechatUser);
        studentRepository.save(wechatStudentUserBO);

        wechatActionRecordRepository.save(new ActionRecord(wechatUser, Action.CreateStudent, wechatStudentUserBO.getAccount()));
        return wechatStudentUserBO;

    }


    public void updateStudentInfo(WechatStudentUserBO wechatStudentUserBO) {

        WechatStudentUserBO studentInfo = studentInfoService.getStudentInfo(wechatStudentUserBO.getAccount(), wechatStudentUserBO.getEnablePassword());

        wechatStudentUserBO.updateClassInfo(studentInfo.getUrpClassNum(), studentInfo.getAcademyName(),
                studentInfo.getSubjectName(), studentInfo.getClassName());

        studentRepository.save(wechatStudentUserBO);

        wechatActionRecordRepository.save(new ActionRecord(wechatStudentUserBO.getUnionId().getWechatUser("wx05f7264e83fa40e9"), Action.UpdateStudentInfo,
                wechatStudentUserBO.getAccount()));


    }
}
