/**
 
 * Author: Administrator
 * Created: 2011-5-11
 */
package com.huijia.eap.commons.mvc.validate.rules;

import org.nutz.lang.Strings;

import com.huijia.eap.util.IPUtil;

public class IPValidateRule extends AbstractIterableValidateRule {

	@Override
	protected boolean validateSingle(Object value) {
		String ip = value == null ? null : String.valueOf(value);
		if (Strings.isBlank(ip))
			return true;
		return (IPUtil.isValid(ip));
	}


}
