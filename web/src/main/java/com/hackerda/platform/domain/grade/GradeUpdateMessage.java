package com.hackerda.platform.domain.grade;

import com.google.common.collect.Lists;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.wechat.WechatTemplateMessage;
import com.hackerda.platform.domain.wechat.WechatUser;
import com.hackerda.platform.utils.DateUtils;
import lombok.ToString;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

/**
 *         {{first.DATA}}
 *         考生姓名：{{keyword1.DATA}}
 *         考试时间：{{keyword2.DATA}}
 *         成绩发布时间：{{keyword3.DATA}}
 *         {{remark.DATA}}
 */
@ToString
public class GradeUpdateMessage implements WechatTemplateMessage {

    private static final String templateId = "ZyNE1ExdTGAb9DRa6e8sg3iYvxouRZDlo9XZSDKtEJw";

    private final WechatUser toWechatUser;
    private final GradeBO gradeBO;
    private final StudentUserBO studentUserBO;

    private static final WxMpTemplateMessage.MiniProgram miniProgram = new WxMpTemplateMessage.MiniProgram(
            "wx05f7264e83fa40e9",
            "pages/core/cj/cj", false);

    public GradeUpdateMessage(WechatUser wechatUser, GradeBO gradeBO, StudentUserBO studentUserBO) {
        toWechatUser = wechatUser;
        this.gradeBO = gradeBO;
        this.studentUserBO = studentUserBO;
    }


    @Override
    public String getMpTemplateId() {
        return templateId;
    }

    @Override
    public WxMpTemplateMessage genMpTemplateMessage() {

        WxMpTemplateData first = new WxMpTemplateData("first", gradeBO.getCourseName()+"成绩更新啦！分数："+ gradeBO.getScore());
        WxMpTemplateData keyword1 = new WxMpTemplateData("keyword1", studentUserBO.getName());
        WxMpTemplateData keyword2 = new WxMpTemplateData("keyword2", gradeBO.getExamTime(),
                "#173177");
        WxMpTemplateData keyword3 = new WxMpTemplateData("keyword3",
                DateUtils.dateToChinese(gradeBO.getOperateTime()),"#173177");

        WxMpTemplateData remark = new WxMpTemplateData("remark", "点击即可查看详情，如有问题添加黑科校际吴彦祖（hkdhdj666）","#173177");


        return WxMpTemplateMessage.builder()
                .toUser(toWechatUser.getOpenId())
                .templateId(templateId)
                .miniProgram(miniProgram)
                .data(Lists.newArrayList(first, keyword1, keyword2, keyword3, remark))
                .build();
    }

    @Override
    public String getSendApp() {
        return toWechatUser.getAppId();
    }
}
