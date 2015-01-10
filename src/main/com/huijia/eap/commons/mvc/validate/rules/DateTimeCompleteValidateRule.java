/**
 
 * Author: liunan
 * Created: 2011-8-18
 */
package com.huijia.eap.commons.mvc.validate.rules;

import java.util.regex.Pattern;

public class DateTimeCompleteValidateRule extends AbstractRegexpValidateRule {

	private static final Pattern dt_pattern = Pattern.compile("^(\\d{4})-([01]?\\d)-([0123]?\\d) (([01][0-9]|2[0123]):([0-5][0-9]):([0-5][0-9]))$");

	@Override
	protected Pattern getPattern() {
		return dt_pattern;
	}

}
