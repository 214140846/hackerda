package com.hackerda.platform.domain.exam;

import com.google.common.collect.Lists;
import com.hackerda.platform.domain.wechat.WechatTemplateMessage;
import com.hackerda.platform.domain.wechat.WechatUser;
import com.hackerda.platform.utils.DateUtils;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

public class ExamRemindMessage implements WechatTemplateMessage {

    /**
     * {{first.DATA}}
     * 考试科目：{{keyword1.DATA}}
     * 考试时间：{{keyword2.DATA}}
     * 考试地点：{{keyword3.DATA}}
     * 监考老师：{{keyword4.DATA}}
     * {{remark.DATA}}
     */
    private static final String templateId = "3DLzeLNPMwrhDk2ZOB81C-Uf1VNuVBycfU2p0qWPKYs";
    private final WechatUser toWechatUser;
    private final ExamTimetableBO examTimetableBO;
    private final String reviewInformationUrl;

    public ExamRemindMessage(WechatUser wechatUser,  ExamTimetableBO examTimetableBO, String reviewInformationUrl) {
        this.toWechatUser = wechatUser;
        this.examTimetableBO = examTimetableBO;
        this.reviewInformationUrl = reviewInformationUrl;
    }


    @Override
    public String getMpTemplateId() {
        return templateId;
    }

    @Override
    public WxMpTemplateMessage genMpTemplateMessage() {

        WxMpTemplateData first = new WxMpTemplateData("first", "高等数学（第七版-上册）课后答案已更新，点击即可查看","#cc3333");
        WxMpTemplateData keyword1 = new WxMpTemplateData("keyword1", examTimetableBO.getCourseName(),"#173177");
        WxMpTemplateData keyword2 = new WxMpTemplateData("keyword2", DateUtils.dateToChinese(examTimetableBO.getExamDate()),"#173177");
        WxMpTemplateData keyword3 = new WxMpTemplateData("keyword3",
                examTimetableBO.getRoomName(),"#173177");

        WxMpTemplateData remark = new WxMpTemplateData("remark", "如果想要其他科目的，可以加黑科校际吴彦祖（hkdhdj666），尽量给大家找蛤。","#173177");

        return WxMpTemplateMessage.builder()
                .toUser(toWechatUser.getOpenId())
                .templateId(templateId)
                .url(reviewInformationUrl)
                .data(Lists.newArrayList(first, keyword1, keyword2, keyword3, remark))
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
