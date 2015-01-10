/**
 
 * Author: Administrator
 * Created: 2011-5-11
 */
package com.huijia.eap.commons.mvc.validate.rules;

public class IPAfterValidateRule extends AbstractIPRangeValidateRule {

	@Override
	protected IPRange getRange(String b, String v) {
		return new IPRange(v, b);
	}

}
