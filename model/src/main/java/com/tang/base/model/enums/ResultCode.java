package com.tang.base.model.enums;

public enum ResultCode {
	SUCCESS(1, "操作成功"), FAILURE(-1, "操作失败"), NO_DATA_FOUND(101, "没找到数据"), SERVICE_ERROR(500, "服务器内部错误");

	private Integer code;
	private String desc;

	private ResultCode(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

}
