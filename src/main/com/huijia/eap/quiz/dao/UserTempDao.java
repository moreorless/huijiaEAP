package com.huijia.eap.quiz.dao;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.auth.bean.User;
import com.huijia.eap.quiz.data.UserTemp;

@IocBean(name = "userTempDao", fields = { "dataSource" })
public class UserTempDao extends NutDao {

	public List<UserTemp> fetchListBySegmentId(long segmentId) {
		return this.query(UserTemp.class, Cnd
				.where("segmentid", "=", segmentId).asc("code"));
	}

	public void deleteByCompanyId(long companyId) {
		this.clear(UserTemp.class, Cnd.where("companyid", "=", companyId));
	}

	public void deleteBySegmentId(long segmentId) {
		this.clear(UserTemp.class, Cnd.where("segmentid", "=", segmentId));
	}

	public UserTemp checkUser(String code, String password) {
		return this.fetch(UserTemp.class,
				Cnd.where("code", "=", code).and("password", "=", password));
	}

	public UserTemp fetchByCode(String code) {
		return this.fetch(UserTemp.class, Cnd.where("code", "=", code));
	}

	public void deleteByCompanyId(String code) {
		this.clear(UserTemp.class, Cnd.where("code", "=", code));
	}

}
