package com.yumaolin.util.Json;

import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;
/**
 * http://www.cnblogs.com/az19870227/archive/2011/09/19/2180993.html
 * @author yumaolin
 */

public class JsonUtil {

    /**
     * Java bean转Json,格式化Date对象
     */
    public static String writeJsonObjectMessage(Object obj,String dateFormat){
   	JsonConfig config = new JsonConfig();   
   	config.setIgnoreDefaultExcludes(false);      
   	config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT); 
   	//config.registerJsonBeanProcessor(Date.class, new JsDateJsonBeanProcessor()); // 当输出时间格式时，采用和JS兼容的格式输出  
   	config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor(dateFormat));//自定义时间格式
   	JSONObject jsonObject = JSONObject.fromObject(obj,config);
   	return jsonObject.toString();
    }
    
    /**
     * List 转Json,转Json,格式化Date对象
     */
    @SuppressWarnings("rawtypes")
    public static String writeJsonArrayMessage(Object obj,String dateFormat){
	JsonConfig config = new JsonConfig();   
	config.setIgnoreDefaultExcludes(false);//默认为false，即过滤默认的key       
	config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT); 
	//config.registerJsonBeanProcessor(Date.class, new JsDateJsonBeanProcessor()); // 当输出时间格式时，采用和JS兼容的格式输出  
	config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor(dateFormat));
	//处理Integer为null时输出为0的问题 
	config.registerDefaultValueProcessor(Integer.class, new DefaultValueProcessor(){   
	                   public Object getDefaultValue(Class type){
	                       return null;   
	                   }
	        }
	);
	JSONArray jsonObject = JSONArray.fromObject(obj,config);
	return jsonObject.toString();
    }
}
