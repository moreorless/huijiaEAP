/**
 
 * Author: Administrator
 * Created: 2011-5-11
 */
package com.huijia.eap.commons.mvc.validate.rules;

import java.util.ArrayList;
import java.util.Collection;

import org.nutz.lang.Lang;

public abstract class AbstractIterableValidateRule implements ValidateRule {
	
	@Override
	public int[] validate(Object value) {
		
		if (value != null) {
			
			Object[] array;
			if (value.getClass().isArray()) {
				array = (Object[]) value;
			} else if (value instanceof Collection) {
				Collection<?> c = (Collection<?>) value;
				array = c.toArray(new Object[c.size()]);
			} else {
				array = new Object[] {value};
			}
			
			ArrayList<Integer> errors = new ArrayList<Integer>();
			for (int i = 0; i < array.length; i++) {
				if (!validateSingle(array[i]))
					errors.add(i);
			}
			
			int[] result = null;
			if (!errors.isEmpty()) {
				result = new int[errors.size()];
				for (int i = 0; i < result.length; i++)
					result[i] = errors.get(i);
			}
			return result;
		}
		
		return validateSingle(null) ? null : new int[] {0};
	}
	
	abstract protected boolean validateSingle(Object value);

}
