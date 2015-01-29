package com.huijia.eap.auth.user.module;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Chain;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.huijia.eap.annotation.AuthBy;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.user.service.UserService;
import com.huijia.eap.commons.nav.Navigator;
import com.huijia.eap.commons.nav.NavigatorHelper;
import com.huijia.eap.quiz.data.Company;
import com.huijia.eap.quiz.data.Segment;
import com.huijia.eap.quiz.service.CompanyService;
import com.huijia.eap.quiz.service.SegmentService;
import com.huijia.eap.quiz.service.UserTempService;

/**
 * 用户管理
 * 
 * @author gaoxl
 * 
 */
@IocBean
@InjectName
@AuthBy(check = false)
@At("/user")
public class UserModule {
	private final static String OPERATION_ADD = "add";
	private final static String OPERATION_EDIT = "edit";
	private final static String OPERATION_READ = "read";

	@Inject
	private UserService userService;

	@Inject
	private CompanyService companyService;

	@Inject
	private SegmentService segmentService;
	
	@Inject
	private UserTempService userTempService;

	@At
	@Ok("jsp:jsp.auth.user.list")
	public void list(HttpServletRequest request) {
		List<User> userList = userService.getUsers();
		request.setAttribute("userList", userList);
	}

	@At
	@Ok("jsp:jsp.auth.user.edit")
	public void prepare(HttpServletRequest request,
			@Param("userId") long userId, @Param("operation") String operation) {
		User user = new User();
		if (OPERATION_EDIT.equals(operation)) {
			user = userService.fetch(userId);

			user.setPassword(User.PASSWORD_FADE);

			String authNavs = user.getAuthedNavs();
			if (authNavs != null) {
				String[] authedNavIds = authNavs.split(";");
				request.setAttribute("authedNavIds", authedNavIds);
			}
		}
		request.setAttribute("user", user);

		List<Navigator> navigators = NavigatorHelper.loadNavigator();
		request.setAttribute("navigators", navigators);

		List<Company> companyList = companyService.query(null, null);
		List<Segment> segmentList = segmentService.query(null, null);
		request.setAttribute("companyList", companyList);
		request.setAttribute("segmentList", segmentList);
	}

	@At
	@Ok("forward:/user/list")
	@Chain("validate")
	public void add(@Param("..") User user) {
		userService.insert(user);
	}

	@At
	@Ok("forward:/user/list")
	@Chain("validate")
	public void edit(@Param("..") User user) {
		userService.update(user);
	}
	
	@At
	@Ok("forward:/signout")
	@Chain("validate")
	public void register(@Param("..") User user) {
		userService.insert(user);
		userTempService.deleteByCode(user.getCode());
	}

	@At
	// @Ok("json")
	@Ok("forward:/quiz/enquizlist")
	@Chain("validate")
	public void editAjax(HttpServletRequest request, @Param("..") User user) {
		userService.update(user);
	}

	@At
	@Ok("forward:/user/list")
	public void delete(@Param("userId") long userId) {
		userService.delete(userId);
	}

	/**
	 * 名称重名验证
	 */
	@At("/isValidName")
	@Ok("json")
	@Fail("json")
	public boolean saveValidName(@Param("name") String name,
			@Param("userId") String userId, @Param("operation") String operation) {

		if (operation == null || operation.trim().length() == 0 || name == null
				|| name.trim().length() == 0)
			return false;

		User user = userService.fetchByName(name);
		switch (operation) {
		case OPERATION_EDIT:
			return user == null || userId != null && userId.matches("^\\d+$")
					&& user.getUserId() == Long.parseLong(userId);
		case OPERATION_ADD:
		default:
			return user == null;
		}
	}
}
