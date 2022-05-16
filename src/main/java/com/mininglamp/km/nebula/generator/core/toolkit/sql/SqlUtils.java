package com.mininglamp.km.nebula.generator.core.toolkit.sql;

import com.mininglamp.km.nebula.generator.core.toolkit.StringPool;
import com.mininglamp.km.nebula.generator.core.toolkit.annotation.SqlLike;

/**
 * @author xuliang
 */
public class SqlUtils {

    /**
     * 用%连接like
     *
     * @param str 原字符串
     * @return like 的值
     */
    public static String concatLike(Object str, SqlLike type) {
        switch (type) {
            case LEFT:
                return StringPool.PERCENT + str;
            case RIGHT:
                return str + StringPool.PERCENT;
            default:
                return StringPool.PERCENT + str + StringPool.PERCENT;
        }
    }
}
