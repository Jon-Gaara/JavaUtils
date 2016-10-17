package com.yumaolin.util.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Ignore;
import org.junit.Test;

import com.yumaolin.util.EncryptionAlgorithm.MD5Encryption;
import com.yumaolin.util.FileCompression.FileCompressionFor7z;
import com.yumaolin.util.Mail.JavaSendMailForSSL;
/*
 * JUnit 4 开始使用 Java 5 中的注解（annotation），常用的几个 annotation 介绍：
 * @BeforeClass：针对所有测试，只执行一次，且必须为static void
 * @Before：初始化方法
 * @Test：测试方法，在这里可以测试期望异常和超时时间
 * @After：释放资源
 * @AfterClass：针对所有测试，只执行一次，且必须为static void
 * @Ignore：忽略的测试方法一个
 * 
 * 单元测试用例执行顺序为：
 *    @BeforeClass –> @Before –> @Test –> @After –> @AfterClass
 * 每一个测试方法的调用顺序为：
 *    @Before –> @Test –> @After
 */
public class UtilTest {
    @Ignore
    public void SendMailForSSLTest(){
	 File file = new File("d:\\111.jpg");
	 File file2 = new File("d:\\222.jpg");
	 String body = "测试邮件<br/><img src='cid:"+file.getName()+"'><br/>";
	 System.out.println(body);
	 JavaSendMailForSSL.sendMail("测试邮件!",body,new File[]{file2},new File[]{file});
	 System.out.println("发送邮件成功!");
    }
    
    @Ignore
    public void MD5EncryptionTest(){
    	System.out.println(MD5Encryption.getMD5("yml","MD5","gbk"));
    }
    
    @Ignore
    public void FileCompressionFor7zTest(){
	try{
	    FileCompressionFor7z.lzmaZip("d:\\111.jpg","d:\\111.7z");
	    FileCompressionFor7z.lzmaZip2("d:\\111.7z","d:\\111.jpg");
	    FileCompressionFor7z.unzipFor7z("D:\\111.7z","d:\\111.jpg");
	}catch(Exception e){
		e.printStackTrace();
	}
    }
    
    /*
     * strictfp 要求以严格的浮点计算产生理想的结果 可能会产生溢出
     * StrictMath和Math区别
     * StrictMath使用"只有发布的Math库"(fdlibm)实现算法,以确保所有平台上得到相同的结果,性能比Math差
     */
    @Test
    public void  ProjectTest() throws NoSuchMethodException, SecurityException, UnsupportedEncodingException{
	GregorianCalendar ca = new  GregorianCalendar();
	int month = ca.get(Calendar.DAY_OF_YEAR);
	int year = ca.get(Calendar.YEAR);
	int werk = ca.get(Calendar.DAY_OF_WEEK)-1;
	System.out.println(year+" "+month+" "+werk);
    }
  
}
