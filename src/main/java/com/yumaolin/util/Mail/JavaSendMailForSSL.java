package com.yumaolin.util.Mail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yumaolin.util.FileResolve.PropertiesUtils;

public class JavaSendMailForSSL {
    /**
     * javax.net.ssl.SSLHandshakeException: Received fatal alert: handshake_failure
     * 安全性机制导致的访问https会报错，官网上有替代的jar包，换掉就好了
     * http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html
     * 
     * javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException: PKIX path building failed: 
     * sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
     * 使用DOS命令进入InstallCert.java 存放目录运行 javac InstallCert.java 将InstallCert.java 编译成 .class文件
     * 在运行 java InstallCert smtp.xyz.com:465【其中xyz是要使用的mail,例如：smtp.126.com】【465是端口号】
     * 得到jssecacerts文件后复制到jdk1.6.0_14\jre\lib\security目录  然后再发送邮件就OK了(没有遇到这个情况)
     */
    private static final  Logger logger = LoggerFactory.getLogger(JavaSendMailForSSL.class);
    private static String userName = PropertiesUtils.getPropertiesValue("FromMailAddress");
    private static String password = PropertiesUtils.getPropertiesValue("password"); 
    private static String port = PropertiesUtils.getPropertiesValue("port"); 
    private static String smtpHostName = PropertiesUtils.getPropertiesValue("smtpHostName"); 
    private static String toMailAddress = PropertiesUtils.getPropertiesValue("toMailAddress");
    private static final String ssl_factory="javax.net.ssl.SSLSocketFactory";
    
    
    public JavaSendMailForSSL() {}
    
    @SuppressWarnings("restriction")
	public static void sendMail(String subject,String body,File[] attachment,File[] fileImage){
	Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
	Properties prop = System.getProperties();
	prop.setProperty("mail.smtp.host", smtpHostName);
	prop.setProperty("mail.smtp.socketFactory.class", ssl_factory);
	prop.setProperty("mail.smtp.socketFactory.fallback","false");
	prop.setProperty("mail.smtp.port", port);
	prop.setProperty("mail.smtp.socketFactory.port", port);
	prop.setProperty("mail.smtp.auth","true");
	Session session = Session.getInstance(prop);
	//session.setDebug(true);//开启debug模式
	Message message = new MimeMessage(session);
	try {
	    message.setFrom(new InternetAddress(userName));
	    //指定收件人，多人时用逗号分隔
	    InternetAddress[] tos = InternetAddress.parse(toMailAddress,false);
    	    message.setRecipients(Message.RecipientType.TO,tos);//收件人
    	    //message.setRecipients(Message.RecipientType.CC,tos);//抄送人
    	    //message.setRecipients(Message.RecipientType.BCC,tos);//密送人
    	    message.setSubject(subject);
    	    Multipart multipart = new MimeMultipart(); //Multipart对象
    	    BodyPart bp = new MimeBodyPart();
    	    bp.setContent(body, "text/html;charset=UTF-8");
    	    multipart.addBodyPart(bp);
    	    
    	      //添加图片的内容
    	    if(fileImage!=null && fileImage.length>0){
	    		for(File imageFile:fileImage){
	    		    MimeBodyPart bodyPart = new MimeBodyPart();
	    		    DataSource source = new FileDataSource(imageFile);
	    		    bodyPart.setDataHandler(new DataHandler(source));
	    		    //System.out.println(imageFile.getName());
	    		    bodyPart.setContentID(MimeUtility.encodeWord(imageFile.getName()));
	    		    multipart.addBodyPart(bodyPart);
	    		}
    	    }
    	    
    	    //添加附件的内容
            if (attachment != null && attachment.length>0) {
        	for(File textFile:attachment){
        	    BodyPart attachmentBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(textFile);
                    attachmentBodyPart.setDataHandler(new DataHandler(source));
                    // 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定
                    // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
                    //sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                    //messageBodyPart.setFileName("=?GBK?B?" + enc.encode(attachment.getName().getBytes()) + "?=");
                    //MimeUtility.encodeWord可以避免文件名乱码
                    
    		    attachmentBodyPart.setFileName(MimeUtility.encodeWord(textFile.getName()));
                    multipart.addBodyPart(attachmentBodyPart);
        	}
            }
    	    message.setContent(multipart);
    	    message.setSentDate(new Date());
    	    message.saveChanges();//保存邮件
    	    
    	    Transport transport = session.getTransport("smtp");//smtp验证，就是你用来发邮件的邮箱用户名密码
    	    //message.writeTo(new FileOutputStream("D:\\1.eml"));//保存本地测试 
            transport.connect(smtpHostName, userName, password);//发送
            transport.sendMessage(message, message.getAllRecipients());
	} catch (AddressException e) {
	    logger.error(e.getMessage());
	} catch (MessagingException e) {
	    logger.error(e.getMessage());
	}catch(UnsupportedEncodingException e){
	    logger.error(e.getMessage());
	}catch(Exception e){
	    logger.error(e.getMessage());
	}
    }
    
    public static void main(String[] args) {
	//File file = new File("d:\\111.jpg");
	//File file2 = new File("d:\\222.jpg");
	String body = "测试邮件<br/>asdasdas<br/>";
	System.out.println(body);
	JavaSendMailForSSL.sendMail("测试邮件!",body,null,null);
	System.out.println("发送邮件成功!");
    }
}
