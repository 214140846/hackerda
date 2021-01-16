package com.hackerda.platform.application;

import com.hackerda.platform.domain.wechat.UnionId;
import com.hackerda.platform.domain.wechat.UnionIdRepository;
import com.hackerda.platform.domain.wechat.WechatMpService;
import com.hackerda.platform.domain.wechat.WechatUser;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UnionIdApp {

    @Autowired
    private UnionIdRepository unionIdRepository;
    @Autowired
    private WechatMpService wechatMpService;

    public UnionId saveUnionId(String unionId, WechatUser wechatUser) {

        UnionId savedUnionId = unionIdRepository.find(unionId);

        if (savedUnionId.isEmpty()) {
            return crateAndSave(unionId, wechatUser);
        }

        savedUnionId.bindOpenid(wechatUser);
        unionIdRepository.save(savedUnionId);

        return savedUnionId;

    }


    public UnionId saveUnionId(WechatUser wechatUser) {

        UnionId savedUnionId = unionIdRepository.find(wechatUser);

        if (savedUnionId.isEmpty()) {
            WxMpUser userInfo = wechatMpService.getUserInfo(wechatUser);
            wechatUser.setSubscribe(userInfo.getSubscribe());
            return crateAndSave(userInfo.getUnionId(), wechatUser);
        }

        savedUnionId.bindOpenid(wechatUser);
        unionIdRepository.save(savedUnionId);

        return savedUnionId;
    }

    private UnionId crateAndSave(String unionId, WechatUser wechatUser) {

        UnionId ofNew = UnionId.ofNew(unionId);
        ofNew.bindOpenid(wechatUser);

        unionIdRepository.save(ofNew);

        return ofNew;

    }

}
