package com.tang.base.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tang.base.model.enums.ResultCode;
import com.tang.base.web.common.exception.BusinessException;

@Controller
@RequestMapping("demo")
public class DemoController {

	@RequestMapping("1")
	public ModelAndView gotoDemoPage() {
		ModelAndView model = new ModelAndView();
		model.setViewName("demo");
		int num = (int) (Math.random() * 100);
		if (num % 2 == 0) {
			throw new BusinessException(ResultCode.NO_DATA_FOUND.getCode(), "访问的数据不存在");
		}
		return model;
	}
}
