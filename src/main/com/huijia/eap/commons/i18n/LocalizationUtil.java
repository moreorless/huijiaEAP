package com.huijia.eap.commons.i18n;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import org.nutz.lang.Lang;

public class LocalizationUtil {
	
	public static void setSessionLocale(HttpServletRequest request, HttpServletResponse response, HttpSession session, String locale) {
//		if (JstlLocalizationAdaptor.DEFAULT_LOCALE.equals(locale) || Locale.SIMPLIFIED_CHINESE.toString().equals(locale))
//			session.removeAttribute("javax.servlet.jsp.jstl.fmt.locale");
//		else
			session.setAttribute("javax.servlet.jsp.jstl.fmt.locale", locale);
			
		Cookie cookie = new Cookie("lang", locale);
		cookie.setPath(request.getContextPath() + "/");
		cookie.setMaxAge(31536000); //365 days
		response.addCookie(cookie);
	}
	
	public static String getSessionLocale(HttpSession session) {
		return (String) session.getAttribute("javax.servlet.jsp.jstl.fmt.locale");
	}

	public static final String getMessage(HttpServletRequest request, Bundle bundle, String key, String... args) {
		String message = getMessage(request.getSession(false), bundle, key, args);
		if (message == null)
			message = getMessage(request.getServletContext(), bundle, key, args);
		return message;
	}

	/**
	 * 获取本地化字符串
	 * @param request HttpServletRequest对象
	 * @param bundle bundle对象
	 * @param key 资源键
	 * @param args 字符填充参数
	 * @return
	 */
	public static final String getMessage(HttpServletRequest request, Bundle bundle, String key, Object... args) {
		String[] args_str = (String[]) Lang.array2array(args, String.class);
		
		return getMessage(request, bundle, key, args_str);
	}
	
	@SuppressWarnings("unchecked")
	public static final String getMessage(ServletContext ctx, Bundle bundle, String key, String... args) {
		
		LocalizationContext messages = (LocalizationContext) ctx.getAttribute(bundle == null ? Bundle.MAIN_BUNDLE.getName() : bundle.getName());
		if (messages == null) {
			Map<String, Map<String, LocalizationContext>> localeMap = (Map<String, Map<String, LocalizationContext>>) ctx.getAttribute(JstlLocalizationAdaptor.class.getName());
			if (localeMap != null && localeMap.containsKey(JstlLocalizationAdaptor.DEFAULT_LOCALE)) {
				Map<String, LocalizationContext> bundleMap = localeMap.get(JstlLocalizationAdaptor.DEFAULT_LOCALE);
				messages = bundleMap.get(bundle.getName());
			}
		}

		if (messages != null) {
			return format(messages, key, args);
		}
		
		return null;
	}
	
	public static final String getMessage(ServletContext sc, String locale, Bundle bundle, String key, String... args) {
		LocalizationContext messages = null;
		Map<String, Map<String, LocalizationContext>> localeMap = (Map<String, Map<String, LocalizationContext>>) sc.getAttribute(JstlLocalizationAdaptor.class.getName());
		if (localeMap != null && localeMap.containsKey(locale)) {
			Map<String, LocalizationContext> bundleMap = localeMap.get(locale);
			messages = bundleMap.get(bundle.getName());
		}

		if (messages != null) {
			return format(messages, key, args);
		}
		
		return null;
	}
	
	public static final String getMessage(HttpSession session, Bundle bundle, String key, String...args) {
		if (session != null) {
			LocalizationContext context = (LocalizationContext) session.getAttribute(bundle == null ? Bundle.MAIN_BUNDLE.getName() : bundle.getName());
			if (context == null)
				return getMessage(session.getServletContext(), bundle, key, args);
			else
				return format(context, key, args);
		}
		
		return null;
	}
	
	protected static final String format(LocalizationContext context, String key, String...args) {
		if (context.getResourceBundle() != null && key != null && key.trim().length() > 0 && context.getResourceBundle().containsKey(key)) {
			String message = context.getResourceBundle().getString(key);
			if (args != null) {
				MessageFormat formatter = new MessageFormat("");
				if (context.getLocale() != null) {
					formatter.setLocale(context.getLocale());
				}
				formatter.applyPattern(message);
				message = formatter.format(args);
			}
			
			return message;
		}
		
		return null;
	}
	
