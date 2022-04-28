package com.mininglamp.km.nebula.generator.model;


import lombok.*;

/**
 * 界面配置
 *
 * @author daiyi
 * @date 2021/9/17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Config {

    /**
     * 数据库url
     */
    private String url;

    /**
     * 配置名称
     */
    private String name;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 主键
     */
    private String primaryKey;

    /**
     * 实体名 (未使用)
     */
    private String modelName;

    /**
     * dao名称 (未使用)
     */
    private String daoName;

    /**
     * domain后缀
     */
    private String modelSuffix;

    /**
     * domain后缀
     */
    private String mapperSuffix;

    /**
     * dao后缀
     */
    private String daoSuffix;

    /**
     * 工程目录
     */
    private String projectFolder;
    /**
     * model实体包路径
     */
    private String modelPackage;
    /**
     * model工程路径
     */
    private String modelTargetFolder;
    /**
     * modelmaven路径
     */
    private String modelMvnPath;

    /**
     * 以下 (未使用)
     */
    private String daoPackage;
    private String daoTargetFolder;
    private String daoMvnPath;

    private String xmlPackage;
    private String xmlTargetFolder;
    private String xmlMvnPath;

    private String jdbcPath;

}
