package com.tang.base.web.view;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.tang.base.common.utils.ExcelExportUtil;

public class ExcelView extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 模板文件名
		String tplPath = null;
		String tplName = model.get("tplName").toString();
		if (StringUtils.isNotBlank(tplName)) {
			tplPath = ExcelView.class.getClassLoader().getResource("/").getFile() + tplName;
		}
		// 下载后显示名称
		String fileName = model.get("fileName").toString();
		String downloadName = getDownloadName(request, fileName);
		response.setContentType("application/x-download");
		response.addHeader("Content-Disposition", "attachment;filename=" + downloadName);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> dataList = (List<Map<String, Object>>) model.get("dataList");
		if (StringUtils.isNotEmpty(tplPath)) {
			ExcelExportUtil.exportExcelForMap(tplPath, dataList, response.getOutputStream());
		} else {
			ExcelExportUtil.exportExcelForMap(workbook, dataList, response.getOutputStream());
		}
	}


	public static String getDownloadName(HttpServletRequest request, String filename) throws Exception {
		String Agent = request.getHeader("User-Agent");
		if (null != Agent) {
			Agent = Agent.toLowerCase();
			if (Agent.indexOf("firefox") != -1) {
				filename = new String(filename.getBytes("utf-8"), "ISO8859-1");
			} else if (Agent.indexOf("msie") != -1) {
				filename = URLEncoder.encode(filename, "UTF-8");
			} else {
				filename = URLEncoder.encode(filename, "UTF-8");
			}
		}
		return filename;
	}
}
