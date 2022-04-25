package com.mininglamp.km.nebula.generator.core.config.converts;


import com.mininglamp.km.nebula.generator.core.config.GlobalConfig;
import com.mininglamp.km.nebula.generator.core.config.ITypeConvert;
import com.mininglamp.km.nebula.generator.core.config.rules.IColumnType;

import static com.mininglamp.km.nebula.generator.core.config.converts.TypeConverts.contains;
import static com.mininglamp.km.nebula.generator.core.config.rules.DbColumnType.*;

/**
 * @author xuliang
 */
public class NebulaTypeConvert implements ITypeConvert {

    public static final NebulaTypeConvert INSTANCE = new NebulaTypeConvert();

    @Override
    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
        return TypeConverts.use(fieldType)
                .test(contains("int").then(LONG))
                .test(contains("double").then(DOUBLE))
                .test(contains("bool").then(BOOLEAN))
                .test(contains("string").then(STRING))
                .test(contains("timestamp").then(TIMESTAMP))
                .or(STRING);
    }
}
