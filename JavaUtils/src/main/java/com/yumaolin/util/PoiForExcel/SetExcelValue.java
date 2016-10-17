package com.yumaolin.util.PoiForExcel;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;


public class SetExcelValue {
	/**
	 * 设置单元格的值2
	 * 
	 */
	public static void setTheCellValue(CellStyleControl styleFactory,Cell cell , int index, Object object, CellConfig config) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if("index".equals(config.getColName())) {
			cell.setCellStyle(styleFactory.getCenterStyle());
			cell.setCellValue(index+1);
		} else {
			Object value = null;
			if(object instanceof Map){
				Map map = (Map) object;
				value = map.get(config.getFieldName().toUpperCase());
			}else{
				String methodName = config.getFieldName().substring(0, 1).toUpperCase().concat(config.getFieldName().substring(1));
				value = object.getClass().getMethod("get"+methodName).invoke(object);
			}
			if(value!=null && !"".equals(value.toString())){
				//判断是否有值映射
				if(config.getValueMap()!=null){
					value = config.getValueMap().get(value);
					setValueByType(cell,config.getCellType(),value);
				}else{
					setValueByType(cell,config.getCellType(),value);
				}
			} else {
				cell.setCellValue("");
			}
		}
	}
	/**
	 * 根据类型来赋值，数据格式化
	 * @param cell
	 * @param type
	 * @param value
	 */
	private static void setValueByType(Cell cell,CellType type, Object value) {
		switch(type) {
		case Money : 
			//金额数值统一做除100处理
			cell.setCellValue(Double.valueOf(value.toString())/100.0);
			break;
		case Percent : cell.setCellValue(Double.valueOf(value.toString())); break;
		case Date : 
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				cell.setCellValue(sdf.parse(value.toString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;
		case Number : 
			cell.setCellValue(Double.parseDouble(value.toString()));
			break;
		default: cell.setCellValue(value.toString());
		}
	}
	/**
	 * 根据类型来复制和样式
	 * @param styleFactory 
	 * @param cell
	 * @param type
	 * @param value
	 */
	public static void setValueAndStyleByType(CellStyleControl styleFactory,Cell cell,CellType type, Object value,CellStyle cellStyle) {
		cell.setCellStyle(styleFactory.getStyleByType(type,cellStyle));
		switch(type) {
		case Money :
			cell.setCellValue(Double.valueOf(value.toString()));
			break;
		case Percent : 
			cell.setCellValue(Double.valueOf(value.toString()));
			break;
		default: cell.setCellValue(value.toString());
		}
	}
}
