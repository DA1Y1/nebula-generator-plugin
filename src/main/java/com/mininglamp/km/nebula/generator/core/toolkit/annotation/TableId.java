
package com.mininglamp.km.nebula.generator.core.toolkit.annotation;

import java.lang.annotation.*;

/**
 * 表主键标识
 *
 * @author hubin
 * @since 2016-01-23
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface TableId {

    /**
     * 字段名（该值可无）
     */
    String value() default "";

    /**
     * 主键类型
     * {@link IdType}
     */
    IdType type() default IdType.NONE;
}
