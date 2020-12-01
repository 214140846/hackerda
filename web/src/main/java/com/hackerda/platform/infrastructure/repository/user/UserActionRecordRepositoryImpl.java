package com.hackerda.platform.infrastructure.repository.user;

import com.hackerda.platform.domain.user.action.UserActionRecordBO;
import com.hackerda.platform.domain.user.action.UserActionRecordRepository;
import com.hackerda.platform.infrastructure.database.mapper.UserActionRecordMapper;
import com.hackerda.platform.infrastructure.database.model.UserActionRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserActionRecordRepositoryImpl implements UserActionRecordRepository {

    @Autowired
    private UserActionRecordMapper userActionRecordMapper;

    @Override
    public void store(UserActionRecordBO record) {

        UserActionRecord actionRecord = new UserActionRecord();

        actionRecord.setOperator(record.getOperator());
        actionRecord.setActionTarget(record.getActionTarget().getCode());

        actionRecord.setUserAction(record.getUserAction().getCode());
        actionRecord.setTargetId(record.getTargetId());

        actionRecord.setActionValue(record.getActionValue());
        actionRecord.setOperateTime(record.getOperateTime());

        userActionRecordMapper.insertSelective(actionRecord);
    }
}
