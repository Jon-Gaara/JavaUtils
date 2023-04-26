package com.jon.encryption;

import java.util.List;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

/**
 * 基于阿里巴巴和拼多多加解密文档实现，可搜索加解密
 * 1.淘宝密文字段检索方案：https://open.taobao.com/docV3.htm?docId=106213&docType=1
 * 2.阿里巴巴文字段检索方案：https://jaq-doc.alibaba.com/docs/doc.htm?treeId=1&articleId=106213&docType=1
 * 3.拼多多密文字段检索方案：https://open.pinduoduo.com/application/document/browse?idStr=3407B605226E77F2
 *
 * @author Jon Yu
 */
public class SearchEncrypt {

    /**
     * 单词数字明文，默认长度
     */
    private static final int DEFAULT_WORD_LENGTH = 4;
    /**
     * 中文明文，默认长度
     */
    private static final int DEFAULT_CH_WORD_LENGTH = 2;
    /**
     * 模糊搜索分隔符
     */
    private static final String DEFAULT_FUZZY_SPLIT = "~";
    /**
     * 固定搜索分隔符
     */
    private static final String DEFAULT_FIXED_SPLIT = "$";

    /**
     * 提取索引值
     * @param encryptedData 加密串
     * @return 索引值
     */
    public static String extractIndex(String encryptedData) {
        return extractInfo(encryptedData, 0);
    }

    /**
     * 提取密文
     * @param encryptedData 加密串
     * @return 密文
     */
    public static String extractEncryptedData(String encryptedData) {
        return extractInfo(encryptedData, 1);
    }

    /**
     * 提取版本号
     * @param encryptedData 加密串
     * @return 版本号
     */
    public static String extractVersion(String encryptedData) {
        return extractInfo(encryptedData, 2);
    }

    private static String extractInfo(String encryptedData, int extract) {
        if (encryptedData == null || encryptedData.length() < 4) {
            return null;
        }
        char sepInData = encryptedData.charAt(0);
        if (encryptedData.charAt(encryptedData.length() - 2) != sepInData) {
            return null;
        }
        List<String> parts = StrUtil.splitTrim(encryptedData, sepInData);
        if (sepInData != DEFAULT_FIXED_SPLIT.charAt(0)) {
            if (extract == 0 || extract == 1) {
                extract = extract == 0 ? 1 : 0;
            }
        }
        return parts.get(extract);
    }

    /**
     * 拼接模糊查询串
     *
     * @param encryptedData  加密密文
     * @param encryptedIndex 加密索引值
     * @param version        密钥版本
     * @return 查询串
     */
    public static String joinFuzzyQuery(String encryptedData, String encryptedIndex, String version) {
        return DEFAULT_FUZZY_SPLIT + encryptedData + DEFAULT_FUZZY_SPLIT + encryptedIndex + DEFAULT_FUZZY_SPLIT + version + DEFAULT_FUZZY_SPLIT + DEFAULT_FUZZY_SPLIT;
    }

    /**
     * 拼接固定查询串
     *
     * @param encryptedData  加密密文
     * @param encryptedIndex 加密索引值
     * @param version        密钥版本
     * @return 查询串
     */
    public static String joinFixedQuery(String encryptedData, String encryptedIndex, String version) {
        return DEFAULT_FIXED_SPLIT + encryptedIndex + DEFAULT_FIXED_SPLIT + encryptedData + DEFAULT_FIXED_SPLIT + version + DEFAULT_FIXED_SPLIT + DEFAULT_FIXED_SPLIT;
    }

    /**
     * 创建通用索引
     *
     * @param key     密钥
     * @param content 明文
     * @return 加密字符串
     */
    public static String createCurrencyWordIndex(final byte[] key, String content) {
        return createWordIndex(key, content, DEFAULT_WORD_LENGTH);
    }

    public static String createCurrencyWordIndex(final byte[] key, String content, int wordSize) {
        if (wordSize < DEFAULT_WORD_LENGTH) {
            wordSize = DEFAULT_WORD_LENGTH;
        }
        return createWordIndex(key, content, wordSize);
    }

    /**
     * 创建中文索引
     *
     * @param key     密钥
     * @param content 明文
     * @return 加密字符串
     */
    public static String createChWordIndex(final byte[] key, String content) {
        return createWordIndex(key, content, DEFAULT_CH_WORD_LENGTH);
    }

