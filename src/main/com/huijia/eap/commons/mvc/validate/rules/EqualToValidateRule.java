/**
 
 * Author: Administrator
 * Created: 2011-5-11
 */
package com.huijia.eap.commons.mvc.validate.rules;

import org.nutz.lang.Strings;

public class EqualToValidateRule extends AbstractComparableValidateRule {

	@Override
	protected boolean compare(Object base, Object value) {
		String equalTo = value == null ? null : String.valueOf(value);
		if (!Strings.isBlank(equalTo)) {
			return (equalTo.equals(String.valueOf(base)));
		}
		return Strings.isBlank(base == null ? null : String.valueOf(base));
	}

}
