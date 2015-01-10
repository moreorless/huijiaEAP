package com.huijia.eap.auth.authentication;

import java.util.Map;

import com.huijia.eap.auth.bean.User;


/**
 * 认证器接口
 *
 */
public interface IAuthenticator {
	
	public static final String INFO_KEY_USERNAME = "username";
	public static final String INFO_KEY_PASSWORD = "password";
	public static final String INFO_KEY_PWD_ENCRYPTED = "pwd-encrytped";
	
	public static final String TYPE_KEY = "CUPID.AUTHENTICATOR.TYPE";
	
	/**
	 * 初始化
	 */
	public void init();
	
	/**
	 * 认证器类型字符串，<b>必须全局唯一，不可使用"default"</b>
	 * @return
	 */
	public String getType();

	/**
	 * 认证
	 * @param info 认证所需的信息表
	 * @return
	 */
	public boolean authenticate(Map<String, Object> info);
	
	/**
	 * 认证成功后操作
	 * @param user 认证的对象
	 * @param info
	 */
	public void ok(User user, Map<String, Object> info);
	
	/**
	 * 认证失败后操作
	 * @param username 认证使用的用户名
	 * @param info
	 */
	public void fail(String username, Map<String, Object> info);
	
}
