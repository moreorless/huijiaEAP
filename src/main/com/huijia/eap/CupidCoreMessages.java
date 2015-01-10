package com.huijia.eap;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


public abstract class CupidCoreMessages {
	
	private static final String BUNDLE_NAME = "com.huijia.eap.messages";

	private static ResourceBundle RESOURCE_BUNDLE; 
	
	static {
		if (GlobalConfig.defaultLocale != null) {
			try {
				RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, GlobalConfig.defaultLocale);
			} catch (Exception e) {
				
			}
		}
		
		if (RESOURCE_BUNDLE == null)
			RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.SIMPLIFIED_CHINESE); 
	}
	
	private static String format(String msg, Object... args) {
		String message = msg;
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				message = message.replaceAll("\\{" + i + "\\}", String.valueOf(args[i]));
			}
		}
		return message;
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return "!!!" + key + "!!!";
		}
	}
	
	public static String getString(String key, Object... args) {
		String message = getString(key);
		return format(message, args);
	}
	
	public static String getString(Class<?> clazz, String key) {
		if (clazz == null)
			return getString(key);
		
		String newKey = clazz.getName();
		if (key != null) {
			if (key.startsWith("."))
				newKey += key;
			else
				newKey += "." + key;
		}
		return getString(newKey);
	}
	
	public static String getString(Class<?> clazz, String key, Object... args) {
		String message = getString(clazz, key);
		return format(message, args);
	}
}
