package com.huijia.eap.commons.mvc.view.exhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.huijia.eap.commons.i18n.Bundle;

public abstract class ExceptionWrapper {
	
	public static final String ERROR_KEY = "CUPID_ERRORS";
	public static final String MESSAGE_KEY = "CUPID_MESSAGES";

	/**
	 * 错误代码类
	 *
	 */
	public static class EC  {//short for ErrorCode
		
		/**
		 * <ul>
		 * <li>当resource=true时，资源键值，即对应资源文件中=前的部分</li>
		 * <li>当resource=false时，错误描述</li>
		 * </ul>
		 */
		protected String key;
		
		/**
		 * 资源所在包<br/>
		 * <b>仅当resource=true时有效</b>
		 */
		protected Bundle bundle;
		
		/**
		 * 是否为资源，如果是则需要从资源文件中获取实际错误描述
		 */
		protected boolean resource = true;
		
		/**
		 * 资源参数<br/>
		 * <b>仅当resource=true时有效</b>
		 */
		protected Object[] args;
		
		/**
		 * 用于生成资源型错误代码
		 * @param key
		 * @param args
		 */
		public EC(String key, Object...args) {
			this.key = key;
			this.args = args;
		}
		
		public EC(String key, Bundle bundle, Object...args) {
			this.key = key;
			this.bundle = bundle;
			this.args = args;
		}
		
		/**
		 * 用于生成描述型错误代码或无参数型错误代码
		 * @param key
		 * @param resource
		 */
		public EC(String key, boolean resource) {
			this.key = key;
			this.resource = resource;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public boolean isResource() {
			return resource;
		}

		public void setResource(boolean resource) {
			this.resource = resource;
		}

		public Bundle getBundle() {
			return bundle;
		}

		public void setBundle(Bundle bundle) {
			this.bundle = bundle;
		}

		public Object[] getArgs() {
			return args;
		}

		public void setArgs(Object[] args) {
			this.args = args;
		}
	}
	
	public static final RuntimeException wrapErrors(List<EC> errors) {
		return wrapErrors(errors, new ECException());
	}
	
	public static final RuntimeException wrapErrors(EC[] errors) {
		return wrapErrors(errors, new ECException());
	}
	
	public static final RuntimeException wrapError(EC error) {
		return wrapError(error, new ECException());
	}
	
	public static final <T extends ECException> RuntimeException wrapErrors(List<EC> errors, T ex) {
		ECException exception = ex;
		if (exception == null)
			exception = new ECException();
		exception.addErrors(errors);
		
		return exception;
	}
	
	public static final <T extends ECException> RuntimeException wrapErrors(EC[] errors, T ex) {
		return wrapErrors(Arrays.asList(errors), ex);
	}
	
	public static final <T extends ECException> RuntimeException wrapError(EC error, T ex) {
		List<EC> errors = new ArrayList<EC>();
		errors.add(error);
		return wrapErrors(errors, ex);
	}
	
	@SuppressWarnings("unchecked")
	public static final void saveErrors(HttpServletRequest request, List<EC> errors) {
		if (errors != null && !errors.isEmpty()) {
			List<EC> e = (List<EC>) request.getAttribute(ERROR_KEY);
			if (e != null)
				e.addAll(errors);
			else
				e = errors;
			request.setAttribute(ERROR_KEY, e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static final void saveErrors(HttpSession session, List<EC> errors) {
		if (errors != null && !errors.isEmpty()) {
			List<EC> e = (List<EC>) session.getAttribute(ERROR_KEY);
			if (e != null)
				e.addAll(errors);
			else
				e = errors;
			session.setAttribute(ERROR_KEY, e);
		}
	}
	
	public static final void saveError(HttpServletRequest request, EC error) {
		List<EC> errors = new ArrayList<EC>();
		errors.add(error);
		saveErrors(request, errors);
	}
	
	public static final void saveError(HttpSession session, EC error) {
		List<EC> errors = new ArrayList<EC>();
		errors.add(error);
		saveErrors(session, errors);
	}
	
	@SuppressWarnings("unchecked")
	public static final void saveMessages(HttpServletRequest request, List<EC> messages) {
		if (messages != null && !messages.isEmpty()) {
			List<EC> m = (List<EC>) request.getAttribute(MESSAGE_KEY);
			if (m != null)
				m.addAll(messages);
			else
				m = messages;
			request.setAttribute(MESSAGE_KEY, m);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static final void saveMessages(HttpSession session, List<EC> messages) {
		if (messages != null && !messages.isEmpty()) {
			List<EC> m = (List<EC>) session.getAttribute(MESSAGE_KEY);
			if (m != null)
				m.addAll(messages);
			else
				m = messages;
			session.setAttribute(MESSAGE_KEY, m);
		}
	}
	
	public static final void saveMessage(HttpServletRequest request, EC message) {
		List<EC> messages = new ArrayList<EC>();
		messages.add(message);
		saveMessages(request, messages);
	}
	
	public static final void saveMessage(HttpSession session, EC message) {
		List<EC> messages = new ArrayList<EC>();
		messages.add(message);
		saveMessages(session, messages);
	}
	
	private static final boolean has(HttpServletRequest request, String key) {
		HttpSession session = request.getSession(false);
		return request.getAttribute(key) != null || (session != null && session.getAttribute(key) != null);
	}
	
	public static final boolean hasErrors(HttpServletRequest request) {
		return has(request, ERROR_KEY);
	}
	
	public static final boolean hasMessages(HttpServletRequest request) {
		return has(request, MESSAGE_KEY);
	}
	
	@SuppressWarnings("unchecked")
	private static final List<EC> get(HttpServletRequest request, String key, boolean remove) {
		List<EC> messages = (List<EC>) request.getAttribute(key);
		if (messages == null) {
			HttpSession session = request.getSession(false);
			if (session != null)
				messages = (List<EC>) session.getAttribute(key);
			if (remove && messages != null)
				session.removeAttribute(key);
		}
		return messages;
	}
	
	public static final List<EC> getErrors(HttpServletRequest request, boolean remove) {
		return get(request, ERROR_KEY, remove);
	}
	
	public static final List<EC> getErrors(HttpServletRequest request) {
		return get(request, ERROR_KEY, true);
	}
	
	public static final List<EC> getMessages(HttpServletRequest request, boolean remove) {
		return get(request, MESSAGE_KEY, remove);
	}
	
	public static final List<EC> getMessages(HttpServletRequest request) {
		return get(request, MESSAGE_KEY, true);
	}
	
	public static final void removeMessages(HttpServletRequest request) {
		request.removeAttribute(MESSAGE_KEY);
		HttpSession session = request.getSession(false);
		if (session != null)
			removeMessages(session);
	}
	
	public static final void removeMessages(HttpSession session) {
		session.removeAttribute(MESSAGE_KEY);
	}
	
	public static final void removeErrors(HttpServletRequest request) {
		request.removeAttribute(ERROR_KEY);
		HttpSession session = request.getSession(false);
		if (session != null)
			removeErrors(session);
	}
	
	public static final void removeErrors(HttpSession session) {
		session.removeAttribute(ERROR_KEY);
	}
}
