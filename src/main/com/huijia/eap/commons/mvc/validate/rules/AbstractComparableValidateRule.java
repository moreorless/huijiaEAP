/**
 
 * Author: Administrator
 * Created: 2011-5-10
 */
package com.huijia.eap.commons.mvc.validate.rules;

import java.util.Collection;

public abstract class AbstractComparableValidateRule implements ValidateRule {
	
	@Override
	public int[] validate(Object value) {
		
		Object b = null, v = value;
		if (value != null) {
			Object[] array = new Object[0];
			if (value.getClass().isArray()) {
				array = (Object[]) value;
			} else if (value instanceof Collection) {
				Collection<?> c = (Collection<?>) value;
				array = c.toArray(new Object[c.size()]);
			}

			if (array.length > 0)
				b = array[0];
			if (array.length > 1)
				v = array[1];
		}
		
		boolean result = compare(b, v);
		return result ? null : new int[] {0};
	}
	
	abstract protected boolean compare(Object base, Object value);

}
