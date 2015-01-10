package com.huijia.eap.commons.mvc.adaptor.injector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.lang.Streams;
import org.nutz.mvc.adaptor.ParamInjector;

public class ChartImageInjector implements ParamInjector {

	@Override
	public Object get(ServletContext sc, HttpServletRequest req,
			HttpServletResponse resp, Object refer) {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			Streams.writeAndClose(baos, req.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return baos.toByteArray();
	}

}
