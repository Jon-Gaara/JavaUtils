package Win7x64.JBIG.api;

/**
 * JBIG格式图片转化为pbm
 * Linux 64位把libWIN7_64_JBIG.so放在JavaLibrary下面
 * windows 64位WIN7_64_JBIG.dll放在JavaLibrary下面
 * 该方法没有对二进制字符串做校验 如果给的不是jbig的二进制字符串就会报错导致tomcat崩溃
 * 用 000001校验
 */
public class JBIGapi {
	static{
		try {
		    System.out.println("JavaLibrary :"+System.getProperty("java.library.path")); 
		    System.loadLibrary("WIN7_64_JBIG"); 
		}catch (Throwable t) {
		   t.printStackTrace();
		}
	}
	public static native byte ConvertJBGtoPBM(byte[] jfnin,byte[] jfout);
	public static boolean ConvertJBGtoPBM(String jfnin,String jfout){
		boolean flag=false;
		byte[] bytes=jfnin.getBytes();
		byte[] out=jfout.getBytes();
		byte b=ConvertJBGtoPBM(bytes,out);
		if(b==1){
			flag=true;
		}
		return flag;
	}
}