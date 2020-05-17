package com.hackerda.platform.service.wechat.handler.messageHandler;

import com.hackerda.platform.builder.TemplateBuilder;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ExampleTemplateHandler implements WxMpMessageHandler {

    @Resource
    private TemplateBuilder templateBuilder;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) {

        System.out.println("！！！模板测试！！！");
        try {
            List<WxMpTemplateData> datas = new ArrayList<>();

            WxMpTemplateData first = new WxMpTemplateData();
            first.setName("first");
            first.setValue("绑定页面");
            WxMpTemplateData name = new WxMpTemplateData();
            name.setName("name");
            name.setValue("测试");
            WxMpTemplateData grade = new WxMpTemplateData();
            grade.setName("grade");
            grade.setValue("样例");
            datas.add(first);
            datas.add(name);
            datas.add(grade);
            String url = "http://suagr.tunnel.echomod.cn/login/" + wxMpXmlMessage.getFromUser();
//            wxMpService.getTemplateMsgService().sendTemplateMsg(templateBuilder.build(wxMpXmlMessage, datas, url));
            return null;
        } catch (Exception e) {
            log.error("在组装返回信息时出现错误 {}", e.getMessage());
        }
        return null;
    }

}
