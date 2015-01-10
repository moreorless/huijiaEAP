/**
 
 * Author: liunan
 * Created: 2011-9-9
 */
package com.huijia.eap.commons.mvc.validate.rules;

import java.util.regex.Pattern;

public class PasswordValidateRule extends AbstractRegexpValidateRule {
	
	private static final String punct = "`~!@#$%^&*()\\-=_+\\[\\]\\{}|;':\",./<>?";

	@Override
	protected Pattern getPattern() {
		return Pattern.compile("^((?=.*\\d)(?=.*[a-zA-Z])|(?=.*\\d)(?=.*[" + punct + "])|(?=.*[a-zA-Z])(?=.*[" + punct + "]))[0-9a-zA-Z" + punct + "]{8,}$");
	}

}
