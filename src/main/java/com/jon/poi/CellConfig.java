package com.jon.poi;

import java.util.Map;

public class CellConfig {

    private String colName;// 表头名称
    private String fieldName;// 对应实体字段名称
    private CellTypeEnum cellType;// 格式化类型
    private Map<String, String> valueMap;// 值映射类型

    /**
     * 
     * @param colName 列名
     * @param fieldName 实体对应字段名 必须有get方法
     */
    public CellConfig(String colName, String fieldName) {
        super();
        this.colName = colName;
		this.fieldName = fieldName;
    }

    /**
     * 
     * @param colName 列名
     * @param fieldName 字段名 必须有get方法
     * @param cellType 格式化类型名 CellType枚举类型
     */
    public CellConfig(String colName, String fieldName, CellTypeEnum cellType) {
        super();
        this.colName = colName;
		this.fieldName = fieldName;
        this.cellType = cellType;
    }

    /**
     * 
     * @param colName 列名
     * @param fieldName 实体字段名 必须有get方法
     * @param cellType 格式化类型
     * @param valueMap 键值映射，将filedName的值作为map的键转换，如 Map<String,String> map = new ....; map.put("01","正常")...
     */
    public CellConfig(String colName, String fieldName, CellTypeEnum cellType, Map<String, String> valueMap) {
        super();
        this.colName = colName;
		this.fieldName = fieldName;
        this.cellType = cellType;
        this.valueMap = valueMap;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
    }

    public CellTypeEnum getCellType() {
        return cellType;
    }

    public void setCellType(CellTypeEnum cellType) {
        this.cellType = cellType;
    }

    public Map<String, String> getValueMap() {
        return valueMap;
    }

    public void setValueMap(Map<String, String> valueMap) {
        this.valueMap = valueMap;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }
}
