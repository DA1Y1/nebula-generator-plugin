
package com.mininglamp.km.nebula.generator.core.config.querys;

import com.mininglamp.km.nebula.generator.core.config.IDbQuery;
import com.mininglamp.km.nebula.generator.core.toolkit.annotation.DbType;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author nieqiuqiu
 * @date 2020-01-09
 * @since 3.3.1
 */
public class DbQueryRegistry {

    private final Map<DbType, IDbQuery> db_query_enum_map = new EnumMap<>(DbType.class);

    public DbQueryRegistry() {
        db_query_enum_map.put(DbType.MYSQL, new MySqlQuery());
        db_query_enum_map.put(DbType.NEBULA, new NebulaQuery());
    }

    public IDbQuery getDbQuery(DbType dbType) {
        return db_query_enum_map.get(dbType);
    }
}
