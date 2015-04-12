package com.huijia.eap.auth.data;

import java.io.Serializable;

/**
 * 用户认证策略配置
 */
public class UserCheckBean implements Serializable{
	
	public static final String CUPID_CONTEXT_KEY = "user-check-conf";
	
	/**
	 * @param validates  是否支持多种认证方式
	 * @param timeLock   认证时间锁定
	 * @param countLock  认证次数锁定
	 */
	public UserCheckBean(){
		
	}
	public UserCheckBean(int timeLock,int countLock, int sessionLock){
		this.timeLock = timeLock;
		this.countLock = countLock;
		this.sessionLock = sessionLock;
	}
	
	private int timeLock = 5;
	
	private int countLock = 10;
	
	private int sessionLock;

	public int getTimeLock() {
		return timeLock;
	}

	public void setTimeLock(int timeLock) {
		this.timeLock = timeLock;
	}

	public int getCountLock() {
		return countLock;
	}

	public void setCountLock(int countLock) {
		this.countLock = countLock;
	}
	public int getSessionLock() {
		return sessionLock;
	}
	public void setSessionLock(int sessionLock) {
		this.sessionLock = sessionLock;
	}

}
