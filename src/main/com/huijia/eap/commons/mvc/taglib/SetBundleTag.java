package com.huijia.eap.commons.mvc.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import org.apache.taglibs.standard.tag.common.core.NullAttributeException;
import org.apache.taglibs.standard.tag.common.core.SetSupport;
import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;

public class SetBundleTag extends SetSupport {

	private String value_;
	
	public void setValue(String bundle_) {
		this.value_ = bundle_;
	}

	@Override
	public int doStartTag() throws JspException {
		evaluateExpressions();
		return super.doStartTag();
	}

	@Override
	public void release() {
		super.release();
		this.value_ = null;
	}
	
	private void evaluateExpressions() throws JspException {

		try {
			value = ExpressionUtil.evalNotNull(
					"setBundle", "value", "${" + value_ + "}", LocalizationContext.class, this,
					pageContext);
		} catch (NullAttributeException ex) {
			// explicitly let 'value' be null
			value = null;
		}
	}
	
}
