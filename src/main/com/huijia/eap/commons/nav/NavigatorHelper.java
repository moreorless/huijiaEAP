package com.huijia.eap.commons.nav;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;



import com.huijia.eap.GlobalConfig;

public class NavigatorHelper {

	public static String NAV_SESSION_KEY = "navigators_in_session";
	private static Logger logger = Logger.getLogger(NavigatorHelper.class);
	/**
	 * 加载导航菜单配置
	 * @return
	 */
	public static List<Navigator> loadNavigator(){
		List<Navigator> navigators = new LinkedList<>();
		String confFile = GlobalConfig.getContextValue("conf.dir") + File.separator + "navigator.xml";
		
		try{
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new File(confFile));
			
			Element root = doc.getRootElement();
			List<Element> allChildren = root.getChildren(); 
			for(Element navEle : allChildren) { 
				Navigator nav = new Navigator();
				nav.setId(navEle.getAttributeValue("id"));
				nav.setName(navEle.getAttributeValue("name"));
				nav.setUrl(navEle.getAttributeValue("url"));
				nav.setIcon(navEle.getAttributeValue("icon"));
				navigators.add(nav);
			}
			
		}catch (Exception e) {
			logger.error("加载导航配置出错", e);
		}
		return navigators;
	}
}
