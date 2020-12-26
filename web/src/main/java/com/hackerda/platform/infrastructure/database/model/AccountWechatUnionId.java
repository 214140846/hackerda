package com.hackerda.platform.infrastructure.database.model;

import lombok.Data;

import java.util.Date;

@Data
public class AccountWechatUnionId {

    private Integer account;

    private String unionId;

    private String appId;

    private String openId;


}
