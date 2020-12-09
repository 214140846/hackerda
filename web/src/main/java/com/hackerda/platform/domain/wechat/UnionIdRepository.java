package com.hackerda.platform.domain.wechat;

import com.hackerda.platform.domain.student.StudentAccount;

public interface UnionIdRepository {

    void save(UnionId unionId);

    UnionId find(String unionId);

    UnionId find(StudentAccount studentAccount);
}
