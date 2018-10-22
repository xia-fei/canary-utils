package com.wing.insight.config;

import com.google.common.base.Throwables;
import com.qccr.framework.insight.listener.AppEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * @author 夏飞
 * 独立的类用来下载配置
 */
public class LoadConfig {

    private AppNameContext appNameContext;
    private Logger LOGGER = LoggerFactory.getLogger(LoadConfig.class);

    private Properties superConfigProperties = null;
    private boolean isMachineConfig = false;

    public Properties getProperties() {
        return superConfigProperties;
    }

    public LoadConfig(String appName) {
        this.appNameContext = new AppNameContext(appName);
        startLoad();
    }

    private void startLoad() {
        Properties locationProperties = getLocationProperties();
        buildAppEnv(locationProperties);
        if (isMachineConfig || AppEnv.get().isNeedSuperConfig()) {
            LOGGER.info("加载AppEnv配置信息" + AppEnv.get().toString());
            superConfigProperties = new ConfigHttp().doLoadSuperConfig(AppEnv.get());
            try {
                new ConfigStorage().saveSuperProperties(superConfigProperties);
            } catch (IOException e) {
                LOGGER.warn("配置信息保存失败");
            }
            if (superConfigProperties == null) {
                superConfigProperties = loadLocationFile();
                LOGGER.info("配置中心获取失败从本地加载");
            }
        } else {
            superConfigProperties = loadLocationFile();
            LOGGER.info("needSuperConfig=false,从本地获取");
        }
        superConfigProperties.setProperty("app_name", this.appNameContext.getAppName());
    }


    private Properties loadLocationFile() {
        try {
            Properties locationProperties = new Properties();
            locationProperties.load(new FileInputStream(AppEnv.get().getSuperConfigFile()));
            return locationProperties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setSystemProperties() {
        for (Map.Entry<Object, Object> entry : this.superConfigProperties.entrySet()) {
            System.getProperties().put(entry.getKey(), entry.getValue());
        }
    }


    private Properties getLocationProperties() {
        File locationFile;
        if (appNameContext.getUserLocationFile().exists()) {
            locationFile = appNameContext.getUserLocationFile();
        } else if (appNameContext.getMachineLocationFile().exists()) {
            locationFile = appNameContext.getMachineLocationFile();
            isMachineConfig = true;
        } else {
            throw new IllegalArgumentException("请初始化配置中心信息");
        }
        Properties LocationProperties = new Properties();
        try {
            LocationProperties.load(new FileInputStream(locationFile));
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
        return LocationProperties;
    }

    private void buildAppEnv(Properties locationProperties) {
        String readFromSuperConfig = locationProperties.getProperty("read_from_super_config");
        String projectNumber = locationProperties.getProperty("project_number");
        String projectEnv = locationProperties.getProperty("project_env");
        String projectSubEnv = locationProperties.getProperty("sub_env");
        AppEnv.create(appNameContext.getAppName(), projectNumber, projectEnv, projectSubEnv, isYes(readFromSuperConfig), appNameContext.getConfigFile(projectNumber, projectEnv));
    }

    private boolean isYes(String readFromSuperConfig) {
        return readFromSuperConfig != null && readFromSuperConfig.equalsIgnoreCase("yes");
    }


}
