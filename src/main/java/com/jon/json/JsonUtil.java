package com.jon.json;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Java 对象转 JSON（由 json-lib 迁移到 Jackson）。
 * 
 * @author yumaolin
 */
public class JsonUtil {

    private static ObjectMapper createMapper(String dateFormat) {
        ObjectMapper mapper = new ObjectMapper();
        if (dateFormat != null && !dateFormat.isEmpty()) {
            mapper.setDateFormat(new SimpleDateFormat(dateFormat, Locale.CHINESE));
        }
        // 空 bean 序列化为 {} 而不是抛异常，保持原 json-lib 行为
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.FAIL_ON_SELF_REFERENCES);
        return mapper;
    }

    /**
     * Java bean转Json,格式化Date对象
     */
    public static String writeJsonObjectMessage(Object obj, String dateFormat) {
        try {
            return createMapper(dateFormat).writeValueAsString(obj);
        } catch (Exception e) {
            throw new IllegalStateException("JSON 序列化失败", e);
        }
    }

    /**
     * List 转Json,格式化Date对象
     */
    public static String writeJsonArrayMessage(Object obj, String dateFormat) {
        try {
            return createMapper(dateFormat).writeValueAsString(obj);
        } catch (Exception e) {
            throw new IllegalStateException("JSON 序列化失败", e);
        }
    }
}
