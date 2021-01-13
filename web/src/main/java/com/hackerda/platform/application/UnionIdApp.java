package com.hackerda.platform.application;

import com.hackerda.platform.domain.wechat.UnionId;
import com.hackerda.platform.domain.wechat.UnionIdRepository;
import com.hackerda.platform.domain.wechat.WechatMpService;
import com.hackerda.platform.domain.wechat.WechatUser;
import com.hackerda.platform.service.wechat.UnSubscribeException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UnionIdApp {

    @Autowired
    private UnionIdRepository unionIdRepository;
    @Autowired
    private WechatMpService wechatMpService;

    public UnionId getUnionId(String unionId, WechatUser wechatUser) {

        UnionId savedUnionId = unionIdRepository.find(unionId);

        if (savedUnionId.isEmpty()) {
            UnionId ofNew = UnionId.ofNew(unionId);
            ofNew.bindOpenid(wechatUser);

            unionIdRepository.save(ofNew);

            return ofNew;
        }

        if (!savedUnionId.hasOpenId(wechatUser)) {
            savedUnionId.bindOpenid(wechatUser);
            unionIdRepository.save(savedUnionId);
        }

        return savedUnionId;

    }


    public void saveUnionId(WechatUser wechatUser) {

        UnionId unionId = unionIdRepository.find(wechatUser);

        if (unionId.isEmpty()) {
            WxMpUser userInfo = wechatMpService.getUserInfo(wechatUser);
            getUnionId(userInfo.getUnionId(), wechatUser);
        }
    }

}
