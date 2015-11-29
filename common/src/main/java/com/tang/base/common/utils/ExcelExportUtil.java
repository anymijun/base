package com.tang.base.common.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelExportUtil {
	public static final String OFFICE_2003 = ".xls";

	public static final String OFFICE_2007 = ".xlsx";

	public static void exportExcelForMap(String tplPath, List<Map<String, Object>> dataList,
			OutputStream outputStream) {
		InputStream fileInput = null;
		Workbook workBook = null;
		try {
			fileInput = new FileInputStream(tplPath);
			String postfix = tplPath.substring(tplPath.lastIndexOf("."));
			if (postfix.endsWith(ExcelExportUtil.OFFICE_2003)) {
				workBook = new HSSFWorkbook(new POIFSFileSystem(fileInput));
			} else if (postfix.endsWith(ExcelExportUtil.OFFICE_2007)) {
				workBook = new XSSFWorkbook(fileInput);
			} else {
				throw new RuntimeException("模板文件格式不支持");
			}
			exportExcelForMap(workBook, dataList, outputStream);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("未找到模板文件");
		} catch (IOException e) {
			throw new RuntimeException("读取导出模板文件失败");
		} finally {
			if (fileInput != null) {
				try {
					fileInput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void exportExcelForMap(Workbook workBook, List<Map<String, Object>> dataList,
			OutputStream outputStream) {
		try {
			Sheet sheet = workBook.getSheetAt(0);
			int lastRowNum = sheet.getLastRowNum();
			Row row = sheet.getRow(lastRowNum);
			int rowNum = row.getLastCellNum();

			List<String> rowNames = new ArrayList<String>();
			List<CellStyle> styles = new ArrayList<CellStyle>();
			for (int i = 0; i < rowNum; i++) {
				Cell cell = row.getCell(i);
				rowNames.add(cell.getStringCellValue());
				styles.add(cell.getCellStyle());
			}
			if (dataList != null && dataList.size() > 0) {
				for (int i = 0; i < dataList.size(); i++) {
					Map<String, Object> rowData = dataList.get(i);
					rowData.put("NUM", lastRowNum + i);
					createRow(sheet, rowData, rowNames, styles, lastRowNum + i);
					rowData = null;
				}
			} else {
				sheet.removeRow(row);
			}
			workBook.write(outputStream);
			outputStream.flush();
		} catch (IOException e) {
			throw new RuntimeException("导出文件发生异常");
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					throw new RuntimeException("关闭导出文件流发生异常");
				}
			}
		}
	}

	private static void createRow(Sheet sheet, Map<String, Object> rowData, List<String> mapping,
			List<CellStyle> styles, int startRowNum) {
		Row row = sheet.createRow(startRowNum);
		for (int i = 0; i < mapping.size(); i++) {
			String name = mapping.get(i);
			Object obj = rowData.get(name);
			Cell newCell = row.createCell(i);
			CellStyle style = styles.get(i);
			newCell.setCellStyle(style);
			if (obj != null) {
				if (obj instanceof Date) {
					newCell.setCellValue((Date) obj);
				} else if (obj instanceof BigDecimal) {
					double dd = ((BigDecimal) obj).doubleValue();
					newCell.setCellValue(dd);
				} else {
					newCell.setCellValue(obj.toString());
				}
			}
		}
	}
}
