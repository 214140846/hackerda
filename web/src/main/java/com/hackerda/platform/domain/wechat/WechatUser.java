package com.hackerda.platform.domain.wechat;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
public class WechatUser implements Serializable {

    private final String appId;
    private final String openId;
    @EqualsAndHashCode.Exclude
    private boolean subscribe;

    public WechatUser(String appId, String openId) {
        this.appId = appId;
        this.openId = openId;
        this.subscribe = true;
    }

    public WechatUser(String appId, String openId, boolean subscribe) {
        this.appId = appId;
        this.openId = openId;
        this.subscribe = subscribe;
    }


    public String asValue() {
        return appId + ":" +openId;
    }

    public static WechatUser ofNull() {
        return new WechatUser("", "");
    }
}
