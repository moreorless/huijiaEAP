package com.huijia.eap;

import org.nutz.mvc.NutConfig;
import org.nutz.mvc.UrlMapping;
import org.nutz.mvc.impl.NutLoading;

import com.huijia.eap.commons.i18n.JstlLocalizationAdaptor;


public class CupidNutLoading extends NutLoading {
	
	private JstlLocalizationAdaptor adaptor;

	@Override
	public UrlMapping load(NutConfig config) {
		adaptor = new JstlLocalizationAdaptor();
		adaptor.init(config);
		return super.load(config);
	}

	@Override
	public void depose(NutConfig config) {
		super.depose(config);
		adaptor.destroy(config);
	}

}
