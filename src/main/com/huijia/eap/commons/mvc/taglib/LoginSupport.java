package com.huijia.eap.commons.mvc.taglib;

import org.apache.taglibs.standard.tag.el.core.IfTag;

import com.huijia.eap.auth.Auths;


public class LoginSupport extends IfTag {
	
	protected boolean isLogin() {
		return (Auths.getUser(pageContext.getSession()) != null);
	}
	
}
