package com.huijia.eap.commons.mvc.ioc;

import java.io.File;
import java.io.IOException;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.Ioc2;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.impl.ScopeContext;
import org.nutz.ioc.loader.combo.ComboIocLoader;
import org.nutz.lang.Lang;
import org.nutz.lang.util.Disks;
import org.nutz.mvc.IocProvider;
import org.nutz.mvc.NutConfig;


public class ComboIocProvider implements IocProvider {

	public Ioc create(NutConfig config, String[] args) {
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.startsWith("../")) {
				File parent = new File(Disks.normalize(getClass().getClassLoader().getResource("").getPath()));
				File file = new File(parent, arg);
				if (file.exists()) {
					try {
						args[i] = file.getCanonicalPath();
					} catch (IOException e) {
					}
				}
			}
		}
		
		try {
			Ioc ioc = new NutIoc(new ComboIocLoader(args), new ScopeContext("app"), "app");
			if (ioc instanceof Ioc2) {
				I18NValueProxyMaker vpm = new I18NValueProxyMaker(config.getServletContext());
				((Ioc2) ioc).addValueProxyMaker(vpm);
			}
			
			return ioc;
		}
		catch (ClassNotFoundException e) {
			throw Lang.wrapThrow(e);
		}
	}

}
