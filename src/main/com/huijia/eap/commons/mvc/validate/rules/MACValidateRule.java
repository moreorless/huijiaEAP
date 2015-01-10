/**
 
 * Author: Administrator
 * Created: 2011-5-11
 */
package com.huijia.eap.commons.mvc.validate.rules;

import java.util.regex.Pattern;

public class MACValidateRule extends AbstractRegexpValidateRule {

	private static final Pattern mac_pattern = Pattern.compile("^(([0-9a-zA-Z]){1,2}[:-]){5}([0-9a-zA-Z]){1,2}");

	@Override
	protected Pattern getPattern() {
		return mac_pattern;
	}

}
