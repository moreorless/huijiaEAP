package com.huijia.eap;

import org.nutz.mvc.NutConfig;

/**
 * 应用启动以及关闭时的额外处理监听器，<b>须配合Ioc一起使用</b>
 * 
 * <p>
 * 实现类继承此接口，并实现两个方法后，还必须在Ioc中配置实例，并且实例的name必须以'$setup_'开头
 * </p>
 *
 */
public interface SetupListener {
	
	/**
	 * 启动时执行
	 * @param config
	 */
	public void init(NutConfig config);

	/**
	 * 停止时执行
	 * @param config
	 */
	public void destroy(NutConfig config);
	
	
}
