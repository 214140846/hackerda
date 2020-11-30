package com.hackerda.platform.domain.user.action;

public enum ActionTarget {

    USER(0),

    ROLE(1)
    ;

    private final int code;

    ActionTarget(int i) {
        this.code = i;
    }

    public int getCode () {
        return this.code;
    }

}
