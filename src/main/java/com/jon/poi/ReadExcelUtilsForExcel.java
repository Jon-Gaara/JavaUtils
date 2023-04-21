package com.jon.poi;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcelUtilsForExcel {

	private Workbook wb;
	private Sheet sheet;
	private CellStyle setBorder;
	private Font font;
	
	public ReadExcelUtilsForExcel(Workbook wb){
		this.wb = wb;
	}

	public String[][] readExcelContent(){

		String[][] content = null;
		sheet = wb.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();

		Row row = sheet.getRow(1);
		if(row == null){
			return content;
		}
		//int colNum = row.getPhysicalNumberOfCells();
		int colNum = row.getLastCellNum();
		content = new String[rowNum+1][colNum];
		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = 0; i <= rowNum; i++){
			row = sheet.getRow(i);
			if (null == row) {
				break;
			}
			//如果某行第一列出现空 就不读取这行
			if(StringUtils.isEmpty(getCellFormatValue(row.getCell(0)))){
				continue;
			}
			int j = 0;
			while (j < colNum){
				// 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
				// 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
			    content[i][j]=getCellFormatValue(row.getCell(j));
			    j++;
			}
		}
		setBorder = wb.createCellStyle();
		font = wb.createFont();
		return content;
	}

	@SuppressWarnings("deprecation")
	private static String getCellFormatValue(Cell cell) {
		String cellvalue = "";
		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()){
			// 如果当前Cell的Type为NUMERIC
			case NUMERIC:
				BigDecimal db = new BigDecimal(String.valueOf(cell.getNumericCellValue()));// 避免精度问题，先转成字符串
				cellvalue = db.toPlainString();
				break;
			case FORMULA:{
				// 判断当前的cell是否为Date
				if (DateUtil.isCellDateFormatted(cell)) {
					// 如果是Date类型则，转化为Data格式
					// 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
					// cellvalue = cell.getDateCellValue().toLocaleString();
					// 方法2：这样子的data格式是不带带时分秒的：2011-10-12
					Date date = cell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					cellvalue = sdf.format(date);
				}else{// 如果是纯数字
					DecimalFormat df = new DecimalFormat("0");
					cellvalue = df.format(cell.getNumericCellValue());//取得当前Cell的数值
				}
				break;
			}
			case STRING:{// 如果当前Cell的Type为STRIN
				// 取得当前的Cell字符串
				cellvalue = cell.getRichStringCellValue().getString();
				break;
			}default:{// 默认的Cell值
				cellvalue = "";
			}
			}
		} else {
			cellvalue = "";
		}
		return cellvalue.trim();
	}

	public void writeInTemplate(String newContent, int beginRow,int beginCell, boolean flag){
		Row row = sheet.getRow(beginRow);
		if (null == row) {
			// 如果不做空判断，你必须让你的模板文件画好边框，beginRow和beginCell必须在边框最大值以内
			// 否则会出现空指针异常
			row = sheet.createRow(beginRow);
		}
		Cell cell = row.getCell(beginCell);
		sheet.autoSizeColumn(beginCell);
		if (null == cell) {
			cell = row.createCell(beginCell);
		}
		// 设置存入内容为字符串
		//cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellType(CellType.STRING);
		getHssfCellStyle();
		cell.setCellStyle(setBorder);
		// 向单元格中放入值
		cell.setCellValue(newContent);
	}

	public void getHssfCellStyle() {
		// 字体高度
		font.setFontHeightInPoints((short) 12);
		font.setBold(true);
		font.setColor(Font.COLOR_NORMAL);
		setBorder.setBorderBottom(BorderStyle.THIN);
		setBorder.setBorderLeft(BorderStyle.THIN);
		setBorder.setBorderTop(BorderStyle.THIN);
		setBorder.setBorderRight(BorderStyle.THIN);
		setBorder.setFont(font);
	}
	public static void main(String[] args) throws Exception {
		File file = new File("d:\\交易明细信息.xls");
		InputStream input = new BufferedInputStream(new FileInputStream(file));
		/**
		 * 如果是xlsx文件，则按照xlsx文件类型读取
		 */
		if(FileMagic.valueOf(input) == FileMagic.OOXML){
			Workbook wb = new XSSFWorkbook(input);
			String[][] list = new ReadExcelUtilsForExcel(wb).readExcelContent();
			if(list != null){
				for(String[] map:list){
					System.out.print(StringUtils.join(map,"|"));
					System.out.println();
				}
			}
		}else if(FileMagic.valueOf(input) == FileMagic.OLE2){
			POIFSFileSystem fs  = new POIFSFileSystem(input);
			Workbook wb = new HSSFWorkbook(fs);
			String[][] list = new ReadExcelUtilsForExcel(wb).readExcelContent();
			for(String[] map:list){
				System.out.print(StringUtils.join(map,"|"));
				System.out.println();
			}
		}else{
			throw new IOException("该文件类型不是excel文件类型!");
		}
	}
}
