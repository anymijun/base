package com.tang.base.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tang.base.model.enums.ResultCode;
import com.tang.base.web.common.WebResult;

public class BaseController {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public WebResult getOkResult() {
		WebResult message = new WebResult();
		message.setCode(ResultCode.SUCCESS.getCode());
		return message;
	}

	public WebResult getOkResult(Object data) {
		WebResult message = new WebResult();
		message.setCode(ResultCode.SUCCESS.getCode());
		message.setData(data);
		return message;
	}

	public WebResult getErrorMsgResult(Integer code, String msg) {
		WebResult message = new WebResult();
		message.setCode(code);
		message.setMsg(msg);
		return message;
	}

	public WebResult getErrorMsgResult(ResultCode resultCode) {
		WebResult message = new WebResult();
		message.setCode(resultCode.getCode());
		message.setMsg(resultCode.getDesc());
		return message;
	}

}
