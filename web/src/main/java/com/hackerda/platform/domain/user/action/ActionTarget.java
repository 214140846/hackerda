package com.hackerda.platform.domain.user.action;

public enum ActionTarget {

    USER(0),

    ROLE(1),

    POST(2)
    ;

    private final int code;

    ActionTarget(int i) {
        this.code = i;
    }

    public int getCode () {
        return this.code;
    }

}
