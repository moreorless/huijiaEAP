/**
 
 * Author: liunan
 * Created: 2011-8-5
 */
package com.huijia.eap.commons.mvc.validate.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

public class RegexpValidateRule implements ValidateRule {

	@Override
	public int[] validate(Object value) {
		if (value != null && value.getClass().isArray()) {
			Object[] array = (Object[]) value;
			if (array.length > 1 && array[0] instanceof Pattern) {
				Pattern pattern = (Pattern) array[0];
				Object values = array[1];
				
				if (values != null) {
					Object[] v;
					if (values.getClass().isArray()) {
						v = (Object[]) array[1];
					} else if (values instanceof Collection) {
						Collection<?> c = (Collection<?>) values;
						v = c.toArray(new Object[c.size()]);
					} else {
						v = new Object[] {values};
					}
					
					ArrayList<Integer> errors = new ArrayList<Integer>();
					for (int i = 0; i < v.length; i++) {
						if (!check(pattern, String.valueOf(v[i]))) {
							errors.add(i);
						}
					}
					int[] result = null;
					if (!errors.isEmpty()) {
						result = new int[errors.size()];
						for (int i = 0; i < result.length; i++)
							result[i] = errors.get(i);
					}
					return result;
				}
			}
		}
		
		return new int[0];
	}
	
	protected boolean check(Pattern pattern, String value) {
		return pattern.matcher(value).matches();
	}

}
