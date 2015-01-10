/**
 
 * Author: liunan
 * Created: 2011-8-18
 */
package com.huijia.eap.commons.mvc.validate.rules;

import java.util.regex.Pattern;

public class TimeCompleteValidateRule extends AbstractRegexpValidateRule {

	private static final Pattern time_pattern = Pattern.compile("^([01][0-9]|2[0123]):([0-5][0-9]):([0-5][0-9])$");

	@Override
	protected Pattern getPattern() {
		return time_pattern;
	}

}
