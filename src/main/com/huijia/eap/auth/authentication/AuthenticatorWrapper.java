package com.huijia.eap.auth.authentication;

import java.util.Map;

import org.apache.log4j.Logger;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;

import com.huijia.eap.auth.bean.User;

/**
 * 认证器包装类，提供认证器缺省的实现——仅记录日志，无任何实质操作
 *
 */
public abstract class AuthenticatorWrapper implements IAuthenticator {
	
	private static final Logger logger = Logger.getLogger(AuthenticatorWrapper.class);
	
	@Override
	public void init() {
		if (logger.isDebugEnabled())
			logger.debug("Authenticator[" + getClass() + " : " + getType() + "] initializing...");
	}

	@Override
	public void ok(User user, Map<String, Object> info) {
		if (logger.isDebugEnabled())
			logger.debug("user[" + user.getName() + "] authenticated OK, with authenticate infos: " + Json.toJson(info, JsonFormat.compact()));
	}

	@Override
	public void fail(String username, Map<String, Object> info) {
		if (logger.isDebugEnabled())
			logger.debug("user[" + username + "] authenticated Failed, with authenticate infos: " + Json.toJson(info, JsonFormat.compact()));
	}

}
