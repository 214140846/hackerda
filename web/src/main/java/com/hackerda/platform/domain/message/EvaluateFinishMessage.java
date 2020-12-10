package com.hackerda.platform.domain.message;

import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.wechat.WechatTemplateMessage;
import com.hackerda.platform.domain.wechat.WechatUser;
import lombok.Data;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

import java.util.ArrayList;
import java.util.List;

@Data
public class EvaluateFinishMessage implements WechatTemplateMessage {

    private final WechatUser toWechatUser;

    private final String mpTemplateId;

    private final StudentUserBO studentUserBO;

    public EvaluateFinishMessage(WechatUser wechatUser, String mpTemplateId, StudentUserBO studentUserBO) {
        this.toWechatUser = wechatUser;
        this.mpTemplateId = mpTemplateId;
        this.studentUserBO = studentUserBO;
    }


    @Override
    public String getMpTemplateId() {
        return mpTemplateId;
    }

    @Override
    public WxMpTemplateMessage genMpTemplateMessage() {
        List<WxMpTemplateData> templateData = new ArrayList<>();
        //first关键字
        WxMpTemplateData first = new WxMpTemplateData();
        first.setName("first");
        first.setValue(studentUserBO.getName()+"同学，教务评估已经完成");

        WxMpTemplateData remark = new WxMpTemplateData();
        remark.setName("remark");
        remark.setValue("如有疑问微信添加吴彦祖【hkdhd666】");

        templateData.add(first);
        templateData.add(remark);

        return WxMpTemplateMessage.builder()
                .toUser(toWechatUser.getOpenId())
                .templateId(mpTemplateId)
                .data(templateData)
                .build();
    }

    @Override
    public String getSendApp() {
        return toWechatUser.getAppId();
    }
}
