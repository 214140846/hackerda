package com.hackerda.platform.domain.wechat;

import com.hackerda.platform.domain.student.StudentAccount;

import java.util.List;
import java.util.Map;

public interface UnionIdRepository {

    void save(UnionId unionId);

    UnionId find(String unionId);

    UnionId find(StudentAccount studentAccount);

    Map<StudentAccount, UnionId> find(List<StudentAccount> studentAccountList);

    UnionId find(WechatUser wechatUser);
}
