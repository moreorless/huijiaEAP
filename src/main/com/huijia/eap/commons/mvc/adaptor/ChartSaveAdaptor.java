package com.huijia.eap.commons.mvc.adaptor;

import java.lang.reflect.Type;

import org.nutz.lang.Lang;
import org.nutz.mvc.adaptor.ParamInjector;
import org.nutz.mvc.annotation.Param;

import com.huijia.eap.commons.mvc.adaptor.injector.ChartImageInjector;


public class ChartSaveAdaptor extends ExtendPairAdaptor {

	@Override
	protected ParamInjector evalInjectorBy(Type type, Param param) {
		Class<?> clazz = Lang.getTypeClass(type);
		if (clazz.isArray() && (Byte.class.isAssignableFrom(clazz.getComponentType()) || byte.class.isAssignableFrom(clazz.getComponentType())) && "chart".equals(param.value()))
			return new ChartImageInjector();
		return super.evalInjectorBy(type, param);
	}

}
