package com.jon.encryption;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 原 DES/ECB 实现（密钥硬编码、无 IV）存在严重安全缺陷，已迁移为 AES/GCM：
 * - 密钥种子从环境变量 DES_KEY_STRING 读取，不再写入源码
 * - 每次加密生成随机 IV 并拼在密文头部，解密时自动取出
 */
public class DESUtil {

    private static final int IV_LENGTH = 12;
    private static final int GCM_TAG_BITS = 128;
    static SecretKey key;

    /**
     * 根据参数生成KEY
     */
    public static void getKey(String strKey) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            key = new SecretKeySpec(sha.digest(strKey.getBytes(StandardCharsets.UTF_8)), "AES");
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("生成密钥失败", e);
        }
    }

    /**
     * 加密String明文输入,String密文输出
     */
    public static String getEncString(String strMing) {
        String keyString = System.getProperty("des.key", System.getenv("DES_KEY_STRING"));
        if (keyString == null || keyString.isEmpty()) {
            throw new IllegalStateException("请先设置环境变量 DES_KEY_STRING");
        }
        DESUtil.getKey(keyString);
        try {
            byte[] byteMing = strMing.getBytes(StandardCharsets.UTF_8);
            byte[] byteMi = getEncCode(byteMing);
            return Base64Encryption.encode(byteMi);
        } catch (Exception e) {
            throw new IllegalStateException("加密失败", e);
        }
    }

    /**
     * 解密 以String密文输入,String明文输出
     */
    public static String getDesString(String strMi) {
        String keyString = System.getProperty("des.key", System.getenv("DES_KEY_STRING"));
        if (keyString == null || keyString.isEmpty()) {
            throw new IllegalStateException("请先设置环境变量 DES_KEY_STRING");
        }
        DESUtil.getKey(keyString);
        try {
            byte[] byteMi = Base64Encryption.decode(strMi);
            byte[] byteMing = getDesCode(byteMi);
            return new String(byteMing, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("解密失败", e);
        }
    }

    /**
     * 加密以byte[]明文输入,byte[]密文输出（IV 拼接在密文头部）
     */
    private static byte[] getEncCode(byte[] byteS) {
        try {
            byte[] iv = new byte[IV_LENGTH];
            new SecureRandom().nextBytes(iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_BITS, iv));
            byte[] cipherText = cipher.doFinal(byteS);
            byte[] result = Arrays.copyOf(iv, IV_LENGTH + cipherText.length);
            System.arraycopy(cipherText, 0, result, IV_LENGTH, cipherText.length);
            return result;
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("加密失败", e);
        }
    }

    /**
     * 解密以byte[]密文输入,以byte[]明文输出
     */
    private static byte[] getDesCode(byte[] byteD) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_BITS, byteD, 0, IV_LENGTH));
            return cipher.doFinal(byteD, IV_LENGTH, byteD.length - IV_LENGTH);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("解密失败", e);
        }
    }

    public static void main(String[] args) {
        System.out.println(DESUtil.getEncString("123456"));
    }
}
