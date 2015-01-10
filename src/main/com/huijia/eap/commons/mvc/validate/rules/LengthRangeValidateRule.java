/**
 
 * Author: Administrator
 * Created: 2011-5-11
 */
package com.huijia.eap.commons.mvc.validate.rules;

import java.util.Collection;
import java.util.Map;

import org.nutz.lang.Strings;

public class LengthRangeValidateRule extends AbstractRangeValidateRule {

	@Override
	protected boolean range(Object base, Object start, Object stop) {
		
		int min = start == null ? Integer.MIN_VALUE : Integer.parseInt(String.valueOf(start));
		int max = stop == null ? Integer.MAX_VALUE : Integer.parseInt(String.valueOf(stop));
		
		if (base == null)
			return true;
		
		int value;
		if (base.getClass().isArray()) {
			value = ((Object[]) base).length;
		} else if (base instanceof Collection) {
			value = ((Collection<?>) base).size();
		} else if (base instanceof Map) {
			value = ((Map<?, ?>) base).size();
		} else {
			value = Strings.trim(String.valueOf(base)).length();
		}
		
		return value >= min && value <= max;
	}

}
