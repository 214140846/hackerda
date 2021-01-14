package com.hackerda.platform.domain.student;

import com.google.common.collect.Lists;
import com.hackerda.platform.domain.wechat.WechatTemplateMessage;
import com.hackerda.platform.domain.wechat.WechatUser;
import com.hackerda.platform.utils.DateUtils;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

/**
 * {{first.DATA}}
 * 联系人：{{keyword1.DATA}}
 * 联系方式：{{keyword2.DATA}}
 * {{remark.DATA}}
 */
public class FindMessage implements WechatTemplateMessage {

    private static final String templateId = "3DLzeLNPMwrhDk2ZOB81C-Uf1VNuVBycfU2p0qWPKYs";

    private final WechatUser toWechatUser;

    public FindMessage(WechatUser toUser) {
        this.toWechatUser = toUser;
    }

    @Override
    public String getMpTemplateId() {
        return templateId;
    }

    @Override
    public WxMpTemplateMessage genMpTemplateMessage() {

        WxMpTemplateData first = new WxMpTemplateData("first", "有同学捡到了你们班同学孙苗苗的身份证！麻烦帮忙联系一下失主。打下面电话找回");
//        WxMpTemplateData keyword1 = new WxMpTemplateData("keyword1", studentUserBO.getName());
        WxMpTemplateData keyword2 = new WxMpTemplateData("keyword2", "18145149662",
                "#173177");

        WxMpTemplateData remark = new WxMpTemplateData("remark", "如有问题添加黑科校际吴彦祖（hkdhdj666）","#173177");


        return WxMpTemplateMessage.builder()
                .toUser(toWechatUser.getOpenId())
                .templateId(templateId)
                .data(Lists.newArrayList(first,  keyword2, remark))
                .build();
    }

    @Override
    public String getSendApp() {
        return toWechatUser.getAppId();
    }

    @Override
    public WechatUser getToUser() {
        return toWechatUser;
    }
}
