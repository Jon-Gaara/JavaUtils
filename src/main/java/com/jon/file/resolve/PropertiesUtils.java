package com.jon.file.resolve;

import java.nio.charset.StandardCharsets;
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
            return new String(rs.getString(propertiesKey).getBytes(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8);
        } catch (Exception e) {
            loggger.error(e.getMessage());
        }
        return null;
    }
}
