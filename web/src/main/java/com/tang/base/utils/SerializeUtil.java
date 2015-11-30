package com.tang.base.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerializeUtil {
	private static final Logger logger = LoggerFactory.getLogger(SerializeUtil.class);

	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			//序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T unserialize(byte[] bytes) {
		if (bytes == null)
			return null;
		ByteArrayInputStream bais = null;
		try {
			//反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (T) ois.readObject();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
}
