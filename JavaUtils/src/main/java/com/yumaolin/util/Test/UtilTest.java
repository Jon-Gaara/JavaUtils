package com.yumaolin.util.Test;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

import org.junit.Ignore;
import org.junit.Test;

import com.yumaolin.util.EncryptionAlgorithm.MD5Encryption;
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
public class UtilTest{

    
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
	    //FileCompressionFor7z.lzmaZip("d:\\111.jpg","d:\\111.7z");
	    //FileCompressionFor7z.lzmaZip2("d:\\111.7z","d:\\111.jpg");
	    //FileCompressionFor7z.unzipFor7z("D:\\111.7z","d:\\111.jpg");
	}catch(Exception e){
		e.printStackTrace();
	}
    }
    
    /*
     * strictfp 要求以严格的浮点计算产生理想的结果 可能会产生溢出
     * StrictMath和Math区别
     * StrictMath使用"只有发布的Math库"(fdlibm)实现算法,以确保所有平台上得到相同的结果,性能比Math差
     */
    @Ignore
    public void  ProjectTest() throws Exception{
	/* EnumTest[] test= EnumTest.values();
	 for(EnumTest enumTest:test){
	    System.out.println("当前灯 name: "+enumTest.name());
	    System.out.println("当前灯 ordinal : "+enumTest.ordinal());
	    System.out.println("当前灯: " + enumTest);
	 }*/
	ArrayList<Integer> list = new ArrayList<Integer>();
	for(int i=1;i<5;i++){list.add(i*i);}
	System.out.println(new ObjectAnlyzer().toString(list));
	int[] array = {5,8,6,9,0};
	Arrays.sort(array);
	System.out.println(Arrays.toString((int[])new ObjectAnlyzer().copyOf(array,10)));
    }
    
    @Ignore
    public void ProxyTest(){
	Object[] elements = new Object[1000];
	for(int i=0;i<elements.length;i++){
	    Integer value = i+1;
	    InvocationHandler handler = new TraceHandle(value);
	    Object proxy = Proxy.newProxyInstance(null,new Class[]{Comparable.class},handler);
	    elements[i]=proxy;
	}
	Integer key = new Random().nextInt(elements.length)-1;
	int result = Arrays.binarySearch(elements, key);
	if(result>=0)System.out.println(elements[result]);
    }
    
    public static String getDateForStr(Date date,String dateFormat){
  	return new SimpleDateFormat(dateFormat).format(new Date());
      }
      
      public static Date getStrForDate(String dateStr,String dateFormat) throws ParseException{
  	return new SimpleDateFormat(dateFormat).parse(dateStr);
      }
      
    @Test
    public void ThrowTest() throws Exception{
	/*Path path =Paths.get("d:/","111.jpg");
	if(Files.exists(path)){
	    byte[] bytes = Files.readAllBytes(path);
	    BasicFileAttributes bfa = Files.readAttributes(path,BasicFileAttributes.class);
	    System.out.println(Files.exists(path));
	    System.out.println(Arrays.toString(bytes));
	}
	IOReaderUtils.FileIterator(Paths.get("d:/home"));*/
	/*ScriptEngineManager sem= new ScriptEngineManager();
	ScriptEngine se = sem.getEngineByName("javascript");
	se.eval("print(12312312)");*/
	
	//IOReaderUtils.ZipFileIterator(Paths.get("d:/222.zip"));
	/*List<String> list  = new ArrayList<String>();
	list.add("A");
	list.add("B");
	list.add("C");
	list.add("D");
	list.add("E");
	list.add("F");
	ListIterator<String> listIt = list.listIterator();
	while(listIt.hasNext()){
	    System.out.println(listIt.next());
	}
	System.out.println(Collections.max(list));
	Collections.sort(list, Collections.reverseOrder());
	System.out.println(list);
	Collections.shuffle(list);
	System.out.println(list);
	Thread t = new Thread(()->System.out.println("123123"));
	t.start();*/
	//BufferedReader buf = new BufferedReader(new InputStreamReader(new FileInputStream(""),"utf-8"));
	//System.out.print(System.getProperty("line.separator"));
	//Logger.getLogger("com.yumaolin.util.Test.UtilTest").log(Level.INFO,"123123123",new IOException());
   }
}
