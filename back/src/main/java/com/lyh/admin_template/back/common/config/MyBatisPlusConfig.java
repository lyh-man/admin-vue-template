package com.lyh.admin_template.back.common.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义一个 MybatisPlus 配置类，配置分页插件、乐观锁插件
 * mapper 扫描也可在此写上
 */
@Configuration
@MapperScan(basePackages = {"com.lyh.admin_template.back.mapper", "com.lyh.admin_template.back.modules.oss.mapper"})
public class MyBatisPlusConfig {
    /**
     * 分页插件
     * @return 分页插件的实例
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 乐观锁插件
     * @return 乐观锁插件的实例
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer builderCustomizer() {
        return builder -> {
            // 所有 Long 类型转换成 String 到前台
            builder.serializerByType(Long.class, ToStringSerializer.instance);
        };
    }
}

