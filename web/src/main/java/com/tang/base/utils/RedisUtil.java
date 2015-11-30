package com.tang.base.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tang.base.common.utils.PrimitiveType;

import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisUtil {
	private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

	public static interface RedisConstant {
		public String ONLY_SET_IF_NOT_EXIST = "NX";
		public String ONLY_SET_IF_EXIST = "XX";
		public String TIME_UNITS_SECONDS = "EX";
		public String TIME_UNITS_MILLISECONDS = "PX";
	}

	private String beanId;

	private RedisUtil(String beanId) {
		this.beanId = beanId;
	}

	public static RedisUtil getInstance() {
		return new RedisUtil("shardedJedisPool");
	}

//	public static RedisUtil getSessionInstance() {
//		return new RedisUtil("sessionShardedJedisPool");
//	}

	public static RedisUtil getInstance(String beanId) {
		return new RedisUtil(beanId);
	}

	/**
	 * 获得连接池
	 * 
	 * @return
	 */
	public ShardedJedisPool getPool() {
		return SpringContextUtil.getBean(beanId, ShardedJedisPool.class);
	}

	/**
	 * 返回ShardedJedis对象，该对象一直使用一个连接，注意最后调用完毕后需要自动释放连接。如下:
	 * 
	 * <PRE>
	 * ShardedJedis jedis =RedisUtil.getJedis();   
	try {  
	jedis.set("name", "minxr");  
	String ss = jedis.get("name");  
	System.out.println(ss);  
	jedis.del("name");  
	System.out.println(jedis.get("name"));
	…… 
	} catch (Exception e) {  
	e.printStackTrace();  
	} finally {  
	RedisUtil.returnJedis(jedis);  
	}
	 * </PRE>
	 * 
	 * @return
	 */
	public ShardedJedis getJedis() {
		return getPool().getResource();
	}

	public static void returnJedis(ShardedJedis jedis) {
		if (jedis != null)
			jedis.close();
	}

	public static interface JedisAutoReturn extends JedisCommands, BinaryJedisCommands {
		// Map<String, String> hgetAll(String key);
	}

	private static Map<Method, Method> cache = new HashMap<Method, Method>();

	private static Method getMethod(Method m) throws SecurityException, NoSuchMethodException {
		Method ret = cache.get(m);
		if (ret != null)
			return ret;

		Class<ShardedJedis> c = ShardedJedis.class;
		ret = c.getMethod(m.getName(), m.getParameterTypes());
		cache.put(m, ret);
		return ret;
	}

	/**
	 * 返回的JedisAutoReturn对象，每次执行方法后都会自动释放连接
	 * 
	 * @return
	 */
	public JedisAutoReturn getJedisAutoReturn() {
		Object proxy = Proxy.newProxyInstance(ShardedJedisPool.class.getClassLoader(),
				new Class[] { JedisAutoReturn.class }, new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						ShardedJedisPool pool = getPool();
						ShardedJedis jedis = null;
						try {
							jedis = pool.getResource();
							if (JedisAutoReturn.class != method.getDeclaringClass()) {
								return method.invoke(jedis, args);
							} else {
								return getMethod(method).invoke(jedis, args);
							}
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
							// 把redis当作缓存处理，若发生异常，catch住，使用者需要针对默认返回值作处理，一般穿透到数据库
							Class<?> c = method.getReturnType();
							if (c.isPrimitive()) {
								return PrimitiveType.getPrimitiveDefaultValue(c);
							} else {
								return null;
							}
						} finally {
							// System.out.println("结束");
							jedis.close();
						}
					}
				});
		return (JedisAutoReturn) proxy;
	}
}
