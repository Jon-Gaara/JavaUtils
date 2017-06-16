package com.yumaolin.util.Mail;

import java.io.File;
import java.io.UnsupportedEncodingException;
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

public class JavaSendMailForNoSSL {
    public JavaSendMailForNoSSL() {}
    private static final  Logger logger = LoggerFactory.getLogger(JavaSendMailForSSL.class);
    private static String userName = PropertiesUtils.getPropertiesValue("FromMailAddress");
    private static String password = PropertiesUtils.getPropertiesValue("password"); 
    //private static String port = PropertiesUtils.getPropertiesValue("port"); 
    private static String smtpHostName = PropertiesUtils.getPropertiesValue("smtpHostName"); 
    private static String toMailAddress = PropertiesUtils.getPropertiesValue("toMailAddress");
    
    public static void sendMail(String subject,String body,File[] attachment,File[] fileImage){
   	Properties prop = System.getProperties();
   	prop.setProperty("mail.smtp.host", smtpHostName);
   	//prop.setProperty("mail.smtp.port", port);//不传Port默认25
   	prop.setProperty("mail.smtp.auth","true");
   	Session session = Session.getInstance(prop);
   	//session.setDebug(true);//开启debug模式
   	Message message = new MimeMessage(session);
   	try {
   	    message.setFrom(new InternetAddress(userName));
   	  //指定收件人，多人时用逗号分隔
   	    InternetAddress[] tos = InternetAddress.parse(toMailAddress,false);
       	    message.setRecipients(Message.RecipientType.TO,tos);
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
       		    System.out.println(imageFile.getName());
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
}
