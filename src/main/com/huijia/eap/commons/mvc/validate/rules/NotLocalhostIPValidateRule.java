/**
 
 * author: liunan
 * created: 2012-9-19
 */
package com.huijia.eap.commons.mvc.validate.rules;

import org.nutz.lang.Strings;

public class NotLocalhostIPValidateRule extends AbstractIterableValidateRule {

	@Override
	protected boolean validateSingle(Object value) {
		String ip = value == null ? null : String.valueOf(value);
		if (Strings.isBlank(ip))
			return true;
		return !"127.0.0.1".equals(ip) && !"localhost".equalsIgnoreCase(ip) && !"::1".equals(ip);
	}

}
