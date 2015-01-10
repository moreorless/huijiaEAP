package com.huijia.eap.auth;

import java.util.List;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.auth.bean.Permission;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.cache.AuthCache;
import com.huijia.eap.auth.matcher.ExactPermissionMatcher;
import com.huijia.eap.auth.matcher.PermissionMatcher;


@IocBean(name="authorizer")
public class DefaultAuthorizerImpl {
	
	private static final PermissionMatcher DEFAULT_MATCHER = new ExactPermissionMatcher();
	
	private AuthCache cache;
	
	private PermissionMatcher matcher;

	public DefaultAuthorizerImpl() {
		matcher = DEFAULT_MATCHER;
	}

	public AuthCache getCache() {
		return cache;
	}

	@Inject("refer:defaultAuthCache")
	public void setCache(AuthCache cache) {
		this.cache = cache;
	}

	public PermissionMatcher getMatcher() {
		return matcher;
	}

	@Inject("refer:defaultPermissionMatcher")
	public void setMatcher(PermissionMatcher matcher) {
		this.matcher = matcher;
	}

	public boolean hasPermission(User user, Permission permission) {
		return hasPermission(user, permission, matcher);
	}
	
	public boolean hasPermission(User user, Permission permission, PermissionMatcher matcher) {
		
		PermissionMatcher m = matcher;
		if (m == null)
			m = this.matcher;
		
		if (user != null && cache.isValidUser(user.getUserId())) {
			List<Permission> permissions = cache.getPermissionsByUser(user.getUserId());
			
			for (Permission matching : permissions) {
				if (m.match(permission, matching, user))
					return true;
			}
			
			if (permissions == null || permissions.isEmpty())
				return m.match(permission, null, user); //当用户没有角色时，处理module为core的请求
		}
		
		return false;
	}
}
