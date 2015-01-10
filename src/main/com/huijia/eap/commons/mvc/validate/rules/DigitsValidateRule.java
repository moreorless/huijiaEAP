/**
 
 * Author: Administrator
 * Created: 2011-5-11
 */
package com.huijia.eap.commons.mvc.validate.rules;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import org.nutz.lang.Strings;

public class DigitsValidateRule extends AbstractRegexpValidateRule {

	private static final Pattern digits_pattern = Pattern.compile("^\\d+$");

	@Override
	protected boolean validateSingle(Object value) {
		if (value instanceof BigDecimal) {
			String digits = ((BigDecimal) value).toPlainString();
			return super.validateSingle(digits);
		}else if (value instanceof Number) {
			if (value instanceof Double || value instanceof Float)
				return false;
		} else if (value != null || value instanceof String && !Strings.isBlank(((String) value))) {
			String digits = String.valueOf(value);
			return super.validateSingle(digits);
		}
		
		return true;
	}

	@Override
	protected Pattern getPattern() {
		return digits_pattern;
	}

}
