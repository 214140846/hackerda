
package com.hackerda.platform.config;


import com.hackerda.platform.domain.grade.GradeFetchTask;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.infrastructure.AntiDuplicateLinkedBlockingQueue;
import com.hackerda.platform.infrastructure.database.model.StudentUser;
import com.hackerda.platform.task.GradeAutoUpdateScheduled;
import com.hackerda.spider.*;
import com.hackerda.spider.captcha.CaptchaImage;
import com.hackerda.spider.captcha.ICaptchaProvider;
import com.hackerda.spider.config.SpiderConfiguration;
import com.hackerda.spider.cookie.AccountCookiePersist;
import com.hackerda.spider.predict.CaptchaPredict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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

    @Value("${school.server.url:http://xsurp.usth.edu.cn}")
    private String server;

    @Bean
    @Scope("prototype")
    public UrpSpider urpBaseSpider(RestTemplate spiderTemplate,
                                   CaptchaPredict captchaPredict, ICaptchaProvider<CaptchaImage> captchaProvider,
                                   AccountCookiePersist<String> cookiePersist, IExceptionHandler spiderExceptionHandler){

        return new UrpCommonSpider(server, spiderTemplate, captchaPredict, captchaProvider, cookiePersist,
                spiderExceptionHandler);
    }

    @Bean
    @Scope("prototype")
    public UrpEvaluationSpider urpEvaluationSpider(RestTemplate searchSpiderTemplate,
                                   CaptchaPredict captchaPredict, ICaptchaProvider<CaptchaImage> captchaProvider,
                                   AccountCookiePersist<String> cookiePersist, IExceptionHandler spiderExceptionHandler){

        return new UrpEvaluationSpiderImpl(server, searchSpiderTemplate, captchaPredict, captchaProvider,
                cookiePersist, spiderExceptionHandler);
    }

    @Bean
    public UrpSearchSpider uepSearchSpider(RestTemplate searchSpiderTemplate,
                                           CaptchaPredict captchaPredict, ICaptchaProvider<CaptchaImage> captchaProvider,
                                           AccountCookiePersist<String> cookiePersist){

        return new UrpSearchSpiderImpl(server, searchSpiderTemplate, captchaPredict, captchaProvider, cookiePersist);
    }

    @Bean
    public BlockingQueue<GradeFetchTask> gradeFetchQueue(){

         return new AntiDuplicateLinkedBlockingQueue<>(1024);
    }

}