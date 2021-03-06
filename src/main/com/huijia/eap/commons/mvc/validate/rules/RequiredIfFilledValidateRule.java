/**
 
 * Author: Administrator
 * Created: 2011-5-11
 */
package com.huijia.eap.commons.mvc.validate.rules;

import org.nutz.lang.Strings;

public class RequiredIfFilledValidateRule extends
		AbstractComparableValidateRule {

	@Override
	protected boolean compare(Object base, Object value) {
		
		if (value != null && !Strings.isBlank(String.valueOf(value)) && (base == null || base instanceof String && Strings.isBlank((String) base)))
			return false;
		return true;
	}

}
