package com.jon.file;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class FileDataFormat {

    private static final String enter = "\r\n";
    private static final String split = "|";
    // private static final String
    // startMark="000000000|000000000000003|00000000|0000000000000000000000000000000000000000000000000000000000000000|000000000000000000000|0000000000000000000000000000000000000000000000000000000000000000|0000000000000000000000000000000000000000000000000000000000000000|000000000000|0|"+Consts.getSuitString(String.valueOf(NumberUtil.muitDouble(String.valueOf(totalCount.getSumTotal2())).longValue()),12)+"|00000000000000000000|0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    private static final String endMark =
        "999999999|999999999999999|99999999|9999999999999999999999999999999999999999999999999999999999999999|999999999999999999999|9999999999999999999999999999999999999999999999999999999999999999|9999999999999999999999999999999999999999999999999999999999999999|999999999999|9|999999999999|99999999999999999999|9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999";
    private static final String rowStartMark = "200200222";
    private static final String rowEndMark =
        "00000000000000000001|商户划款                                                                                            ";

    public void format(final String oldFilePath, final String newFilePath) throws Exception {
        List<String> list = new ArrayList<>();
        StringBuilder sbuilder = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(oldFilePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "gbk"));) {
            String textData;
            while ((textData = br.readLine()) != null) {
                list.add(textData);
            }
            try (FileOutputStream fileout = new FileOutputStream(newFilePath);
                BufferedOutputStream buff = new BufferedOutputStream(fileout)) {
                sbuilder.append(list.get(0)).append(enter);
                for (int i = 1; i < list.size() - 1; i++) {
                    String[] strAnd = list.get(i).split("\\|");
                    sbuilder.append(rowStartMark).append(split).append(getStringFormat(15, strAnd[1])).append(split)
                        .append(getStringFormat(8, strAnd[2])).append(split).append(getStringFormat(64, strAnd[3]))
                        .append(split).append(getStringFormat(21, strAnd[4])).append(split)
                        .append(getStringFormat(64, strAnd[5])).append(split).append(getStringFormat(64, strAnd[6]))
                        .append(split).append(getStringFormat(12, strAnd[7])).append(split).append("1").append(split)
                        .append(getStringFormatZero(12, strAnd[9])).append(split).append(rowEndMark).append(enter);
                }
                sbuilder.append(endMark);
                buff.write(sbuilder.toString().getBytes("GBK"));
            }
        }
    }

    /**
     * 长度不足补空格
     */
    public String getStringFormat(int length, String str) throws Exception {
        if (StringUtils.isNotEmpty(str)) {
            str = str.trim();
        }
        int strLength = str.getBytes("GBK").length;// 12
        int sLength = str.length();// 4
        String strL;
        if (length > strLength) {
            strL = String.format("%-" + (length - strLength + sLength) + "s", str);
        } else {
            strL = str;
        }
        return strL;
    }

    /**
     * 长度不足补0
     */
    public String getStringFormatZero(int length, String str) throws Exception {
        if (StringUtils.isNotEmpty(str)) {
            str = str.trim();
        }
        int strLength = str.getBytes("GBK").length;
        int sLength = str.length();
        String strL;
        if (length > strLength) {
            strL = String.format("%0" + (length - strLength + sLength) + "d", Long.valueOf(str));
        } else {
            strL = str;
        }
        return strL;
    }

    public static void main(String[] args) throws Exception {
        final String oldFilePath = "d:/20170109.txt";
        final String newFilePath = "d:/new20170109.txt";
        FileDataFormat fileDataFormat = new FileDataFormat();
        fileDataFormat.format(oldFilePath, newFilePath);
    }
}
