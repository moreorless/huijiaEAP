/**
 
 * Author: Administrator
 * Created: 2011-4-22
 */
package com.huijia.eap.commons.mvc.view;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.el.El;
import org.nutz.lang.Lang;
import org.nutz.lang.Streams;
import org.nutz.lang.Strings;
import org.nutz.lang.segment.CharSegment;
import org.nutz.lang.util.Context;
import org.nutz.mvc.view.AbstractPathView;
import org.nutz.mvc.view.RawView;

import com.huijia.eap.commons.mvc.util.BrowserUtil;


public class DownloadView extends RawView {
	
	private CharSegment name;
	
	private Map<String, El> exps;

	public DownloadView(String name) {
		super("stream");
		if (null != name) {
			this.name = new CharSegment(Strings.trim(name));
			this.exps = new HashMap<String, El>();
			for (String key : this.name.keys()) {
				this.exps.put(key, new El(key));
			}
		}
	}
	
	@Override
	public void render(HttpServletRequest req, HttpServletResponse resp,
			Object obj) throws Throwable {
		
		String path = evalPath(req, obj);

		if (BrowserUtil.isIE(req))
			path = URLEncoder.encode(path, "UTF-8");
		else
			path = new String(path.getBytes(), "ISO-8859-1");
		resp.setHeader("Content-Disposition", "attachment;filename=\""
				+ path + "\"");
		
		Object o = obj;
		if (obj instanceof File) {
			o = Streams.fileIn((File) obj);
		}
		
		super.render(req, resp, o);
	}
	
	protected String evalPath(HttpServletRequest req, Object obj) {
		if (null == name)
			return null;

		Context context = Lang.context();

		// 解析每个表达式
		Context expContext = AbstractPathView.createContext(req, obj);
		for (Entry<String, El> en : exps.entrySet())
			context.set(en.getKey(), en.getValue().eval(expContext).toString());

		// 生成解析后的路径
		return Strings.trim(this.name.render(context).toString());
		
	}

}
