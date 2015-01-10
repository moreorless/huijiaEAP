package com.huijia.eap.auth.matcher;

import org.nutz.lang.Lang;

import com.huijia.eap.auth.bean.Permission;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.matcher.method.PermissionMethod;


public class ExactPermissionMatcher implements PermissionMatcher {

	@Override
	public boolean match(Permission required, Permission matching, User currentUser) {
		return required != null && matching != null
				&& Lang.equals(required.getModule(), matching.getModule())
				&& Lang.equals(required.getAction(), matching.getAction())
				&& Lang.equals(required.getResource(), matching.getResource());
	}

	@Override
	public PermissionMethod[] getMethods() {
		return null;
	}

}
