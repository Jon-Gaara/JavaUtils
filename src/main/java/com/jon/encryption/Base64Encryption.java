package com.jon.encryption;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;

public class Base64Encryption {
    /**
     * Base64解密
     * 
     * @throws Exception
     */
    public static String decode(String base64Data, String charType) throws Exception {
        byte[] decode = Base64.decodeBase64(base64Data);
        return new String(decode, charType);
    }

    public static String decode(byte[] base64Data, String charType) throws Exception {
        byte[] decode = Base64.decodeBase64(base64Data);
        return new String(decode, charType);
    }

    public static byte[] decode(String base64Data) {
        return Base64.decodeBase64(base64Data);
    }

    /**
     * Base64加密。输出单行纯 ASCII；encodeBase64Chunked 会插 CRLF，破坏 JSON/Header/URL 场景
     */
    public static String encode(String binaryData, String charType) throws Exception {
        byte[] encode;
        if (charType == null) {
            encode = binaryData.getBytes(StandardCharsets.UTF_8);
        } else {
            encode = binaryData.getBytes(charType);
        }
        return Base64.encodeBase64String(encode);
    }

    public static String encode(byte[] binaryData) throws Exception {
        return Base64.encodeBase64String(binaryData);
    }
}