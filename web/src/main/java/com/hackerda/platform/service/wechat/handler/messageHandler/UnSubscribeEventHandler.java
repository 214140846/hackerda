package com.hackerda.platform.service.wechat.handler.messageHandler;

import com.hackerda.platform.domain.wechat.UnionId;
import com.hackerda.platform.domain.wechat.UnionIdRepository;
import com.hackerda.platform.domain.wechat.WechatUser;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class UnSubscribeEventHandler implements WxMpMessageHandler {

    @Autowired
    private UnionIdRepository unionIdRepository;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        String appId = wxMpService.getWxMpConfigStorage().getAppId();
        String fromUser = wxMessage.getFromUser();

        UnionId unionId = unionIdRepository.find(new WechatUser(appId, fromUser));
        if(!unionId.isEmpty()) {
            unionId.unSubscribe(appId);
            unionIdRepository.save(unionId);
        }


        return null;
    }
}
