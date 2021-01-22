package com.hackerda.platform.domain.student;

import com.hackerda.platform.domain.constant.SubscribeScene;
import com.hackerda.platform.domain.wechat.UnionId;

import java.util.Collection;
import java.util.List;

public interface StudentRepository {

    WechatStudentUserBO findWetChatUser(StudentAccount account);

    List<WechatStudentUserBO> findWetChatUser(Integer urpClassNum);

    WechatStudentUserBO findWetChatUser(UnionId unionId);

    StudentUserBO find(StudentAccount account);

    List<WechatStudentUserBO> getByAccountList(Collection<StudentAccount> accountList);

    void save(StudentUserBO studentUser);

    void save(WechatStudentUserBO studentUser);

}
