package com.huijia.eap;

import org.nutz.lang.Lang;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.view.AbstractPathView;

import com.huijia.eap.auth.PermissionException;
import com.huijia.eap.commons.mvc.view.ExceptionJspView;


public class FailProcessor extends org.nutz.mvc.impl.processor.FailProcessor {
	
	public static final String DEFAULT_ATTRIBUTE = "err";

	@Override
	public void process(ActionContext ac) throws Throwable {
		Throwable err = ac.getError();
		if (err != null)
			ac.getRequest().setAttribute(DEFAULT_ATTRIBUTE, err);
		
		if (err != null && ( err instanceof PermissionException || Lang.unwrapThrow(err) instanceof PermissionException )) {
			Object re = ac.getMethodReturn();
			if (re != null && re instanceof AbstractPathView || view instanceof AbstractPathView) {
				ac.setMethodReturn(new ExceptionJspView(null));
			}
		}
		super.process(ac);
	}

}
