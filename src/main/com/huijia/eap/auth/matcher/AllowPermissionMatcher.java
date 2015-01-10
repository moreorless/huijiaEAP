package com.huijia.eap.auth.matcher;

import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.auth.bean.Permission;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.matcher.method.PermissionMethod;


/**
 * 全部允许匹配方法
 * @author liunan
 *
 */
@IocBean(name="ALLOW_PERMISSION_MATCHER")
public class AllowPermissionMatcher implements PermissionMatcher {

	@Override
	public boolean match(Permission required, Permission matching, User currentUser) {
		return true;
	}

	@Override
	public PermissionMethod[] getMethods() {
		return null;
	}
}
