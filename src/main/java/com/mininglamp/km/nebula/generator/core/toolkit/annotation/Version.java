
package com.mininglamp.km.nebula.generator.core.toolkit.annotation;

import java.lang.annotation.*;

/**
 * 乐观锁注解
 * <p>
 * 支持的字段类型:
 * long,
 * Long,
 * int,
 * Integer,
 * java.util.Date,
 * java.sql.Timestamp,
 * java.time.LocalDateTime
 *
 * @author TaoYu
 * @since 2016-01-23
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface Version {
}
