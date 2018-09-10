package com.wing.insight.config;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.qccr.framework.insight.listener.AppEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author 夏飞
 */
public class ConfigMain {
    private final Logger LOGGER = LoggerFactory.getLogger(ConfigMain.class);

    private final ConfigHttp configHttp = new ConfigHttp();
    private final AppNameContext appNameContext;
    /**
     * 应用名称
     */
    private final String appName;

    /**
     * 真实配置文件
     */
    private Properties realConfigProperties;

    public ConfigMain(String appName) {
        this.appName = appName;
        this.appNameContext = new AppNameContext(appName);
        init();
    }

    /**
     * sept1. 获取坐标
     * sept2. 请求配置信息
     * sept3. 储存下来
     */
    private void init() {
        Properties locationProperties = getLocationProperties();
        buildAppEnv(locationProperties);
        LOGGER.info("加载AppEnv配置信息" + AppEnv.get().toString());
        Properties configProperties = configHttp.doLoadSuperConfig(AppEnv.get());
        if (configProperties != null) {
            try {
                new ConfigStorage().saveSuperProperties(configProperties);
            } catch (IOException e) {
                LOGGER.error("配置文件保存失败", e);
                throw new RuntimeException(e);
            }
        } else {
            LOGGER.error("配置中文心文件拉取失败将从本地读取");
        }

    }

    private void buildAppEnv(Properties locationProperties) {
        String readFromSuperConfig = locationProperties.getProperty("read_from_super_config");
        String projectNumber = locationProperties.getProperty("project_number");
        String projectEnv = locationProperties.getProperty("project_env");
        String projectSubEnv = locationProperties.getProperty("sub_env");
        AppEnv.create(appName, projectNumber, projectEnv, projectSubEnv, Boolean.valueOf(readFromSuperConfig), getConfigFile(projectNumber, projectEnv));
    }

    /**
     * 获取配置文件位置
     */
    private File getConfigFile(String projectNumber, String projectEnv) {
        String fileName = Joiner.on("_").join(appName, projectNumber, projectEnv.toUpperCase(), "super_config.properties");
        return new File(appNameContext.getBasePath() + fileName);
    }

    private Properties getLocationProperties() {
        File locationFile;
        if (appNameContext.getUserLocationFile().exists()) {
            locationFile = appNameContext.getUserLocationFile();
        } else if (appNameContext.getMachineLocationFile().exists()) {
            locationFile = appNameContext.getMachineLocationFile();
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


}
