/**
 
 * author: liunan
 * created: 2013-1-28
 */
package com.huijia.eap.auth.authentication;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.lang.Strings;

import com.huijia.eap.auth.user.service.UserService;
import com.huijia.eap.commons.i18n.Bundle;
import com.huijia.eap.commons.mvc.view.exhandler.ExceptionWrapper;
import com.huijia.eap.commons.mvc.view.exhandler.ExceptionWrapper.EC;


public class DefaultAuthenticator extends WebAuthenticatorWrapper {
	
	private UserService service;

	public UserService getService() {
		return service;
	}

	public void setService(UserService service) {
		this.service = service;
	}

	@Override
	public String getType() {
		return "default";
	}

	@Override
	protected boolean concreteAuthenticate(Map<String, Object> info) {
		String username = (String) info.get(INFO_KEY_USERNAME);
		String password = (String) info.get(INFO_KEY_PASSWORD);
		boolean encrypted = false;
		if (info.containsKey(INFO_KEY_PWD_ENCRYPTED))
			encrypted = (Boolean) info.get(INFO_KEY_PWD_ENCRYPTED);
		
		if (Strings.isEmpty(username) || Strings.isEmpty(password)) {
			EC error;
			if (Strings.isEmpty(username) && Strings.isEmpty(password))
				error = new EC("auth.signin.errors.input", new Bundle("auth"));
			else if (Strings.isEmpty(username))
				error = new EC("auth.signin.errors.input.username", new Bundle("auth"));
			else
				error = new EC("auth.signin.errors.input.password", new Bundle("auth"));
			throw ExceptionWrapper.wrapError(error);
		}
		return service.checkUser(username, password, encrypted) != null;
	}

	@Override
	protected boolean isREST(HttpServletRequest request, Map<String, Object> info) {
		String username = (String) info.get(INFO_KEY_USERNAME);
		String u = request.getParameter("u");
		String p = request.getParameter("p");
		return (username == null || username.trim().length() == 0) && u != null && u.trim().length() > 0 && p != null && p.trim().length() > 0;
	}

	@Override
	protected Map<String, Object> parseRESTInfos(HttpServletRequest request,
			Map<String, Object> info) {
		String u = request.getParameter("u");
		String p = request.getParameter("p");
		HttpServletResponse response = getResponse(info);
		// 解决ie浏览器跨域session丢失问题 参考 http://msdn.microsoft.com/en-us/library/ms537341.aspx
		response.addHeader("P3P","CP=CAO PSA OUR");
		
		info.put(INFO_KEY_USERNAME, u);
		info.put(INFO_KEY_PASSWORD, p);
		info.put(INFO_KEY_PWD_ENCRYPTED, true);
		return info;
	}

	@Override
	public boolean isRESTParam(String paramName) {
		return "u".equals(paramName) || "p".equals(paramName);
	}

}
