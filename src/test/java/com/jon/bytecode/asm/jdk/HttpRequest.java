package com.jon.bytecode.asm.jdk;


import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String url;
    private Map<String, String> header = new HashMap<>();
    private String method;
    private String body;

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public String getMethod() {
        return method;
    }

    public String getBody() {
        return body;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
