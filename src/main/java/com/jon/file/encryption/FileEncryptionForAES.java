package com.jon.file.encryption;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES文件加密<br>
 * 每次加密生成随机 IV 并置于密文头部（CBC 固定 IV 会导致确定性加密，泄露明文相等性）
 */
public class FileEncryptionForAES {

    private static final int BLOCK_SIZE = 16;

    private static SecretKey normalizeKey(byte[] key) {
        if (key == null) {
            throw new IllegalArgumentException("key 不能为空");
        }
        if (key.length % BLOCK_SIZE != 0) {
            int groups = key.length / BLOCK_SIZE + 1;
            byte[] temp = new byte[groups * BLOCK_SIZE];
            System.arraycopy(key, 0, temp, 0, key.length);
            key = temp;
        }
        if (key.length != 16 && key.length != 24 && key.length != 32) {
            throw new IllegalArgumentException("AES 密钥长度必须是 16/24/32 字节(补零后为 " + key.length + ")");
        }
        return new SecretKeySpec(key, "AES");
    }

    /**
     * AES加密，输出 = 16 字节随机 IV + 密文
     */
    public static byte[] encodeAES(byte[] key, byte[] content) throws Exception {
        SecretKey secretKey = normalizeKey(key);
        byte[] iv = new byte[BLOCK_SIZE];
        new SecureRandom().nextBytes(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
        byte[] cipherText = cipher.doFinal(content);
        byte[] result = Arrays.copyOf(iv, BLOCK_SIZE + cipherText.length);
        System.arraycopy(cipherText, 0, result, BLOCK_SIZE, cipherText.length);
        return result;
    }

    /**
     * AES解密，密文前 16 字节为 IV
     * 
     * @param key 密钥信息
     * @param content 待加密信息
     */
    public static byte[] decodeAES(byte[] key, byte[] content) throws Exception {
        if (content == null || content.length <= BLOCK_SIZE) {
            throw new IllegalArgumentException("密文长度非法");
        }
        SecretKey secretKey = normalizeKey(key);
        IvParameterSpec iv = new IvParameterSpec(content, 0, BLOCK_SIZE);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        return cipher.doFinal(content, BLOCK_SIZE, content.length - BLOCK_SIZE);
    }

    /**
     * 加密文件（流式，不把整个文件读进内存）：输出 = IV + 密文
     * 
     * @param key 密钥
     * @param plainFilePath 明文文件路径路径
     * @param secretFilePath 密文文件路径
     */
    public static void encodeAESFile(byte[] key, String plainFilePath, String secretFilePath) throws Exception {
        SecretKey secretKey = normalizeKey(key);
        byte[] iv = new byte[BLOCK_SIZE];
        new SecureRandom().nextBytes(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
        try (FileInputStream fis = new FileInputStream(plainFilePath);
            FileOutputStream fos = new FileOutputStream(secretFilePath)) {
            fos.write(iv);
            try (CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {
                byte[] buffer = new byte[8192];
                int count;
                while ((count = fis.read(buffer)) != -1) {
                    cos.write(buffer, 0, count);
                }
            }
        }
    }

    /**
     * 解密文件（流式，不把整个文件读进内存）
     * 
     * @param key 密钥
     * @param plainFilePath 明文文件路径路径
     * @param secretFilePath 密文文件路径
     */
    public static void decodeAESFile(byte[] key, String plainFilePath, String secretFilePath) throws Exception {
        SecretKey secretKey = normalizeKey(key);
        try (FileInputStream fis = new FileInputStream(secretFilePath)) {
            byte[] iv = new byte[BLOCK_SIZE];
            if (fis.read(iv) != BLOCK_SIZE) {
                throw new IllegalArgumentException("密文长度非法");
            }
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
            try (CipherInputStream cis = new CipherInputStream(fis, cipher);
                FileOutputStream fos = new FileOutputStream(plainFilePath)) {
                byte[] buffer = new byte[8192];
                int count;
                while ((count = cis.read(buffer)) != -1) {
                    fos.write(buffer, 0, count);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            byte[] key = "1234567887654321".getBytes();// 密钥
            String plainFilePath = "d:/1111111.txt";// 明文文件路径
            String secretFilePath = "d:/222222.txt";// 密文文件路径
            encodeAESFile(key, plainFilePath, secretFilePath);// 加密
            plainFilePath = "d:/333333.txt";// 解密
            secretFilePath = "d:/222222.txt";
            decodeAESFile(key, plainFilePath, secretFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}