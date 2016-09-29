package com.yumaolin.util.FileReader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtils {
    private static final Logger loggger = LoggerFactory.getLogger(PropertiesUtils.class);
    
    public PropertiesUtils() {}
    
    public static String getPropertiesValue(String propertiesKey){
	/*String path = (PropertiesUtils.class.getResource("/").getPath()+"mail.properties").replace("%20"," ");
	InputStream in =new FileInputStream(new File(path));
	Properties prop = new Properties();
	prop.load(in);
	return  prop.getProperty(propertiesKey);*/
	try {
	    Locale locale1 = new Locale("zh","CN"); 
	    ResourceBundle rs = ResourceBundle.getBundle("mail",locale1);
	    return new String(rs.getString(propertiesKey).getBytes("ISO-8859-1"),"utf-8");
	} catch (Exception e) {
	    loggger.error(e.getMessage());
	}
	  return null;
    }
    
    public static void main(String[] args){
	 String str = PropertiesUtils.getPropertiesValue("port");
	 System.out.println(str);
    }
}
