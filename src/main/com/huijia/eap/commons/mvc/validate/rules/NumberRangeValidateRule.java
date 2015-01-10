/**
 
 * Author: Administrator
 * Created: 2011-5-11
 */
package com.huijia.eap.commons.mvc.validate.rules;

import org.nutz.lang.Strings;

public class NumberRangeValidateRule extends
		AbstractRangeValidateRule {
	
	private ValidateRule rule = new NumberValidateRule();
	
	private boolean leftClosed = true;
	
	private boolean rightClosed = true;
	
	public NumberRangeValidateRule() {
		
	}
	
	public NumberRangeValidateRule(boolean leftClosed, boolean rightClosed) {
		this.leftClosed = leftClosed;
		this.rightClosed = rightClosed;
	}
	
	@Override
	protected boolean range(Object base, Object start, Object stop) {
		String min = start == null ? null : String.valueOf(start);
		String max = stop == null ? null : String.valueOf(stop);

		if (base == null || base instanceof String && Strings.isBlank((String) base))
			return true;
		int[] result = rule.validate(base);
		if (result != null && result.length > 0)
			return false;
		if (min == null || min instanceof String && Strings.isBlank((String) min))
			return true;
		result = rule.validate(min);
		if (result != null && result.length > 0)
			return false;
		if (max == null || max instanceof String && Strings.isBlank((String) max))
			return true;
		result = rule.validate(max);
		if (result != null && result.length > 0)
			return false;
		
		
		boolean leftCondition = false, rightCondition = false;
		
		if (!Strings.isBlank(min) && min.indexOf(".") > -1 || !Strings.isBlank(max) && max.indexOf(".") > -1) {
			double lower = min == null ? Double.MIN_VALUE : Double.parseDouble(min);
			double upper = max == null ? Double.MAX_VALUE : Double.parseDouble(max);
			double value = Double.parseDouble(String.valueOf(base));
			
			leftCondition = leftClosed ? value >= lower : value > lower;
			rightCondition = rightClosed ? value <= upper : value < upper;
		} else {
			long lower = min == null ? Long.MIN_VALUE : Long.parseLong(min);
			long upper = max == null ? Long.MAX_VALUE : Long.parseLong(max);
			long value = Long.parseLong(String.valueOf(base));
			
			leftCondition = leftClosed ? value >= lower : value > lower;
			rightCondition = rightClosed ? value <= upper : value < upper;
		}
		
		return leftCondition && rightCondition;
		
	}
	
}
