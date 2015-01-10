package com.huijia.eap.auth.matcher;

import com.huijia.eap.auth.bean.Permission;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.matcher.method.PermissionMethod;


/**
 * 许可验证器接口
 * @author liunan
 *
 */
public interface PermissionMatcher {

	/**
	 * 验证
	 * @param required 请求的许可对象
	 * @param matching 已具有的许可对象
	 * @param currentUser 当前登录用户
	 * @return
	 */
	public boolean match(Permission required, Permission matching, User currentUser);
	
	/**
	 * 获取本验证器支持的验证方法集合
	 * @return 返回null则表明本验证方法不支持方法定义
	 */
	public PermissionMethod[] getMethods();
}
