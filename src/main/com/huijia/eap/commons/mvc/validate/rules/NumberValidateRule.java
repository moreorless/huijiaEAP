/**
 
 * Author: Administrator
 * Created: 2011-5-11
 */
package com.huijia.eap.commons.mvc.validate.rules;

import java.util.regex.Pattern;

public class NumberValidateRule extends AbstractRegexpValidateRule {

	private static final Pattern number_pattern = Pattern.compile("^-?(?:\\d+|\\d{1,3}(?:,\\d{3})+)(?:\\.\\d+)?$");

	@Override
	protected Pattern getPattern() {
		return number_pattern;
	}

}
