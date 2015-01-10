package com.huijia.eap.commons.mvc.view;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jofc2.model.IChart;

import org.nutz.json.JsonFormat;

import com.huijia.eap.commons.mvc.util.BrowserUtil;


public class ChartView extends ExceptionJsonView {

	public ChartView(JsonFormat format) {
		super(format);
	}

	@Override
	public void render(HttpServletRequest req, HttpServletResponse resp,
			Object obj) throws IOException {
		
		if (obj instanceof IChart) {
			
			String skin = "default";
			Cookie[] cookies = req.getCookies();
			for (Cookie cookie : cookies) {
				if ("SKIN".equals(cookie.getName())) {
					skin = cookie.getValue();
					break;
				}
			}
			((IChart) obj).setCurrentTheme(skin);
			
			if (BrowserUtil.isIE(req)) { //针对IE在HTTPS下不能正常处理flash数据的问题进行处理
				resp.setHeader("Cache-Control", "cache, max-age=0");
				resp.setHeader("Pragma", "public");
			} else
				resp.setHeader("Cache-Control", "no-cache");
			resp.setContentType("text/plain");

			Writer writer = resp.getWriter();
			writer.write(((IChart) obj).toString());
			writer.flush();

			resp.flushBuffer();
			
			return;
		}
		
		super.render(req, resp, obj);
	}

}
