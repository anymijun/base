package com.tang.base.service.aop;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.tang.base.utils.RedisUtil;
import com.tang.base.utils.RedisUtil.JedisAutoReturn;

@Aspect
@Component
public class CacheAspect implements Ordered {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String EXECUTION = "@annotation(com.tang.base.service.aop.Cache)";

	public CacheAspect() {
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Around(EXECUTION)
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		String clazz = pjp.getTarget().getClass().getSimpleName();
		String methodName = pjp.getSignature().getName();
		Object[] args = pjp.getArgs();
		logger.debug("Invoke {}.{}({})", clazz, methodName, args);

		Method method = ((MethodSignature) pjp.getSignature()).getMethod();
		Cache cache = (Cache) method.getAnnotation(Cache.class);
		byte[] cacheKeyb = null;
		String cacheKeys = cache.cacheKey();
		if (StringUtils.isBlank(cacheKeys)) {
			StringBuilder sb = new StringBuilder();
			sb.append(clazz).append(":").append(method).append(":").append(JSON.toJSONString(args));
			cacheKeyb = sb.toString().getBytes();
		} else {
			cacheKeyb = cacheKeys.getBytes();
		}

		int expire = cache.expire();
		JedisAutoReturn jedis = RedisUtil.getInstance().getJedisAutoReturn();
		byte[] arr = jedis.get(cacheKeyb);
		RedisObject ro = RedisObject.fromByteArray(arr);
		if (ro == null) {
			Object result = pjp.proceed();
			ro = new RedisObject((Serializable) result);
			jedis.set(cacheKeyb, ro.toByteArray());
			jedis.expire(cacheKeyb, expire);
			return result;
		} else {
			return ro.getObject();
		}
	}

	public int getOrder() {
		return 1;
	}
}
