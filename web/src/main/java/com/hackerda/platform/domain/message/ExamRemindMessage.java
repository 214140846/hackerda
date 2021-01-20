package com.hackerda.platform.domain.message;

import com.google.common.collect.Lists;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.wechat.WechatTemplateMessage;
import com.hackerda.platform.domain.wechat.WechatUser;
import com.hackerda.platform.infrastructure.database.model.example.ExamTimetable;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

import java.util.Calendar;
import java.util.Date;

/**
 * {{first.DATA}}
 * 考试科目：{{keyword1.DATA}}
 * 考试时间：{{keyword2.DATA}}
 * 考试地点：{{keyword3.DATA}}
 * 监考老师：{{keyword4.DATA}}
 * {{remark.DATA}}
 */
public class ExamRemindMessage implements WechatTemplateMessage {

    private static final String templateId = "3DLzeLNPMwrhDk2ZOB81C-Uf1VNuVBycfU2p0qWPKYs";

    private final StudentUserBO studentUserBO;
    private final WechatUser receiver;
    private final ExamTimetable examTimetable;
    private final int period;

    public ExamRemindMessage(StudentUserBO studentUserBO, WechatUser receiver, ExamTimetable examTimetable, int period) {
        this.studentUserBO = studentUserBO;
        this.receiver = receiver;
        this.examTimetable = examTimetable;
        this.period = period;
    }


    @Override
    public String getMpTemplateId() {
        return templateId;
    }

    @Override
    public WxMpTemplateMessage genMpTemplateMessage() {
        WxMpTemplateData first = new WxMpTemplateData("first",
                studentUserBO.getName() + "同学，" + getTimeDesc(period));
        WxMpTemplateData keyword1 = new WxMpTemplateData("keyword1", examTimetable.getCourseName());
        WxMpTemplateData keyword2 = new WxMpTemplateData("keyword2", getDateStr(examTimetable.getExamDate()));
        WxMpTemplateData keyword3 = new WxMpTemplateData("keyword3", examTimetable.getRoomName());
        WxMpTemplateData remark = new WxMpTemplateData("remark", "如不需要接收此类信息，点击下方菜单订阅管理关闭");

        WxMpTemplateMessage.MiniProgram miniProgram = new WxMpTemplateMessage.MiniProgram(
                "wx05f7264e83fa40e9",
                "pages/core/ks/ks", false);

        return WxMpTemplateMessage.builder()
                .toUser(receiver.getOpenId())
                .templateId(templateId)
                .miniProgram(miniProgram)
                .data(Lists.newArrayList(first, keyword1, keyword2, keyword3, remark))
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
                cal.get(Calendar.DAY_OF_MONTH) + "日"+
                cal.get(Calendar.HOUR_OF_DAY) + "时" +
                cal.get(Calendar.MINUTE) + "分";
    }


    private String getTimeDesc(int period) {
        if(period == 0) {
            return "有考试将于今天进行";
        }

        else if(period == 1) {
            return "有考试将于明天进行";
        } else if(period > 1) {
            return "有考试将于"+ period + "天后进行";
        }

        throw new IllegalArgumentException("period must grater than 0, actual: "+ period);
    }
}
