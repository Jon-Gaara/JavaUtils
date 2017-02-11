package com.yumaolin.util.SendHttpRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import com.yumaolin.util.EncryptionAlgorithm.Base64Encryption;

import net.sf.json.JSONObject;
/**
 * Java自带Api发送HTTP请求
 */
public class JavaApiSendHttpRequest {
	
  private static Integer  readTimeout=5000; //连接超时
  private static Integer  connectionTimeout = 10000;//操作超时
  private static String requestEncoding = "UTF-8";//请求编码
  
  /**
   * 发送GET请求
   */
  public  static String sendGet(String url,String param,String apiKey){
	  StringBuilder result =new StringBuilder();
	  BufferedReader reader = null;
	  try {
		String urlParam = url+"?"+param;
		URL realUrl  = new URL(urlParam);
		URLConnection urlConnection = realUrl.openConnection();//打开和URL直接的连接
		urlConnection.setRequestProperty("accept", "*/*");//设置通用属性
		urlConnection.setRequestProperty("connection", "Keep-Alive");
		urlConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 1.5; SV1)");
		urlConnection.setRequestProperty("apikey", apiKey);
		urlConnection.setReadTimeout(readTimeout);
		urlConnection.setConnectTimeout(connectionTimeout);
		urlConnection.connect();//建立实际的连接
		Map<String,List<String>> headerFields= urlConnection.getHeaderFields();//获取所有响应头字段
		for(String key:headerFields.keySet()){//遍历所有响应头字段
			System.out.println(key+"------>"+headerFields.get(key));
		}
		reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),requestEncoding));//读取URL相应
		String nextLine;
		while((nextLine = reader.readLine())!=null){
			result.append(nextLine);
		}
	  } catch (Exception e) {
		e.printStackTrace();
	}finally{
		try {
			if(reader!=null){
				reader.close();
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	  return result.toString();
  }
  /**
   * 发送POST请求
   */
  public static String sendPost(String url,String param,String apiKey){
	  StringBuilder result  = new StringBuilder();
	  BufferedReader reader = null; 
	  OutputStreamWriter output =null;
	  try {
		URL readerUrl  = new URL(url);
		HttpURLConnection connection  = (HttpURLConnection) readerUrl.openConnection();
		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("connection", "Keep-Alive");
		connection.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 6.1; rv:2.0.1) Gecko/20100101 Firefox/4.0.1");
		connection.setRequestProperty("apikey", apiKey);
		connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		connection.setInstanceFollowRedirects(true);
		connection.setDoInput(true);//就可以使用conn.getInputStream().read()
		connection.setDoOutput(true);//就可以使用conn.getOutputStream().write()
		connection.setUseCaches(false);
		connection.setRequestMethod("POST");
		connection.setReadTimeout(readTimeout);//读操作超时
		connection.setConnectTimeout(connectionTimeout);//连接操作超时
		connection.connect();
		output = new OutputStreamWriter(connection.getOutputStream());// 获取URLConnection对象对应的输出流
		output.write(param);// 发送请求参数
		output.flush();// flush输出流的缓冲
		Map<String,List<String>> headerFields= connection.getHeaderFields();//获取所有响应头字段
		for(String key:headerFields.keySet()){//遍历所有响应头字段
			System.out.println(key+"------>"+headerFields.get(key));
		}
		reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),requestEncoding));
		String nextLine;
		while((nextLine = reader.readLine())!=null){
			result.append(nextLine);
		}
	 } catch (Exception e) {
			e.printStackTrace();
	 }finally{
		 try {
			 if(reader!=null){
				 reader.close(); 
			 }
			 if(output!=null){
				 output.close();
			 }
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }
	  return result.toString();
  }
  
  /**
   * 上传文件
   */
  public static String SendHttpUploadFile(String httpUrl,String file,String apiKey){
	  BufferedReader buffer = null;
	  String result=null;
	  StringBuffer sbf = new StringBuffer();
	  try{
		  URL url = new URL(httpUrl);
		  HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		  	connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setRequestProperty("apikey", apiKey);
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			connection.setInstanceFollowRedirects(true);
			connection.setDoInput(true);//就可以使用conn.getInputStream().read()
			connection.setDoOutput(true);//就可以使用conn.getOutputStream().write()
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setReadTimeout(readTimeout);//读操作超时
			connection.setConnectTimeout(connectionTimeout);//连接操作超时
			connection.getOutputStream().write(Base64Encryption.decode(file,"ISO-8859-1").getBytes("ISO-8859-1"));
			connection.connect();
			InputStream is = connection.getInputStream();
			buffer = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			String strRead = null;
			while ((strRead = buffer.readLine()) != null) {
			    sbf.append(strRead);
			    sbf.append("\r\n");
			}
			buffer.close();
			result =ascii2native(sbf.toString());
	  }catch(Exception e){
		  e.printStackTrace();
	  }
	  return result;
  } 
  
  public static void main(String[] args) throws Exception{
	  /*String url = sendGet("http://apis.baidu.com/apistore/weatherservice/cityname","cityname=深圳","1fe02d93e72a22bc6474235054d42125");
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
  
  private static String ascii2native (String asciicode)
  {
      String[] asciis = asciicode.split ("\\\\u");
      String nativeValue = asciis[0];
      try
      {
          for ( int i = 1; i < asciis.length; i++ )
          {
              String code = asciis[i];
              nativeValue += (char) Integer.parseInt (code.substring (0, 4), 16);
              if (code.length () > 4)
              {
                  nativeValue += code.substring (4, code.length ());
              }
          }
      }
      catch (NumberFormatException e)
      {
          return asciicode;
      }
      return nativeValue;
  }
  
  public static String parseJson(String url){
	  JSONObject jsonObject =JSONObject.fromObject(url);
	  StringBuilder nowWeather = new StringBuilder();
		 if(jsonObject!=null &&  jsonObject.size()!=0){
			 Integer errNum = (Integer) jsonObject.get("errNum");
			 String errMsg = jsonObject.getString("errMsg");
			 System.out.println(errMsg);
			if(errNum==0){
				  	 JSONObject retData = jsonObject.getJSONObject("retData");
					 String	city = (String) retData.get("city");//城市
					 //String pinyin = (String) retData.get("pinyin");//城市拼音
					 //String citycode = (String) retData.get("citycode");//城市编码
					 String	date = (String) retData.get("date");//日期
					 String	time = (String) retData.get("time");//发布时间
					 //String postCode = (String) retData.get("postCode");//邮编
					 //String longitude =String.valueOf((Double) retData.get("longitude"));//经度
					 //String latitude = String.valueOf((Double) retData.get("latitude"));//维度
					 //String altitude = (String) retData.get("altitude");//海拔
					 String	weather = (String) retData.get("weather");//天气情况
					 String	temp = (String) retData.get("temp");//气温
					 String	l_tmp = (String) retData.get("l_tmp");//最低气温
					 String	h_tmp =(String) retData.get("h_tmp");//最高气温
					 String	WD = (String) retData.get("WD");//风向
					 String	WS = (String) retData.get("WS");//风力
					 String	sunrise = (String) retData.get("sunrise");//日出时间
					 String	sunset = (String) retData.get("sunset");//日落时间
					 nowWeather.append("城市:"+city+" 发布日期:"+date+" "+time+" 天气情况 :"+weather+" 气温 ：")
					 .append(temp+" 最低气温:"+l_tmp+" 最高气温:"+h_tmp+" 风向:"+WD+" 风力："+WS+" 日出时间:"+sunrise+" 日落时间:"+sunset);
					 System.out.println(nowWeather);
			}
		 }
		 return nowWeather.toString();
  }
  
}
