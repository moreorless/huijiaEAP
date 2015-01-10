package com.huijia.eap.commons.mvc.validate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 验证支持注解
 * <p>
 * 可配置在属性或参数中，标明需要校验
 * </p>
 * 
 * @author liunan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD })
public @interface Validations {
	
	ValidateType[] rules() default {};
	
	/**
	 * 暂未使用
	 * @return
	 */
	String value() default "";
}
