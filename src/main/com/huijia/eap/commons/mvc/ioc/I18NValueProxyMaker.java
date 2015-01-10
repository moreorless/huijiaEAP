package com.huijia.eap.commons.mvc.ioc;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.nutz.ioc.IocMaking;
import org.nutz.ioc.ValueProxy;
import org.nutz.ioc.ValueProxyMaker;
import org.nutz.ioc.meta.IocValue;
import org.nutz.ioc.val.StaticValue;

import com.huijia.eap.commons.i18n.Bundle;
import com.huijia.eap.commons.i18n.LocaleMessage;
import com.huijia.eap.commons.i18n.LocalizationUtil;


public class I18NValueProxyMaker implements ValueProxyMaker {
	
	private static final Logger logger = Logger.getLogger(I18NValueProxyMaker.class);
	
	private ServletContext sc;

	public I18NValueProxyMaker(ServletContext sc) {
		this.sc = sc;
	}

	@Override
	public String[] supportedTypes() {
		return new String[] { "i18n" };
	}

	@Override
	public ValueProxy make(IocMaking ing, IocValue iv) {
		
		if ("i18n".equals(iv.getType())) {
			LocaleMessage message = (LocaleMessage) iv.getValue();
			String text = LocalizationUtil.getMessage(sc, new Bundle(message.getBundle()), message.getKey(), parseArgs(message.getArgs()));
			if (text == null) {
				if (logger.isDebugEnabled())
					logger.debug("Found no key [" + message.getKey() + "] in bundle[" + message.getBundle() + "], use default[" + message.getDef() + "] instead.");
				text = message.getDef();
			}
			
			return new StaticValue(text);
		}
		
		return null;
	}
	
	private String[] parseArgs(Object[] args) {
		String[] parsed = null;
		if (args != null) {
			parsed = new String[args.length];
			for (int i = 0; i < args.length; i++) {
				if (args[i] instanceof LocaleMessage) {
					LocaleMessage message = (LocaleMessage) args[i];
					parsed[i] = LocalizationUtil.getMessage(sc, new Bundle(message.getBundle()), message.getKey(), parseArgs(message.getArgs()));
					
					if (parsed[i] == null && logger.isDebugEnabled()) {
						logger.debug("Found no key [" + message.getKey() + "] in bundle[" + message.getBundle() + "], use default[" + message.getDef() + "] instead.");
					}
				} else
					parsed[i] = String.valueOf(args[i]);
			}
		}
		return parsed;
	}

}
