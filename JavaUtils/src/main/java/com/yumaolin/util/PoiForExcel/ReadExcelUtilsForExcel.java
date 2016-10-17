package com.yumaolin.util.PoiForExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.BLACK;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcelUtilsForExcel {
	private Workbook wb;
	private static Sheet sheet;
	private static Row row;
	private static CellStyle setBorder;
	private static Font font;
	private static long fileSize = 1024*1024*10;//10mb
	
	public ReadExcelUtilsForExcel(Workbook wb){
		this.wb = wb;
	}

	public  String[][] readExcelContent(){
	    	String[][] content;
		sheet = wb.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		row = sheet.getRow(1);
		//int colNum = row.getPhysicalNumberOfCells();
		int colNum = row.getLastCellNum();
		content = new String[rowNum+1][colNum];
		System.out.println(content.length);
		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = 0; i <= rowNum; i++){
			row = sheet.getRow(i);
			if (null == row) {
				break;
			}
			if(StringUtils.isEmpty(getCellFormatValue(row.getCell(0)))){//如果某行第一列出现空 就不读取这行
				continue;
			}
			int j = 0;
			while (j < colNum){
				// 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
				// 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
				// str += getStringCellValue(row.getCell((short) j)).trim() +
				// "-";
				/*sbd.append(getCellFormatValue(row.getCell(j)).trim() == null
						|| "".equals(getCellFormatValue(row.getCell(j)).trim()) ? nullStr
						: getCellFormatValue(row.getCell(j)).trim() + SPLITF);*/
			    content[i][j]=getCellFormatValue(row.getCell(j));
			    j++;
			}
		}
		setBorder = wb.createCellStyle();
		font = wb.createFont();
		return content;
	}

	private static String getCellFormatValue(Cell cell) {
		String cellvalue = "";
		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()){
			// 如果当前Cell的Type为NUMERIC
			case Cell.CELL_TYPE_NUMERIC:
				BigDecimal db = new BigDecimal(cell.getNumericCellValue()+"");// 避免精度问题，先转成字符串
				cellvalue = db.toPlainString();
				break;
			case Cell.CELL_TYPE_FORMULA:{
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
			case Cell.CELL_TYPE_STRING:{// 如果当前Cell的Type为STRIN
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

	public static void writeInTemplate(String newContent, int beginRow,int beginCell, boolean flag){
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
		cell.setCellType(Cell.CELL_TYPE_STRING);
		getHssfCellStyle(flag);
		cell.setCellStyle(setBorder);
		// 向单元格中放入值
		cell.setCellValue(newContent);
	}

	public static void getHssfCellStyle(boolean flag) {
		// cell.setCellStyle(styleFactory.getHeaderStyle());
		font.setFontHeightInPoints((short) 12); // 字体高度
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setColor(BLACK.index);
		setBorder.setBorderBottom(CellStyle.BORDER_THIN);
		setBorder.setBorderLeft(CellStyle.BORDER_THIN);
		setBorder.setBorderTop(CellStyle.BORDER_THIN);
		setBorder.setBorderRight(CellStyle.BORDER_THIN);
		setBorder.setFont(font);
	}
	public static void main(String[] args) throws Exception {
		File file = new File("d:\\交易明细信息.xls");
		//File file = new File("d:\\主表.xls");
		InputStream input = new FileInputStream(file);
		if (!input.markSupported()) {
			 input = new PushbackInputStream(input, 8);
	    }
		//boolean flag = ReadExcelUtilsForXls.getTypeByStream(file, "xlsx");
		//POIXMLDocument.hasOOXMLHeader(input)判断是否是xlsx文件
		/**
		 * 如果是xlsx文件，则按照xlsx文件类型读取
		 */
		if(POIXMLDocument.hasOOXMLHeader(input)){
			if(file.length()>fileSize){
				System.out.println("http://blog.csdn.net/lee_guang/article/details/8936178");
			}else{
				Workbook wb = new XSSFWorkbook(input);
				String[][] list = new ReadExcelUtilsForExcel(wb).readExcelContent();
				for(String[] map:list){
					for(int i=0;i<map.length;i++){
						String value = map[i];
						System.out.print(value+"|");
					}
					System.out.println();
				}
			}
		}else if(POIFSFileSystem.hasPOIFSHeader(input)){
			POIFSFileSystem fs  = new POIFSFileSystem(input);
			Workbook wb = new HSSFWorkbook(fs);
			String[][] list = new ReadExcelUtilsForExcel(wb).readExcelContent();
			for(String[]  map:list){
				for(int i=0;i<map.length;i++){
					String value = map[i];
					System.out.print(value+"|");
				}
				System.out.println();
			}
		}else{
			throw new IOException("该文件类型不是excel文件类型!");
		}
	}
}
