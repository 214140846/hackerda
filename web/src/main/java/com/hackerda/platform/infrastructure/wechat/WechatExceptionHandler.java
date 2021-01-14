package com.hackerda.platform.infrastructure.wechat;

import com.hackerda.platform.domain.wechat.UnionId;
import com.hackerda.platform.domain.wechat.UnionIdRepository;
import com.hackerda.platform.domain.wechat.WechatUser;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WechatExceptionHandler {

    @Autowired
    private UnionIdRepository unionIdRepository;

    public void handle(WxErrorException wxErrorException, WechatUser wechatUser) {
        if(wxErrorException.getError().getErrorCode() == 43004) {
            UnionId unionId = unionIdRepository.find(wechatUser);
            unionId.unSubscribe(wechatUser.getAppId());
            unionIdRepository.save(unionId);
        }
    }

}
