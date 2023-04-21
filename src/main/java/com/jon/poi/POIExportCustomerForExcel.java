package com.jon.poi;

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
    // 行数
    private int vIndex = 0;
    private Sheet hs;
    private CellStyleControl styleFactory;
    private OutputStream output;
    private String newFileName;
    private Map<String, CellStyle> cellStylemap = new HashMap<>();

    public POIExportCustomerForExcel(Workbook wb, OutputStream output) {
        this.wb = wb;
        this.output = output;
    }

    public void getExcelCreate(String fileName) {
        // response.reset();
        /* ===== 1.设置响应头 ===== */
        // setReaponseHeader(request,response,fileName);
        /* ===== 2.创建HSSFWorkbook ===== */
        newFileName = fileName.substring(0, fileName.lastIndexOf("."));
        // 创建工作薄
        hs = wb.createSheet(newFileName);
        // 创建样式工程
        styleFactory = new CellStyleControl(wb);
    }

    public Integer getTermTitleStyle(List<CellConfig> configs) {
        // 创建标题项
        Row titleRow = hs.createRow(vIndex);
        for (int i = 0; i < configs.size(); i++) {
            titleRow.createCell(i);
            hs.setColumnWidth(i, (short)(256 * 12));
            titleRow.getCell(i).setCellStyle(styleFactory.getTitleStyle());
        }
        // 合并标题栏
        hs.addMergedRegion(new CellRangeAddress(0, 0, 0, configs.size() - 1));
        titleRow.getCell(0).setCellValue(newFileName);
        titleRow.setHeight((short)(256 * 2));
        vIndex += 1;
        return vIndex;
    }

    /**
     * 创建明细项标题
     * 
     * @param configs
     * @param vIndex
     */
    public void getDetailTitle(List<CellConfig> configs, int vIndex) {
        // 创建明细项标题(表头)
        // v_index += 1;
        Row detailTitle = hs.createRow(vIndex);
        detailTitle.setHeight((short)(128 * 3));
        int col = 0;
        for (CellConfig conf : configs) {
            // 创建cell
            Cell cell = detailTitle.createCell(col++);
            // 根据类型添加样式
            cell.setCellStyle(styleFactory.getHeaderStyle());
            // 添加值
            cell.setCellValue(conf.getColName());
        }
    }

    /**
     * 生成列表数据
     * 
     * @param configs
     * @param xycList
     * @param total
     * @param vIndex
     * @throws IOException
     */
    public <T> void getExcelValue(List<CellConfig> configs, List<T> xycList, LinkedHashMap<Integer, Object[]> total)
        throws IOException {
        CellStyle cellStyle = wb.createCellStyle();
        // 生成列表数据
        for (int index = 0; index < xycList.size(); index++) {
            vIndex += 1;
            Row row = hs.createRow(vIndex);
            row.setHeight((short)(128 * 3));

            for (int i = 0; i < configs.size(); i++) {
                row.createCell(i);
                try {
                    // 样式
                    if (cellStylemap.get(configs.get(i).getCellType().toString()) != null) {
                        cellStyle = cellStylemap.get(configs.get(i).getCellType().toString());
                    } else {
                        cellStyle = wb.createCellStyle();
                        cellStyle = styleFactory
                            .getStyleByType(CellTypeEnum.valueOf(configs.get(i).getCellType().toString()), cellStyle);
                        cellStylemap.put(configs.get(i).getCellType().toString(), cellStyle);
                    }
                    if (cellStyle != null) {
                        row.getCell(i).setCellStyle(cellStyle);
                    }
                    // 赋值
                    SetExcelValue.setTheCellValue(styleFactory, row.getCell(i), index, xycList.get(index),
                        configs.get(i));
                } catch (SecurityException | IllegalArgumentException | NoSuchMethodException | IllegalAccessException
                    | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        // 合计行
        if (total != null) {
            vIndex += 1;
            Row row = hs.createRow(vIndex);
            row.setHeight((short)(128 * 3));
            for (int i = 0; i < configs.size(); i++) {
                Cell cell = row.createCell(i);
                // 根据类型添加样式
                cell.setCellStyle(styleFactory.getCenterStyle());
                if (i == 0) {
                    cell.setCellValue("合计");
                }
            }
            for (Entry<Integer, Object[]> entry : total.entrySet()) {
                Cell cell = row.getCell(entry.getKey());
                // 添加值
                SetExcelValue.setValueAndStyleByType(styleFactory, cell,
                    CellTypeEnum.valueOf(entry.getValue()[1].toString()), entry.getValue()[0], cellStyle);
            }
        }
        // 调整每列宽度自适应
        for (int j = 0; j < configs.size(); j++) {
            // hs.autoSizeColumn(j,true);//SXSSFWorkbook自适应报错
            hs.setColumnWidth(j, configs.get(j).getFieldName().length() * 2 * 256);
        }
        wb.write(output);
    }

    public static void main(String[] args) {

        try (FileOutputStream output = new FileOutputStream("d:/test.xlsx");) {
            Workbook wb = new SXSSFWorkbook(100);
            POIExportCustomerForExcel poi = new POIExportCustomerForExcel(wb, output);
            List<CellConfig> configs = new ArrayList<>();
            configs.add(new CellConfig("姓名", "name", CellTypeEnum.String));
            configs.add(new CellConfig("年龄", "age", CellTypeEnum.Number));
            configs.add(new CellConfig("身高", "heigh", CellTypeEnum.String));
            List<User> list = new ArrayList<User>();
            for (int i = 0; i < 10000; i++) {
                User user = new User();
                user.setName("jone");
                user.setAge("20");
                user.setHeigh("1.80");
                list.add(user);
            }
            String fileName = "test.xlsx";
            Long startTime = System.currentTimeMillis();
            // 创建excel
            poi.getExcelCreate(fileName);
            // poi.v_index = poi.getTermTitleStyle(configs);
            // 创建明细项标题
            poi.getDetailTitle(configs, poi.vIndex);
            // excel写值
            poi.getExcelValue(configs, list, null);
            System.out.println(System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
