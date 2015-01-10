/**
 
 * Author: Administrator
 * Created: 2011-5-11
 */
package com.huijia.eap.commons.mvc.validate.rules;

import com.huijia.eap.util.IPUtil;

public abstract class AbstractIPRangeValidateRule extends
		AbstractComparableValidateRule {
	
	protected class IPRange {
		long start;
		long stop;
		
		IPRange(String start, String stop) {
			this.start = IPUtil.ip2int(start);
			this.stop = IPUtil.ip2int(stop);
		}
	}

	@Override
	protected boolean compare(Object base, Object value) {
		String b = String.valueOf(base);
		String v = String.valueOf(value);
		
		if (IPUtil.isValid(b) && IPUtil.isValid(v)) {
			IPRange range = getRange(b, v);
			return range.start < range.stop;
		}
		
		return false;
	}
	
	abstract protected IPRange getRange(String b, String v);

}
