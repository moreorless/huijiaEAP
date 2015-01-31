package com.huijia.eap;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

import com.huijia.eap.quiz.service.handler.SMSHandler;
import com.huijia.eap.util.LocalAddressUtil;
import com.huijia.eap.util.dao.DaoUtil;

public class MainModuleSetup implements Setup{
private static final Logger logger = Logger.getLogger(MainModuleSetup.class);
	
	private List<SetupListener> setupListeners = new ArrayList<SetupListener>();

	@Override
	public void init(NutConfig config) {
		
		String webdir = new File(config.getAppRoot()).toPath().normalize().toString(); //未使用getCanonicalPath，避免需要捕获异常
		
		// 初始化系统环境变量
		GlobalConfig.addContextValue("context.path", config.getServletContext().getContextPath());
		GlobalConfig.addContextValue("web.dir", webdir);
		GlobalConfig.addContextValue("webinf.dir", webdir + "/WEB-INF");
		GlobalConfig.addContextValue("conf.dir", webdir + "/WEB-INF/_conf");
		GlobalConfig.addContextValue("server.ip", LocalAddressUtil.getIp());
		GlobalConfig.addContextValue("server.hostname", LocalAddressUtil.getHostName());
		GlobalConfig.addContextValue("server.starting", String.valueOf(System.currentTimeMillis()));
		
		//添加短信服务变量
		SMSHandler SMSService = new SMSHandler("1", 1);
		SMSService.initSMSService();
		GlobalConfig.addContextValue("SMS", SMSService);
		
		System.setProperty("conf.dir", webdir + "/WEB-INF/_conf");
		
		//为确保DaoUtil在所有的伴随启动初始化类之前初始化，避免某些$startup_由于名称原因在DaoUtil之前初始化，从而造成调出出错
		DaoUtil.me().setIoc(config.getIoc());
		
		String[] names = config.getIoc().getNames();
		for (String name : names) {
			if (name != null && name.trim().toLowerCase().startsWith("$setup_")) {
				SetupListener listener = config.getIoc().get(SetupListener.class, name);
				setupListeners.add(listener);
				
				logger.info("SetupListener [" + name.replace("$setup_", "") + "] is loading...");
				listener.init(config);
				logger.info("SetupListener [" + name.replace("$setup_", "") + "] has been loaded.");
			}
		}
		
		
	}

	@Override
	public void destroy(NutConfig config) {
		Collections.reverse(setupListeners);
		for (SetupListener listener : setupListeners) {
			listener.destroy(config);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
		}
	}
	
}
