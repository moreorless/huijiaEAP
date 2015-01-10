package com.huijia.eap.commons.mvc.view;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.mvc.View;

import com.huijia.eap.util.SerializeUtil;


/**
 * XStream生成的json的视图
 * @author peizl
 */
public class XJsonView implements View {


	@Override
	public void render(HttpServletRequest req, HttpServletResponse resp,
			Object obj) throws IOException {
		resp.setHeader("Cache-Control", "no-cache");
		resp.setContentType("text/plain");

		Writer writer = resp.getWriter();
	
		writer.write(obj == null ? "null" : SerializeUtil.getInstance().toJson(obj));
		writer.flush();

		resp.flushBuffer();
	}

}
