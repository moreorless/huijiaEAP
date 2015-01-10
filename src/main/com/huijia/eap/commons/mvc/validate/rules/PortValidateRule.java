/**
 
 * Author: Administrator
 * Created: 2011-5-11
 */
package com.huijia.eap.commons.mvc.validate.rules;

import org.nutz.lang.Strings;

public class PortValidateRule extends DigitsValidateRule {

	@Override
	protected boolean validateSingle(Object value) {
		if (value == null || Strings.isBlank(String.valueOf(value)))
			return true;
		int port = Integer.parseInt(String.valueOf(value));
		return super.validateSingle(value) && port > 0 && port < 65536;
	}

}
