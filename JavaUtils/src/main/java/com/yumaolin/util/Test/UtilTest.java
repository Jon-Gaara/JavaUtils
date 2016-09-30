package com.yumaolin.util.Test;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import com.yumaolin.util.EncryptionAlgorithm.MD5Encryption;
import com.yumaolin.util.FileCompression.FileCompressionFor7z;
import com.yumaolin.util.Mail.JavaSendMailForSSL;

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
    
    @Test
    public void MD5EncryptionTest(){
    	System.out.println(MD5Encryption.getMD5("yml","MD5","gbk"));
    }
    
    @Test
    public void FileCompressionFor7zTest(){
	try{
	    FileCompressionFor7z.lzmaZip("d:\\111.jpg","d:\\111.7z");
	    FileCompressionFor7z.lzmaZip2("d:\\111.7z","d:\\111.jpg");
	    FileCompressionFor7z.unzipFor7z("D:\\111.7z","d:\\111.jpg");
	}catch(Exception e){
		e.printStackTrace();
	}
    }
    
}
