package com.tang.base.common.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tang.base.common.utils.ExcelExportUtil;

import junit.framework.TestCase;

public class ExcelExportUtilTest extends TestCase {

	public void testExportExcel1() {
		String tplpath = "D:\\test\\test1_tpl.xls";
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> user1 = new HashMap<String, Object>();
		user1.put("name", "猪八戒");
		user1.put("sex", "男");
		user1.put("age", "50");
		dataList.add(user1);
		Map<String, Object> user2 = new HashMap<String, Object>();
		user2.put("name", "孙悟空");
		user2.put("sex", "男");
		user2.put("age", "10");
		dataList.add(user2);
		
		Map<String, Object> user3 = new HashMap<String, Object>();
		user3.put("name", "沙和尚");
		user3.put("sex", "男");
		user3.put("age", "130");
		dataList.add(user3);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("D:\\test\\test1.xls");
			ExcelExportUtil.exportExcelForMap(tplpath, dataList, fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
