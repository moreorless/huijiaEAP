/**
 
 * Author: Administrator
 * Created: 2011-5-11
 */
package com.huijia.eap.commons.mvc.validate.rules;

import java.text.ParseException;

public class DTAfterValidateRule extends AbstractDTRangeValidateRule {

	@Override
	protected DateRange getRange(String base, String value)
			throws ParseException {
		return new DateRange(value, base);
	}

}
