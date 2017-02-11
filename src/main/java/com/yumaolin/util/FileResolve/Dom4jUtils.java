package com.yumaolin.util.FileResolve;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;


public class Dom4jUtils {
     
     private Document document;
    
    public void getXMLDocumentForFile(String filePath) throws DocumentException{
	SAXReader saxReader = new SAXReader();
	document = saxReader.read(new File(filePath));
    }
    
    public void getXMLDocument(String xmlString) throws Exception{
	document = DocumentHelper.parseText(xmlString);
	this.getXMLElement();
    }
    
    public void getXMLDocument(){
	 document = DocumentHelper.createDocument();//创建根节点
    }
    
    private void getXMLElement() throws Exception{
	//获取根节点元素对象
	Element node = document.getRootElement();
	node.addCDATA("ASDASD");//添加CDATA区域
	this.listNodes(node);
	writer(document);
    }
    
    
    /**
     *	获取所有节点及其子节点 
     */
    @SuppressWarnings("unchecked")
    public void listNodes(Element node) {
	System.out.println("当前节点的名称：：" + node.getName());
	// 获取当前节点的所有属性节点
	List<Attribute> list = node.attributes();
	// 遍历属性节点
	for (Attribute attr : list) {
	    System.out.println(attr.getText() + "-----" + attr.getName()+ "---" + attr.getValue());
	}
	if (StringUtils.isNotBlank(node.getTextTrim())) {
	    System.out.println("文本内容：：：：" + node.getText());
	}

	// 当前节点下面子节点迭代器
	Iterator<Element> it = node.elementIterator();
	// 遍历
	while (it.hasNext()) {
	    // 获取某个子节点对象
	    Element e = it.next();
	    // 对子节点进行遍历
	    listNodes(e);
	}
    }
    
    	/**
	 * 把document对象写入新的文件
	 * 
	 * @param document
	 * @throws Exception
	 */
	public void writer(Document document) throws Exception {
	    	// 紧凑的格式
		//OutputFormat format = OutputFormat.createCompactFormat();
		// 排版缩进的格式
		OutputFormat format = OutputFormat.createPrettyPrint();
		// 设置编码
		format.setEncoding("UTF-8");
		// 创建XMLWriter对象,指定了写出文件及编码格式
		//XMLWriter writer = new XMLWriter(new FileWriter(new File("d:/a.xml")),format);
		XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(new File("d:/a.xml")), "UTF-8"), format);
		
		writer.write(document);
		writer.flush();
		// 关闭操作
		writer.close(); 
	}

    
    public static void main(String[] args) throws Exception {
	/*String str="<?xml version='1.0' encoding='UTF-8'?>"
		+ "<person><name id='x001'><firstName>愚</firstName><lastName>蠢</lastName></name><age id='x002'><year>1980</year></age></person>";
	new Dom4jUtils().getXMLDocument(str);*/
    }

}
