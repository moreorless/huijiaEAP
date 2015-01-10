/**
 
 * Author: Administrator
 * Created: 2011-5-3
 */
package com.huijia.eap.commons.mvc.validate.annotation;

import com.huijia.eap.commons.i18n.Bundle;

/**
 * 校验规则类型注解
 * @author liunan
 *
 */
public @interface ValidateType {

	public static enum Type {
		
		required, requiredIfChecked, requiredIfFilled, requiredIfEqualTo, minlength, maxlength, rangelength,
		min, max, range, email, url, number, digits, equalTo, ip, ipv6, port, mac,
		datetime, datetimeComplete, time, timeComplete,
		dtBefore, dtAfter, ipBefore, ipAfter, notLocalhostIp,
		regexp, @Deprecated password, pwd,
		gt, lt, ge, le

	};
	
	Type type();
	
	String[] parameters() default {};
	
	String errorMsg();
	
	boolean resource() default false;
	
	String bundle() default Bundle.DEFAULT;
	
	String value() default "";
}
