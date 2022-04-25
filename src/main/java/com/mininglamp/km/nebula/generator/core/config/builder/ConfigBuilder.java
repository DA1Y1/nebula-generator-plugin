
package com.mininglamp.km.nebula.generator.core.config.builder;


import com.mininglamp.km.nebula.generator.core.InjectionConfig;
import com.mininglamp.km.nebula.generator.core.config.*;
import com.mininglamp.km.nebula.generator.core.config.po.CustomerTemplateInfo;
import com.mininglamp.km.nebula.generator.core.config.po.TableField;
import com.mininglamp.km.nebula.generator.core.config.po.TableFill;
import com.mininglamp.km.nebula.generator.core.config.po.TableInfo;
import com.mininglamp.km.nebula.generator.core.config.rules.NamingStrategy;
import com.mininglamp.km.nebula.generator.core.toolkit.CollectionUtils;
import com.mininglamp.km.nebula.generator.core.toolkit.StringPool;
import com.mininglamp.km.nebula.generator.core.toolkit.StringUtils;
import com.mininglamp.km.nebula.generator.core.toolkit.annotation.IdType;
import com.mininglamp.km.nebula.generator.core.toolkit.annotation.TableId;
import com.mininglamp.km.nebula.generator.core.toolkit.annotation.Version;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 配置汇总 传递给文件生成工具
 *
 * @author YangHu, tangguo, hubin, Juzi
 * @since 2016-08-30
 */
public class ConfigBuilder {

    /**
     * 模板路径配置信息
     */
    private final TemplateConfig template;

    /**
     * 路径模板配置
     */
    private final PathTemplateConfig pathTemplateConfig;
    /**
     * 数据库配置
     */
    private final DataSourceConfig dataSourceConfig;
    /**
     * SQL连接
     */
    private final Connection connection;
    /**
     * 数据库表信息
     */
    private List<TableInfo> tableInfoList;
    /**
     * 包配置详情
     */
    private Map<String, String> packageInfo;
    /**
     * 路径配置信息
     */
    private Map<String, String> pathInfo;
    /**
     * 策略配置
     */
    private StrategyConfig strategyConfig;
    /**
     * 全局配置信息
     */
    private GlobalConfig globalConfig;
    /**
     * 注入配置信息
     */
    private InjectionConfig injectionConfig;


    private static Map<String, String> NEBULA_TO_JDBC_TYPE = new HashMap<>();

    static {
        NEBULA_TO_JDBC_TYPE.put("string", "VARCHAR");
        NEBULA_TO_JDBC_TYPE.put("int64", "BIGINT");
        NEBULA_TO_JDBC_TYPE.put("timestamp", "TIMESTAMP");
        NEBULA_TO_JDBC_TYPE.put("double", "DOUBLE");
        NEBULA_TO_JDBC_TYPE.put("bool", "BOOLEAN");
    }


    /**
     * 在构造器中处理配置
     *
     * @param packageConfig      包配置
     * @param pathTemplateConfig
     * @param dataSourceConfig   数据源配置
     * @param strategyConfig     表配置
     * @param template           模板配置
     * @param globalConfig       全局配置
     */
    public ConfigBuilder(PackageConfig packageConfig, PathTemplateConfig pathTemplateConfig, DataSourceConfig dataSourceConfig, StrategyConfig strategyConfig,
                         TemplateConfig template, GlobalConfig globalConfig) {
        // 全局配置
        this.globalConfig = Optional.ofNullable(globalConfig).orElseGet(GlobalConfig::new);
        // 模板配置
        this.template = Optional.ofNullable(template).orElseGet(TemplateConfig::new);
        // 包配置
        if (null == packageConfig) {
            packageConfig = new PackageConfig();
        }
        handlerPackage(this.template, this.globalConfig.getOutputDir(), packageConfig);
        // 处理自定义模板
        this.pathTemplateConfig = Optional.ofNullable(pathTemplateConfig).orElseGet(PathTemplateConfig::new);
        for (CustomerTemplateInfo info : this.pathTemplateConfig.getTemplateInfo()) {
            handlerCustomerTemplate(this.globalConfig.getOutputDir(), info, packageConfig);
        }
        this.dataSourceConfig = dataSourceConfig;
        this.connection = dataSourceConfig.getConn();
        // 策略配置
        this.strategyConfig = Optional.ofNullable(strategyConfig).orElseGet(StrategyConfig::new);
        this.tableInfoList = getTablesInfo(this.strategyConfig);
    }

