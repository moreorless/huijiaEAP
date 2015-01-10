package com.huijia.eap.commons.mvc.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.json.JsonFormat;
import org.nutz.lang.Lang;
import org.nutz.mvc.view.UTF8JsonView;

import com.huijia.eap.commons.data.JsonException;
import com.huijia.eap.commons.i18n.LocalizationUtil;
import com.huijia.eap.commons.mvc.view.exhandler.ECException;
import com.huijia.eap.commons.mvc.view.exhandler.ExceptionWrapper;
import com.huijia.eap.commons.mvc.view.exhandler.ExceptionWrapper.EC;

public class ExceptionJsonView extends UTF8JsonView {

	public ExceptionJsonView(JsonFormat format) {
		super(format);
	}

	@Override
	public void render(HttpServletRequest req, HttpServletResponse resp,
			Object obj) throws IOException {
		
		Object e = obj;
		
		if (obj instanceof Throwable) {
			Throwable t = null;
			ECException ecex = null;
			if (obj instanceof ECException) {
				ecex = (ECException) obj;
			} else {
				t = Lang.unwrapThrow((Throwable) obj);
				if (t instanceof ECException)
					ecex = (ECException) t;
			}
			
			if (ecex != null) {
				List<EC> errors = ((ECException) obj).getErrors();
				for (EC error : errors) {
					if (error.isResource()) {
						error.setKey(LocalizationUtil.getMessage(req, error.getBundle(), error.getKey(), (String[]) Lang.array2array(error.getArgs(), String.class)));
						error.setResource(false);
					}
				}
				
				e = ecex;
			} else if (t != null && JsonException.class.isAssignableFrom(t.getClass())) {
				e = t;
			} else if (!JsonException.class.isAssignableFrom(obj.getClass())) {
				// e = EncodeExceptionUtil.encodeException(Lang.unwrapThrow((Throwable) obj));
				e = Lang.unwrapThrow((Throwable) obj);
			}
			
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		
		List<EC> messages = ExceptionWrapper.getMessages(req);
		if (messages != null) {
			Map<String, Object> json = new HashMap<String, Object>();
			json.put("obj", obj);
			List<String> msgs = new ArrayList<String>();
			for (EC message : messages) {
				if (message.isResource()) {
					msgs.add(LocalizationUtil.getMessage(req, message.getBundle(), message.getKey(), (String[]) Lang.array2array(message.getArgs(), String.class)));
				} else {
					msgs.add(message.getKey());
				}
			}
			json.put(ExceptionWrapper.MESSAGE_KEY, msgs);
			e = json;
		}
		
		super.render(req, resp, e);
	}

}
