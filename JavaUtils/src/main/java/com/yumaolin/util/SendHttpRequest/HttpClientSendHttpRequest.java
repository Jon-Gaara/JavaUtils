package com.yumaolin.util.SendHttpRequest;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientSendHttpRequest {
	
	private static String requestEncoding = "UTF-8";//请求编码
	
	 /** 
	  * post方式提交表单（模拟用户登录请求） 
	  */  
	public static String sendPostForm(String url,Map<String,String> param){
		//创建默认的HttpClient实例
		CloseableHttpClient httpClient = HttpClients.createDefault();
		//HttpPost实例
		HttpPost httpPost = new HttpPost(url);
		//設置httpGet的头部參數信息     
		//httpPost.setHeader(arg0);
		httpPost.setHeader("apikey","1fe02d93e72a22bc6474235054d42125");
		//创建参数队列
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		if(param!=null && param.size()>0){
			Iterator<String> it = param.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				list.add(new BasicNameValuePair(key,param.get(key)));
			}
		}
		UrlEncodedFormEntity uefEntity;
		String entityStr = "";
		try{
			uefEntity  = new UrlEncodedFormEntity(list,requestEncoding);
			httpPost.setEntity(uefEntity);
			System.out.println("executing request "+httpPost.getURI());
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try{
				System.out.println(response.getStatusLine().getStatusCode());
				if(response.getStatusLine().getStatusCode()==200){
				    HttpEntity entity = response.getEntity();
					if(entity!=null){
						entityStr = EntityUtils.toString(entity,requestEncoding);
						System.out.println("----------------------------------");
						System.out.println("Response content: "+entityStr);
						System.out.println("----------------------------------");
						return entityStr;
					}
				}
				
			}finally{
				response.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				if(httpClient!=null){
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
	public static String sendGet(String url,String param){
		CloseableHttpClient httpClient =HttpClients.createDefault();
		if(StringUtils.isNotBlank(param)){
			url = url+"?"+param;
		}
		try{
			// 创建httpget
			HttpGet httpGet = new HttpGet(url);
			System.out.println("executing request " + httpGet.getURI());  
			//httpGet.setHeader("apikey","1fe02d93e72a22bc6474235054d42125");
			//httpGet.setHeader("apix-key","a4958d51d93f47d74bf712d8f4d83491");
			//执行get请求.    
			CloseableHttpResponse response = httpClient.execute(httpGet);
			try{
				HttpEntity entity = response.getEntity();
				System.out.println("-------------------------------");
				System.out.println(response.getStatusLine().getStatusCode());
				if(response.getStatusLine().getStatusCode()==200){
				    // 打印响应内容长度    
				    System.out.println("Response content length: " + entity.getContentLength());  
				    // 打印响应内容    
				    System.out.println("Response content: " + EntityUtils.toString(entity));  
				}
				 System.out.println("------------------------------------");  
			}finally{
				  response.close();  
			}
		}catch(Exception e){
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
	 *上传文件 
	 */
	public static void SendHttpUploadFile(String url,Map<File,String> files,Map<String,String> textValue){
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try{
			HttpPost httpPost = new HttpPost(url);
			MultipartEntityBuilder muitipart = MultipartEntityBuilder.create();
			
			//HttpEntity entity  = MultipartEntityBuilder.create().addPart("bin",bin).addPart("comment",comment).build();
			/**
			 * 文件上传
			 */
			for(Entry<File,String> fileSet :files.entrySet()){
			    String setKey = fileSet.getValue();
			    File setValue = fileSet.getKey();
			    FileBody  bin = new FileBody(setValue);
			    muitipart.addPart(setKey, bin);
			    StringBody comment = new StringBody("A binary file of some kind", ContentType.DEFAULT_BINARY);
			    muitipart.addPart("comment", comment);
			}
			
			/**
			 * 属性上传
			 */
			for(Entry<String, String> textSet :textValue.entrySet()){
			    muitipart.addTextBody(textSet.getKey(),textSet.getValue());
			}
			
			
			HttpEntity entity  = muitipart.build();
			httpPost.setEntity(entity);
			System.out.println("executing request "+httpPost.getRequestLine());
			CloseableHttpResponse response = httpClient.execute(httpPost);  
		    try{
		    	System.out.println("------------------------------");
		    	System.out.println(response.getStatusLine());
		    	HttpEntity resEntity  = response.getEntity();
		    	if(response.getStatusLine().getStatusCode()==200){
		    		System.out.println("Response content length: " +resEntity.getContentLength());
		    	}
		    	 EntityUtils.consume(resEntity);
		    }finally{
		    	response.close();
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String str = sendGet("http://api.avatardata.cn/MD5/Decode","key=17433d3c279041bb99f11f8cbc30dc11&md5=ad44680182ece4de467d6a7b91ea5463");
		//String str = sendGet("http://apis.haoservice.com/creditop/IcQuery/QueryEntBaseInfo","entName=440310809147096&key=5ab4dbe042b44a51b33c02ccfecb0182");
		System.out.println(str);
		//440310809147096,430223197607296926
		Map<String,String> param = new HashMap<String,String>();
	    Map<File,String> fileMap = new HashMap<File,String>();
	    fileMap.put(new File("d:\\111.jpg"),"pic");
	    fileMap.put(new File("d:\\222.jpg"),"pic");
	    fileMap.put(new File("d:\\123123.png"),"pic");
	    
	    Map<String,String> textMap = new HashMap<String,String>();
	    textMap.put("showId","10001");
	    HttpClientSendHttpRequest.SendHttpUploadFile("http://192.168.0.136/api/showorder/shareImage.action",fileMap,textMap);
	    Map<String,String> map = new HashMap<String,String>();
	    map.put("sign",new String("".getBytes("utf-8"),"gbk"));
	    map.put("value",new String("一易购".getBytes("gbk"),"gbk"));
	    map.put("goods",new String("一易购".getBytes("gbk"),"utf-8"));
	    HttpClientSendHttpRequest.sendPostForm("http://localhost/tfbPay/appNotify.action", map);
	}
}
