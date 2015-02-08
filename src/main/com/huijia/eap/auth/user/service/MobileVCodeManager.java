package com.huijia.eap.auth.user.service;

import java.util.HashMap;
import java.util.Map;

import com.huijia.eap.auth.bean.MobileVCode;

public class MobileVCodeManager {

	private static MobileVCodeManager _instance = new MobileVCodeManager();
	
	private int TIMEOUT = 300;
	
	private MobileVCodeManager(){}
	
	private Map<String, MobileVCode> cache = new HashMap<String, MobileVCode>();
	
	public static MobileVCodeManager me(){
		return _instance;
	}
	
	public void addVCode(MobileVCode code){
		if(code == null) return;
		this.cache.put(code.getMobile(), code);
	}
	
	/**
	 * 检查校验码是否有效
	 * @param code
	 * @return
	 */
	public boolean checkVCode(MobileVCode code){
		if(code == null) return false;
		
		MobileVCode oldVCode = cache.get(code.getMobile());
		if(oldVCode == null) return false;
		
		// 校验码不匹配
		if(!oldVCode.getValidCode().equals(code.getValidCode())) return false;
		
		// 超时
		if(System.currentTimeMillis() - oldVCode.getTimestamp() > TIMEOUT * 1000){
			return false;
		}
		
		return true;
	}
}
