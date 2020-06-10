package com.lyh.admin_template.back.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.validation.Validator;
import java.util.Locale;

/**
 * 配置国际化语言
 */
@Configuration
public class LocaleConfig {

    /**
     * 默认解析器
     */
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        cookieLocaleResolver.setCookieName("language");
        cookieLocaleResolver.setCookieMaxAge(-1);
        return cookieLocaleResolver;
    }

    /**
     * 定义拦截器,其中 language 表示切换语言的参数名。
     * 拦截请求后，根据请求中的 language 参数去设置语言。
     */
    @Bean
    public WebMvcConfigurer localeInterceptor() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
                localeInterceptor.setParamName("language");
                registry.addInterceptor(localeInterceptor);
            }
        };
    }

    /**
     * 由于返回类型不是 MessageSource 类型，所以需要在 @Bean 注解中指明 name 参数，用于自动装配到 MessageSource 中。
     * 使用时 通过 @Autowired 注入 MessageSource 即可。
     * setBasenames 用于指定国际化资源文件前缀
     * setDefaultEncoding 用于指定编码字符集
     */
    @Bean(name = "messageSource")
    public ResourceBundleMessageSource getMessageSource() throws Exception {
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setDefaultEncoding("UTF-8");
        resourceBundleMessageSource.setBasenames("static/i18n/messages");
        return resourceBundleMessageSource;
    }

    /**
     * 配置 JSR303 国际化问题，让其加载国际化资源文件
     * @return
     * @throws Exception
     */
    @Bean
    public Validator getValidator() throws Exception {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(getMessageSource());
        return validator;
    }

}
