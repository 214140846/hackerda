package com.hackerda.platform.service;

import com.google.common.collect.ImmutableMap;
import com.hackerda.platform.application.StudentSettingApp;
import com.hackerda.platform.config.wechat.WechatMpPlusProperties;
import com.hackerda.platform.controller.vo.CreateCommentResultVO;
import com.hackerda.platform.controller.vo.StudentSettingVO;
import com.hackerda.platform.controller.vo.UserExtInfoVO;
import com.hackerda.platform.domain.student.StudentSettingsBO;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.wechat.WechatUser;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserExtInfoService {

    @Autowired
    private WechatMpPlusProperties wechatMpPlusProperties;
    @Autowired
    private StudentSettingApp studentSettingApp;

    private final Map<String, String> switchNameMap =
            ImmutableMap.of("grade", "新成绩提醒", "course", "上课提醒", "exam", "考试提醒");

    public UserExtInfoVO getUserExtInfo(WechatStudentUserBO wechatStudentUserBO) {

        UserExtInfoVO vo = new UserExtInfoVO();

        vo.setSubscribePlus(isBindPlus(wechatStudentUserBO));

        return vo;

    }


    public StudentSettingVO getStudentSetting(WechatStudentUserBO wechatStudentUserBO) {

        StudentSettingVO vo = new StudentSettingVO();

        if(isBindPlus(wechatStudentUserBO)) {
            StudentSettingsBO settings = studentSettingApp.getSettings(wechatStudentUserBO.getAccount());
            vo.setHasBindPlus(true);

            List<StudentSettingVO.SwitchItem> switchItems = settings.getSwitchNameList().stream()
                    .filter(x-> switchNameMap.get(x) != null)
                    .map(x -> new StudentSettingVO.SwitchItem(x, switchNameMap.get(x), settings.getSwitchValue(x)))
                    .collect(Collectors.toList());

            vo.setSwitchItemList(switchItems);
        }

        return vo;
    }

    public CreateCommentResultVO updateSetting(WechatStudentUserBO wechatStudentUserBO, String key, String value) {

        CreateCommentResultVO vo = new CreateCommentResultVO();

        if(isBindPlus(wechatStudentUserBO)) {
            studentSettingApp.updateFiled(wechatStudentUserBO.getAccount(), key, BooleanUtils.toBoolean(value));

            vo.setRelease(true);
        }

        return vo;
    }


    private boolean isBindPlus(WechatStudentUserBO wechatStudentUserBO ) {

        WechatUser wechatUser = wechatStudentUserBO.getWechatUser(wechatMpPlusProperties.getAppId());

        return wechatUser != null && wechatUser.isSubscribe();
    }


}
