package com.jon.encryption;

import java.security.MessageDigest;
import org.apache.commons.lang3.StringUtils;

public class MD5Encryption {
    private static final String format = "utf-8";

    /**
     * 二进制转十六进制
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer md5str = new StringBuffer();
        // 把数组每一字节换成16进制连成md5字符串
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];

            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str.toString().toLowerCase();
    }

    /**
     * 生成md5或者SHA
     * 
     * @param message
     * @return
     */
    public static String getMD5(String message, String messageType, String codeFormat) {
        try {
            // 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
            MessageDigest md = MessageDigest.getInstance(messageType);
            // 2 将消息变成byte数组
            byte[] input;
            if (StringUtils.isNotBlank(codeFormat)) {
                input = message.getBytes(codeFormat);
            } else {
                input = message.getBytes(format);
            }
            md.update(input);

            // 3 计算后获得字节数组,这就是那128位了
            byte[] buff = md.digest();

            // 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
            return bytesToHex(buff);
        } catch (Exception e) {
            // 返回空串会让失败与合法摘要无法区分
            throw new IllegalStateException("摘要计算失败: " + messageType, e);
        }
    }
}
