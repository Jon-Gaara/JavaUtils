package com.jon.json;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class JsonUtilTest {

    public static class Sample {
        private Date birth;

        public Sample(Date birth) {
            this.birth = birth;
        }

        public Date getBirth() {
            return birth;
        }
    }

    @Test
    public void formatsDateWithGivenPattern() {
        @SuppressWarnings("deprecation")
        Date date = new Date(120, 0, 2); // 2020-01-02
        String json = JsonUtil.writeJsonObjectMessage(new Sample(date), "yyyy-MM-dd");
        assertTrue(json, json.contains("\"birth\":\"2020-01-02\""));
    }

    @Test
    public void serializesListAsArray() {
        Map<String, Object> row = new HashMap<>();
        row.put("name", "jon");
        String json = JsonUtil.writeJsonArrayMessage(Arrays.asList(row), "yyyy-MM-dd");
        assertTrue(json, json.startsWith("[") && json.endsWith("]"));
        assertTrue(json, json.contains("\"name\":\"jon\""));
    }

    @Test
    public void serializesNestedBeansWithoutJsonLib() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", 1);
        data.put("ok", true);
        String json = JsonUtil.writeJsonObjectMessage(data, null);
        assertTrue(json, json.contains("\"id\":1"));
        assertFalse("date format should not leak when pattern is null", json.contains("yyyy"));
    }
}