	public static final String[] getAvailableLocaleNames(ServletContext sc) {
		Map<String, Map<String, LocalizationContext>> localeMap = (Map<String, Map<String, LocalizationContext>>) sc.getAttribute(JstlLocalizationAdaptor.class.getName());
		Set<String> locales = new HashSet<String>();
		locales.addAll(localeMap.keySet());
		locales.remove(JstlLocalizationAdaptor.DEFAULT_LOCALE);
		return locales.toArray(new String[locales.size()]);
	}
	
	/*@Deprecated
	public static final String getDynamicMessageByValue(HttpSession session, String key, String value) {
		Bundle bundle = new Bundle("dynamic");
		String currentLocale = (String) session.getAttribute("javax.servlet.jsp.jstl.fmt.locale");
		if (currentLocale != null && currentLocale.trim().length() > 0) {
			ServletContext sc = session.getServletContext();
			Map<String, Map<String, LocalizationContext>> lc = (Map<String, Map<String, LocalizationContext>>) sc.getAttribute(JstlLocalizationAdaptor.class.getName() + ".dynamic");
			if (lc != null && lc.containsKey(currentLocale)) {
				Map<String, LocalizationContext> bundleMap = lc.get(currentLocale);
				if (bundleMap.containsKey(bundle.getName())) {
					LocalizationContext ctx = bundleMap.get(bundle.getName());
					if (ctx.getResourceBundle().containsKey(key + "." + value)) {
						return ctx.getResourceBundle().getString(key + "." + value);
					}
				}
			}
		}
		
		return value;
	}
	
	public static final String getDynamicMessage(HttpSession session, DynamicI18NKey dynamicKey) {
		Bundle bundle = new Bundle("dynamic");
		String currentLocale = (String) session.getAttribute("javax.servlet.jsp.jstl.fmt.locale");
		if (currentLocale != null && currentLocale.trim().length() > 0) {
			switch (dynamicKey.getResourceBy()) {
				case CURRENTVALUE:
					return getMessage(session, bundle, dynamicKey.getResourceKey());
				case ID:
				default:
					String key = dynamicKey.getResourceKey();
					Map<String, Map<String, LocalizationContext>> localeMap = (Map<String, Map<String, LocalizationContext>>) session.getServletContext().getAttribute(JstlLocalizationAdaptor.class.getName());
					if (localeMap != null && localeMap.containsKey(JstlLocalizationAdaptor.DEFAULT_LOCALE)) {
						Map<String, LocalizationContext> bundleMap = localeMap.get(JstlLocalizationAdaptor.DEFAULT_LOCALE);
						LocalizationContext messages = bundleMap.get(bundle.getName());
						if (messages != null && messages.getResourceBundle().containsKey(key)) {
							String defaultValue = messages.getResourceBundle().getString(key);
							if (defaultValue != null && defaultValue.equals(dynamicKey.getCurrentValue())) {
								return getMessage(session, bundle, key);
							}
						}
					}
					break;
			}
		}	
		
		return dynamicKey.getCurrentValue();
	}
	
	public static final String getDynamicMessage(ServletContext sc, String locale, DynamicI18NKey dynamicKey) {
		Bundle bundle = new Bundle("dynamic");
		if (locale != null && locale.trim().length() > 0) {
			switch (dynamicKey.getResourceBy()) {
				case CURRENTVALUE:
					return getMessage(sc, locale, bundle, dynamicKey.getResourceKey());
				case ID:
				default:
					String key = dynamicKey.getResourceKey();
					Map<String, Map<String, LocalizationContext>> localeMap = (Map<String, Map<String, LocalizationContext>>) sc.getAttribute(JstlLocalizationAdaptor.class.getName());
					if (localeMap != null && localeMap.containsKey(JstlLocalizationAdaptor.DEFAULT_LOCALE)) {
						Map<String, LocalizationContext> bundleMap = localeMap.get(JstlLocalizationAdaptor.DEFAULT_LOCALE);
						LocalizationContext messages = bundleMap.get(bundle.getName());
						if (messages != null && messages.getResourceBundle().containsKey(key)) {
							String defaultValue = messages.getResourceBundle().getString(key);
							if (defaultValue != null && defaultValue.equals(dynamicKey.getCurrentValue())) {
								return getMessage(sc, locale, bundle, key);
							}
						}
					}
					break;
			}
		}	
		
		return dynamicKey.getCurrentValue();
	}
	
	public static final String getDynamicMessage(HttpSession session, Bundle bundle, String key) {
		String currentLocale = (String) session.getAttribute("javax.servlet.jsp.jstl.fmt.locale");
		if (currentLocale != null && currentLocale.trim().length() > 0) {
			return getMessage(session, bundle, key);
		}
		
		return null;
	}*/
}
