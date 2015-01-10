package com.huijia.eap.commons.mvc.taglib;

import javax.servlet.jsp.JspTagException;

/**
 * 检查是否登录标签
 * <p>
 * 使用方法与&lt;c:if&gt;类似，只是不需要设置value属性值，即：<br/>
 * &lt;cupid:ifLogin&gt;<br/>
 * &nbsp;&nbsp;&lt;%-- do something, e.g. redirect to login page --%&gt;<br/>
 * &lt;/cupid:ifLogin&gt;
 * </p>
 *
 */
public class IfLogin extends LoginSupport {

	@Override
	protected boolean condition() throws JspTagException {
		return isLogin();
	}

}
