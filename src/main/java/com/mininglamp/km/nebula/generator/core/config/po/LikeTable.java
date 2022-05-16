package com.mininglamp.km.nebula.generator.core.config.po;


import com.mininglamp.km.nebula.generator.core.toolkit.annotation.SqlLike;
import com.mininglamp.km.nebula.generator.core.toolkit.sql.SqlUtils;

/**
 * 表名拼接
 *
 * @author nieqiuqiu
 * @date 2019-11-26
 * @since 3.3.0
 */
public class LikeTable {

    private String value;

    private SqlLike like = SqlLike.DEFAULT;

    public LikeTable(String value) {
        this.value = value;
    }

    public LikeTable(String value, SqlLike like) {
        this.value = value;
        this.like = like;
    }

    @Override
    public String toString() {
        return getValue();
    }

    public String getValue() {
        return SqlUtils.concatLike(this.value, like);
    }

}
