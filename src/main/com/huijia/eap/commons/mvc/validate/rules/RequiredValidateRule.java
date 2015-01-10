/**
 
 * Author: Administrator
 * Created: 2011-5-11
 */
package com.huijia.eap.commons.mvc.validate.rules;

import org.nutz.lang.Strings;

public class RequiredValidateRule extends AbstractIterableValidateRule {

	@Override
	protected boolean validateSingle(Object value) {
		if (value == null || value instanceof String && Strings.isBlank((String) value))
			return false;
		return true;
	}

}
