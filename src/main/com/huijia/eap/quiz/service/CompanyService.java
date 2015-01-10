package com.huijia.eap.quiz.service;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.commons.service.TblIdsEntityService;
import com.huijia.eap.quiz.data.Company;

@IocBean
public class CompanyService extends TblIdsEntityService<Company>{

	@Inject("refer:companyDao")
	public void setCompanyDao(Dao dao) {
		setDao(dao);
	}
	
}
