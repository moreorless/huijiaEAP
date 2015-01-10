package com.huijia.eap.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于声明子模块或入口（方法）的权限验证配置
 * <p>这个注解可以用于类和入口（方法）上，优先使用入口（方法）上的配置</p>
 * 
 * <ul>
 *   <li>value - 指定匹配方法类，必须是{@link #PermissionMatcher}的实现类</li>
 *   <li>module - 模块名称，如果为空，则使用类名称</li>
 *   <li>login - 是否需要登录才能进入，缺省为true；如果注解在类上的值为false，则该子模块的所有入口均不需要登录即可进入</li>
 *   <li>check - 是否需要验证权限，缺省为false；如果注解在类上的值为false，则该子模块的所有入口均不需要验证权限即可进入，也即相当于value=AllowPermissionMachter.class</li>
 *   <li>returnable - 重新登录后是否允许直接跳转回本入口，缺省为true</li>
 * </ul>
 * 
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface AuthBy {
	
	static enum AuthType {
		NORMAL, TREE
	};
	
	String module() default "";
	
	MatchBy[] value() default {};
	
	boolean login() default true;
	
	boolean check() default true;
	
	boolean returnable() default true;
	
	AuthType type() default AuthType.NORMAL;
}
