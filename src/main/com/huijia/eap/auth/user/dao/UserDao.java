package com.huijia.eap.auth.user.dao;

import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.auth.bean.User;

@IocBean(name="userDao", fields={"dataSource"})
public class UserDao extends NutDao {

	public User checkUser(String username, String password) {
		return this.fetch(User.class, Cnd.where("name", "=", username).and("password", "=", password));
	}
	
	public User fetchByName(String username) {
		return this.fetch(User.class, Cnd.where("name", "=", username));
	}
	
}
