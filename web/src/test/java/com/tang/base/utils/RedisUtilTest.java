package com.tang.base.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tang.base.utils.RedisUtil.JedisAutoReturn;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-common.xml" })
public class RedisUtilTest {
	@Test
	public void testAdd() {
		byte[] key = "redisUtilTest1".getBytes();
		JedisAutoReturn jedisAutoReturn = RedisUtil.getInstance().getJedisAutoReturn();
		String str = jedisAutoReturn.set(key, "value".getBytes());
		System.out.println("set result :" + str);
		byte[] val = jedisAutoReturn.get(key);
		System.out.println(new String(val));
	}

}
