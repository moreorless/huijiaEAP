/**
 
 * Author: Administrator
 * Created: 2011-5-11
 */
package com.huijia.eap.commons.mvc.validate.rules;

import org.nutz.lang.Strings;

public class RequiredIfEqualToValidateRule extends AbstractRangeValidateRule {

	@Override
	protected boolean range(Object required, Object depend, Object value) {
		
		if (depend != null) {
			String dependValue = String.valueOf(depend);
			String valueStr = value == null ? null : String.valueOf(value);
			
			if (dependValue.equals(valueStr) && (required == null || required instanceof String && Strings.isBlank((String) required)))
				return false;
		}
		
		return true;
	}

}
