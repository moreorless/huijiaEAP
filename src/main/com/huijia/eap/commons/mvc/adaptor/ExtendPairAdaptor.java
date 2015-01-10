/**
 
 * Author: Administrator
 * Created: 2011-3-24
 */
package com.huijia.eap.commons.mvc.adaptor;

import java.lang.reflect.Type;

import org.nutz.mvc.adaptor.PairAdaptor;
import org.nutz.mvc.adaptor.ParamInjector;
import org.nutz.mvc.annotation.Param;

import com.huijia.eap.commons.mvc.adaptor.injector.MapFieldInjector;


public class ExtendPairAdaptor extends PairAdaptor {

	public static final String indicator = "map:";
	
	@Override
	protected ParamInjector evalInjectorBy(Type type, Param param) {
		
		if (param != null && param.value().startsWith(indicator)) {
			String prefix = param.value().substring(indicator.length());
			return new MapFieldInjector(prefix, type);
		}
		
		return super.evalInjectorBy(type, param);
	}

}
