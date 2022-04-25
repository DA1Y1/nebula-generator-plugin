package com.mininglamp.km.nebula.generator.core.config;


import com.mininglamp.km.nebula.generator.core.config.po.CustomerTemplateInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于nebula的路径查询，需要单独生成一套模板
 * @author xuliang
 */
public class PathTemplateConfig {

    List<CustomerTemplateInfo> templateInfo = new ArrayList<>();

    public PathTemplateConfig() {
    }


    /**
     * ID类型
     */
    String idType = "String";

    /**
     * 是否生成
     */
    boolean isCreate = true;

    public List<CustomerTemplateInfo> getTemplateInfo() {
        return templateInfo;
    }

    public void setTemplateInfo(List<CustomerTemplateInfo> templateInfo) {
        this.templateInfo = templateInfo;
    }

    public void addTemplateInfo(CustomerTemplateInfo templateInfo){
        this.templateInfo.add(templateInfo);
    }

    public boolean isCreate() {
        return isCreate;
    }

    public void setCreate(boolean create) {
        isCreate = create;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdType() {
        return idType;
    }
}
