package com.jon.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import com.jon.encryption.Base64Encryption;

import net.sf.json.JSONObject;

/**
 * Java自带Api发送HTTP请求
 */
public class JavaApiSendHttpRequest {

    private static final Integer READ_TIMEOUT = 5000; // 连接超时
    private static final Integer CONNECTION_TIMEOUT = 10000;// 操作超时

    /**
     * 发送GET请求
     */
    public static String sendGet(String url, String param, String apiKey) {
        StringBuilder result = new StringBuilder();
        try {
            String urlParam = url + "?" + param;
            URL realUrl = new URL(urlParam);
            // 打开和URL直接的连接
            URLConnection urlConnection = realUrl.openConnection();
            // 设置通用属性
            urlConnection.setRequestProperty("accept", "*/*");
            urlConnection.setRequestProperty("connection", "Keep-Alive");
            urlConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 1.5; SV1)");
            urlConnection.setRequestProperty("apikey", apiKey);
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            // 建立实际的连接
            urlConnection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
            // 遍历所有响应头字段
            for (String key : headerFields.keySet()) {
                System.out.println(key + "------>" + headerFields.get(key));
            }
            // 读取相应URL
            try (BufferedReader reader =
                new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8))) {
                String nextLine;
                while ((nextLine = reader.readLine()) != null) {
                    result.append(nextLine);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * 发送POST请求
     */
    public static String sendPost(String url, String param, String apiKey) {
        StringBuilder result = new StringBuilder();
        try {
            URL readerUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection)readerUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                "Mozilla/5.0 (Windows NT 6.1; rv:2.0.1) Gecko/20100101 Firefox/4.0.1");
            connection.setRequestProperty("apikey", apiKey);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setInstanceFollowRedirects(true);
			// 就可以使用conn.getInputStream().read()
            connection.setDoInput(true);
			;// 就可以使用conn.getOutputStream().write()
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
			// 读操作超时
            connection.setReadTimeout(READ_TIMEOUT);
			// 连接操作超时
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.connect();
			// 获取URLConnection对象对应的输出流
			try(OutputStreamWriter output = new OutputStreamWriter(connection.getOutputStream())){
				// 发送请求参数
				output.write(param);
				// flush输出流的缓冲
				output.flush();
			}
			// 获取所有响应头字段
            Map<String, List<String>> headerFields = connection.getHeaderFields();
			// 遍历所有响应头字段
            for (String key : headerFields.keySet()) {
                System.out.println(key + "------>" + headerFields.get(key));
            }
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));){
				String nextLine;
				while ((nextLine = reader.readLine()) != null) {
					result.append(nextLine);
				}
			}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * 上传文件
     */
    public static String SendHttpUploadFile(String httpUrl, String file, String apiKey) {
        BufferedReader buffer;
        String result = null;
        StringBuilder sbf = new StringBuilder();
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("apikey", apiKey);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setInstanceFollowRedirects(true);
            connection.setDoInput(true);// 就可以使用conn.getInputStream().read()
            connection.setDoOutput(true);// 就可以使用conn.getOutputStream().write()
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setReadTimeout(READ_TIMEOUT);// 读操作超时
            connection.setConnectTimeout(CONNECTION_TIMEOUT);// 连接操作超时
            connection.getOutputStream().write(Base64Encryption.decode(file, StandardCharsets.ISO_8859_1.name()).getBytes(StandardCharsets.ISO_8859_1));
            connection.connect();
            InputStream is = connection.getInputStream();
            buffer = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String strRead;
            while ((strRead = buffer.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            buffer.close();
            result = ascii2native(sbf.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
       /* String url = sendGet("http://apis.baidu.com/apistore/weatherservice/cityname","cityname=深圳","1fe02d93e72a22bc6474235054d42125");
        System.out.println(url);
        parseJson(url);*/
        /* File file = new File("d://111.jpg");
         FileInputStream fileInput = new FileInputStream(file);
         FileOutputStream fileout = new FileOutputStream("d://222.jpg");
          String result =null;
          byte buffer[] = new byte[(int) file.length()];
          fileInput.read(buffer);
          result = Base64Encryption.encode(buffer);
          fileInput.close();
          //System.out.println(result);
          fileout.write(Base64Encryption.decode(result));
          fileout.close();
         String url=SendHttpUploadFile("http://apis.baidu.com/image_search/shitu/shitu_image",result,"1fe02d93e72a22bc6474235054d42125");
         System.out.println(url);*/
    }

    private static String ascii2native(String asciicode) {
        String[] asciis = asciicode.split("\\\\u");
        StringBuilder nativeValue = new StringBuilder(asciis[0]);
        try {
            for (int i = 1; i < asciis.length; i++) {
                String code = asciis[i];
                nativeValue.append((char) Integer.parseInt(code.substring(0, 4), 16));
                if (code.length() > 4) {
                    nativeValue.append(code.substring(4));
                }
            }
        } catch (NumberFormatException e) {
            return asciicode;
        }
        return nativeValue.toString();
    }

    public static String parseJson(String url) {
        JSONObject jsonObject = JSONObject.fromObject(url);
        StringBuilder nowWeather = new StringBuilder();
        if (jsonObject != null && jsonObject.size() != 0) {
            Integer errNum = (Integer)jsonObject.get("errNum");
            String errMsg = jsonObject.getString("errMsg");
            System.out.println(errMsg);
            if (errNum == 0) {
                JSONObject retData = jsonObject.getJSONObject("retData");
                String city = (String)retData.get("city");
                // String pinyin = (String) retData.get("pinyin");
                // String citycode = (String) retData.get("citycode");
                String date = (String)retData.get("date");
                String time = (String)retData.get("time");
                // String postCode = (String) retData.get("postCode");
                // String longitude =String.valueOf((Double) retData.get("longitude"));
                // String latitude = String.valueOf((Double) retData.get("latitude"));
                // String altitude = (String) retData.get("altitude");
                String weather = (String)retData.get("weather");
                String temp = (String)retData.get("temp");
                String l_tmp = (String)retData.get("l_tmp");
                String h_tmp = (String)retData.get("h_tmp");
                String WD = (String)retData.get("WD");
                String WS = (String)retData.get("WS");
                String sunrise = (String)retData.get("sunrise");
                String sunset = (String)retData.get("sunset");
                nowWeather.append("城市:" + city + " 发布日期:" + date + " " + time + " 天气情况 :" + weather + " 气温 ：")
                    .append(temp + " 最低气温:" + l_tmp + " 最高气温:" + h_tmp + " 风向:" + WD + " 风力：" + WS + " 日出时间:" + sunrise
                        + " 日落时间:" + sunset);
                System.out.println(nowWeather);
            }
        }
        return nowWeather.toString();
    }

}
