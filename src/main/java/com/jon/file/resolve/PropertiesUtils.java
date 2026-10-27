package com.jon.file.resolve;

import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtils {
    private static final Logger loggger = LoggerFactory.getLogger(PropertiesUtils.class);

    public static String getPropertiesValue(String propertiesKey) {
        try {
            Locale locale1 = new Locale("zh", "CN");
            ResourceBundle rs = ResourceBundle.getBundle("mail", locale1);
            // Java 9+ 默认按 UTF-8 读取 properties，ISO-8859-1 中转会把中文变成 '?'
            return rs.getString(propertiesKey);
        } catch (Exception e) {
            loggger.error("读取配置失败: " + propertiesKey, e);
            // 返回 null 只会把 NPE 推迟到调用方，fail fast 保留真实原因
            throw new IllegalStateException("缺少配置项或配置文件不可读: " + propertiesKey, e);
        }
    }
}
