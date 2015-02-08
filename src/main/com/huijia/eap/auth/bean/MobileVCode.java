package com.huijia.eap.auth.bean;

import java.util.Date;

/**
 * 手机校验码
 * @author leon
 *
 */
public class MobileVCode {
	private String mobile; 			// 手机号
	private String validCode;	 	// 手机校验码
	private long timestamp;   			// 生成校验码的时间
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getValidCode() {
		return validCode;
	}
	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
}
