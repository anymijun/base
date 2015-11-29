package com.tang.base.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>();

	private static final Object object = new Object();
	
	public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	
	public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

	public static final String FORMAT_YYYYMMDD = "yyyyMMdd";

	public static final String MOMENT_START = "00:00:00";

	public static final String MOMENT_END = "23:59:59";

	/**
	 * 获取SimpleDateFormat
	 * 
	 * @param pattern
	 *            日期格式
	 * @return SimpleDateFormat对象
	 * @throws RuntimeException
	 *             异常：非法日期格式
	 */
	public static SimpleDateFormat getDateFormat(String pattern) throws RuntimeException {
		SimpleDateFormat dateFormat = threadLocal.get();
		if (dateFormat == null) {
			synchronized (object) {
				if (dateFormat == null) {
					dateFormat = new SimpleDateFormat(pattern);
					dateFormat.setLenient(false);
					threadLocal.set(dateFormat);
				}
			}
		}
		dateFormat.applyPattern(pattern);
		return dateFormat;
	}

	/**
	 * 获取日期时间字符串
	 * 
	 * @param date
	 *            date
	 * @return string like 'yyyy-MM-dd HH:mm:ss'
	 */
	private static SimpleDateFormat sdfYMDHMS = DateUtil.getDateFormat(FORMAT_YYYY_MM_DD_HH_MM_SS);

	public static String getDateString(Date date) {
		return sdfYMDHMS.format(date);
	}

	private static SimpleDateFormat sdfYMDSimple = new SimpleDateFormat(FORMAT_YYYYMMDD);

	public static String getYMD(Date date) {
		return sdfYMDSimple.format(date);
	}


}
