package com.yumaolin.util.FileResolve;

import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;


public class SAXUtils {
    public static void main(String[] args) throws Exception {
	SAXUtils utils = new SAXUtils();
	long start = System.currentTimeMillis();
	utils.StAXResolve(null);
	System.out.println(System.currentTimeMillis()-start);
	start = System.currentTimeMillis();
	utils.SAXResolve(null);
	System.out.println(System.currentTimeMillis()-start);
    }

    public void SAXResolve(String str) throws Exception{
	String url;
	if(StringUtils.isEmpty(str)){
	     url = "http://www.w3c.org";
	}else{
	    url = str;
	}
	
	DefaultHandler handler = new DefaultHandler(){
	    public void startElement(String namespaceURI,String lname,String qname,Attributes attr){
		if(lname.equalsIgnoreCase("a") && attr!=null){
		    for(int i=0;i<attr.getLength();i++){
			String aname  = attr.getLocalName(i);
			if(aname.equals("href")){
			    System.out.println(attr.getValue(i));
			}
		    }
		}
	    }
	};
	SAXParserFactory factory = SAXParserFactory.newInstance();
	factory.setNamespaceAware(true);
	factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
	SAXParser parser = factory.newSAXParser();
	InputStream in = new URL(url).openStream();
	parser.parse(in, handler);
    }
    
    public void StAXResolve(String str) throws Exception{
	String urlString;
	if(StringUtils.isEmpty(str)){
	    urlString = "http://www.w3c.org";
	}else{
	    urlString = str;
	}
	URL url = new URL(urlString);
	InputStream in = url.openStream();
	XMLInputFactory factory = XMLInputFactory.newInstance();
	XMLStreamReader reader = factory.createXMLStreamReader(in);
	while(reader.hasNext()){
	    int event = reader.next();
	    if(event==XMLStreamConstants.START_ELEMENT){
		if(reader.getLocalName().equals("a")){
		    String href = reader.getAttributeValue(null,"href");
		    if(StringUtils.isNotEmpty(href)){
			System.out.println(href);
		    }
		}
	    }
	}
    }
}
