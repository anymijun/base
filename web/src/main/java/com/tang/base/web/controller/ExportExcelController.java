package com.tang.base.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tang.base.service.Constants;
import com.tang.base.web.view.ExcelView;

@Controller
@RequestMapping("exportExcel")
public class ExportExcelController {
	@RequestMapping("user")
	public ModelAndView exportExcel() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("tplName", Constants.EXCEL_TPL_PATH.USER);
		model.put("fileName", "西天取经用户列表.xls");
		model.put("dataList", getDefaultDataList());
		return new ModelAndView(new ExcelView(), model);
	}

	private List<Map<String, Object>> getDefaultDataList() {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> user1 = new HashMap<String, Object>();
		user1.put("name", "孙悟空");
		user1.put("sex", "男");
		user1.put("age", "50");
		dataList.add(user1);
		Map<String, Object> user2 = new HashMap<String, Object>();
		user2.put("name", "猪八戒");
		user2.put("sex", "男");
		user2.put("age", "40");
		dataList.add(user2);

		Map<String, Object> user3 = new HashMap<String, Object>();
		user3.put("name", "沙和尚");
		user3.put("sex", "男");
		user3.put("age", "30");
		dataList.add(user3);
		return dataList;
	}

}
