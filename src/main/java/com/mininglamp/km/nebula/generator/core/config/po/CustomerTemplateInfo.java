package com.mininglamp.km.nebula.generator.core.config.po;

import lombok.Data;

/**
 * @author xuliang
 */
@Data
public class CustomerTemplateInfo {

    /**
     * 模板路径
     */
    String templatePath;
    /**
     * 文件后缀
     */
    String suffix;
    /**
     * 文件名
     */
    String fileName;
    /**
     * 报名
     */
    String folder;
    /**
     * package 路径
     */
    String packagePath;
    /**
     * 文件路径
     */
    String filePath;
    /**
     * key, 会把该字段绑定到beetl的变量里
     */
    String key;

    public CustomerTemplateInfo(String key, String templatePath, String suffix, String fileName, String folder) {
        this.key = key;
        this.templatePath = templatePath;
        this.suffix = suffix;
        this.fileName = fileName;
        this.folder = folder;
    }

}
