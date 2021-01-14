package com.hackerda.platform.domain.wechat;

import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

public interface WechatTemplateMessage {

    String getMpTemplateId();

    WxMpTemplateMessage genMpTemplateMessage();

    String getSendApp();

    WechatUser getToUser();
}
