
package com.mininglamp.km.nebula.generator.core.config.querys;

import com.mininglamp.km.nebula.generator.core.config.IDbQuery;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 表数据查询抽象类
 *
 * @author hubin
 * @since 2018-01-16
 */
public abstract class AbstractDbQuery implements IDbQuery {


    @Override
    public boolean isKeyIdentity(ResultSet results) throws SQLException {
        return false;
    }


    @Override
    public String[] fieldCustom() {
        return null;
    }
}
