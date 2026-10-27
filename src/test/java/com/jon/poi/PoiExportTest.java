package com.jon.poi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PoiExportTest {

    private Workbook wb;

    @Before
    public void setUp() {
        wb = new HSSFWorkbook();
    }

    @After
    public void tearDown() throws Exception {
        wb.close();
    }

    @Test
    public void twoArgCellConfigDefaultsToStringType() {
        // 修复前 cellType 为 null，导出时 NPE
        assertEquals(CellTypeEnum.String, new CellConfig("列名", "field").getCellType());
    }

    @Test
    public void moneyFormattingConsistentBetweenDetailAndTotal() {
        Sheet sheet = wb.createSheet();
        Row row = sheet.createRow(0);
        Cell detail = row.createCell(0);
        Cell total = row.createCell(1);
        CellStyleControl factory = new CellStyleControl(wb);

        SetExcelValue.setValueByType(detail, CellTypeEnum.Money, "12345");
        SetExcelValue.setValueAndStyleByType(factory, total, CellTypeEnum.Money, "12345", wb.createCellStyle());

        // 两个入口都必须除以 100，否则合计行会是明细的 100 倍
        assertTrue(Math.abs(detail.getNumericCellValue() - 123.45) < 0.001);
        assertTrue(Math.abs(total.getNumericCellValue() - 123.45) < 0.001);
    }
}
