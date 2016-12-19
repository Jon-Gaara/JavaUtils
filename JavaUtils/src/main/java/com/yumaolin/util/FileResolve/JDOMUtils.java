package com.yumaolin.util.FileResolve;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class JDOMUtils {
    public static Map<String,String> doXMLParse(String strxml)
	    throws JDOMException, IOException
	  {
	    if ((strxml == null) || ("".equals(strxml))) {
	      return null;
	    }
	    Map<String,String> m = new HashMap<String,String>();
	    InputStream in = new ByteArrayInputStream(strxml.getBytes());
	    SAXBuilder builder = new SAXBuilder();
	    Document doc = builder.build(in);
	    Element root = doc.getRootElement();
	    List<?> list = root.getChildren();
	    Iterator<?> it = list.iterator();
	    while (it.hasNext())
	    {
	      Element e = (Element)it.next();
	      String k = e.getName();
	      String v = "";
	      List<?> children = e.getChildren();
	      if (children.isEmpty()) {
	        v = e.getTextNormalize();
	      } else {
	        v = getChildrenText(children);
	      }
	      m.put(k, v);
	    }
	    in.close();
	    
	    return m;
	  }
	  
	  public static String getChildrenText(List<?> children)
	  {
	    StringBuffer sb = new StringBuffer();
	    if (!children.isEmpty())
	    {
	      Iterator<?> it = children.iterator();
	      while (it.hasNext())
	      {
	        Element e = (Element)it.next();
	        String name = e.getName();
	        String value = e.getTextNormalize();
	        List<?> list = e.getChildren();
	        sb.append("<" + name + ">");
	        if (!list.isEmpty()) {
	          sb.append(getChildrenText(list));
	        }
	        sb.append(value);
	        sb.append("</" + name + ">");
	      }
	    }
	    return sb.toString();
	  }

}
