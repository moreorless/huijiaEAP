package com.huijia.eap.auth.authentication;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huijia.eap.commons.i18n.Bundle;
import com.huijia.eap.commons.mvc.view.exhandler.ExceptionWrapper;
import com.huijia.eap.commons.mvc.view.exhandler.ExceptionWrapper.EC;

/**
 * 用于Web的认证器基类，<b>需要用于Web认证的认证器都必须集成自此类</b>
 *
 */
public abstract class WebAuthenticatorWrapper extends AuthenticatorWrapper {
	
	protected ServletContext sc;

	/**
	 * 获取当前所在的ServletContext对象
	 * @return
	 */
	public ServletContext getServletContext() {
		return sc;
	}

	public void setServletContext(ServletContext sc) {
		this.sc = sc;
	}
	
	/**
	 * 获取HttpServletRequest对象，<b>一般用于{@link #authenticate(Map)}方法中</b>
	 * @param info
	 * @return
	 */
	protected HttpServletRequest getRequest(Map<String, Object> info) {
		Object value = info.get(HttpServletRequest.class.getName());
		if (value != null && HttpServletRequest.class.isAssignableFrom(value.getClass())) {
			return (HttpServletRequest) value;
		}
		
		return null;
	}
	
	/**
	 * 获取HttpServletResponse对象，<b>一般用于{@link #authenticate(Map)}方法中</b>
	 * @param info
	 * @return
	 */
	protected HttpServletResponse getResponse(Map<String, Object> info) {
		Object value = info.get(HttpServletResponse.class.getName());
		if (value != null && HttpServletResponse.class.isAssignableFrom(value.getClass())) {
			return (HttpServletResponse) value;
		}
		
		return null;
	}

	@Override
	public void init() {
		super.init();
		sc.setAttribute(TYPE_KEY, getType());
	}

	@Override
	public boolean authenticate(Map<String, Object> info) {
		HttpServletRequest request = getRequest(info);
		HttpServletResponse response = getResponse(info);
		if (request == null || response == null)
			return false;
		boolean isREST = isREST(request, info);
		if (isREST)
			info = parseRESTInfos(request, info);
		if (!isREST && !"POST".equals(request.getMethod())) {
			EC error = new EC("auth.signin.errors.csrfprevent", new Bundle("auth"));
			throw ExceptionWrapper.wrapError(error);
		}
		
		//避免记录日志时调用getOutputStream
		info.remove(HttpServletRequest.class.getName());
		info.remove(HttpServletResponse.class.getName());
		return concreteAuthenticate(info);
	}
	
	/**
	 * 实际的认证实现，无需再实现{@link IAuthenticator#authenticate(Map)}
	 * @param info
	 * @return
	 */
	protected abstract boolean concreteAuthenticate(Map<String, Object> info);
	
	/**
	 * 当前认证请求是否为REST方式（即URL直接登录方式）
	 * @param request
	 * @param info
	 * @return
	 */
	protected abstract boolean isREST(HttpServletRequest request, Map<String, Object> info);
	
	/**
	 * 判断参数名是否为REST方式时使用的参数
	 * @param paramName
	 * @return
	 */
	public abstract boolean isRESTParam(String paramName);
	
	/**
	 * 解析REST方式的参数，并更新到认证所需信息表中
	 * @param request
	 * @param info
	 * @return
	 */
	protected abstract Map<String, Object> parseRESTInfos(HttpServletRequest request, Map<String, Object> info);

}
