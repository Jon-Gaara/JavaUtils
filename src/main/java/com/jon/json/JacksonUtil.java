package com.jon.json;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class JacksonUtil {
    private ObjectMapper objectMapper;
    private JsonGenerator jsonGenerator;
    private StringWriter sWriter = new StringWriter();

    public JacksonUtil(String dateFormat) throws IOException {
        objectMapper = new ObjectMapper();
        // jsonGenerator = objectMapper.getFactory().createGenerator(out,JsonEncoding.UTF8);
        jsonGenerator = objectMapper.getFactory().createGenerator(sWriter);
        SimpleDateFormat simple = new SimpleDateFormat(dateFormat);
		//自定义日期形式
        objectMapper.setDateFormat(simple);
    }

    public String writeJsonObjectMessageForJsonGenerator(Object date) throws IOException {
        jsonGenerator.writeObject(date);
        return sWriter.toString();
    }

    public String writeJsonObjectMessageForJsonGenerator(Map<String, Object> data) throws IOException {
        jsonGenerator.writeStartObject();
        for (Map.Entry<String, Object> map : data.entrySet()) {
            jsonGenerator.writeObjectField(map.getKey(), map.getValue());
        }
        jsonGenerator.writeEndObject();
        jsonGenerator.flush();
        return sWriter.toString();
    }

    public String writeJsonArrayMessageForJsonGenerator(List<Map<String, Object>> dataList) throws IOException {
        jsonGenerator.writeStartArray();
        for (Map<String, Object> data : dataList) {
            jsonGenerator.writeStartObject();
            for (Map.Entry<String, Object> map : data.entrySet()) {
                jsonGenerator.writeObjectField(map.getKey(), map.getValue());
            }
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.flush();
        return sWriter.toString();
    }

    public String writeJsonObjectMessageForObjectMapper(Object date) throws IOException {
        objectMapper.writeValue(sWriter, date);
        return sWriter.toString();
    }

    public Object JsonStringToJavaBean(String jsonString, Class<?> clazz) throws IOException {
		// 可用转换普通javaBean/List/Map/对象数组
        Object obj = objectMapper.readValue(jsonString, clazz);
        return obj;
    }

    public String JavaBeanToXML(Object obj) throws IOException {
        XmlMapper xml = new XmlMapper();
        xml.writeValue(sWriter, obj);
        return sWriter.toString();
        // xml.writeValueAsString(obj);//此方法也行
    }

    public void destory() {
        try {
            if (jsonGenerator != null) {
                jsonGenerator.flush();
            }
            if (jsonGenerator != null && !jsonGenerator.isClosed()) {
                jsonGenerator.close();
            }
            jsonGenerator = null;
            objectMapper = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("1231231", 1231231);
        map.put("asdasda", "asdasdsa");
        map.put("date", new Date());
        for (int i = 0; i < 5; i++) {
            mapList.add(map);
        }
        JacksonUtil jackson = new JacksonUtil("yyyy-MM-dd");
        jackson.destory();
    }

}
