package com.lyh.admin_template.back.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 国际化工具类，用于获取国际化文件中的数据
 */
@Component
public class MessageSourceUtil {

    @Autowired
    private MessageSource messageSource;

    public String getMessage(String code) {
        return getMessage(code, null);
    }

    public String getMessage(String code, Object[] args) {
        return getMessage(code, args, "");
    }

    public String getMessage(String code, Object[] args, String defaultMsg) {
        // 获取当前语言信息
        Locale locale = LocaleContextHolder.getLocale();
        // 根据 code 从国际化资源文件中查找相关信息
        return messageSource.getMessage(code, args, defaultMsg, locale);
    }

}
