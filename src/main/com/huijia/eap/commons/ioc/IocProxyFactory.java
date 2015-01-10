package com.huijia.eap.commons.ioc;

import org.nutz.ioc.Ioc;
import org.nutz.mvc.NutConfig;

import com.huijia.eap.SetupListener;

/**
 * IoC静态变量、方法代理工厂
 * <p>
 * 应注册为SetupListener，在IoC引擎加载时执行此工厂初始化方法<br/>
 * 初始化方法会根据initNamePrefix名称获取IoC中的Bean，已使得Bean获得初始化，即静态变量注入和静态方法调用
 * </p>
 * @author liunan
 *
 */
public class IocProxyFactory implements SetupListener {
	
	private String initNamePrefix;

	private static final IocProxyFactory factory = new IocProxyFactory();
	
	private IocProxyFactory() {}
	
	public static final IocProxyFactory me() {
		return factory;
	}
	
	public void setInitNamePrefix(String initNamePrefix) {
		this.initNamePrefix = initNamePrefix;
	}
	
	@Override
	public void init(NutConfig config) {
		Ioc ioc = config.getIoc();
		String[] names = ioc.getNames();
		for (String name : names) {
			if (name != null && name.startsWith(initNamePrefix)) {
				ioc.get(null, name);
			}
		}
	}

	@Override
	public void destroy(NutConfig config) {
		//do nothing
	}


}
