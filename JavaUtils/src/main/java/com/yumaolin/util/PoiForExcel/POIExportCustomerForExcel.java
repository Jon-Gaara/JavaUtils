package com.yumaolin.util.PoiForExcel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class POIExportCustomerForExcel {
	private Workbook wb; 
	private int v_index = 0;//行数
	private Sheet hs;
	private CellStyleControl styleFactory;
	private CellStyle cellStyle;
	private OutputStream output;
	private String newFileName;
	private Map<String,CellStyle> cellStylemap = new HashMap<String,CellStyle>();
	
	public POIExportCustomerForExcel(Workbook wb,OutputStream output){
		this.wb = wb;
		this.output = output;
	}
	public void getExcelCreate(String fileName) throws IOException{
		//response.reset();
		/* ===== 1.设置响应头 ===== */
		//setReaponseHeader(request,response,fileName);
		/* ===== 2.创建HSSFWorkbook ===== */
		newFileName = fileName.substring(0, fileName.lastIndexOf("."));
		//创建工作薄
		hs = wb.createSheet(newFileName);
		//创建样式工程
		styleFactory = new CellStyleControl(wb);
		cellStyle = wb.createCellStyle();
	}
	public Integer getTermTitleStyle(List<CellConfig> configs){
		//创建标题项
		Row titleRow = hs.createRow(v_index);
		for(int i=0;i<configs.size();i++){
			titleRow.createCell(i);
			hs.setColumnWidth(i, (short) (256 * 12)); 
			titleRow.getCell(i).setCellStyle(styleFactory.getTitleStyle());
		}
		//合并标题栏
		hs.addMergedRegion(new CellRangeAddress(0, 0, 0, configs.size()-1));
		titleRow.getCell(0).setCellValue(newFileName);
		titleRow.setHeight((short) (256*2));
		v_index += 1;
		return v_index;
	}
	
	/**
	 * 创建明细项标题
	 * @param configs
	 * @param v_index
	 */
	public void getDetailTitle(List<CellConfig> configs,int v_index){
		//创建明细项标题(表头)
	    //v_index += 1;
		Row detailTitle = hs.createRow(v_index);
		detailTitle.setHeight((short) (128*3));
		int col = 0;
		for(CellConfig conf:configs){
			//创建cell
			Cell cell = detailTitle.createCell(col++);
			//根据类型添加样式
			cell.setCellStyle(styleFactory.getHeaderStyle());
			//添加值
			cell.setCellValue(conf.getColName());
		}
	}
	
	/**
	 * 生成列表数据
	 * @param configs
	 * @param xycList
	 * @param total
	 * @param v_index
	 * @throws IOException 
	 */
	public <T> void getExcelValue(List<CellConfig> configs,List<T> xycList,LinkedHashMap<Integer,Object[]> total) throws IOException{
		//生成列表数据
			for(int index=0;index<xycList.size();index++){
				v_index+=1;
				Row row = hs.createRow(v_index);
				row.setHeight((short) (128*3));
				for(int i=0;i<configs.size();i++){
					row.createCell(i);
					try {
						//样式
						if(cellStylemap.get(configs.get(i).getCellType().toString())!=null){
							cellStyle = cellStylemap.get(configs.get(i).getCellType().toString());
						}else{
							cellStyle = styleFactory.getStyleByType(CellType.valueOf(configs.get(i).getCellType().toString()),cellStyle);
							cellStylemap.put(configs.get(i).getCellType().toString(), cellStyle);
						}
						if(cellStyle!=null)
							row.getCell(i).setCellStyle(cellStyle);
						//赋值
						SetExcelValue.setTheCellValue(styleFactory,row.getCell(i), index, xycList.get(index), configs.get(i));
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		//合计行
		if(total!=null) {
			v_index+=1;
			Row row = hs.createRow(v_index);
			row.setHeight((short) (128*3));
			for(int i=0;i<configs.size();i++) {
				Cell cell = row.createCell(i);
				//根据类型添加样式
				cell.setCellStyle(styleFactory.getCenterStyle());
				if(i==0) {
					cell.setCellValue("合计");
				}
			}
			for(Entry<Integer, Object[]> entry : total.entrySet()) {
				Cell cell = row.getCell(entry.getKey());
				//添加值
				SetExcelValue.setValueAndStyleByType(styleFactory, cell, CellType.valueOf(entry.getValue()[1].toString()), entry.getValue()[0],cellStyle);
			}
		}
		//调整每列宽度自适应
		for(int j=0;j<configs.size();j++){
			//hs.autoSizeColumn(j);//SXSSFWorkbook自适应报错
			hs.setColumnWidth(j,configs.get(j).getFieldName().length()*2*256);
		}
		wb.write(output);
	}
	public static void main(String[] args){
			FileOutputStream output = null;
		try{
			Workbook wb = new SXSSFWorkbook(100);
			//Workbook wb = new XSSFWorkbook();
			//Workbook wb = new HSSFWorkbook();
			output = new FileOutputStream(new File("d:/test.xlsx"));
			POIExportCustomerForExcel poi = new POIExportCustomerForExcel(wb,output);
			List<CellConfig> configs = new ArrayList<CellConfig>();
			configs.add(new CellConfig("姓名", "name",CellType.String));
			configs.add(new CellConfig("年龄", "age",CellType.Number));
			configs.add(new CellConfig("身高", "heigh",CellType.Number));
			List<User>  list = new ArrayList<User>();
			for(int i=0;i<10000;i++){
				User user  = new User();
				user.setName("jone");
				user.setAge("20");
				user.setHeigh("1.80");
				list.add(user);
			}
			String fileName="test.xlsx";
			Long startTime = System.currentTimeMillis();
			poi.getExcelCreate(fileName);//创建excel
			poi.v_index = poi.getTermTitleStyle(configs);
			poi.getDetailTitle(configs,poi.v_index);//创建明细项标题
			poi.getExcelValue(configs,list, null);//excel写值
			System.out.println(System.currentTimeMillis()-startTime);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(output!=null){
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
