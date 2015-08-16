package com.huijia.eap.quiz.service;

import java.util.List;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.commons.service.TblIdsEntityService;
import com.huijia.eap.quiz.data.NormalScoreConfig;

@IocBean
public class NormalScoreConfigService extends TblIdsEntityService<NormalScoreConfig>{

	@Inject("refer:normalScoreConfigDao")
	public void setConfigDao(Dao dao) {
		setDao(dao);
	}
	
	public List<NormalScoreConfig> getConfigList(){
		return this.query(null, null);
	}
	
}
