package com.mininglamp.km.nebula.generator.core.toolkit.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xuliang
 */

@Getter
@AllArgsConstructor
public enum DbType {

    /**
     * MYSQL
     */
    MYSQL("mysql", "MySql数据库"),
    /**
     * nebula 图数据库
     */
    NEBULA("nebula", "nebula 图数据库"),
    /**
     * UNKONWN DB
     */
    OTHER("other", "其他数据库");

    /**
     * 数据库名称
     */
    private final String db;
    /**
     * 描述
     */
    private final String desc;

    /**
     * 获取数据库类型
     *
     * @param dbType 数据库类型字符串
     */
    public static DbType getDbType(String dbType) {
        for (DbType type : DbType.values()) {
            if (type.db.equalsIgnoreCase(dbType)) {
                return type;
            }
        }
        return OTHER;
    }
}
