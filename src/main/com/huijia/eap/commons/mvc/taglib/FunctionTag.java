package com.huijia.eap.commons.mvc.taglib;

import org.nutz.lang.Mirror;

public class FunctionTag {

	public static boolean hasProperty(Object o, String property) {
		Mirror<Object> mirror = Mirror.me(o);
		if (mirror == null)
			return false;
		
		try {
			return mirror.getField(property) == null ? false : true;
		} catch (NoSuchFieldException e) {
			return false;
		}
	}
	
	public static boolean isCurrentProduct(Object o) {
		String product = String.valueOf(o);
		
		return System.getProperty("tsoc.product").equalsIgnoreCase(product.indexOf("-") > -1 ? product : "tsoc-" + product);
	}
	
	public static boolean isNumber(Object o) {
		if (o == null)
			return false;
		if (o instanceof Number)
			return true;
		String number = String.valueOf(o);
		try {
			Double.parseDouble(number);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
}
