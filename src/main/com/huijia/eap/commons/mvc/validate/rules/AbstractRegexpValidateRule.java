/**
 
 * Author: Administrator
 * Created: 2011-5-11
 */
package com.huijia.eap.commons.mvc.validate.rules;

import java.util.regex.Pattern;

import org.nutz.lang.Strings;

public abstract class AbstractRegexpValidateRule extends AbstractIterableValidateRule {

	@Override
	protected boolean validateSingle(Object value) {
		
		if (value == null || value instanceof String && Strings.isBlank(((String) value)))
			return true;
		
		return getPattern().matcher(String.valueOf(value)).matches();
	}

	abstract protected Pattern getPattern();

}
