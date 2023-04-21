package com.jon.pattern;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 正则表达式
 * 
 * @author yumaolin
 */
public class PatternUtils {

    /**
     * 验证是否为小数(保留两位有效数字)
     */
    public static final String decimals = "^[0-9]+\\.{0,1}[0-9]{0,2}$";
    /**
     * 验证Email
     */
    public static final String email = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /**
     * 验证手机和电话号码(带区号)
     */
    public static final String phone =
        "^(((13[0-9])|(14[5|7])|(15[0-3,5-9])|(17[0,5-8])|(18[0-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$";
    /**
     * 验证中国身份证15位
     */
    public static final String idCard = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    /**
     * 验证中国身份证18位
     */
    public static final String idCard2 =
        "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x)$";
    /**
     * 验证中文
     */
    public static final String chinese = "^[\u4e00-\u9fa5]$";
    /**
     * 数字和26个英文字母
     */
    public static final String numberAndEnglish = "^[A-Za-z0-9]+$";
    private static final int[] weightFactor = new int[] {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static final String[] verify = new String[] {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

    public static boolean verifyDecimals(String pattern, String data) {
        Pattern str = Pattern.compile(pattern);
        return str.matcher(data).matches();
    }

	/**
	 * 份证最后一位校验
	 * @param idCard
	 * @return
	 */
    public static boolean idCardLastPattern(String idCard) {
        if (StringUtils.isEmpty(idCard)) {
            return false;
        }
        if (idCard.length() == 15) {
            return true;
        }
        String[] idCardStrArray = idCard.split("");
        int idCardArray = 0;
        for (int i = 0, k = idCardStrArray.length - 1; i < k; i++) {
			idCardArray += Integer.parseInt(idCardStrArray[i]) * weightFactor[i];
        }
        int last = idCardArray % 11;
        String checkCode = verify[last];

        if (idCardStrArray[idCard.length()-1].equalsIgnoreCase(checkCode)) {
            return true;
        } else {
            return false;
        }
    }
}
