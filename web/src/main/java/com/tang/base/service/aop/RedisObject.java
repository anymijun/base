package com.tang.base.service.aop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisObject<T extends Serializable> implements Serializable {
	private static final long serialVersionUID = 8872785204451917054L;

	private static final Logger logger = LoggerFactory.getLogger(RedisObject.class);

	private long saveTime;

	private T object;

	public RedisObject(T object) {
		this.saveTime = System.currentTimeMillis();
		this.object = object;
	}

	public RedisObject(long saveTime, T object) {
		this.saveTime = saveTime;
		this.object = object;
	}

	public long getSaveTime() {
		return saveTime;
	}

	public T getObject() {
		return object;
	}

	public byte[] toByteArray() {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(this);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static RedisObject<? extends Serializable> fromByteArray(byte[] bytes) {
		if (bytes == null)
			return null;
		ByteArrayInputStream bais = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (RedisObject<?>) ois.readObject();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <E extends Serializable> RedisObject<E> fromByteArray(byte[] bytes, Class<E> clazz) {
		if (bytes == null)
			return null;
		ByteArrayInputStream bais = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (RedisObject<E>) ois.readObject();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

}
