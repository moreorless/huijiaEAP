/**
 
 * Author: Administrator
 * Created: 2011-5-11
 */
package com.huijia.eap.commons.mvc.validate.rules;

public class IPBeforeValidateRule extends AbstractIPRangeValidateRule {

	@Override
	protected IPRange getRange(String b, String v) {
		return new IPRange(b, v);
	}

}
