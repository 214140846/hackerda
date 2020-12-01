package com.hackerda.platform.domain.user.action;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class UserActionRecordBO {

    private final String operator;

    private final UserAction userAction;

    private final ActionTarget actionTarget;

    private final String targetId;

    private final String actionValue;

    private final Date operateTime;
}
