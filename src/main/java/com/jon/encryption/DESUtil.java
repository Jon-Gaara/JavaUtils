package com.jon.encryption;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

public class DESUtil {

    public static final String KEY_STRING = "0a137b375cc3881a70e186ce2172c8d1";// 生成密钥的字符串
    static Key key;

    /**
     * 根据参数生成KEY
     */
    public static void getKey(String strKey) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("DES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(strKey.getBytes());
            // _generator.init(new SecureRandom(strKey.getBytes()));Linux下会报错
            generator.init(secureRandom);
            key = generator.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密String明文输入,String密文输出
     */
    public static String getEncString(String strMing) {
        DESUtil.getKey(KEY_STRING);// 生成密匙
        byte[] byteMi;
        byte[] byteMing;
        String strMi = "";
        try {
            byteMing = strMing.getBytes(StandardCharsets.UTF_8);
            byteMi = getEncCode(byteMing);
            strMi = Base64Encryption.encode(byteMi);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strMi;
    }

    /**
     * 解密 以String密文输入,String明文输出
     */
    public static String getDesString(String strMi) {
        DESUtil.getKey(KEY_STRING);// 生成密匙
        byte[] byteMing;
        byte[] byteMi;
        String strMing = "";
        try {
            byteMi = Base64Encryption.decode(strMi);
            byteMing = getDesCode(byteMi);
            strMing = new String(byteMing, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strMing;
    }

    /**
     * 加密以byte[]明文输入,byte[]密文输出
     */
    private static byte[] getEncCode(byte[] byteS) {
        byte[] byteFina = null;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byteFina = cipher.doFinal(byteS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteFina;
    }

    /**
     * 解密以byte[]密文输入,以byte[]明文输出
     */
    private static byte[] getDesCode(byte[] byteD) {
        Cipher cipher;
        byte[] byteFina = null;
        try {
            cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");// DES/ECB/NoPadding;DES/ECB/ISO10126Padding
            cipher.init(Cipher.DECRYPT_MODE, key);
            byteFina = cipher.doFinal(byteD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteFina;
    }

    public static void main(String[] args) {
        System.out.println(DESUtil.getEncString("123456"));
    }
}
