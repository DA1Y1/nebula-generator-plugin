package com.mininglamp.km.nebula.generator.model;


import lombok.*;

/**
 * 界面配置
 *
 * @author daiyi
 * @date 2021/9/17
 */
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
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
    private String modelPostfix;

    /**
     * domain后缀
     */
    private String mapperPostfix;

    /**
     * dao后缀
     */
    private String daoPostfix;

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

    /**
     * 是否分页
     */
    private boolean offsetLimit;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getDaoName() {
        return daoName;
    }

    public void setDaoName(String daoName) {
        this.daoName = daoName;
    }

    public String getModelPostfix() {
        return modelPostfix;
    }

    public void setModelPostfix(String modelPostfix) {
        this.modelPostfix = modelPostfix;
    }

    public String getMapperPostfix() {
        return mapperPostfix;
    }

    public void setMapperPostfix(String mapperPostfix) {
        this.mapperPostfix = mapperPostfix;
    }

    public String getDaoPostfix() {
        return daoPostfix;
    }

    public void setDaoPostfix(String daoPostfix) {
        this.daoPostfix = daoPostfix;
    }

    public String getProjectFolder() {
        return projectFolder;
    }

    public void setProjectFolder(String projectFolder) {
        this.projectFolder = projectFolder;
    }

    public String getModelPackage() {
        return modelPackage;
    }

    public void setModelPackage(String modelPackage) {
        this.modelPackage = modelPackage;
    }

    public String getModelTargetFolder() {
        return modelTargetFolder;
    }

    public void setModelTargetFolder(String modelTargetFolder) {
        this.modelTargetFolder = modelTargetFolder;
    }

    public String getModelMvnPath() {
        return modelMvnPath;
    }

    public void setModelMvnPath(String modelMvnPath) {
        this.modelMvnPath = modelMvnPath;
    }

    public String getDaoPackage() {
        return daoPackage;
    }

    public void setDaoPackage(String daoPackage) {
        this.daoPackage = daoPackage;
    }

    public String getDaoTargetFolder() {
        return daoTargetFolder;
    }

    public void setDaoTargetFolder(String daoTargetFolder) {
        this.daoTargetFolder = daoTargetFolder;
    }

    public String getDaoMvnPath() {
        return daoMvnPath;
    }

    public void setDaoMvnPath(String daoMvnPath) {
        this.daoMvnPath = daoMvnPath;
    }

    public String getXmlPackage() {
        return xmlPackage;
    }

    public void setXmlPackage(String xmlPackage) {
        this.xmlPackage = xmlPackage;
    }

    public String getXmlTargetFolder() {
        return xmlTargetFolder;
    }

    public void setXmlTargetFolder(String xmlTargetFolder) {
        this.xmlTargetFolder = xmlTargetFolder;
    }

    public String getXmlMvnPath() {
        return xmlMvnPath;
    }

    public void setXmlMvnPath(String xmlMvnPath) {
        this.xmlMvnPath = xmlMvnPath;
    }
}
