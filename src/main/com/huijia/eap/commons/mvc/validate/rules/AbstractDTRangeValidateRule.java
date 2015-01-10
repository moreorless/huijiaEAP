/**
 
 * Author: Administrator
 * Created: 2011-5-11
 */
package com.huijia.eap.commons.mvc.validate.rules;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.nutz.lang.Strings;

public abstract class AbstractDTRangeValidateRule extends
		AbstractComparableValidateRule {

	protected static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	protected class DateRange {
		Date start;
		Date stop;
		
		DateRange(String start, String stop) throws ParseException {
			this.start = sdf.parse(start);
			this.stop = sdf.parse(stop);
		}
	}

	@Override
	protected boolean compare(Object base, Object value) {
		String b = base == null ? null : String.valueOf(base);
		String v = value == null ? null : String.valueOf(value);
		if (Strings.isBlank(b) || Strings.isBlank(v))
			return false;
			
		try {
			DateRange range = getRange(b, v);
			return range.start.getTime() < range.stop.getTime();
		} catch (ParseException e) {
			return false;
		}
	}
	
	abstract protected DateRange getRange(String base, String value) throws ParseException;
	
}
