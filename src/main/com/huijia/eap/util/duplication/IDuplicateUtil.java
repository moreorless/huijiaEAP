package com.huijia.eap.util.duplication;

/**
 * 
 * 复制工具接口定义
 *
 * @author liunan
 */
public interface IDuplicateUtil
{
	/**
	 * 复制对象，返回属性完全相同的另外一个对象实例
	 * @param o
	 * @return
	 */
	public <T> T duplicate(T o);
}

