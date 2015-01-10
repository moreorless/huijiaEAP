/**
 
 * Author: Administrator
 * Created: 2011-5-11
 */
package com.huijia.eap.commons.mvc.validate.rules;

import java.util.Collection;

public abstract class AbstractRangeValidateRule implements ValidateRule {

	@Override
	public int[] validate(Object value) {
		Object base = null, start = null, stop = null;
		if (value != null) {
			Object[] array = new Object[0];
			if (value.getClass().isArray()) {
				array = (Object[]) value;
			} else if (value instanceof Collection) {
				Collection<?> c = (Collection<?>) value;
				array = c.toArray(new Object[c.size()]);
			}

			if (array.length > 0)
				base = array[0];
			if (array.length > 1)
				start = array[1];
			if (array.length > 2)
				stop = array[2];
		}
		
		boolean result = range(base, start, stop);
		return result ? null : new int[] {0};
	}
	
	abstract protected boolean range(Object base, Object start, Object stop);

}
