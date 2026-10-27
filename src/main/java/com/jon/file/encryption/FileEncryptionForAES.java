package com.jon.file.encryption;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
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
     * 加密文件
     * 
     * @param key 密钥
     * @param plainFilePath 明文文件路径路径
     * @param secretFilePath 密文文件路径
     */
    public static void encodeAESFile(byte[] key, String plainFilePath, String secretFilePath) throws Exception {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        FileOutputStream fos = null;
        try {

            fis = new FileInputStream(plainFilePath);
            bos = new ByteArrayOutputStream(fis.available());

            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, count);
            }
            bos.flush();

            byte[] bytes = encodeAES(key, bos.toByteArray());

            fos = new FileOutputStream(secretFilePath);
            fos.write(bytes);
            fos.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
        }
    }

    /**
     * 解密文件
     * 
     * @param key 密钥
     * @param plainFilePath 明文文件路径路径
     * @param secretFilePath 密文文件路径
     */
    public static void decodeAESFile(byte[] key, String plainFilePath, String secretFilePath) throws Exception {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(secretFilePath);
            bos = new ByteArrayOutputStream(fis.available());

            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, count);
            }
            bos.flush();

            byte[] bytes = decodeAES(key, bos.toByteArray());

            fos = new FileOutputStream(plainFilePath);
            fos.write(bytes);
            fos.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.getLocalizedMessage();
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