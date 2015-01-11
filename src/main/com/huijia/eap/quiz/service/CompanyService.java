package com.huijia.eap.quiz.service;

import java.util.List;

import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.auth.bean.User;
import com.huijia.eap.commons.mvc.Pager;
import com.huijia.eap.commons.service.TblIdsEntityService;
import com.huijia.eap.quiz.data.Company;

@IocBean
public class CompanyService extends TblIdsEntityService<Company>{

	@Inject("refer:companyDao")
	public void setCompanyDao(Dao dao) {
		setDao(dao);
	}
	
	/**
	 * 分页返回所有用户列表
	 */
	public Pager<Company> paging(Condition condition, Pager<Company> pager) {
		List<Company> users = query(condition, this.dao().createPager(pager.getPage(), pager.getPageSize()));
		pager.setRecords(this.count(condition));
		pager.setData(users);
		
		return pager;
	}
	
}
