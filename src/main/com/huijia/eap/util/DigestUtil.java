package com.huijia.eap.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 摘要工具
 * @author liunan
 *
 */
public class DigestUtil {
	
	/**
	 * 生成SHA摘要字节，使用本地编码
	 * @param text
	 * @return
	 */
	public static final byte[] encodeSHAToByte(String text) {
		return encode("SHA", text, null);
	}
	
	/**
	 * 生成SHA摘要字符串，使用本地编码
	 * @param text
	 * @return
	 */
	public static final String encodeSHA(String text) {
		return bytesToHex(encodeSHAToByte(text));
	}
	
	/**
	 * 使用指定编码生成SHA摘要字节
	 * @param text
	 * @param encoding
	 * @return
	 */
	public static final byte[] encodeSHAToByte(String text, String encoding) {
		return encode("SHA", text, encoding);
	}
	
	/**
	 * 使用指定编码生成SHA摘要字符串
	 * @param text
	 * @param encoding
	 * @return
	 */
	public static final String encodeSHA(String text, String encoding) {
		return bytesToHex(encodeSHAToByte(text, encoding));
	}
	
	/**
	 * 生成MD5摘要字节，使用本地编码
	 * @param text
	 * @return
	 */
	public static final byte[] encodeMD5ToByte(String text) {
		return encode("MD5", text, null);
	}
	
	/**
	 * 生成MD5摘要字符串，使用本地编码
	 * @param text
	 * @return
	 */
	public static final String encodeMD5(String text) {
		return bytesToHex(encodeMD5ToByte(text));
	}
	
	/**
	 * 使用指定编码生成MD5摘要字节
	 * @param text
	 * @param encoding
	 * @return
	 */
	public static final byte[] encodeMD5ToByte(String text, String encoding) {
		return encode("MD5", text, encoding);
	}
	
	/**
	 * 使用指定编码生成MD5摘要字符串
	 * @param text
	 * @param encoding
	 * @return
	 */
	public static final String encodeMD5(String text, String encoding) {
		return bytesToHex(encodeMD5ToByte(text, encoding));
	}
	
	private static final byte[] encode(String algorithm, String text, String encoding) {
		byte[] result = null;
		if (text != null) {
			try {
				MessageDigest digest = MessageDigest.getInstance(algorithm == null ? "MD5" : algorithm);
				result = digest.digest(encoding == null ? text.getBytes() : text.getBytes(encoding));
			} catch (NoSuchAlgorithmException e) {
			} catch (UnsupportedEncodingException e) {
			}
		}
		
		return result;
	}
	
	private static final String bytesToHex(byte[] bytes) {
		
		if (bytes == null)
			return null;
		
		StringBuilder sb = new StringBuilder();
		String hex;
		for (byte b : bytes) {
			hex = Integer.toHexString(b &0xFF);
			if (hex.length() == 1)
				sb.append("0");
			sb.append(hex.toLowerCase());
		}
		
		return sb.toString();
	}

}
