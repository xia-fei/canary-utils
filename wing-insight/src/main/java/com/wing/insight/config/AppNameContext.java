package com.wing.insight.config;

import com.google.common.base.Joiner;

import java.io.File;

/**
 * @author 夏飞
 * APPName 上下文
 */
public class AppNameContext {
    private String appName;

    public String getAppName() {
        return appName;
    }

    public AppNameContext(String appName) {
        this.appName = appName;
    }

    /**
     * 基础环境配置
     */
    public String getBasePath() {
        return "/data/html/configs/" + appName + "/insight/";
    }

    public File getUserLocationFile() {
        return new File(getBasePath() + appName + "_super_env.properties");
    }

    public File getMachineLocationFile() {
        return new File("/data/html/insight/" + appName + "/super_env.properties");
    }

    public File getConfigFile(String projectNumber, String projectEnv){
        String fileName = Joiner.on("_").join(appName, projectNumber, projectEnv.toUpperCase(), "super_config.properties");
        return new File(getBasePath() + fileName);
    }

}
