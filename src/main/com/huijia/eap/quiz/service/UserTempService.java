package com.huijia.eap.quiz.service;

import java.util.List;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.commons.service.TblIdsEntityService;
import com.huijia.eap.quiz.dao.UserTempDao;
import com.huijia.eap.quiz.data.Company;
import com.huijia.eap.quiz.data.UserTemp;
import com.huijia.eap.util.DigestUtil;

@IocBean
public class UserTempService extends TblIdsEntityService<Company> {

	@Inject("refer:userTempDao")
	public void setCompanyDao(Dao dao) {
		setDao(dao);
	}

	public UserTemp insert(UserTemp userTemp) {
		userTemp.setPassword(DigestUtil.encodeSHA(userTemp.getPassword()));
		return this.dao().insert(userTemp);
	}

	public UserTemp fetchByCode(String code) {

		return ((UserTempDao) this.dao()).fetchByCode(code);
	}

	public void update(UserTemp userTemp) {
		this.dao().update(userTemp);
	}

	public void deleteBySegmentId(long segmentId) {
		((UserTempDao) this.dao()).deleteBySegmentId(segmentId);
	}

	public void deleteByCompanyId(long companyId) {
		((UserTempDao) this.dao()).deleteByCompanyId(companyId);
	}

	public List<UserTemp> fetchListBySegmentId(long segmentId) {
		return ((UserTempDao) this.dao()).fetchListBySegmentId(segmentId);
	}

	public UserTemp checkUserTemp(String code, String pwd, boolean encrypted) {
		if (encrypted) {
			return ((UserTempDao) this.dao()).checkUser(code, pwd);
		}
		return ((UserTempDao) this.dao()).checkUser(code,
				DigestUtil.encodeSHA(pwd));
	}

	public void deleteByCode(String code) {
		((UserTempDao) this.dao()).deleteByCompanyId(code);
		
	}

}
