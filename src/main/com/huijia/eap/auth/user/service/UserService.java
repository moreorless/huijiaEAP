package com.huijia.eap.auth.user.service;

import java.util.List;
import java.util.regex.Pattern;

import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.user.dao.UserDao;
import com.huijia.eap.commons.mvc.Pager;
import com.huijia.eap.commons.service.TblIdsEntityService;
import com.huijia.eap.quiz.service.CompanyService;
import com.huijia.eap.quiz.service.SegmentService;
import com.huijia.eap.util.DigestUtil;

@IocBean(name = "userService")
public class UserService extends TblIdsEntityService<User> {

	@Inject
	private CompanyService companyService;

	@Inject
	private SegmentService segmentService;

	@Inject("refer:userDao")
	public void setUserDao(Dao dao) {
		setDao(dao);
	}

	public User insert(User user) {
		user.setUserId(getTblMaxIdWithUpdate());
		user.setPassword(DigestUtil.encodeSHA(user.getPassword()));
		user.setCompanyId(segmentService.fetch(user.getSegmentId())
				.getCompanyId());
		return this.dao().insert(user);
	}

	public void update(User user) {
		if ("".equals(user.getPassword())
				|| User.PASSWORD_FADE.equals(user.getPassword())) {
			user.setPassword(fetch(user.getUserId()).getPassword());
		} else {
			user.setPassword(DigestUtil.encodeSHA(user.getPassword()));
		}
		this.dao().update(user);
	}

	public User checkUser(String username, String pwd) {
		return checkUser(username, pwd, false);
	}

	public User checkUser(String username, String pwd, boolean encrypted) {
		if (encrypted) {
			return ((UserDao) this.dao()).checkUser(username, pwd);
		}
		return ((UserDao) this.dao()).checkUser(username,
				DigestUtil.encodeSHA(pwd));
	}

	public User fetchByName(String name) {
		return ((UserDao) this.dao()).fetchByName(name);
	}

	public List<User> fetchBySegmentId(long segmentId) {
		return ((UserDao) this.dao()).fetchBySegmentId(segmentId);
	}

	public void deleteBySegmentId(long segmentId) {
		((UserDao) this.dao()).deleteBySegmentId(segmentId);
	}

	public void deleteByCompanyId(long companyId) {
		((UserDao) this.dao()).deleteByCompanyId(companyId);
	}

	/**
	 * 分页返回所有用户列表
	 */
	public Pager<User> paging(Condition condition, Pager<User> pager) {
		List<User> users = query(condition,
				this.dao().createPager(pager.getPage(), pager.getPageSize()));
		pager.setRecords(this.count(condition));
		pager.setData(users);

		return pager;
	}

	/**
	 * 返回所有用户列表(内置用户,超级管理员除外)
	 */
	public List<User> getUsers() {
		return this.query(Cnd.where("type", ">", 0), null);
	}

	/**
	 * 返回用户列表
	 */
	public List<User> getUsers(Condition condition) {
		return this.dao().query(User.class, condition, null);
	}

	private Pattern where = Pattern.compile("^[ \t]*WHERE[ \t]+",
			Pattern.CASE_INSENSITIVE);

	private Condition mergeCondition(Condition condition) {
		Condition newCondition;
		String con = null;
		if (condition != null)
			con = condition.toSql(getEntity());
		if (con == null) {
			newCondition = Cnd.where("type", ">", 0).and("userId", ">", 1);
		} else if (where.matcher(con).find()) {
			newCondition = Cnd.wrap(con.replaceFirst("^[ \t]*WHERE[ \t]+",
					" WHERE type>0 AND userId>1 AND "));
		} else {
			newCondition = Cnd.wrap(" WHERE type>0 AND userId>1 " + con);
		}
		return newCondition;
	}

	@Override
	public List<User> query(Condition condition, org.nutz.dao.pager.Pager pager) {
		return super.query(mergeCondition(condition), pager);
	}

	@Override
	public int count(Condition condition) {
		return super.count(mergeCondition(condition));
	}

	@Override
	public int count() {
		return this.count(mergeCondition(null));
	}

}
