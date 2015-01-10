package com.huijia.eap.annotation;

public @interface DynamicAuthItem {

	String key() default "";
	
	AuthBy by();
	
}
