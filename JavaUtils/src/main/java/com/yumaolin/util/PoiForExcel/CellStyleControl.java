package com.yumaolin.util.PoiForExcel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

public class CellStyleControl {
	private Workbook wb;
	private CellStyle titleStyle;
	private CellStyle  headStyle;
	private CellStyle  centerStyle;
	//private XSSFCellStyle  moneyStyle;
	//private XSSFCellStyle  percentStyle;
	//private XSSFCellStyle numberStyle;
	//private XSSFCellStyle longDateStyle;
	//private XSSFCellStyle borderStyle;
	
	/**
	 * 构造
	 * @param wb
	 */
	public CellStyleControl(Workbook wb){
		this.wb = wb;
	}
	
	/* ==================== 字体 =================== */
	
	/**
	 * 标题字体
	 * @param wb
	 * @return
	 */
	public Font getTitleFont() {
		Font titleFont = wb.createFont(); 
		titleFont.setFontName("宋体"); 
		//设置字体大小
		titleFont.setFontHeightInPoints((short) 16);
		//字体加粗
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		return titleFont;
	}
	
	/**
	 * 表头字体
	 * @param wb
	 * @return
	 */
	public Font getHeaderFont() {
		Font font = wb.createFont(); 
		font.setFontName("宋体"); 
		//字体加粗
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		return font;
	}
	
