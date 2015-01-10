package com.huijia.eap.commons.mvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ExceptionJspView extends ExceptionForwardView {
	
	private boolean showBack = true;

	public ExceptionJspView(String name, boolean showBack) {
		super(name == null || name.trim().length() == 0 ? getPath() : name);
		this.showBack = showBack;
	}

	public ExceptionJspView(String name) {
		this(name, true);
	}

	protected static String getPath() {
		return "/error/error.jsp";
	}

	@Override
	protected String getExt() {
		return ".jsp";
	}


	@Override
	public void render(HttpServletRequest req, HttpServletResponse resp,
			Object obj) throws Exception {
		if (!showBack)
			req.setAttribute("huijia_errorpage_showback", false);
		super.render(req, resp, obj);
	}
}
