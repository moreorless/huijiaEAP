package com.huijia.eap.commons.i18n;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import org.apache.log4j.Logger;
import org.nutz.mvc.NutConfig;
import org.nutz.resource.NutResource;
import org.nutz.resource.Scans;
import org.nutz.resource.impl.FileResource;

import com.huijia.eap.GlobalConfig;
import com.huijia.eap.SetupListener;
import com.huijia.eap.annotation.JstlLocalization;


public class JstlLocalizationAdaptor implements SetupListener {
	
	private static final Logger logger = Logger.getLogger(JstlLocalizationAdaptor.class);
	
	public static final String DEFAULT_LOCALE = "default";
	
	private static final String pattern = "[.]properties$";
	
	@Override
	public void init(NutConfig config) {
		
		JstlLocalization localization = config.getMainModule().getAnnotation(JstlLocalization.class);
		ResourceBundle bundle = null;
		LocalizationContext context = null;
		if (localization != null && localization.value().trim().length() > 0) {
			
			Map<String, Map<String, LocalizationContext>> i18nLocalizations = new HashMap<String, Map<String, LocalizationContext>>();
			config.getServletContext().setAttribute(this.getClass().getName(), i18nLocalizations);
			Locale[] locales = Locale.getAvailableLocales();
			Set<String> availableLocales = new HashSet<String>();
			for (Locale locale : locales) {
				availableLocales.add(locale.toString());
			}
			availableLocales.add(DEFAULT_LOCALE);
			/*i18nLocalizations.put(DEFAULT_LOCALE, new HashMap<String, LocalizationContext>());
			for (Locale locale : locales) {
				i18nLocalizations.put(locale.toString(), new HashMap<String, LocalizationContext>());
			}*/
			
			String path = localization.value();
			if (path.startsWith("../")) {
				String parent = new File(getClass().getClassLoader().getResource("").getPath()).toPath().normalize().toString();
				File file = new File(parent, path);
				if (file.exists()) {
					path = file.toPath().normalize().toString();
				}
			}
			List<NutResource> resources = Scans.me().scan(path, "^.+" + pattern);
			
			String contextDefaultLocale = config.getServletContext().getInitParameter("javax.servlet.jsp.jstl.fmt.locale");
			if (contextDefaultLocale != null && contextDefaultLocale.trim().length() == 0)
				contextDefaultLocale = null;
			if (contextDefaultLocale != null) {
				GlobalConfig.defaultLocale = (new Locale(contextDefaultLocale));
				config.getServletContext().setAttribute("javax.servlet.jsp.jstl.fmt.locale", contextDefaultLocale);
			}
			
			for (NutResource resource : resources) {
				
				String name = resource.getName();
				if (resource instanceof FileResource) //TODO: remove this after Issue454 fixed
					name = ((FileResource) resource).getFile().getPath().replace("\\", "/");
				String[] segs = name.replaceFirst(pattern, "").split("/");
				String localeName;
				if (contextDefaultLocale != null && contextDefaultLocale.equals(segs[segs.length - 2])) {
					localeName = DEFAULT_LOCALE;
				} else if (availableLocales.contains(segs[segs.length - 2])) {
					localeName = segs[segs.length - 2];
				} else if (localization.value().toLowerCase().indexOf(segs[segs.length - 2]) > -1) {
					if (contextDefaultLocale == null)
						localeName = DEFAULT_LOCALE;
					else
						localeName = "zh_CN";
				} else {
					if (logger.isDebugEnabled())
						logger.debug("Resource [" + name + "] is not under available Locale's name path!");
					
					continue;
				}
				
				Map<String, LocalizationContext> contextMap = i18nLocalizations.get(localeName);
				if (contextMap == null) {
					contextMap = new HashMap<String, LocalizationContext>();
					i18nLocalizations.put(localeName, contextMap);
				}
				
				Bundle bundleName = new Bundle(segs[segs.length - 1]);
//				String oriBundle = segs[segs.length - 1];
//				String bundleName = Bundle.PREFIX + oriBundle;
				
				try {
					bundle = new PropertyResourceBundle(resource.getReader());
				} catch (IOException e) {
					logger.warn("Loading resource [" + name + "] error", e);
				}
				
				context = new LocalizationContext(bundle);
				contextMap.put(bundleName.getName(), context);
				if (DEFAULT_LOCALE.equals(localeName)) {
					config.getServletContext().setAttribute(bundleName.getName(), context);
					if (bundleName.isDefault())
						config.getServletContext().setAttribute("javax.servlet.jsp.jstl.fmt.localizationContext.application", context);
				}
			}
			
			/*if (localization.dynamic().trim().length() > 0) {
				Map<String, Map<String, LocalizationContext>> dynamicLocalizations = new HashMap<String, Map<String, LocalizationContext>>();
				config.getServletContext().setAttribute(this.getClass().getName() + ".dynamic", dynamicLocalizations);
				List<NutResource> dynamicResources = Scans.me().scan(localization.dynamic(), "^.+" + pattern);
				for (NutResource resource : dynamicResources) {
					
					String name = resource.getName();
					if (resource instanceof FileResource) //TODO: remove this after Issue454 fixed
						name = ((FileResource) resource).getFile().getPath().replace("\\", "/");
					String[] segs = name.replaceFirst(pattern, "").split("/");
					String localeName;
					if (localization.dynamic().toLowerCase().indexOf(segs[segs.length - 2]) > -1) {
						localeName = "zh_CN";
					} else if (i18nLocalizations.containsKey(segs[segs.length - 2])) {
						localeName = segs[segs.length - 2];
					} else {
						if (logger.isDebugEnabled())
							logger.debug("Dynamic resource [" + name + "] is not under available Locale's name path!");
						
						continue;
					}

					Map<String, LocalizationContext> contextMap = dynamicLocalizations.get(localeName);
					if (contextMap == null) {
						contextMap = new HashMap<String, LocalizationContext>();
						dynamicLocalizations.put(localeName, contextMap);
					}
					
					Bundle bundleName = new Bundle(segs[segs.length - 1]);
					try {
						bundle = new PropertyResourceBundle(resource.getReader());
					} catch (IOException e) {
						logger.warn("Loading resource [" + name + "] error", e);
					}
					
					context = new LocalizationContext(bundle);
					contextMap.put(bundleName.getName(), context);
				}
			}*/
			
		}

	}

	@Override
	public void destroy(NutConfig config) {
		//do nothing
	}


}
