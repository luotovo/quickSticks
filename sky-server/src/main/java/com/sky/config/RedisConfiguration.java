package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {
    @Bean
    // 创建Redis模板对象，并注入到Spring容器中，声明一下
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        log.info("开始创建Redis模板对象...");
        RedisTemplate redisTemplate = new RedisTemplate();
        // 设置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //设置redis key序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
