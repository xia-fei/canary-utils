package com.wing.insight.config;

import com.qccr.framework.insight.listener.AppEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author 夏飞
 */
public class ConfigStorage {
    private final Logger LOGGER = LoggerFactory.getLogger(ConfigStorage.class);


    public void saveSuperProperties(Properties superProperties) throws IOException {
        File configFile = AppEnv.get().getSuperConfigFile();
        //目录不存在则创建
        if (!configFile.getParentFile().exists()) {
            configFile.getParentFile().mkdirs();
        }

        //保存
        superProperties.store(new FileOutputStream(configFile), "super_config");
        LOGGER.info("配件文件保存成功 path:{}", configFile.getAbsolutePath());
    }
}
