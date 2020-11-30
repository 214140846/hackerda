package com.hackerda.platform.domain.user.action;

public enum UserAction {

    GRANT_ROLE(0),

    REVOKE_ROLE(1),

    CREATE_ROLE(2),

    CHANGE_POST_STATUS(3)
    ;

    private final int code;

    UserAction(int code) {
        this.code = code;
    }

    public int getCode () {
        return this.code;
    }

}
