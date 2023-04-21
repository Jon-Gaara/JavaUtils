package com.jon.http;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientSendHttpRequest {

    private static String requestEncoding = "UTF-8";// 请求编码
    private static PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    static {
        // Increase max total connection to 200
        cm.setMaxTotal(200);
        // Increase default max connection per route to 20
        cm.setDefaultMaxPerRoute(20);
    }

    /**
     * post方式提交表单（模拟用户登录请求）
     */
    public static String sendPostForm(String url, Map<String, String> param) {
        // 创建默认的HttpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        // HttpPost实例
        HttpPost httpPost = new HttpPost(url);
        // 設置httpGet的头部參數信息
        // httpPost.setHeader(arg0);
        // httpPost.setHeader("apikey","1fe02d93e72a22bc6474235054d42125");
        httpPost.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
        httpPost.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));
        // 创建参数队列
        List<NameValuePair> list = new ArrayList<>();
        if (param != null && param.size() > 0) {
            /*
             * Iterator<String> it = param.keySet().iterator();
             * while(it.hasNext()){ String key = it.next(); list.add(new
             * BasicNameValuePair(key,param.get(key))); }
             */
            for (Map.Entry<String, String> entry : param.entrySet()) {
                list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        UrlEncodedFormEntity uefEntity;
        String entityStr = "";
        try {
            uefEntity = new UrlEncodedFormEntity(list, requestEncoding);
            httpPost.setEntity(uefEntity);
            System.out.println("executing request " + httpPost.getURI());
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                System.out.println(response.getStatusLine().getStatusCode());
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        entityStr = EntityUtils.toString(entity, requestEncoding);
                        System.out.println("----------------------------------");
                        System.out.println("Response content: " + entityStr);
                        System.out.println("----------------------------------");
                        return entityStr;
                    }
                }

            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * post提交Json数据
     */
    public static String sendPostFormForJson(String url, String param) {
        // 创建默认的HttpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // HttpPost实例
        HttpPost httpPost = new HttpPost(url);
        // 設置httpGet的头部參數信息
        // httpPost.setHeader(arg0);
        // httpPost.setHeader("apikey","1fe02d93e72a22bc6474235054d42125");
        httpPost.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
        httpPost.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));
        String entityStr = "";
        try {
            StringEntity jsonEntity = new StringEntity(param, ContentType.APPLICATION_JSON);
            // uefEntity = new UrlEncodedFormEntity(list,requestEncoding);
            httpPost.setEntity(jsonEntity);
            System.out.println("executing request " + httpPost.getURI());
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                System.out.println(response.getStatusLine().getStatusCode());
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        entityStr = EntityUtils.toString(entity, requestEncoding);
                        System.out.println("----------------------------------");
                        System.out.println("Response content: " + entityStr);
                        System.out.println("----------------------------------");
                        return entityStr;
                    }
                }

            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 发送Get请求
     */
    public static String sendGet(String url, String param) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        if (StringUtils.isNotBlank(param)) {
            url = url + "?" + param;
        }
        try {
            // 创建httpget
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
            httpGet.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));
            System.out.println("executing request " + httpGet.getURI());
            // httpGet.setHeader("apikey","1fe02d93e72a22bc6474235054d42125");
            // httpGet.setHeader("apix-key","a4958d51d93f47d74bf712d8f4d83491");
            // 执行get请求.
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                HttpEntity entity = response.getEntity();
                System.out.println("-------------------------------");
                System.out.println(response.getStatusLine().getStatusCode());
                if (response.getStatusLine().getStatusCode() == 200) {
                    // 打印响应内容长度
                    System.out.println("Response content length: " + entity.getContentLength());
                    // 打印响应内容
                    System.out.println("Response content: " + EntityUtils.toString(entity));
                }
                System.out.println("------------------------------------");
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 上传文件
     */
    public static void sendHttpUploadFile(String url, Map<File, String> files, Map<String, String> textValue) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
            httpPost.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));
            MultipartEntityBuilder muitipart = MultipartEntityBuilder.create();

            // HttpEntity entity =
            // MultipartEntityBuilder.create().addPart("bin",bin).addPart("comment",comment).build();
            /**
             * 文件上传
             */
            for (Entry<File, String> fileSet : files.entrySet()) {
                String setKey = fileSet.getValue();
                File setValue = fileSet.getKey();
                FileBody bin = new FileBody(setValue);
                muitipart.addPart(setKey, bin);
                StringBody comment = new StringBody("A binary file of some kind", ContentType.DEFAULT_BINARY);
                muitipart.addPart("comment", comment);
            }

            /**
             * 属性上传
             */
            for (Entry<String, String> textSet : textValue.entrySet()) {
                muitipart.addTextBody(textSet.getKey(), textSet.getValue());
            }

            HttpEntity entity = muitipart.build();
            httpPost.setEntity(entity);
            System.out.println("executing request " + httpPost.getRequestLine());
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                System.out.println("------------------------------");
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (response.getStatusLine().getStatusCode() == 200) {
                    System.out.println("Response content length: " + resEntity.getContentLength());
                    System.out.println("Response content: " + EntityUtils.toString(resEntity));
                }
                EntityUtils.consume(resEntity);
            } finally {
                httpClient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Map<File, String> fileMap = new LinkedHashMap<>();
        fileMap.put(new File("d:\\111.jpg"), "idCardPic");
        fileMap.put(new File("d:\\222.jpg"), "idCardPic");
        fileMap.put(new File("d:\\123123.png"), "idCardPic");

        Map<String, String> textMap = new LinkedHashMap<>();
        textMap.put("userId", "1001639541");
        textMap.put("userName", "18128866178");
        textMap.put("password", "yyg123abc@zl");
        textMap.put("idCard", "450803198910206490");
        textMap.put("bankCode", "10244");
        textMap.put("payPassWord", "10244");
        textMap.put("urPayPassword", "10244");
        textMap.put("bankName", "10244");
        textMap.put("accountName", "10244");
        textMap.put("phone", "18888888888");
        textMap.put("billType", "01");
        textMap.put("pageSize", "10");
        textMap.put("pageNo", "1");
        // textMap.put("umId", "1");
        textMap.put("umRead", "01");
        // textMap.put("umStatus", "00");
        textMap.put("password", "qwer123456");

        HttpClientSendHttpRequest.sendPostForm("http://cs.ego168.cn/api/certification/billingDetails.action", textMap);
        // HttpClientSendHttpRequest.SendHttpUploadFile("http://localhost/api/certification/uploadUsercertification.action",fileMap,textMap);
    }
}
