package com.wing.core.util;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 夏飞
 */
public class ExceptionUtils {
    private static final Logger LOGGER=LoggerFactory.getLogger(ExceptionUtils.class);


    public static String getExceptionMessage(RuntimeException e) {
        try {
            if (NullPointerException.class.isInstance(e)) {
                String[] stackFrames = org.apache.commons.lang3.exception.ExceptionUtils.getStackFrames(e);
                return getFramesMessage(stackFrames, 3);
            } else {
                return fixedLenString(e.getMessage());
            }
        } catch (RuntimeException e2) {
            LOGGER.error("获取excel执行异常信息失败", e2);
        }
        return "未知异常，请联系开发";
    }


    private static String getFramesMessage(String[] stackFrames, int n) {
        n = stackFrames.length > n ? n : stackFrames.length;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            stringBuilder.append(stackFrames[i]).append("\n");
        }
        return stringBuilder.toString();
    }


    private static String fixedLenString(String str) {
        if (!Strings.isNullOrEmpty(str) && str.length() > 1024) {
            return str.substring(0, 1024);
        } else {
            return str;
        }
    }
}
