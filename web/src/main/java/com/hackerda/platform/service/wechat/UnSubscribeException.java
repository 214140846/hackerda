package com.hackerda.platform.service.wechat;

import com.hackerda.platform.domain.wechat.WechatUser;

public class UnSubscribeException extends RuntimeException{

    private final WechatUser wechatUser;

    public UnSubscribeException(WechatUser wechatUser) {
        super(wechatUser.toString() + "haven`t subscribe");
        this.wechatUser =wechatUser;
    }

}
