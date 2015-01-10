
package com.huijia.eap.auth.matcher;

import org.apache.log4j.Logger;

public abstract class TreeResourceMatcher {
	
	protected static final Logger logger = Logger.getLogger(TreeResourceMatcher.class);

	protected boolean matchResource(String require, String having) {
		
		if ("*".equals(having) || "*".equals(require))
			return true;
		else if (require == null || having == null)
			return false;
		else if (require.charAt(0) != '/') {
			if (logger.isDebugEnabled()) {
				logger.debug("Required resource[" + require + "] is not formatted correctly.");
			}
			return false;
		}
		
		return having.equals(require) || (having.length() < require.length() && require.startsWith(having) && require.charAt(having.length()) == '/');
		
	}
	
}
