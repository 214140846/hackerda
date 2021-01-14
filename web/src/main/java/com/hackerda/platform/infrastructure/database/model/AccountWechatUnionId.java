package com.hackerda.platform.infrastructure.database.model;

import lombok.Data;

import java.util.Date;

@Data
public class AccountWechatUnionId {

    private Integer account;

    private String unionId;

    private String appId;

    private String openId;

    private Byte subscribe;


    public boolean isSubscribe () {
        return subscribe == (byte) 1;
    }

    public void setSubscribe (boolean subscribe) {
        this.subscribe = subscribe ? (byte) 1 : (byte) 0;
    }

}
