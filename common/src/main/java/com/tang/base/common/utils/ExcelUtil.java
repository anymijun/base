package com.tang.base.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	public static final String OFFICE_2003 = ".xls";

	public static final String OFFICE_2007 = ".xlsx";

	public static Workbook getWorkbook(String tplPath) throws IOException {
		InputStream fileInput = null;
		Workbook workBook = null;
		fileInput = new FileInputStream(tplPath);
		String postfix = tplPath.substring(tplPath.lastIndexOf("."));
		if (postfix.endsWith(ExcelUtil.OFFICE_2003)) {
			workBook = new HSSFWorkbook(new POIFSFileSystem(fileInput));
		} else if (postfix.endsWith(ExcelUtil.OFFICE_2007)) {
			workBook = new XSSFWorkbook(fileInput);
		}
		return workBook;
	}

	public static void readAndWrite(String tplPath,int col1,int col2,int col3, String desFile) throws IOException {
		OutputStream outputStream = new FileOutputStream(new File(desFile));
		Workbook workBook = getWorkbook(tplPath);
		Sheet sheet = workBook.getSheetAt(0);
		int rowNum = 1;
		Row row = sheet.getRow(rowNum);
		
		while (row != null) {
			try {
				Cell cell1 = row.getCell(col1);
				if (cell1 != null) {
					String val = ShortDataCryptUtil.decrypt(cell1.getStringCellValue());
					System.out.println(val);
					cell1.setCellValue(val);
				}
				Cell cell2 = row.getCell(col2);
				if (cell2 != null) {
					String val = ShortDataCryptUtil.decrypt(cell2.getStringCellValue());
					System.out.println(val);
					cell2.setCellValue(val);
				}
				Cell cell3 = row.getCell(col3);
				if (cell3 != null) {
					String val = ShortDataCryptUtil.decrypt(cell3.getStringCellValue());
					System.out.println(val);
					cell3.setCellValue(val);
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rowNum++;
			row = sheet.getRow(rowNum);
		}
		try {
			workBook.write(outputStream);
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

	public static void main(String[] args) {
		try {
			ExcelUtil.readAndWrite("D:\\test\\phones.xls",1,2,3, "D:\\test\\phones_bak.xls");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
