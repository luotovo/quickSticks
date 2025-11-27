package com.quick.config;

import com.quick.properties.AliOssProperties;
import com.quick.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
配置类，用于创建aliossutils对象
 */
@Configuration
@Slf4j
public class config {
    @Bean
    @ConditionalOnMissingBean//当没有这个bean的时候创建这个bean,确保唯一性
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        log.info("开始创建阿里云文件上传工具类对象:{}", aliOssProperties);
        return new AliOssUtil(aliOssProperties.getEndpoint(), aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(), aliOssProperties.getBucketName());

    }
}
