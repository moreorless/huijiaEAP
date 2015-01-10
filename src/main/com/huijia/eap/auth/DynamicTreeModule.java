package com.huijia.eap.auth;

/**
 * 动态模块属性结构方式控制接口，应与@AuthBy(dynamics={...})配合使用
 * <p>
 * 当一个Controller被声明为@AuthBy(type=AuthType.TREE, dynamics={...})时，则该Controller应该实现本接口，否则在访问时将会报错。
 * </p>
 * @author liunan
 *
 */
public interface DynamicTreeModule {

	/**
	 * 与{@link TreeModule#getPath(String)}功能相同，使用方式也相同，只针对动态模块名增加参数module
	 * @param module 动态模块名
	 * @param node
	 * @return
	 */
	public String getPath(String module, String node);

}
