package com.huijia.eap.commons.ioc;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.nutz.lang.Lang;
import org.nutz.lang.Mirror;

public class StaticPropertySetterIocProxy {
	
	private static final Logger logger = Logger.getLogger(StaticPropertySetterIocProxy.class);
	
	public StaticPropertySetterIocProxy(String type, String field, Object value) {
		List<StaticPropertyInjectProxyBean> beans = new ArrayList<StaticPropertyInjectProxyBean>();
		beans.add(new StaticPropertyInjectProxyBean(type, field, value));
		create(beans);
	}
	
	public StaticPropertySetterIocProxy(List<StaticPropertyInjectProxyBean> beans) {
		if (beans != null)
			create(beans);
	}
	
	protected void create(List<StaticPropertyInjectProxyBean> beans) {
		for (StaticPropertyInjectProxyBean bean : beans) {
			try {
				Class<?> clazz = Lang.loadClass(bean.getType());
				Mirror<?> mirror = Mirror.me(clazz);
				mirror.setValue(clazz, bean.getField(), bean.getValue());
			} catch (Exception e) {
				RuntimeException ex = Lang.wrapThrow(e, "Init static property '%s' of [%s] failed.", bean.getField(), bean.getType());
				
				if (logger.isDebugEnabled())
					logger.debug(ex);
				
				throw ex;
			}
		}
	}
	
}
