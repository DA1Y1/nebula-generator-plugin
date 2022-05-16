
package com.mininglamp.km.nebula.generator.core.config.po;

import com.mininglamp.km.nebula.generator.core.toolkit.annotation.FieldFill;
import lombok.Data;

/**
 * 字段填充
 *
 * @author hubin
 * @since 2017-06-26
 */
@Data
public class TableFill {

    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 忽略类型
     */
    private FieldFill fieldFill;

    public TableFill(String fieldName, FieldFill ignore) {
        this.fieldName = fieldName;
        this.fieldFill = ignore;
    }
}
