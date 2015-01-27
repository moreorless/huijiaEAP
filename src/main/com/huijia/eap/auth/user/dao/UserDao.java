package com.huijia.eap.auth.user.dao;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.auth.bean.User;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.Segment;

@IocBean(name = "userDao", fields = { "dataSource" })
public class UserDao extends NutDao {

	public User checkUser(String username, String password) {
		return this
				.fetch(User.class,
						Cnd.where("name", "=", username).and("password", "=",
								password));
	}

	public User fetchByName(String username) {
		return this.fetch(User.class, Cnd.where("name", "=", username));
	}

	public List<User> fetchBySegmentId(long segmentId) {
		return this.query(User.class, Cnd.where("segmentid", "=", segmentId));
	}

	public void deleteBySegmentId(long segmentId) {
		this.clear(User.class, Cnd.where("segmentid", "=", segmentId));
	}

	public void deleteByCompanyId(long companyId) {
		this.clear(User.class, Cnd.where("companyid", "=", companyId));
	}
}
