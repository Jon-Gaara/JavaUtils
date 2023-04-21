package com.jon.util;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.jon.encryption.MD5Encryption;
import com.jon.mail.JavaSendMailForSSL;

/*
 * JUnit 4 开始使用 Java 5 中的注解（annotation），常用的几个 annotation 介绍：
 * 
 * @BeforeClass：针对所有测试，只执行一次，且必须为static void
 * 
 * @Before：初始化方法
 * 
 * @Test：测试方法，在这里可以测试期望异常和超时时间
 * 
 * @After：释放资源
 * 
 * @AfterClass：针对所有测试，只执行一次，且必须为static void
 * 
 * @Ignore：忽略的测试方法一个
 * 
 * 单元测试用例执行顺序为：
 * 
 * @BeforeClass –> @Before –> @Test –> @After –> @AfterClass 每一个测试方法的调用顺序为：
 * 
 * @Before –> @Test –> @After
 */
public class UtilTest {

    @Test
    public void SendMailForSSLTest() {
        File file = new File("d:\\111.jpg");
        File file2 = new File("d:\\222.jpg");
        String body = "测试邮件<br/><img src='cid:" + file.getName() + "'><br/>";
        System.out.println(body);
        JavaSendMailForSSL.sendMail("测试邮件!", body, new File[] {file2}, new File[] {file});
    }

    @Test
    public void MD5EncryptionTest() {
        Assert.assertNotNull(MD5Encryption.getMD5("yml", "MD5", "gbk"));
    }


    /*
     * strictfp 要求以严格的浮点计算产生理想的结果 可能会产生溢出
     * StrictMath和Math区别
     * StrictMath使用"只有发布的Math库"(fdlibm)实现算法,以确保所有平台上得到相同的结果,性能比Math差
     */
    @Test
    public void ProjectTest() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            list.add(i * i);
        }
        System.out.println(new ObjectAnalyzer().toString(list));
    }

    @Test
    public void ProxyTest() {
        Object[] elements = new Object[1000];
        for (int i = 0; i < elements.length; i++) {
            Integer value = i + 1;
            InvocationHandler handler = new TraceHandle(value);
            Object proxy = Proxy.newProxyInstance(null, new Class[] {Comparable.class}, handler);
            elements[i] = proxy;
        }
        Integer key = new Random().nextInt(elements.length) - 1;
        int result = Arrays.binarySearch(elements, key);
        Assert.assertNotEquals(result,0);
        if (result >= 0){
			System.out.println(elements[result]);
		}
    }

    public static String getDateForStr(Date date, String dateFormat) {
        return new SimpleDateFormat(dateFormat).format(new Date());
    }

    public static Date getStrForDate(String dateStr, String dateFormat) throws ParseException {
        return new SimpleDateFormat(dateFormat).parse(dateStr);
    }
}
