package com.huijia.eap.auth;

/**
 * 树形结构访问控制模块，应与@AuthBy(type=AuthType.TREE)配合使用
 * <p>
 * 当一个Controller被声明为@AuthBy(type=AuthType.TREE)时，则该Controller应该实现本接口，否则在访问时将会报错。
 * </p>
 * @author liunan
 *
 */
public interface TreeModule {

	/**
	 * 根据节点ID或者节点路径获取节点向上遍历直至根节点的路径<br/>
	 * 此方法在进行访问控制时调用，需要特别注意实现效率<br/>
	 * 
	 * 以下树为例：
	 * <pre>
	 * ROOT
	 *   |---A
	 *   |   |---A1
	 *   |   |---A2
	 *   |
	 *   |---B
	 * </pre>
	 * <ul>
	 * <li>当调用此方法：getPath("A1")时，应返回"/ROOT/A/A1"</li>
	 * <li>当调用此方法：getPath("/A/A2")时，应返回"/ROOT/A/A2"</li>
	 * </ul>
	 * 建议此方法的实现使用缓存方式，已避免在进行验证时频繁读取数据库，造成验证速度的瓶颈<br/>
	 * 如果采用读取数据库的方式，则应该采用一次读取，内存递归的方式来实现；即一次性将整个树结构数据从数据库中读取出来，之后在内存中进行递归遍历
	 * 
	 * @param node
	 * @return
	 */
	public String getPath(String node);
	
}