    // ************************ 曝露方法 BEGIN*****************************

    /**
     * 所有包配置信息
     *
     * @return 包配置
     */
    public Map<String, String> getPackageInfo() {
        return packageInfo;
    }


    /**
     * 所有路径配置
     *
     * @return 路径配置
     */
    public Map<String, String> getPathInfo() {
        return pathInfo;
    }

    /**
     * 表信息
     *
     * @return 所有表信息
     */
    public List<TableInfo> getTableInfoList() {
        return tableInfoList;
    }

    public ConfigBuilder setTableInfoList(List<TableInfo> tableInfoList) {
        this.tableInfoList = tableInfoList;
        return this;
    }

    /**
     * 模板路径配置信息
     *
     * @return 所以模板路径配置信息
     */
    public TemplateConfig getTemplate() {
        return this.template;
    }


    public PathTemplateConfig getPathTemplateConfig() {
        return this.pathTemplateConfig;
    }

    // ****************************** 曝露方法 END**********************************

    /**
     * 处理包配置
     *
     * @param template  TemplateConfig
     * @param outputDir
     * @param config    PackageConfig
     */
    private void handlerPackage(TemplateConfig template, String outputDir, PackageConfig config) {
        // 包信息
        packageInfo = CollectionUtils.newHashMapWithExpectedSize(16);
        packageInfo.put(ConstVal.MODULE_NAME, config.getModuleName());
        packageInfo.put(ConstVal.ENTITY, joinPackage(config.getParent(), config.getEntity()));
        packageInfo.put(ConstVal.MAPPER, joinPackage(config.getParent(), config.getMapper()));
        packageInfo.put(ConstVal.XML, joinPackage(config.getParent(), config.getXml()));
        packageInfo.put(ConstVal.SERVICE, joinPackage(config.getParent(), config.getService()));
        packageInfo.put(ConstVal.SERVICE_IMPL, joinPackage(config.getParent(), config.getServiceImpl()));
        packageInfo.put(ConstVal.CONTROLLER, joinPackage(config.getParent(), config.getController()));
        // 设置默认路径
        pathInfo = CollectionUtils.newHashMapWithExpectedSize(16);
        setPathInfo(pathInfo, template.getEntity(getGlobalConfig().isKotlin()), outputDir, ConstVal.ENTITY_PATH, ConstVal.ENTITY);
        setPathInfo(pathInfo, template.getMapper(), outputDir, ConstVal.MAPPER_PATH, ConstVal.MAPPER);
        setPathInfo(pathInfo, template.getXml(), outputDir, ConstVal.XML_PATH, ConstVal.XML);
        setPathInfo(pathInfo, template.getService(), outputDir, ConstVal.SERVICE_PATH, ConstVal.SERVICE);
        setPathInfo(pathInfo, template.getServiceImpl(), outputDir, ConstVal.SERVICE_IMPL_PATH, ConstVal.SERVICE_IMPL);
        setPathInfo(pathInfo, template.getController(), outputDir, ConstVal.CONTROLLER_PATH, ConstVal.CONTROLLER);
        // 设置自定义路径
        Map<String, String> configPathInfo = config.getPathInfo();
        if (null != configPathInfo) {
            for (String path : configPathInfo.keySet()) {
                pathInfo.put(path, configPathInfo.get(path));
            }
        }
    }


