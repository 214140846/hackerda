package com.hackerda.platform.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCommentResultVO {

    private boolean release;

    private String errMsg;

    public CreateCommentResultVO() {

    }
}
