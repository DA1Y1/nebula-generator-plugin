package com.mininglamp.km.nebula.generator.core.toolkit.annotation;

import java.lang.annotation.*;

/**
 * 表字段逻辑处理注解（逻辑删除）
 *
 * @author hubin
 * @since 2017-09-09
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface TableLogic {

    /**
     * 默认逻辑未删除值（该值可无、会自动获取全局配置）
     */
    String value() default "";

    /**
     * 默认逻辑删除值（该值可无、会自动获取全局配置）
     */
    String delval() default "";
}
