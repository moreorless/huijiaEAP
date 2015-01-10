package com.huijia.eap.commons.mvc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class BrowserUtil {

	private static final Pattern UA = Pattern.compile("MSIE(\\s+)(\\d+(\\.\\d+)?)");
	
	private static final Matcher matchIE(HttpServletRequest request) {
		String ua = request.getHeader("user-agent");
		Matcher matcher = UA.matcher(ua);
		return matcher;
	}

	public static final boolean isIE(HttpServletRequest request) {
		return matchIE(request).find();
	}
	
	public static final String getIEVersion(HttpServletRequest request) {
		Matcher matcher = matchIE(request);
		if (matcher.find()) {
			return matcher.group(2);
		}
		
		return null;
	}
}
