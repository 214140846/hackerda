package com.hackerda.platform.domain.user.action;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
public class ActionResult {

    private final boolean success;
    private final String msg;

    private ActionResult(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public static ActionResult success() {
        return new ActionResult(true, "");
    }

    public static ActionResult fail(String message) {
        return new ActionResult(false, message);
    }



}
