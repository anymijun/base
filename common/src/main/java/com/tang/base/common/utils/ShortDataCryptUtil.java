package com.tang.base.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ShortDataCryptUtil {
	 public static String CRYPTCODE = "8UKN95GC4K9SC51V";
	/**
	 * 重载类，使用默认key
	 * 
	 * @param content：待加密内容，普通字符串
	 * @return 加密后的内容，进行byte2hex后的内容
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	public static String encrypt(String content) throws Exception, UnsupportedEncodingException {
		return Byte2Hex(encrypt(content.getBytes("utf-8"), CRYPTCODE.getBytes()));
	}

	/**
	 * 解密收到的十六进制字符串，重载类，使用默认key
	 * 
	 * @param content：十六进制字符串，使用前需要先进行hex2byte操作
	 * @return 解密后的明文字符串（UTF-8编码）
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	public static String decrypt(String content) throws Exception, UnsupportedEncodingException {
		byte[] buffer = decrypt(Hex2Byte(content), CRYPTCODE.getBytes());
		String result = new String(buffer, "utf-8");
		return result;
	}

	/**
	 *
	 * @param content：待加密内容，普通字符串
	 * @param password：十六进制key，使用前需要先进行hex2byte操作
	 * @return 加密后的内容，进行byte2hex后的内容
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	public static String encrypt(String content, String password) throws Exception, UnsupportedEncodingException {
		return Byte2Hex(encrypt(content.getBytes("utf-8"), password.getBytes()));
	}

	/**
	 * 解密收到的十六进制字符串
	 * 
	 * @param content：十六进制字符串，使用前需要先进行hex2byte操作
	 * @param password：十六进制key，使用前需要先进行hex2byte操作
	 * @return 解密后的明文字符串（UTF-8编码）
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	public static String decrypt(String content, String password) throws Exception, UnsupportedEncodingException {
		byte[] buffer = decrypt(Hex2Byte(content), password.getBytes());
		String result = new String(buffer, "utf-8");
		return result;
	}

	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param password
	 *            加密密钥
	 * @return
	 */
	protected static byte[] encrypt(byte[] content, byte[] password) {
		return aesCrypt(Cipher.ENCRYPT_MODE, content, password);
	}

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param password
	 *            解密密钥
	 * @return
	 */
	protected static byte[] decrypt(byte[] content, byte[] password) {
		return aesCrypt(Cipher.DECRYPT_MODE, content, password);
	}

	/**
	 * AES加密/解密
	 * 
	 * @param content
	 *            需要 加密/解密 的内容
	 * @param password
	 *            加密/解密 密钥
	 * @return
	 */
	protected static byte[] aesCrypt(int mode, byte[] content, byte[] password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom sRandom = SecureRandom.getInstance("SHA1PRNG");
			sRandom.setSeed(password);
			kgen.init(128, sRandom);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");

			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(mode, key);

			byte[] result = cipher.doFinal(content);// 加密/解密
			return result;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String Byte2Hex(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] Hex2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;

	}
}
