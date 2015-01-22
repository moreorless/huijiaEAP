package com.huijia.eap.quiz;

import org.nutz.ioc.Ioc;
import org.nutz.mvc.NutConfig;

import com.huijia.eap.SetupListener;
import com.huijia.eap.quiz.cache.QuizCache;

public class QuizSetupListener implements SetupListener{

	@Override
	public void init(NutConfig config) {
		Ioc ioc = config.getIoc();
		
		// load QuizCache
		ioc.get(QuizCache.class);
	}

	@Override
	public void destroy(NutConfig config) {
		
	}

}
