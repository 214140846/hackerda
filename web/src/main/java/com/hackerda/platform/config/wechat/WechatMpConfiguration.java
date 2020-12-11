package com.hackerda.platform.config.wechat;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hackerda.platform.domain.WechatPlatform;
import com.hackerda.platform.service.wechat.interceptor.StudentInfoInterceptor;
import com.hackerda.platform.service.wechat.interceptor.WechatOpenIdInterceptor;
import com.hackerda.platform.service.wechat.WxMessageRouter;
import com.hackerda.platform.service.wechat.handler.messageHandler.*;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMenuService;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * wechat mp configuration
 *
 * @author Binary Wang(https://github.com/binarywang)
 */
@Configuration
@ComponentScan(basePackages = "com.hackerda.platform.config.*")
@EnableConfigurationProperties(value = {WechatMpProProperties.class, WechatMpPlusProperties.class, WechatTemplateProperties.class})
public class WechatMpConfiguration {

    @Resource
    private WechatMpProProperties wechatMpProProperties;

    @Resource
    private WechatMpPlusProperties wechatMpPlusProperties;

    @Resource
    private MiniProgramProperties miniProgramProperties;

    @Resource
    private OpenidMessageHandler openidMessageHandler;

    @Resource
    private WechatOpenIdInterceptor wechatOpenIdInterceptor;

    @Resource
    private SubscribeEventHandler subscribeEventHandler;

    @Resource
    private EvaluationHandler evaluationHandler;

    private static final Map<String, WxMpMessageRouter> routers = Maps.newHashMap();
    private static final Map<String, WxMpService> mpServices = Maps.newHashMap();

    @Bean
    public WxMpService wxPlusService() {
        WxMpInMemoryConfigStorage plusConfig = wechatMpPlusProperties.getWxMpInMemoryConfigStorage();
        WxMpService wxPlusMpService = new WxMpServiceImpl();
        wxPlusMpService.setWxMpConfigStorage(plusConfig);
        return wxPlusMpService;
    }

    @Bean
    public WxMpService wxProService() {
        WxMpInMemoryConfigStorage proConfig = wechatMpProProperties.getWxMpInMemoryConfigStorage();
        WxMpService wxProMpService = new WxMpServiceImpl();
        wxProMpService.setWxMpConfigStorage(proConfig);
        return wxProMpService;
    }

    @Bean
    public Object services(WxMpService wxPlusService, WxMpService wxProService) {
        //plus的配置

        routers.put(wechatMpPlusProperties.getAppId(), this.newRouter(wxPlusService));
        mpServices.put(wechatMpPlusProperties.getAppId(), wxPlusService);

        //pro的配置

        routers.put(wechatMpProProperties.getAppId(), this.newRouter(wxProService));
        mpServices.put(wechatMpProProperties.getAppId(), wxProService);
        return Boolean.TRUE;
    }

    @Bean
    public Map<String , WechatPlatform> wechatPlatformMap(){

        ImmutableMap.Builder<String, WechatPlatform> builder = ImmutableMap.builder();

        return builder.put(wechatMpPlusProperties.getAppId(), WechatPlatform.HKXJ_PLUS)
                .put(wechatMpProProperties.getAppId(), WechatPlatform.HKXJ_PRO)
                .put(miniProgramProperties.getAppId(), WechatPlatform.HKXJ_APP)
                .put("test_appId", WechatPlatform.TEST)
                .build();

    }


    private WxMpMessageRouter newRouter(WxMpService wxMpService) {
        final WxMessageRouter newRouter = new WxMessageRouter(wxMpService);
        newRouter
                .rule()
                    .async(false)
                    .interceptor(wechatOpenIdInterceptor)
                    .content("openid")
                    .handler(openidMessageHandler)
                    .end()
                .rule()
                .async(false)
                .event("subscribe")
                .handler(subscribeEventHandler)
                .end()
                .rule()
                .async(false)
                .interceptor(wechatOpenIdInterceptor)
                .event("CLICK")
                .eventKey("evaluation")
                .handler(evaluationHandler)
                .end();

        ;
        return newRouter;
    }


    public void setPlusMenu(){
        WxMpService service = getMpServices().get(wechatMpPlusProperties.getAppId());
        WxMenu menu = new WxMenu();

        List<WxMenuButton> buttons = new ArrayList<>();
        WxMenuButton button1 = new WxMenuButton();
        button1.setName("一键教评");
        button1.setType("click");
        button1.setKey("evaluation");

        WxMenuButton button2 = new WxMenuButton();
        button2.setName("用户绑定");
        button2.setType("miniprogram");
        button2.setAppId("wx05f7264e83fa40e9");
        button2.setPagePath("pages/more/login");
        button2.setUrl("http://mp.weixin.qq.com");

        WxMenuButton button3 = new WxMenuButton();
        button3.setName("成绩&考试");
        button3.setType("miniprogram");
        button3.setAppId("wx05f7264e83fa40e9");
        button3.setPagePath("pages/index/index");
        button3.setUrl("http://mp.weixin.qq.com");

        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);

        menu.setButtons(buttons);
        System.out.println(menu.toJson());
        try {
            WxMpMenuService menuService = service.getMenuService();

            menuService.menuCreate(menu);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, WxMpMessageRouter> getRouters() {
        return routers;
    }

    public static Map<String, WxMpService> getMpServices() {
        return mpServices;
    }

}
