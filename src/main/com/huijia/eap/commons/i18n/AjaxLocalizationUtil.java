package com.huijia.eap.commons.i18n;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.huijia.eap.annotation.AuthBy;

/**
 * 前台获取国际化字符串方法<br/>
 * 通过组装一个URL地址获取国际化字符串，返回结果为JSON数据<br/>
 * 
 * URI: /util/message?bundle=${bundle}&key=${key}&args=${arg}&args=${arg}
 * 
 * <ul>
 * 参数：
 * <li>${bundle} - 资源所在Bundle</li>
 * <li>${key} - 资源键</li>
 * <li>${arg} - 字符串中的参数，可添加多个</li>
 * </ul>
 * 
 * 在页面配合$.message方法使用<br/>
 * 
 * TODO: 等待Nutz的URL解析bug修复，提供REST风格URL方法
 * @author liunan
 *
 */
@IocBean
@InjectName
@AuthBy(check=false)
@At("/util")
@Ok("json")
public class AjaxLocalizationUtil {
	
	public static class Message {
		protected String bundle;
		protected String key;
		protected String[] args;
		public String getBundle() {
			return bundle;
		}
		public void setBundle(String bundle) {
			this.bundle = bundle;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String[] getArgs() {
			return args;
		}
		public void setArgs(String[] args) {
			this.args = args;
		}
	}

	@At("/message")
	public String getMessage(HttpServletRequest request, @Param("bundle") String bundle, @Param("key") String key, @Param("args") String[] args) {
		String text = LocalizationUtil.getMessage(request, new Bundle(bundle), key, args);
		return text == null ? "???" + key + "???" : text;
	}
	
	@At("/messages")
	public String[] getMessages(HttpServletRequest request, @Param("messages") LocaleMessage[] messages) {
		String[] results = null;
		if (messages != null) {
			results = new String[messages.length];
			for (int i = 0; i < results.length; i++) {
				LocaleMessage message = messages[i];
				String text = LocalizationUtil.getMessage(request, new Bundle(message.bundle), message.key, message.args);
				results[i] = text == null ? "???" + message.key + "???" : text;
			}
		}
		return results;
	}
	
	/**
	 * REST风格获取方法<br/>
	 * URI类似: <i>/util/message/rest/${bundle}/${key}/${arg0}/${arg1}/...</i><br/>
	 * 路径中，${key}之后的部分都会被做为参数使用
	 * 
	 * @param bundle
	 * @param key
	 * @param args
	 * @param request
	 * @return
	 */
	@At("/rest/message/?/?/*")
	public String getMessageREST(String bundle, String key, String[] args, HttpServletRequest request) {
		String text = LocalizationUtil.getMessage(request, new Bundle(bundle), key, args);
		return text == null ? "???" + key + "???" : text;
	}
	
	@At("/setlocale")
	public void setLocal(HttpServletRequest request, HttpServletResponse response, HttpSession session, @Param("locale") String locale) {
		LocalizationUtil.setSessionLocale(request, response, session, locale);
	}
	
}
