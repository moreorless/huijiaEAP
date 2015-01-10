package com.huijia.eap.commons.mvc.view;

import static org.nutz.mvc.view.DefaultViewMaker.*;

import org.nutz.ioc.Ioc;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Strings;
import org.nutz.mvc.View;
import org.nutz.mvc.ViewMaker;

public class ExceptionViewMaker implements ViewMaker {

	@Override
	public View make(Ioc ioc, String type, String value) {
		
		if (VIEW_FORWARD.equalsIgnoreCase(type) || VIEW_FORWARD2.equalsIgnoreCase(type)) {
			return new ExceptionForwardView(value);
		} else if ("error".equalsIgnoreCase(type)) {
			return new ExceptionJspView(value);
		} else if ("error-no-back".equalsIgnoreCase(type)) {
			return new ExceptionJspView(value, false);
		} else if (VIEW_JSON.equalsIgnoreCase(type)) { //取代原UTF8JsonView视图，当遇到Throwable时包裹为JsonException
//			if (Strings.isBlank(value)) 
				return new ExceptionJsonView(new JsonFormat(true).setIgnoreNull(false));
//			else
//				return new ExceptionJsonView(Json.fromJson(JsonFormat.class, value));
		} else if ("chart".equals(type)) {
			return new ChartView(JsonFormat.compact());
		} else if ("xjson".equals(type)) {
			return new XJsonView();
		} else if ("dl".equals(type)) {
			return new DownloadView(value);
		} else if ("image".equalsIgnoreCase(type)) {
			return new ImageView();
		}
		
		return null;
	}

}
