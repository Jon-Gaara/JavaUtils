package com.jon.poi;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

public class CellStyleControl {
    private Workbook wb;
    private CellStyle titleStyle;
    private CellStyle headStyle;
    private CellStyle centerStyle;

    /**
     * 构造
     * 
     * @param wb
     */
    public CellStyleControl(Workbook wb) {
        this.wb = wb;
    }

    /* ==================== 字体 =================== */

    /**
     * 标题字体
     * 
     * @return
     */
    public Font getTitleFont() {
        Font titleFont = wb.createFont();
        titleFont.setFontName("宋体");
        // 设置字体大小
        titleFont.setFontHeightInPoints((short)16);
        // 字体加粗
        // titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        titleFont.setBold(true);
        return titleFont;
    }

    /**
     * 表头字体
     * 
     * @return
     */
    public Font getHeaderFont() {
        Font font = wb.createFont();
        font.setFontName("宋体");
        // 字体加粗
        // font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setBold(true);
        return font;
    }

    /* =====================  样式 ================== */
    /**
     * 标题样式
     * 
     * @return
     */
    public CellStyle getTitleStyle() {
        if (titleStyle == null) {
            titleStyle = wb.createCellStyle();
            titleStyle.setFont(getTitleFont());
            titleStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
            titleStyle.setBorderBottom(BorderStyle.THIN);
            titleStyle.setBorderTop(BorderStyle.THIN);
            titleStyle.setBorderLeft(BorderStyle.THIN);
            titleStyle.setBorderRight(BorderStyle.THIN);
        }
        return titleStyle;
    }

    /**
     * 表头样式，居中，加粗
     *
     * @return
     */
    public CellStyle getHeaderStyle() {
        if (headStyle == null) {
            headStyle = wb.createCellStyle();
            headStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
            headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headStyle.setBorderBottom(BorderStyle.THIN);
            headStyle.setBorderTop(BorderStyle.THIN);
            headStyle.setBorderLeft(BorderStyle.THIN);
            headStyle.setBorderRight(BorderStyle.THIN);
            headStyle.setFont(getHeaderFont());
        }
        return headStyle;
    }

    /**
     * 居中样式
     *
     * @return
     */
    public CellStyle getCenterStyle() {
        if (centerStyle == null) {
            centerStyle = wb.createCellStyle();
            centerStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
            centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            centerStyle.setBorderBottom(BorderStyle.THIN);
            centerStyle.setBorderTop(BorderStyle.THIN);
            centerStyle.setBorderLeft(BorderStyle.THIN);
            centerStyle.setBorderRight(BorderStyle.THIN);
        }
        return centerStyle;
    }

    /**
     * 金额样式
     *
     * @return
     */
    public CellStyle getMoneyStyle(CellStyle moneyStyle) {
        moneyStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        moneyStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        moneyStyle.setBorderBottom(BorderStyle.THIN);
        moneyStyle.setBorderTop(BorderStyle.THIN);
        moneyStyle.setBorderLeft(BorderStyle.THIN);
        moneyStyle.setBorderRight(BorderStyle.THIN);
        DataFormat dataFormat = wb.createDataFormat();
        moneyStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));
        return moneyStyle;
    }

    /**
     * 百分比样式
     *
     * @return
     */
    public CellStyle getPercentStyle(CellStyle percentStyle) {
        DataFormat format = wb.createDataFormat();
        percentStyle.setDataFormat(format.getFormat("0.00%"));
        percentStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        percentStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        percentStyle.setBorderBottom(BorderStyle.THIN);
        percentStyle.setBorderTop(BorderStyle.THIN);
        percentStyle.setBorderLeft(BorderStyle.THIN);
        percentStyle.setBorderRight(BorderStyle.THIN);
        return percentStyle;
    }

    /**
     * 短日期样式
     * 
     * @param wb
     * @return
     */
    public CellStyle getDateStyle(CellStyle dateStyle) {
        DataFormat format = wb.createDataFormat();
        dateStyle.setDataFormat(format.getFormat("yyyy-MM-dd"));
        dateStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dateStyle.setBorderBottom(BorderStyle.THIN);
        dateStyle.setBorderTop(BorderStyle.THIN);
        dateStyle.setBorderLeft(BorderStyle.THIN);
        dateStyle.setBorderRight(BorderStyle.THIN);
        return dateStyle;
    }

    /**
     * 长日期样式
     *
     * @return
     */
    public CellStyle getDatetimeStyle(CellStyle longDateStyle) {
        DataFormat format = wb.createDataFormat();
        longDateStyle.setDataFormat(format.getFormat("yyyy-MM-dd HH:mm"));
        longDateStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        longDateStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        longDateStyle.setBorderBottom(BorderStyle.THIN);
        longDateStyle.setBorderTop(BorderStyle.THIN);
        longDateStyle.setBorderLeft(BorderStyle.THIN);
        longDateStyle.setBorderRight(BorderStyle.THIN);
        return longDateStyle;
    }

    /**
     * 数字边框样式
     *
     * @return
     */
    public CellStyle getNumberStyle(CellStyle numberStyle) {
        numberStyle.setBorderBottom(BorderStyle.THIN);
        numberStyle.setBorderTop(BorderStyle.THIN);
        numberStyle.setBorderLeft(BorderStyle.THIN);
        numberStyle.setBorderRight(BorderStyle.THIN);
        numberStyle.setAlignment(HorizontalAlignment.RIGHT);
        numberStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return numberStyle;
    }

    /**
     * 简单边框样式
     *
     * @return
     */
    public CellStyle getBorderStyle(CellStyle borderStyle) {
        borderStyle.setBorderBottom(BorderStyle.THIN);
        borderStyle.setBorderTop(BorderStyle.THIN);
        borderStyle.setBorderLeft(BorderStyle.THIN);
        borderStyle.setBorderRight(BorderStyle.THIN);
        borderStyle.setAlignment(HorizontalAlignment.CENTER);
        borderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return borderStyle;
    }

    /* =================对外方法================= */
    /**
     * 根据数据类型格式化样式
     * 
     * @param type
     * @return
     */
    public CellStyle getStyleByType(CellTypeEnum type, CellStyle cellStyle) {
        CellStyle style;
        switch (type) {
            case String:
                style = getBorderStyle(cellStyle);
                break;
            case Money:
                style = getMoneyStyle(cellStyle);
                break;
            case Percent:
                style = getPercentStyle(cellStyle);
                break;
            case Number:
                style = getNumberStyle(cellStyle);
                break;
            case Date:
                style = getDateStyle(cellStyle);
                break;
            default:
                style = getBorderStyle(cellStyle);
        }
        return style;
    }

    public short colorChoose(String color) {
        if ("blue".equals(color)) {
            return IndexedColors.BLUE.getIndex();
        } else if ("red".equals(color)) {
            return IndexedColors.RED.getIndex();
        } else if ("yellow".equals(color)) {
            return IndexedColors.YELLOW.getIndex();
        } else {
            return IndexedColors.WHITE.getIndex();
        }
    }
}
