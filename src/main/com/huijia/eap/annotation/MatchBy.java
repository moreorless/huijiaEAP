package com.huijia.eap.annotation;

import com.huijia.eap.auth.matcher.PermissionMatcher;


public @interface MatchBy {

	Class<? extends PermissionMatcher> value();
	
	String ioc() default "";
}
