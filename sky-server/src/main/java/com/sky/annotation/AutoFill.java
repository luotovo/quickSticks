package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//注解的使用位置
@Retention(RetentionPolicy.RUNTIME)//注解的保留时间
public @interface AutoFill {
    //数据库操作类型: INSERT,UPDATE
    OperationType  value();
}
