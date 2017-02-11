package com.yumaolin.util.EncryptionAlgorithm;

import org.apache.commons.codec.binary.Base64;

public class Base64Encryption {
  /**
   * Base64解密
   * @throws Exception 
   */
   public static String  decode(String base64Data,String charType) throws Exception{
	   byte[] decode = Base64.decodeBase64(base64Data);
	   return new String(decode,charType);
   }
   public static  String  decode(byte[] base64Data,String charType) throws Exception{
	   byte[] decode = Base64.decodeBase64(base64Data);
	   return new String(decode,charType);
   }
   
   public static byte[]  decode(String base64Data) throws Exception{
	   byte[] decode = Base64.decodeBase64(base64Data);
	   return decode;
   }
   
   /**
    * Base64加密
    */
   public static String encode(String binaryData,String charType) throws Exception{
	   byte[] encode=null;
	   if(charType==null){
		   encode = binaryData.getBytes();
	   }else{
		   encode = binaryData.getBytes(charType);
	   }
	   return new String(Base64.encodeBase64Chunked(encode));
   }
   
   public static String encode(byte[] binaryData) throws Exception{
	   return new String(Base64.encodeBase64Chunked(binaryData));
   }
}