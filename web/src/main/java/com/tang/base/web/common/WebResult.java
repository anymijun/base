package com.tang.base.web.common;

import java.util.Calendar;

import com.tang.base.model.enums.ResultCode;
import com.tang.base.service.Constants;

public class WebResult {
	// 响应码
	private Integer code;

	// 错误信息
	private String msg;

	// 数据
	private Object data;

	public Integer getCode() {
		if (code == null) {
			return ResultCode.FAILURE.getCode();
		}
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	// 服务器启动时间戳，用来控制前端缓存
	public String getFeVersion() {
		return Constants.FE_VERSION;
	}

	// 服务器响应时间
	public long getServerTime() {
		return Calendar.getInstance().getTimeInMillis();
	}

}
