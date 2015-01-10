package com.huijia.eap.auth.matcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;

import com.huijia.eap.CupidCoreMessages;
import com.huijia.eap.auth.bean.Permission;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.matcher.method.PermissionMethod;
import com.huijia.eap.util.duplication.DuplicateUtilFactory;
import com.huijia.eap.util.duplication.IDuplicateUtil;


public class SimpleRWPermissionMatcher extends TreeResourceMatcher implements PermissionMatcher {
	
	private static final IDuplicateUtil cloner = DuplicateUtilFactory.getUtilInstance();
	
//	private static final String DEFAULT_READ_METHODS = "^(is|view|search|list|read|get).*$";
//	private static final String DEFAULT_WRITE_METHODS = "^(insert|update|delete|import|save|copy|move).*$";
//	private static final String DEFAULT_OUTPUT_METHODS = "^(export|download).*$";
	
//	private static final String READ_METHOD_PREFIX = "r";
//	private static final String WRITE_METHOD_PREFIX = "w";
//	private static final String RW_METHOD_PREFIX = "rw";
//	private static final String OUTPUT_METHOD_PREFIX = "o";
	
	private Set<PermissionMethod> methods = new LinkedHashSet<PermissionMethod>(Arrays.asList(
				new PermissionMethod('r', CupidCoreMessages.getString(SimpleRWPermissionMatcher.class, "method.read"), "^(is|view|search|list|read|get).*$"),
				new PermissionMethod('w', CupidCoreMessages.getString(SimpleRWPermissionMatcher.class, "method.write"), "^(insert|update|delete|import|save|copy|move).*$", "r"),
				new PermissionMethod('o', CupidCoreMessages.getString(SimpleRWPermissionMatcher.class, "method.export"), "^(export|download).*$", "rw")
			));
	


	public void setMethods(PermissionMethod[] methods) {
		if (methods != null && methods.length > 0) {
			for (PermissionMethod method : methods) {
				if (!this.methods.contains(method))
					this.methods.add(method);
			}
		}
	}
	
	public PermissionMethod[] getMethods() {
		PermissionMethod[] methods = new PermissionMethod[this.methods.size()];
		int i = 0; 
		for (PermissionMethod method : this.methods) {
			methods[i++] = cloner.duplicate(method);
		}
		return methods;
	}
	
	@Override
	public boolean match(Permission required, Permission matching, User currentUser) {
		
		if (required != null && "core".equalsIgnoreCase(required.getModule()))
			return true;
		if (required != null && matching != null) {
//			if ("demo".equalsIgnoreCase(required.getModule()))
//				return true;
			
			Permission prefixed = prefixPermission(required);
			return prefixed.getModule().equals(matching.getModule())
					&& matching.getAction().toLowerCase().indexOf(prefixed.getAction()) > -1
					&& matchResource(required.getResource(), matching.getResource());
		}
		
		return false;
	}
	
	protected Permission prefixPermission(Permission required) {
		Permission per = new Permission();
		per.setModule(required.getModule());
		per.setResource(required.getResource());
		
		String action = required.getAction();
		for (PermissionMethod method : methods) {
			if (method.getPatternObject().matcher(action).matches()) {
				per.setAction("" + method.getValue());
				return per;
			}
		}
		
		per.setAction("_");
		return per;
		
//		if (readPattern.matcher(action).matches())
//			per.setAction(READ_METHOD_PREFIX);
//		else if (writePattern.matcher(action).matches())
//			per.setAction(WRITE_METHOD_PREFIX);
//		else if (outputPattern.matcher(action).matches())
//			per.setAction(OUTPUT_METHOD_PREFIX);
//		else
//			per.setAction("_");
//		
//		return per;
	}

}
