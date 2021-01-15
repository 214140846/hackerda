package com.hackerda.platform.service;

import com.hackerda.platform.config.wechat.WechatMpPlusProperties;
import com.hackerda.platform.controller.vo.UserExtInfoVO;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.wechat.WechatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserExtInfoService {

    @Autowired
    private WechatMpPlusProperties wechatMpPlusProperties;

    public UserExtInfoVO getUserExtInfo(WechatStudentUserBO wechatStudentUserBO) {

        UserExtInfoVO vo = new UserExtInfoVO();
        WechatUser wechatUser = wechatStudentUserBO.getWechatUser(wechatMpPlusProperties.getAppId());
        vo.setSubscribePlus(wechatUser != null && wechatUser.isSubscribe());

        return vo;

    }


}