    private void handlerCustomerTemplate(String outputDir, CustomerTemplateInfo templateInfo, PackageConfig config) {
        if (Objects.isNull(templateInfo.getPackagePath())) {
            templateInfo.setPackagePath(joinPackage(config.getParent(), templateInfo.getFolder()));
            templateInfo.setFilePath(joinPath(outputDir, templateInfo.getPackagePath()));
        } else {
            templateInfo.setPackagePath(joinPackage(templateInfo.getPackagePath(), templateInfo.getFolder()));
            templateInfo.setFilePath(joinPath(globalConfig.getModulePath(), templateInfo.getPackagePath()));
        }
        File file = new File(templateInfo.getFilePath());
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    private void setPathInfo(Map<String, String> pathInfo, String template, String outputDir, String path, String module) {
        if (StringUtils.isNotBlank(template)) {
            pathInfo.put(path, joinPath(outputDir, packageInfo.get(module)));
        }
    }

    /**
     * 处理表对应的类名称
     *
     * @param tableList 表名称
     * @param config    策略配置项
     * @return 补充完整信息后的表
     */
    private List<TableInfo> processTable(List<TableInfo> tableList, StrategyConfig config) {
        for (TableInfo tableInfo : tableList) {
            String entityName;
            INameConvert nameConvert = strategyConfig.getNameConvert();
            if (null != nameConvert) {
                // 自定义处理实体名称
                entityName = nameConvert.entityNameConvert(tableInfo);
            } else {
                entityName = NamingStrategy.capitalFirst(processName(tableInfo.getName(), config.getNaming(), config.getTablePrefix()));
            }
            if (StringUtils.isNotBlank(globalConfig.getEntityName())) {
                tableInfo.setEntityName(String.format(globalConfig.getEntityName(), entityName));
            } else {
                tableInfo.setEntityName(strategyConfig, entityName);
            }
            if (StringUtils.isNotBlank(globalConfig.getMapperName())) {
                tableInfo.setMapperName(String.format(globalConfig.getMapperName(), entityName));
            } else {
                tableInfo.setMapperName(entityName + ConstVal.MAPPER);
            }
            if (StringUtils.isNotBlank(globalConfig.getXmlName())) {
                tableInfo.setXmlName(String.format(globalConfig.getXmlName(), entityName));
            } else {
                tableInfo.setXmlName(entityName + ConstVal.MAPPER);
            }
            if (StringUtils.isNotBlank(globalConfig.getServiceName())) {
                tableInfo.setServiceName(String.format(globalConfig.getServiceName(), entityName));
            } else {
                tableInfo.setServiceName("I" + entityName + ConstVal.SERVICE);
            }
            if (StringUtils.isNotBlank(globalConfig.getServiceImplName())) {
                tableInfo.setServiceImplName(String.format(globalConfig.getServiceImplName(), entityName));
            } else {
                tableInfo.setServiceImplName(entityName + ConstVal.SERVICE_IMPL);
            }
            if (StringUtils.isNotBlank(globalConfig.getControllerName())) {
                tableInfo.setControllerName(String.format(globalConfig.getControllerName(), entityName));
            } else {
                tableInfo.setControllerName(entityName + ConstVal.CONTROLLER);
            }
            // 检测导入包
            checkImportPackages(tableInfo);
        }
        return tableList;
    }

    /**
     * 检测导入包
     *
     * @param tableInfo ignore
     */
    private void checkImportPackages(TableInfo tableInfo) {
        if (StringUtils.isNotBlank(strategyConfig.getSuperEntityClass())) {
            // 自定义父类
            tableInfo.getImportPackages().add(strategyConfig.getSuperEntityClass());
        }
        if (null != globalConfig.getIdType() && tableInfo.isHavePrimaryKey()) {
            // 指定需要 IdType 场景
            tableInfo.getImportPackages().add(IdType.class.getCanonicalName());
            tableInfo.getImportPackages().add(TableId.class.getCanonicalName());
        }
        if (StringUtils.isNotBlank(strategyConfig.getVersionFieldName())
                && CollectionUtils.isNotEmpty(tableInfo.getFields())) {
            tableInfo.getFields().forEach(f -> {
                if (strategyConfig.getVersionFieldName().equals(f.getName())) {
                    tableInfo.getImportPackages().add(Version.class.getCanonicalName());
                }
            });
        }
    }


    private Set<String> getGraphTags() {
        Set<String> tags = new HashSet<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("show tags");
             ResultSet results = preparedStatement.executeQuery()) {
            while (results.next()) {
                tags.add(results.getString("Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }

    private Set<String> getGraphEdges() {
        Set<String> edges = new HashSet<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("show edges");
             ResultSet results = preparedStatement.executeQuery()) {
            while (results.next()) {
                edges.add(results.getString("Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return edges;
    }

    private String getIdType() {
        String query = String.format("desc space %s", dataSourceConfig.getSpaceName());
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet results = preparedStatement.executeQuery()) {
            results.next();
            String vid_type = results.getString("Vid Type");
            if (vid_type.contains("STRING")) {
                return "String";
            }
            return "Long";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private List<TableInfo> getGraphTableInfo(StrategyConfig config) {
        Set<String> graphEdges = getGraphEdges();

        Set<String> graphTags = getGraphTags();

        String idType = getIdType();
        this.pathTemplateConfig.setIdType(idType);
        //所有的表信息
        List<TableInfo> tableList = new ArrayList<>();
        for (String tableName : config.getInclude()) {
            boolean isEdge = graphEdges.contains(tableName);
            boolean isTag = graphTags.contains(tableName);
            if (isEdge && isTag) {
                throw new RuntimeException("名称" + tableName + "在点和边中都存在，无法识别");
            }
            if (!isEdge && !isTag) {
                throw new RuntimeException("点或边不存在！：" + tableName);
            }
            TableInfo tableInfo = new TableInfo();
            tableInfo.setTag(isTag);
            tableInfo.setEdge(isEdge);
            tableInfo.setName(tableName);
            tableInfo.setIdType(idType);
            tableInfo.setIdJdbcType("String".equals(idType) ? "VARCHAR" : "BIGINT");
            tableInfo.setHavePrimaryKey(false);
            tableList.add(tableInfo);
        }
        tableList.forEach(ti -> convertGraphTableFields(ti, config));
        return tableList;
    }


    /**
     * 获取所有的数据库表信息
     */
    private List<TableInfo> getTablesInfo(StrategyConfig config) {
        List<TableInfo> includeTableList;
        includeTableList = getGraphTableInfo(config);
        return processTable(includeTableList, config);
    }


    /**
     * 表名匹配
     *
     * @param setTableName 设置表名
     * @param dbTableName  数据库表单
     * @return ignore
     */
    private boolean tableNameMatches(String setTableName, String dbTableName) {
        return setTableName.equalsIgnoreCase(dbTableName)
                || StringUtils.matches(setTableName, dbTableName);
    }

    public TableInfo convertGraphTableFields(TableInfo tableInfo, StrategyConfig config) {
        List<TableField> fieldList = new ArrayList<>();
        List<TableField> commonFieldList = new ArrayList<>();
        IDbQuery dbQuery = dataSourceConfig.getDbQuery();
        String tableName = tableInfo.getName();
        try {
            String tableFieldsSql;
            if (tableInfo.isTag()) {
                tableFieldsSql = String.format("DESC TAG `%s`", tableName);
                tableInfo.setTableClass("vertex");
            } else {
                tableFieldsSql = String.format("DESC EDGE `%s`", tableName);
                tableInfo.setTableClass("edge");
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(tableFieldsSql);
                 ResultSet results = preparedStatement.executeQuery()) {
                while (results.next()) {
                    TableField field = new TableField();
                    String columnName = results.getString(dbQuery.fieldName());
                    field.setKeyFlag(false);

                    // 处理其它信息
                    field.setName(columnName);
                    String newColumnName = columnName;
                    IKeyWordsHandler keyWordsHandler = dataSourceConfig.getKeyWordsHandler();
                    if (keyWordsHandler != null && keyWordsHandler.isKeyWords(columnName)) {
                        System.err.printf("当前表[%s]存在字段[%s]为数据库关键字或保留字!%n", tableName, columnName);
                        field.setKeyWords(true);
                        newColumnName = keyWordsHandler.formatColumn(columnName);
                    }
                    field.setColumnName(newColumnName);
                    field.setType(results.getString(dbQuery.fieldType()));
                    field.setJdbcType(NEBULA_TO_JDBC_TYPE.get(field.getType()));
                    INameConvert nameConvert = strategyConfig.getNameConvert();
                    if (null != nameConvert) {
                        field.setPropertyName(nameConvert.propertyNameConvert(field));
                    } else {
                        field.setPropertyName(strategyConfig, processName(field.getName(), config.getColumnNaming()));
                    }
                    field.setColumnType(dataSourceConfig.getTypeConvert().processTypeConvert(globalConfig, field));
                    String fieldCommentColumn = dbQuery.fieldComment();
                    if (StringUtils.isNotBlank(fieldCommentColumn)) {
                        field.setComment(formatComment(results.getString(fieldCommentColumn)));
                    }
                    // 填充逻辑判断
                    List<TableFill> tableFillList = getStrategyConfig().getTableFillList();
                    if (null != tableFillList) {
                        // 忽略大写字段问题
                        tableFillList.stream().filter(tf -> tf.getFieldName().equalsIgnoreCase(field.getName()))
                                .findFirst().ifPresent(tf -> field.setFill(tf.getFieldFill().name()));
                    }
                    if (strategyConfig.includeSuperEntityColumns(field.getName())) {
                        // 跳过公共字段
                        commonFieldList.add(field);
                        continue;
                    }
                    fieldList.add(field);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception：" + e.getMessage());
        }
        tableInfo.setFields(fieldList);
        tableInfo.setCommonFields(commonFieldList);
        return tableInfo;
    }

    /**
     * 连接路径字符串
     *
     * @param parentDir   路径常量字符串
     * @param packageName 包名
     * @return 连接后的路径
     */
    private String joinPath(String parentDir, String packageName) {
        if (StringUtils.isBlank(parentDir)) {
            parentDir = System.getProperty(ConstVal.JAVA_TMPDIR);
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
        return parentDir + packageName;
    }


    /**
     * 连接父子包名
     *
     * @param parent     父包名
     * @param subPackage 子包名
     * @return 连接后的包名
     */
    private String joinPackage(String parent, String subPackage) {
        return StringUtils.isBlank(parent) ? subPackage : (parent + StringPool.DOT + subPackage);
    }


    /**
     * 处理字段名称
     *
     * @return 根据策略返回处理后的名称
     */
    private String processName(String name, NamingStrategy strategy) {
        return processName(name, strategy, strategyConfig.getFieldPrefix());
    }


    /**
     * 处理表/字段名称
     *
     * @param name     ignore
     * @param strategy ignore
     * @param prefix   ignore
     * @return 根据策略返回处理后的名称
     */
    private String processName(String name, NamingStrategy strategy, Set<String> prefix) {
        String propertyName;
        if (prefix.size() > 0) {
            if (strategy == NamingStrategy.underline_to_camel) {
                // 删除前缀、下划线转驼峰
                propertyName = NamingStrategy.removePrefixAndCamel(name, prefix);
            } else {
                // 删除前缀
                propertyName = NamingStrategy.removePrefix(name, prefix);
            }
        } else if (strategy == NamingStrategy.underline_to_camel) {
            // 下划线转驼峰
            propertyName = NamingStrategy.underlineToCamel(name);
        } else {
            // 不处理
            propertyName = name;
        }
        return propertyName;
    }


    public StrategyConfig getStrategyConfig() {
        return strategyConfig;
    }


    public ConfigBuilder setStrategyConfig(StrategyConfig strategyConfig) {
        this.strategyConfig = strategyConfig;
        return this;
    }


    public GlobalConfig getGlobalConfig() {
        return globalConfig;
    }


    public ConfigBuilder setGlobalConfig(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
        return this;
    }


    public InjectionConfig getInjectionConfig() {
        return injectionConfig;
    }


    public ConfigBuilder setInjectionConfig(InjectionConfig injectionConfig) {
        this.injectionConfig = injectionConfig;
        return this;
    }


    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化数据库注释内容
     *
     * @param comment 注释
     * @return 注释
     * @since 3.4.0
     */
    public String formatComment(String comment) {
        return StringUtils.isBlank(comment) ? StringPool.EMPTY : comment.replaceAll("\r\n", "\t");
    }

}
