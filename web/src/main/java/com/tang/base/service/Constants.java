package com.tang.base.service;

import java.util.Date;
import com.tang.base.common.utils.DateUtil;

public class Constants {
	public static final String FE_VERSION = DateUtil.getYMD(new Date());
	/**
	 * excel导出模板路径名
	 * @author tangliling
	 *
	 */
	public interface EXCEL_TPL_PATH {
		public static final String USER = "tpl/excel/user.xls";
	}
}
