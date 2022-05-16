package com.mininglamp.km.nebula.generator.core.config.querys;

/**
 * @author xuliang
 */
public class NebulaQuery extends AbstractDbQuery  {
    @Override
    public String tablesSql() {
        return "DESC TAG ";
    }

    @Override
    public String tableFieldsSql() {
        return "DESC TAG `%s`";
    }

    @Override
    public String tableName() {
        return null;
    }

    @Override
    public String tableComment() {
        return null;
    }

    @Override
    public String fieldName() {
        return "Field";
    }

    @Override
    public String fieldType() {
        return "Type";
    }

    @Override
    public String fieldComment() {
        return null;
    }

    @Override
    public String fieldKey() {
        return null;
    }
}
