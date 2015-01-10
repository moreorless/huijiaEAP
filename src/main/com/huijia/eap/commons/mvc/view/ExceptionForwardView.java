package com.huijia.eap.commons.mvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.lang.Lang;
import org.nutz.mvc.view.ForwardView;

import com.huijia.eap.FailProcessor;
import com.huijia.eap.auth.PermissionException;
import com.huijia.eap.commons.mvc.view.exhandler.ECException;
import com.huijia.eap.commons.mvc.view.exhandler.ExceptionWrapper;


public class ExceptionForwardView extends ForwardView {

	public ExceptionForwardView(String name) {
		super(name);
	}
	
	private ECException unwrapECException(Throwable t) {
		if (t instanceof ECException)
			return (ECException) t;
		
		Throwable cause = Lang.unwrapThrow(t);
		if (cause instanceof ECException)
			return (ECException) cause;
		
		return null;
	}

	@Override
	public void render(HttpServletRequest req, HttpServletResponse resp,
			Object obj) throws Exception {
		
		ECException e = null;
		if (obj instanceof Throwable)
			e = unwrapECException((Throwable) obj);
		
		if (e == null) {
			Object err = req.getAttribute(FailProcessor.DEFAULT_ATTRIBUTE);
			if (err != null && err instanceof Throwable) {
				e = unwrapECException((Throwable) e);
			}
		}
		
		if (e != null)
			ExceptionWrapper.saveErrors(req, e.getErrors());
		else if (obj instanceof Throwable) {
			Throwable rt = Lang.unwrapThrow((Throwable) obj); //unwrap异常对象，以使得PermissionException被正常处理
			
			/*
			Object excp;
			if (rt instanceof PermissionException)
				excp = rt;
			else {
				excp = EncodeExceptionUtil.encodeException(rt);
			}
			*/
			
			req.setAttribute("huijia_EXCEPTION", rt);
		}
		
		super.render(req, resp, obj);
	}
}
