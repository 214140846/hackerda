package com.hackerda.platform.domain.wechat;

import com.hackerda.platform.infrastructure.wechat.model.AuthResponse;

public interface WechatAuthService {

    /**
     * 微信小程序的code认证转换为openid
     * @param code 小程序端登录时生成的code
     * @return 用户的唯一标识
     */
    String appCodeToOpenId(String code);

    AuthResponse authCode(String code);

}
