package com.update.apt_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 2020/03/29
 * Description:
 *
 * @author liupu
 */

// 添加元注解
// @Target(ElementType.TYPE) // 接口, 类, 枚举, 注解
// @Target(ElementType.FIELD) // 字段, 枚举常量
// @Target(ElementType.METHOD) // 方法
// @Target(ElementType.PARAMETER) // 方法参数
// @Target(ElementType.CONSTRUCTOR) // 构造参数
// @Target(ElementType.LOCAL_VARIABLE) // 局部变量
// @Target(ElementType.ANNOTATION_TYPE) // 注解
// @Target(ElementType.PACKAGE) // 包

@Target(ElementType.FIELD)

// 注解的声明周期
// @Retention(RetentionPolicy.SOURCE) // 编码阶段
// @Retention(RetentionPolicy.CLASS) // 编译阶段
// @Retention(RetentionPolicy.RUNTIME) // 运行阶段

@Retention(RetentionPolicy.CLASS)
public @interface BindView {
    int value();
}
