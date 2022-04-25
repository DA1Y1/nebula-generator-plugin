package com.mininglamp.km.nebula.generator.setting;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.mininglamp.km.nebula.generator.model.Config;
import com.mininglamp.km.nebula.generator.model.User;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


/**
 * 配置持久化
 *
 * @author daiyi
 * @date 2021/9/17
 */
@State(name = "NeublaPersistentConfig", storages = {@Storage("nebula-generator-config.xml")})
public class PersistentConfig implements PersistentStateComponent<PersistentConfig> {

    private Map<String, Config> initConfig;
    private Map<String, User> users;
    private Map<String, Config> historyConfigList;

    @Nullable
    public static PersistentConfig getInstance() {
        return ApplicationManager.getApplication().getService(PersistentConfig.class);
    }

    @Nullable
    public PersistentConfig getState() {
        return this;
    }

    @Override
    public void loadState(PersistentConfig persistentConfig) {
        XmlSerializerUtil.copyBean(persistentConfig, this);
    }


    public Map<String, Config> getInitConfig() {
        return initConfig;
    }

    public void setInitConfig(Map<String, Config> initConfig) {
        this.initConfig = initConfig;
    }


    public Map<String, Config> getHistoryConfigList() {
        return historyConfigList;
    }

    public void setHistoryConfigList(Map<String, Config> historyConfigList) {
        this.historyConfigList = historyConfigList;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public void setUsers(Map<String, User> users) {
        this.users = users;
    }
}