	/* =====================  样式 ================== */
	/**
	 * 标题样式
	 * @param wb
	 * @return
	 */
	public CellStyle getTitleStyle() {
		if(titleStyle==null){
			titleStyle = wb.createCellStyle();
			titleStyle.setFont(getTitleFont());
			titleStyle.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);
			titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
			titleStyle.setBorderBottom((short) 1);
			titleStyle.setBorderTop((short) 1);
			titleStyle.setBorderLeft((short) 1);
			titleStyle.setBorderRight((short) 1);
		}
		return titleStyle;
	}
	
	/**
	 * 表头样式，居中，加粗
	 * @param wb
	 * @return
	 */
	public CellStyle getHeaderStyle() {
		if( headStyle==null ){
			headStyle = wb.createCellStyle(); 
			headStyle.setBorderBottom((short) 1);
			headStyle.setBorderTop((short) 1);
			headStyle.setBorderLeft((short) 1);
			headStyle.setBorderRight((short) 1);
			headStyle.setAlignment(CellStyle.ALIGN_CENTER);
			headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			headStyle.setFont(getHeaderFont());
		}
		return headStyle;
	}
	
	/**
	 * 居中样式
	 * @param wb
	 * @return
	 */
	public CellStyle getCenterStyle() {
		if(centerStyle==null){
			centerStyle = wb.createCellStyle();
			centerStyle.setBorderBottom((short) 1);
			centerStyle.setBorderTop((short) 1);
			centerStyle.setBorderLeft((short) 1);
			centerStyle.setBorderRight((short) 1);
			centerStyle.setAlignment(CellStyle.ALIGN_CENTER);
			centerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		}
		return centerStyle;
	}
	
	/**
	 * 金额样式
	 * @param wb
	 * @return
	 */
	public CellStyle getMoneyStyle(CellStyle  moneyStyle) {
		//if(moneyStyle==null){
			//moneyStyle = wb.createCellStyle();
			moneyStyle.setBorderBottom((short) 1);
			moneyStyle.setBorderTop((short) 1);
			moneyStyle.setBorderLeft((short) 1);
			moneyStyle.setBorderRight((short) 1);
			moneyStyle.setAlignment(CellStyle.ALIGN_RIGHT);
			moneyStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			DataFormat dataFormat= wb.createDataFormat();
			moneyStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));
		//}
		return moneyStyle;
	}
	
	/**
	 * 百分比样式
	 * @param wb
	 * @return
	 */
	public CellStyle getPercentStyle(CellStyle percentStyle) {
		//if(percentStyle==null){
		//	percentStyle = wb.createCellStyle();
		    DataFormat format = wb.createDataFormat();
			percentStyle.setDataFormat(format.getFormat("0.00%"));
			percentStyle.setBorderBottom((short) 1);
			percentStyle.setBorderTop((short) 1);
			percentStyle.setBorderLeft((short) 1);
			percentStyle.setBorderRight((short) 1);
			percentStyle.setAlignment(CellStyle.ALIGN_RIGHT);
			percentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		//}
		return percentStyle;
	}
	
	/**
	 * 短日期样式
	 * @param wb
	 * @return
	 */
	public CellStyle getDateStyle(CellStyle dateStyle) {
		//XSSFCellStyle dateStyle = wb.createCellStyle();
		DataFormat format= wb.createDataFormat();
		dateStyle.setDataFormat(format.getFormat("yyyy-MM-dd"));
		dateStyle.setBorderBottom((short) 1);
		dateStyle.setBorderTop((short) 1);
		dateStyle.setBorderLeft((short) 1);
		dateStyle.setBorderRight((short) 1);
		dateStyle.setAlignment(CellStyle.ALIGN_CENTER);
		dateStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		return dateStyle;
	}
	
	/**
	 * 长日期样式
	 * @param wb
	 * @return
	 */
	public CellStyle getDatetimeStyle(CellStyle longDateStyle) {
		//if(longDateStyle==null){
			//longDateStyle = wb.createCellStyle();
			DataFormat format= wb.createDataFormat();
			longDateStyle.setDataFormat(format.getFormat("yyyy-MM-dd HH:mm"));
			longDateStyle.setBorderBottom((short) 1);
			longDateStyle.setBorderTop((short) 1);
			longDateStyle.setBorderLeft((short) 1);
			longDateStyle.setBorderRight((short) 1);
			longDateStyle.setAlignment(CellStyle.ALIGN_CENTER);
			longDateStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		//}
		return longDateStyle;
	}
	/**
	 * 数字边框样式
	 * @param wb
	 * @return
	 */
	public CellStyle getNumberStyle(CellStyle numberStyle) {
		//if(numberStyle==null){
			//numberStyle = wb.createCellStyle(); 
			numberStyle.setBorderBottom((short) 1);
			numberStyle.setBorderTop((short) 1);
			numberStyle.setBorderLeft((short) 1);
			numberStyle.setBorderRight((short) 1);
			numberStyle.setAlignment(CellStyle.ALIGN_RIGHT);
			numberStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		//}
		return numberStyle;
	}
	
	/**
	 * 简单边框样式
	 * @param wb
	 * @return
	 */
	public CellStyle getBorderStyle(CellStyle borderStyle) {
		//if(borderStyle==null){
			//borderStyle = wb.createCellStyle(); 
			borderStyle.setBorderBottom((short) 1);
			borderStyle.setBorderTop((short) 1);
			borderStyle.setBorderLeft((short) 1);
			borderStyle.setBorderRight((short) 1);
			borderStyle.setAlignment(CellStyle.ALIGN_CENTER);
			borderStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		//}
		return borderStyle;
	}
	
	
	/* =================对外方法================= */
	/**
	 * 根据数据类型格式化样式
	 * @param type
	 * @return
	 */
	public CellStyle getStyleByType(CellType type,CellStyle cellStyle) {
		CellStyle style = null;
		switch(type){
		case String : style = getBorderStyle(cellStyle); break;
		case Money: style = getMoneyStyle(cellStyle); break;
		case Percent: style = getPercentStyle(cellStyle); break;
		case Number: style = getNumberStyle(cellStyle); break;
		case Date : style = getDateStyle(cellStyle); break;
		default: style = getBorderStyle(cellStyle);
		}
		return style;
	}
	public short colorChoose(String color){
		if("blue".equals(color)){
			return IndexedColors.BLUE.getIndex();
		}else if("red".equals(color)){
			return IndexedColors.RED.getIndex();
		}else if("yellow".equals(color)){
			return IndexedColors.YELLOW.getIndex();
		}else{
			return IndexedColors.WHITE.getIndex();
		}
	}
}
