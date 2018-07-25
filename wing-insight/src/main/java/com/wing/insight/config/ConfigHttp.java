package com.wing.insight.config;

import com.alibaba.fastjson.JSONObject;
import com.qccr.commons.httpclient.HttpRequest;
import com.qccr.commons.httpclient.HttpResult;
import com.qccr.framework.insight.listener.AppEnv;
import org.apache.http.client.fluent.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author 夏飞
 */
public class ConfigHttp {
    private static final String SUPER_CONFIG_URL = "http://superconfig.qccr.com/appConfigDetail/queryConfig4Insight.json";
    private final Logger LOGGER=LoggerFactory.getLogger(ConfigHttp.class);
    public Properties doLoadSuperConfig(AppEnv projEnvVO){
        try{
            String projectNumber = projEnvVO.getProjectNumber();
            String environment = projEnvVO.getModuleEnv().name();
            String subEnv = projEnvVO.getSubEnv();
            String appName=projEnvVO.getAppName();
            Form form = Form.form();
            form.add("appName", appName)
                    .add("projectName", projectNumber)
                    .add("environment", environment)
                    .add("subEnv", subEnv);

            //读取远程数据
            @SuppressWarnings("deprecation")
            HttpResult httpResult = HttpRequest.Post(SUPER_CONFIG_URL)
                    .bodyForm(form.build())
                    .execute();

            //请求失败则直接返回
            if(!httpResult.isSuccess()){
                LOGGER.info("Load superConfig failed ["+ SUPER_CONFIG_URL + "?appName=" + appName + "&projectName="
                        + projectNumber + "&environment=" + environment + "&subEnv=" + subEnv +"]");
                return null;
            }
            JSONObject jsonData = httpResult.getJsonData();
            Boolean success = (Boolean)jsonData.get("success");
            if(!success){
                LOGGER.error("Load superConfig data failed ["+ SUPER_CONFIG_URL + "?appName=" + appName + "&projectName="
                        + projectNumber + "&environment=" + environment + "&subEnv=" + subEnv +"]");
                return null;
            }

            //将远程配置存入Properties中
            Properties superProperties = new Properties();
            List<Map<Object, Object>> dataList = (List<Map<Object, Object>>)jsonData.get("data");
            for(Map<Object, Object> data : dataList){
                Object key = data.get("configKey");
                Object value = data.get("configValue");
                if(value == null){
                    continue;
                }
                superProperties.put(key, value);
            }
            LOGGER.info("Load superConfig data success["+ SUPER_CONFIG_URL + "?appName=" + appName + "&projectName="
                    + projectNumber + "&environment=" + environment + "&subEnv=" + subEnv +"], size=" + superProperties.size());
            return superProperties;
        }catch (Throwable e) {
            LOGGER.error("loadSuperConfig save error url= " + SUPER_CONFIG_URL, e);
        }
        return null;
    }
}
