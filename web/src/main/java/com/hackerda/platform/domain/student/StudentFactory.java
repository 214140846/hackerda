package com.hackerda.platform.domain.student;

import com.hackerda.platform.domain.user.Gender;

public interface StudentFactory {

    WechatStudentUserBO create(StudentAccount account, String name, Gender gender, String urpClazzNum);

    WechatStudentUserBO create(StudentAccount account, String password, String name, Gender gender, String urpClazzNum);
}
