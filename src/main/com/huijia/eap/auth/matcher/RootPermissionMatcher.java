package com.huijia.eap.auth.matcher;

import com.huijia.eap.auth.bean.Permission;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.matcher.method.PermissionMethod;

/**
 * <b>Warning：不要随意使用!!!</b>
 * <p>供调用调试和运行状态监控接口使用，仅允许ROOT用户执行</p>
 * @author liunan
 *
 */
public class RootPermissionMatcher implements PermissionMatcher {

	@Override
	public boolean match(Permission required, Permission matching,
			User currentUser) {
		
		if (currentUser != null && currentUser.getType() == User.TYPE_ROOT)
			return true;
		
		return false;
	}

	@Override
	public PermissionMethod[] getMethods() {
		return null;
	}

}
