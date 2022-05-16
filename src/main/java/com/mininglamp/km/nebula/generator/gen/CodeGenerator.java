package com.mininglamp.km.nebula.generator.gen;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.Messages;
import com.mininglamp.km.nebula.generator.core.AutoGenerator;
import com.mininglamp.km.nebula.generator.core.InjectionConfig;
import com.mininglamp.km.nebula.generator.core.config.*;
import com.mininglamp.km.nebula.generator.core.config.po.TableInfo;
import com.mininglamp.km.nebula.generator.core.config.rules.NamingStrategy;
import com.mininglamp.km.nebula.generator.core.engine.BeetlTemplateEngine;
import com.mininglamp.km.nebula.generator.core.toolkit.StringPool;
import com.mininglamp.km.nebula.generator.core.toolkit.annotation.DbType;
import com.mininglamp.km.nebula.generator.model.Config;
import com.mininglamp.km.nebula.generator.tools.PunctuationConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author daiyi
 * @date 2021/9/17
 */

public class CodeGenerator {
    private static final Logger logger = Logger.getInstance(CodeGenerator.class);

    private static CodeGenerator instance = null;

    private CodeGenerator() {
    }

    public static CodeGenerator getInstance() {
        if (Objects.isNull(instance)) return new CodeGenerator();
        return instance;
    }

    /**
     * @param generatorConfig 界面配置
     * @param url             数据库连接
     * @param username        用户名
     * @return 是否成功
     */
    public boolean gen(Config generatorConfig, String url, String username) {

        CodeGenerator commonCodeGen = new CodeGenerator();
        // 数据库连接配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(url);
        dataSourceConfig.setDriverName(com.mininglamp.km.nebula.generator.model.DbType.NEBULA.getDriverClass());
        dataSourceConfig.setSpaceName(getNebulaDataSpaceName(url));
        dataSourceConfig.setUsername(username);
        CredentialAttributes attributes_get = new CredentialAttributes("nebula-mybatis-generator-" + url, username, this.getClass(), false);
        String password = PasswordSafe.getInstance().getPassword(attributes_get);
        dataSourceConfig.setPassword(password);

        dataSourceConfig.setDbType(DbType.NEBULA);
        return commonCodeGen.genCode(dataSourceConfig, generatorConfig);
    }

    /**
     * 代码生成
     *
     * @param dataSourceConfig 数据连接
     * @param generatorConfig  界面配置
     * @return 是否成功
     */
    public boolean genCode(DataSourceConfig dataSourceConfig, Config generatorConfig) {

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        mpg.setDataSource(dataSourceConfig);

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        logger.info("nebula-generator 代码生成schema：" + generatorConfig.getTableName());
        logger.info("nebula-generator 代码生成路径：" + generatorConfig.getProjectFolder());
        gc.setOutputDir(generatorConfig.getProjectFolder() + PunctuationConstants.SLASH + generatorConfig.getDaoMvnPath());
        gc.setAuthor(System.getProperties().get("user.name").toString());
        gc.setOpen(false);

        // 设置文件是否覆盖
        gc.setFileOverride(true);
        gc.setBaseColumnList(true);
        gc.setBaseResultMap(true);
        gc.setEntityName("%s" + generatorConfig.getModelSuffix());
        gc.setMapperName("%s" + generatorConfig.getDaoSuffix());
        gc.setXmlName("%s" + generatorConfig.getMapperSuffix());
        mpg.setGlobalConfig(gc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
            }

            /**
             * 禁止生成的类
             * @param tableInfo
             */
            @Override
            public void initTableMap(TableInfo tableInfo) {
            }
        };
        String templatePath = "template/mapper.xml.btl";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return generatorConfig.getProjectFolder()
                        + PunctuationConstants.SLASH + generatorConfig.getXmlMvnPath()
                        + PunctuationConstants.SLASH + generatorConfig.getXmlPackage()
                        + PunctuationConstants.SLASH + tableInfo.getXmlName() + StringPool.DOT_XML;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        templateConfig.setController(null);
        templateConfig.setService(null);
        templateConfig.setServiceImpl(null);
        mpg.setTemplate(templateConfig);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setEntity("domain");
        pc.setParent(generatorConfig.getModelPackage());
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setEntityTableFieldAnnotationEnable(true);
        strategy.setEntityColumnConstant(true);
        strategy.setChainModel(true);
        // 公共父类
        strategy.setTablePrefix("");
        strategy.setInclude(generatorConfig.getTableName());
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new BeetlTemplateEngine());

        try {
            mpg.execute();
        } catch (Exception e) {
            // 日志答应
            Messages.showInfoMessage(e.getMessage(), "Error");
            logger.warn("nebula-generator 代码生成失败 {}" + e.getMessage(), e);
            return false;
        }
        logger.info("nebula-generator 代码生成完成");
        return true;

    }

    /**
     * 从数据库连接url中获取spacename
     *
     * @param url 数据库连接
     * @return spacename
     */
    public String getNebulaDataSpaceName(String url) {
        int last = url.contains(PunctuationConstants.QUESTION) ? url.indexOf(PunctuationConstants.QUESTION) : url.length();
        int first = url.lastIndexOf("/", last);
        return url.substring(first + 1, last);
    }
}
