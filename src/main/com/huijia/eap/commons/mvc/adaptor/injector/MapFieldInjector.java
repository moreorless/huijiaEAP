package com.huijia.eap.commons.mvc.adaptor.injector;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.castor.Castors;
import org.nutz.lang.Lang;
import org.nutz.lang.Mirror;
import org.nutz.mvc.adaptor.ParamExtractor;
import org.nutz.mvc.adaptor.ParamInjector;
import org.nutz.mvc.adaptor.Params;
import org.nutz.mvc.adaptor.injector.ObjectPairInjector;

public class MapFieldInjector implements ParamInjector {
	
	public static final String seperator = ":";
	
	protected String prefix;
	
	protected Type type;
	
	public MapFieldInjector(String prefix, Type type) {
		this.prefix = prefix;
		this.type = type;
	}

	@Override
	public Object get(ServletContext sc, HttpServletRequest req,
			HttpServletResponse resp, Object refer) {
//		ParamExtractor pe = Params.makeParamExtractor(req, refer);
		Map<String, Object> parameters = new HashMap<String, Object>();
		for (Enumeration<String> enu = req.getParameterNames(); enu.hasMoreElements(); ) {
			String name = enu.nextElement();
			if (name.startsWith(prefix)) {
				String[] values = req.getParameterValues(name);
				name = name.substring(prefix.length() + seperator.length());
				if (values == null || values.length == 0)
					parameters.put(name, null);
				else if (values.length == 1)
					parameters.put(name, values[0]);
				else
					parameters.put(name, values);
			}
		}
		
		Map<String, Object> obj;
		Class<?> clazz = Lang.getTypeClass(type);
		if (type != null && Map.class.isAssignableFrom(clazz) && !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
			obj = (Map<String, Object>) Mirror.me(type).born();
		}
		else
			obj = new HashMap<String, Object>();
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			String[] path = entry.getKey().split(seperator);
			inject(obj, path, entry.getValue());
		}
		return obj;
	}
	
	protected void inject(Map<String, Object> obj, String[] path, Object value) {
		
		if (path == null)
			return;
		
		if (path.length == 1) {
			String key = path[0];
			obj.put(key, value);
		} else if (path.length > 1) {
			String key = path[0];
			Object submap = obj.get(key);
			if (submap == null) {
				submap = new LinkedHashMap<String, Object>();
				obj.put(key, submap);
			}
			
			if (submap instanceof Map) {
				inject((Map<String, Object>) submap, Arrays.copyOfRange(path, 1, path.length), value);
			}
		}
	}

}
