package com.hackerda.platform.domain.message;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.hackerda.platform.domain.course.timetable.CourseTimetableBO;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.wechat.WechatTemplateMessage;
import com.hackerda.platform.domain.wechat.WechatUser;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

import java.util.Map;


/**
 * {{first.DATA}}
 * 课程：{{keyword1.DATA}}
 * 时间：{{keyword2.DATA}}
 * 地点：{{keyword3.DATA}}
 * {{remark.DATA}}
 */
public class CourseTimetableRemindMessage implements WechatTemplateMessage {

    private static final String templateId = "MMAu8JV4sawk1V2IcotI45EWUly3EuBh_Kxdec5fkK0";

    private final WechatUser receiver;
    private final CourseTimetableBO courseTimetableBO;
    private final StudentUserBO studentUserBO;

    private final Map<Integer, String> orderDescMap = ImmutableMap.of(1, "上午第一节", 3, "上午第二节",
            5, "下午第一节", 7, "下午第二节", 9, "晚上第一节");


    public CourseTimetableRemindMessage (WechatUser receiver, CourseTimetableBO courseTimetableBO, StudentUserBO studentUserBO) {
        this.receiver = receiver;
        this.courseTimetableBO = courseTimetableBO;
        this.studentUserBO = studentUserBO;
    }


    @Override
    public String getMpTemplateId() {
        return templateId;
    }

    @Override
    public WxMpTemplateMessage genMpTemplateMessage() {
        WxMpTemplateData first = new WxMpTemplateData("first",
                studentUserBO.getName() + "同学，你有课程要准备开始啦");
        WxMpTemplateData keyword1 = new WxMpTemplateData("keyword1", courseTimetableBO.getCourseBO().getName());
        WxMpTemplateData keyword2 = new WxMpTemplateData("keyword2", getOrderDesc(courseTimetableBO.getClassOrder()));
        WxMpTemplateData keyword3 = new WxMpTemplateData("keyword3", courseTimetableBO.getRoomName());
        WxMpTemplateData remark = new WxMpTemplateData("remark", "如不需要接收此类信息，点击下方菜单订阅管理关闭");

        WxMpTemplateMessage.MiniProgram miniProgram = new WxMpTemplateMessage.MiniProgram(
                "wx05f7264e83fa40e9",
                "pages/core/kb/kb", false);

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


    private String getOrderDesc(int order) {

        return orderDescMap.getOrDefault(order, "");
    }
}
