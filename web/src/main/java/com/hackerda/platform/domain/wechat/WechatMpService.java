package com.hackerda.platform.domain.wechat;

import me.chanjar.weixin.mp.bean.result.WxMpUser;

public interface WechatMpService {

    WxMpUser getUserInfo(WechatUser wechatUser);
}
