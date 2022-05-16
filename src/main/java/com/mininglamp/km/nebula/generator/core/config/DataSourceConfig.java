
package com.mininglamp.km.nebula.generator.core.config;

import com.mininglamp.km.nebula.generator.core.config.converts.MySqlTypeConvert;
import com.mininglamp.km.nebula.generator.core.config.converts.TypeConverts;
import com.mininglamp.km.nebula.generator.core.config.querys.DbQueryRegistry;
import com.mininglamp.km.nebula.generator.core.toolkit.ExceptionUtils;
import com.mininglamp.km.nebula.generator.core.toolkit.annotation.DbType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

/**
 * 数据库配置
 *
 * @author YangHu, hcl
 * @since 2016/8/30
 */
@Data
@Accessors(chain = true)
public class DataSourceConfig {

    /**
     * 数据库信息查询
     */
    private IDbQuery dbQuery;
    /**
     * 数据库类型
     */
    private DbType dbType;
    /**
     * schemaName
     */
    private String schemaName;
    /**
     * 类型转换
     */
    private ITypeConvert typeConvert;
    /**
     * 关键字处理器
     *
     * @since 3.3.2
     */
    private IKeyWordsHandler keyWordsHandler;
    /**
     * 驱动连接的URL
     */
    private String url;
    /**
     * 驱动名称
     */
    private String driverName;
    /**
     * 数据库连接用户名
     */
    private String username;
    /**
     * 数据库连接密码
     */
    private String password;


    /**
     * space name
     */
    private String spaceName;

    public IDbQuery getDbQuery() {
        if (null == dbQuery) {
            DbType dbType = getDbType();
            DbQueryRegistry dbQueryRegistry = new DbQueryRegistry();
            // 默认 MYSQL
            dbQuery = Optional.ofNullable(dbQueryRegistry.getDbQuery(dbType))
                .orElseGet(() -> dbQueryRegistry.getDbQuery(DbType.MYSQL));
        }
        return dbQuery;
    }

    /**
     * 判断数据库类型
     *
     * @return 类型枚举值
     */
    public DbType getDbType() {
        if (null == this.dbType) {
            this.dbType = this.getDbType(this.url.toLowerCase());
            if (null == this.dbType) {
                throw ExceptionUtils.mpe("Unknown type of database!");
            }
        }

        return this.dbType;
    }

    /**
     * 判断数据库类型
     *
     * @param str 用于寻找特征的字符串，可以是 driverName 或小写后的 url
     * @return 类型枚举值，如果没找到，则返回 null
     */
    private DbType getDbType(String str) {
        if (str.contains(":mysql:") || str.contains(":cobar:")) {
            return DbType.MYSQL;
        } else {
            return DbType.OTHER;
        }
    }

    public ITypeConvert getTypeConvert() {
        if (null == typeConvert) {
            DbType dbType = getDbType();
            // 默认 MYSQL
            typeConvert = TypeConverts.getTypeConvert(dbType);
            if (null == typeConvert) {
                typeConvert = MySqlTypeConvert.INSTANCE;
            }
        }
        return typeConvert;
    }

    /**
     * 创建数据库连接对象
     *
     * @return Connection
     */
    public Connection getConn() {
        Connection conn;
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }
}
