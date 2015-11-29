package com.tang.base.web.resolver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.tang.base.model.enums.ResultCode;
import com.tang.base.web.common.WebResult;
import com.tang.base.web.common.exception.BusinessException;

public class IntegratedExceptionHandler implements HandlerExceptionResolver, Ordered {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		logger.error(ex.getMessage(), ex);
		String ajax = request.getHeader("x-requested-with");
		if (StringUtils.isNotBlank(ajax) && ajax.indexOf("XMLHttpRequest") != -1) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(200);
			WebResult result = new WebResult();
			if (ex instanceof BusinessException) {
				BusinessException biz = (BusinessException) ex;
				result.setCode(biz.getCode());
				result.setMsg(biz.getMessage());
			} else {
				result.setCode(500);
				result.setMsg(ex.getMessage());
			}
			try {
				response.getWriter().write(JSON.toJSONString(result));
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			ModelAndView mav = new ModelAndView("/common/blank");
			return mav;// 必需返回视图，否则springmvc认为没有处理异常
		} else {
			if (ex instanceof BusinessException) {
				response.setStatus(200);
				ModelAndView mav = new ModelAndView("/common/error");
				BusinessException biz = (BusinessException) ex;
				mav.addObject("errCode", biz.getCode());
				mav.addObject("errDesc", biz.getMessage());
				return mav;
			} else {
				ByteArrayOutputStream fos = new ByteArrayOutputStream();
				ex.printStackTrace(new PrintStream(fos));
				String trace = fos.toString();
				ModelAndView mav = new ModelAndView("/common/error");
				mav.addObject("errCode", ResultCode.SERVICE_ERROR.getCode());
				mav.addObject("errDesc", ResultCode.SERVICE_ERROR.getDesc());
				mav.addObject("errTrace", trace);
				return mav;
			}
		}
	}

	public int getOrder() {
		return 0;
	}
}
