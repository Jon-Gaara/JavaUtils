package com.jon.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class JacksonUtilTest {

    @Test
    public void consecutiveSerializationsDoNotAccumulate() throws Exception {
        JacksonUtil util = new JacksonUtil("yyyy-MM-dd");
        Map<String, Object> data = new HashMap<>();
        data.put("k", "v1");

        String first = util.writeJsonObjectMessageForJsonGenerator(data);
        String second = util.writeJsonObjectMessageForJsonGenerator(data);

        assertTrue("first output: " + first, first.startsWith("{") && first.endsWith("}"));
        assertEquals("shared StringWriter/generator must not concatenate output", first, second);
        assertFalse("root value separator must be disabled", second.startsWith(" "));
    }

    @Test
    public void objectMapperPathDoesNotAccumulate() throws Exception {
        JacksonUtil util = new JacksonUtil("yyyy-MM-dd");
        Map<String, Object> data = new HashMap<>();
        data.put("k", "v2");

        String first = util.writeJsonObjectMessageForObjectMapper(data);
        String second = util.writeJsonObjectMessageForObjectMapper(data);
        assertEquals(first, second);
    }
}