    public static String createChWordIndex(final byte[] key, String content, int wordSize) {
        if (wordSize < DEFAULT_CH_WORD_LENGTH) {
            wordSize = DEFAULT_CH_WORD_LENGTH;
        }
        return createWordIndex(key, content, wordSize);
    }

    /**
     * @param key      密钥
     * @param content  明文
     * @param wordSize 分组大小
     * @return 加密字符串
     */
    private static String createWordIndex(final byte[] key, String content, int wordSize) {
        if (StrUtil.isBlank(content) || content.length() < wordSize) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(32);
        char[] charArray = content.toCharArray();
        for (int i = 0, k = charArray.length - wordSize + 1; i < k; i++) {
            StringBuilder charStr = new StringBuilder(wordSize);
            for (int j = i, z = i + wordSize; j < z; j++) {
                charStr.append(charArray[j]);
            }
            String str = encryptString(key, charStr.toString());
            stringBuilder.append(str);
        }
        return stringBuilder.toString().replaceAll("=", "");
    }

    /**
     * 字符串加密
     *
     * @param key     密钥
     * @param content 明文
     * @return 加密字符串
     */
    private static String encryptString(final byte[] key, final String content) {
        //构建
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        return aes.encryptBase64(content);
    }

    public static void main(String[] args) {
        //加密字段
        String content = "taobao123";
        //随机生成密钥
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
        final String wordIndex = createCurrencyWordIndex(key, content);
        System.out.println(wordIndex);
        //true
        System.out.println("1." + wordIndex.contains(createCurrencyWordIndex(key, "taob")));
        //true
        System.out.println("2." + wordIndex.contains(createCurrencyWordIndex(key, "taoba")));
        //true
        System.out.println("3." + wordIndex.contains(createCurrencyWordIndex(key, "bao123")));
        //false
        System.out.println("4." + wordIndex.contains(createCurrencyWordIndex(key, "taobb")));
        String joinFuzzyStr = joinFuzzyQuery("123124341zxdasdasda", wordIndex, "12");
        String joinFixedStr = joinFixedQuery("22231aaa44zxdasdasda", wordIndex, "25");
        System.out.println(joinFuzzyStr);
        System.out.println(joinFixedStr);
        System.out.println(extractIndex(joinFuzzyStr));
        System.out.println(extractIndex(joinFixedStr));
        System.out.println(extractEncryptedData(joinFuzzyStr));
        System.out.println(extractEncryptedData(joinFixedStr));
        System.out.println(extractVersion(joinFuzzyStr));
        System.out.println(extractVersion(joinFixedStr));
        //System.out.println(extractIndex("$7AnwZJ1e6BZc$AAAAAADkt0hgZkt6KLklIDxVp+F1wzHsRsPUw0s19fk=$2$$"));
        //System.out.println(extractIndex("~AAAAAADkt0io3kp+aGA1Gq6S1D66O5bhi3ysXosALbSWXV6K+BYciGWzfL7LZk/BDwebszQR5eDWAKLFYoSxgw==~Ew/jZ2uN9cqK5wk1O4mgVINIu9B7nvYINXiZMm/QCuK1zALvOxCf5NMykFJ2~2~~"));
        //System.out.println(extractEncryptedData("$7AnwZJ1e6BZc$AAAAAADkt0hgZkt6KLklIDxVp+F1wzHsRsPUw0s19fk=$2$$"));
        //System.out.println(extractEncryptedData("~AAAAAADkt0io3kp+aGA1Gq6S1D66O5bhi3ysXosALbSWXV6K+BYciGWzfL7LZk/BDwebszQR5eDWAKLFYoSxgw==~Ew/jZ2uN9cqK5wk1O4mgVINIu9B7nvYINXiZMm/QCuK1zALvOxCf5NMykFJ2~2~~"));
        //System.out.println(extractVersion("$7AnwZJ1e6BZc$AAAAAADkt0hgZkt6KLklIDxVp+F1wzHsRsPUw0s19fk=$2$$"));
        //System.out.println(extractVersion("~AAAAAADkt0io3kp+aGA1Gq6S1D66O5bhi3ysXosALbSWXV6K+BYciGWzfL7LZk/BDwebszQR5eDWAKLFYoSxgw==~Ew/jZ2uN9cqK5wk1O4mgVINIu9B7nvYINXiZMm/QCuK1zALvOxCf5NMykFJ2~3~~"));
    }
}
