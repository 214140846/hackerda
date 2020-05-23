
package com.hackerda.platform.config;


import com.hackerda.spider.IExceptionHandler;
import com.hackerda.spider.UrpBaseSpider;
import com.hackerda.spider.UrpSpider;
import com.hackerda.spider.captcha.CaptchaImage;
import com.hackerda.spider.captcha.ICaptchaProvider;
import com.hackerda.spider.client.AccountRestTemplate;
import com.hackerda.spider.config.SpiderConfiguration;
import com.hackerda.spider.predict.CaptchaPredict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

/**
 * @author zhouqinglai
 * @version version
 * @title BasicsConfig
 * @desc  基础配置
 * @date: 2019年05月03日
 */
@Configuration
@Import(SpiderConfiguration.class)
public class BasicsConfig {

    @Autowired
    private IExceptionHandler spiderExceptionHandler;

    @Bean
    @Scope("prototype")
    public UrpSpider urpBaseSpider(AccountRestTemplate<String> client,
                                   CaptchaPredict captchaPredict, ICaptchaProvider<CaptchaImage> captchaProvider){

        return new UrpBaseSpider(client, captchaPredict, captchaProvider, spiderExceptionHandler);
    }

}