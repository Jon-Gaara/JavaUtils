package com.jon.encryption;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.crypto.engines.XSalsa20Engine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Arrays;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;

/**
 * 基于京东加解密文档实现，可搜索加解密
 * https://jos.jd.com/commondoc?listId=345
 * https://open.jd.com/home/home/#/doc/common?listId=987
 *
 * @author Jon Yu
 */
public class JDSearchEncrypt {
    public static void main(String[] args) {
        //随机生成密钥
        final byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), 256).getEncoded();
        final String plaintext = "4127204557";
        final String wildCardKeyWordIndex = obtainWildCardKeyWordIndex(plaintext, key);
        System.out.println(wildCardKeyWordIndex);
        //true
        System.out.println(wildCardKeyWordIndex.contains(calculateWildCardKeyWord("412", key)));
        //false
        System.out.println(wildCardKeyWordIndex.contains(calculateWildCardKeyWord("413", key)));
        //true
        System.out.println(wildCardKeyWordIndex.contains(calculateWildCardKeyWord("557", key)));
        //true
        System.out.println(wildCardKeyWordIndex.contains(calculateWildCardKeyWord("***720", key)));
        //false
        System.out.println(wildCardKeyWordIndex.contains(calculateWildCardKeyWord("**720", key)));
        //true
        System.out.println(wildCardKeyWordIndex.contains(calculateWildCardKeyWord("*127", key)));
        //true
        System.out.println(wildCardKeyWordIndex.contains(calculateWildCardKeyWord("******4557", key)));
        final String keyWordIndex = obtainKeyWordIndex("21312312尔克孜自治州127號2F", key);
        System.out.println(keyWordIndex);
        //true
        System.out.println(keyWordIndex.contains(calculateKeyWord("自治州", key)));
        //true
        System.out.println(keyWordIndex.contains(calculateKeyWord("尔克孜自治州", key)));
        //false
        System.out.println(keyWordIndex.contains(calculateKeyWord("克克", key)));
        //true
        System.out.println(keyWordIndex.contains(calculateKeyWord("2F", key)));
        //false
        System.out.println(keyWordIndex.contains(calculateKeyWord("125號", key)));
        //true
        System.out.println(keyWordIndex.contains(calculateKeyWord("127號", key)));
        System.out.println(decryptXSalsa20(keyWordIndex, key));
        System.out.println(HexUtil.encodeHexStr(IdUtil.getSnowflake().nextIdStr()));
    }

    /**
     * 获取通配符关键字索引
     *
     * @param plaintext 明文
     * @param key 密钥
     * @return 密文
     */
    private static String obtainWildCardKeyWordIndex(final String plaintext, final byte[] key) {
        return HexUtil.encodeHexStr(wildCardXSalsa20(plaintext, key));
    }

    /**
     * 计算通配符关键字
     *
     * @param plaintext 明文
     * @param key 密钥
     * @return 密文
     */
    private static String calculateWildCardKeyWord(final String plaintext, final byte[] key) {
        byte[] output = wildCardXSalsa20(plaintext, key);
        int skip = 0;
        //需要跳过的字节数
        for (char c : plaintext.toCharArray()) {
            if (c == '*') {
                ++skip;
                continue;
            }
            break;
        }
        if (skip > 0) {
            output = Arrays.copyOfRange(output, skip, output.length);
        }
        return HexUtil.encodeHexStr(output);
    }

    /**
     * 通配符XSalsa20算法
     *
     * @param plaintext 明文
     * @param key 密钥
     * @return 密文
     */
    private static byte[] wildCardXSalsa20(final String plaintext, final byte[] key) {
        byte[] plainTextByte = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] ivBytes = Arrays.copyOf(key, 24);
        byte[] output = new byte[plaintext.length()];
        XSalsa20Engine engine = new XSalsa20Engine();
        engine.init(false, new ParametersWithIV(new KeyParameter(key), ivBytes));
        engine.processBytes(plainTextByte, 0, plainTextByte.length, output, 0);
        return output;
    }

    /**
     * 解密WildCardXSalsa20
     * @param plaintext 明文
     * @param key 密钥
     * @return 密文
     */
    private static String decryptWildCardXSalsa20(final String plaintext, final byte[] key) {
        byte[] plainTextByte = HexUtil.decodeHex(plaintext);
        byte[] ivBytes = Arrays.copyOf(key, 24);
        byte[] output = new byte[plainTextByte.length];
        XSalsa20Engine engine = new XSalsa20Engine();
        engine.init(false, new ParametersWithIV(new KeyParameter(key), ivBytes));
        engine.processBytes(plainTextByte, 0, plainTextByte.length, output, 0);
        return new String(output, StandardCharsets.UTF_8);
    }

    /**
     * 获取关键字索引
     *
     * @param plaintext 明文
     * @param key 密钥
     * @return 密文
     */
    private static String obtainKeyWordIndex(final String plaintext, final byte[] key) {
        return xSalsa20(plaintext, key);
    }

    /**
     * 计算关键字
     *
     * @param plaintext 明文
     * @param key 密钥
     * @return 密文
     */
    private static String calculateKeyWord(final String plaintext, final byte[] key) {
        return xSalsa20(plaintext, key);
    }

    /**
     * XSalsa20算法
     *
     * @param plaintext 明文
     * @param key 密钥
     * @return 密文
     */
    private static String xSalsa20(final String plaintext, final byte[] key) {
        String[] plaintextArr = plaintext.split("");
        StringBuilder stringBuilder = new StringBuilder(plaintextArr.length);
        byte[] ivBytes = Arrays.copyOf(key, 24);
        for (String plain : plaintextArr) {
            byte[] plainTextByte = plain.getBytes(StandardCharsets.UTF_8);
            //如果不足4位，则用key中的数据进行填充
            if (plainTextByte.length < 4) {
                byte[] dest = Arrays.copyOf(plainTextByte, 4);
                int padOffset = Math.abs(plainTextByte[0]) % (key.length - 8);
                int padSize = 4 - plainTextByte.length;
                System.arraycopy(key, 4 + padOffset, dest, dest.length - padSize, padSize);
                plainTextByte = dest;
            }
            byte[] output = new byte[4];
            XSalsa20Engine engine = new XSalsa20Engine();
            engine.init(false, new ParametersWithIV(new KeyParameter(key), ivBytes));
            engine.processBytes(plainTextByte, 0, 4, output, 0);
            stringBuilder.append(Base64.encode(output));
        }
        return stringBuilder.toString().replaceAll("=", "");
    }

    /**
     * 解密XSalsa20
     * @param plaintext 明文
     * @param key 密钥
     * @return 密文
     */
    private static String decryptXSalsa20(final String plaintext, final byte[] key) {
        byte[] ivBytes = Arrays.copyOf(key, 24);
        StringBuilder stringBuilder = new StringBuilder(plaintext.length());
        List<String> decryptTextList = splitPlainText(plaintext);
        for (String decryptText : decryptTextList) {
            byte[] decryptTextByte = Base64.decode(decryptText);
            byte[] output = new byte[4];
            XSalsa20Engine engine = new XSalsa20Engine();
            engine.init(false, new ParametersWithIV(new KeyParameter(key), ivBytes));
            engine.processBytes(decryptTextByte, 0, 4, output, 0);
            String text = new String(output, StandardCharsets.UTF_8);
            stringBuilder.append(text.charAt(0));
        }
        return stringBuilder.toString();
    }

    /**
     * 拆分文本
     *
     * @param plainText 明文
     * @return 密文
     */
    private static List<String> splitPlainText(final String plainText) {
        if (plainText.indexOf("=") > 0) {
            return StrUtil.splitTrim(plainText, "=");
        }
        List<String> decryptList = new ArrayList<>();
        for (int i = 0, k = plainText.length(); i < k; i += 6) {
            decryptList.add(plainText.substring(i, i + 6));
        }
        return decryptList;
    }
}
