
package com.mininglamp.km.nebula.generator.core.config.converts;


import com.mininglamp.km.nebula.generator.core.config.ITypeConvert;
import com.mininglamp.km.nebula.generator.core.config.converts.select.BranchBuilder;
import com.mininglamp.km.nebula.generator.core.config.converts.select.Selector;
import com.mininglamp.km.nebula.generator.core.config.rules.IColumnType;
import com.mininglamp.km.nebula.generator.core.toolkit.annotation.DbType;

/**
 * 该注册器负责注册并查询类型注册器
 *
 * @author nieqiuqiu, hanchunlin
 * @since 3.3.1
 */
public class TypeConverts {

    /**
     * 查询数据库类型对应的类型转换器
     *
     * @param dbType 数据库类型
     * @return 返回转换器
     */
    public static ITypeConvert getTypeConvert(DbType dbType) {
        switch (dbType) {
            case MYSQL:
                return MySqlTypeConvert.INSTANCE;
            case NEBULA:
                return NebulaTypeConvert.INSTANCE;
        }
        return null;
    }

    /**
     * 使用指定参数构建一个选择器
     *
     * @param param 参数
     * @return 返回选择器
     */
    static Selector<String, IColumnType> use(String param) {
        return new Selector<>(param.toLowerCase());
    }

    /**
     * 这个分支构建器用于构建用于支持 {@link String#contains(CharSequence)} 的分支
     *
     * @param value 分支的值
     * @return 返回分支构建器
     * @see #containsAny(CharSequence...)
     */
    static BranchBuilder<String, IColumnType> contains(CharSequence value) {
        return BranchBuilder.of(s -> s.contains(value));
    }

    /**
     * @see #contains(CharSequence)
     */
    static BranchBuilder<String, IColumnType> containsAny(CharSequence... values) {
        return BranchBuilder.of(s -> {
            for (CharSequence value : values) {
                if (s.contains(value)) {
                    return true;
                }
            }
            return false;
        });
    }

}
