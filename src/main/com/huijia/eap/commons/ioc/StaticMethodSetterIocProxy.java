package com.huijia.eap.commons.ioc;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.nutz.lang.Lang;
import org.nutz.lang.Mirror;

public class StaticMethodSetterIocProxy {

	private static final Logger logger = Logger.getLogger(StaticMethodSetterIocProxy.class);
	
	protected void create(List<StaticMethodInvokingProxyBean> beans) {
		for (StaticMethodInvokingProxyBean bean : beans) {
			try {
				Class<?> clazz = Lang.loadClass(bean.getType());
				Mirror<?> mirror = Mirror.me(clazz);
				mirror.invoke(clazz, bean.getMethod(), bean.getArgs());
			} catch (Exception e) {
				RuntimeException ex = Lang.wrapThrow(e, "Invoking static method '%s' of [%s] failed.", bean.getMethod(), bean.getType());
				
				if (logger.isDebugEnabled())
					logger.debug(ex);
				
				throw ex;
			}
		}
	}

	public StaticMethodSetterIocProxy(String type, String method, Object... args) {
		List<StaticMethodInvokingProxyBean> beans = new ArrayList<StaticMethodInvokingProxyBean>();
		beans.add(new StaticMethodInvokingProxyBean(type, method, args));
		create(beans);
	}
	
	public StaticMethodSetterIocProxy(List<StaticMethodInvokingProxyBean> beans) {
		if (beans != null)
			create(beans);
	}
}
