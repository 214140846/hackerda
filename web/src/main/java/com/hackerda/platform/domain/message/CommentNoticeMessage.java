package com.hackerda.platform.domain.message;

import com.google.common.collect.Lists;
import com.hackerda.platform.domain.community.CommentBO;
import com.hackerda.platform.domain.wechat.WechatTemplateMessage;
import com.hackerda.platform.domain.wechat.WechatUser;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

import java.util.Calendar;
import java.util.Date;

/**
 * {{first.DATA}}
 * 发送日期：{{keyword1.DATA}}
 * 发送时间：{{keyword2.DATA}}
 * {{remark.DATA}}
 */
public class CommentNoticeMessage implements WechatTemplateMessage {

    private static final String templateId = "5Wh5PCHrSg0DAYy8iASRFXVT-MEUkRl-FYZ3-0CzNa8";

    private final WechatUser receiver;
    private final CommentBO commentBO;

    public CommentNoticeMessage(WechatUser wechatUser, CommentBO commentBO) {
        this.receiver = wechatUser;
        this.commentBO = commentBO;
    }

    @Override
    public String getMpTemplateId() {
        return templateId;
    }

    @Override
    public WxMpTemplateMessage genMpTemplateMessage() {
        WxMpTemplateData first = new WxMpTemplateData("first", "你收到新的评论" + " 内容：" + commentBO.getContent());
        WxMpTemplateData keyword1 = new WxMpTemplateData("keyword1", getDateStr(commentBO.getPostTime()));
        WxMpTemplateData keyword2 = new WxMpTemplateData("keyword2", getTimeStr(commentBO.getPostTime()));
        WxMpTemplateData remark = new WxMpTemplateData("remark", "点击即可查看详情");

        WxMpTemplateMessage.MiniProgram miniProgram = new WxMpTemplateMessage.MiniProgram(
                "wx05f7264e83fa40e9",
                "pages/core/notice/notice", false);

        return WxMpTemplateMessage.builder()
                .toUser(receiver.getOpenId())
                .templateId(templateId)
                .miniProgram(miniProgram)
                .data(Lists.newArrayList(first, keyword1, keyword2, remark))
                .build();
    }

    @Override
    public String getSendApp() {
        return receiver.getAppId();
    }

    @Override
    public WechatUser getToUser() {
        return receiver;
    }

    private String getDateStr(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR) + "年" +
                (cal.get(Calendar.MONTH) + 1) + "月" +
                cal.get(Calendar.DAY_OF_MONTH) + "日";
    }

    private String getTimeStr(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY) + "时" +
                cal.get(Calendar.MINUTE) + "分";
    }
}
