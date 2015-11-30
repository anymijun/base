package com.tang.base.web.controller;

import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tang.base.model.enums.ResultCode;
import com.tang.base.utils.RedisUtil;
import com.tang.base.utils.SerializeUtil;
import com.tang.base.web.common.WebResult;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

@Controller
@RequestMapping("cache")
public class CacheController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheController.class);

	@ResponseBody
	@RequestMapping(value = "/cleanup/{regExg}", method = RequestMethod.GET)
	public WebResult cleanup(@PathVariable String regExg) {
		String[] regExies = regExg.indexOf(",") == -1 ? new String[] { regExg } : regExg.split(",");
		ShardedJedis sharedJedis = RedisUtil.getInstance().getJedis();
		try {
			Collection<Jedis> jedises = sharedJedis.getAllShards();
			for (Jedis jedis : jedises) {
				for (int i = 0, len = regExies.length; i < len; i++) {
					Set<String> keys = jedis.keys(regExies[i]);
					for (String key : keys) {
						LOGGER.info("delete cache key - {}", key);
						RedisUtil.getInstance().getJedisAutoReturn().del(key);
					}
				}
				jedis.close();
			}
		} finally {
			RedisUtil.returnJedis(sharedJedis);
		}
		return getOkResult();
	}

	@ResponseBody
	@RequestMapping(value = "/get/{key}", method = RequestMethod.GET)
	public WebResult get(@PathVariable String key) {
		WebResult result = new WebResult();
		byte[] value = RedisUtil.getInstance().getJedisAutoReturn().get(key.getBytes());
		if (null != value && value.length > 0) {
			Object object = SerializeUtil.unserialize(value);
			result.setData(object);
		} else {
			LOGGER.info("value is null");
		}
		result.setCode(ResultCode.SUCCESS.getCode());
		return result;
	}
}
