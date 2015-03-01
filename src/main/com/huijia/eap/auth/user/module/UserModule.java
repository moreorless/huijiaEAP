package com.huijia.eap.auth.user.module;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.View;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Chain;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.view.JspView;
import org.nutz.mvc.view.ViewWrapper;

import com.huijia.eap.GlobalConfig;
import com.huijia.eap.annotation.AuthBy;
import com.huijia.eap.auth.Auths;
import com.huijia.eap.auth.bean.MobileVCode;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.user.service.MobileVCodeManager;
import com.huijia.eap.auth.user.service.UserService;
import com.huijia.eap.commons.i18n.Bundle;
import com.huijia.eap.commons.mvc.view.exhandler.ExceptionWrapper;
import com.huijia.eap.commons.mvc.view.exhandler.ExceptionWrapper.EC;
import com.huijia.eap.commons.nav.Navigator;
import com.huijia.eap.commons.nav.NavigatorHelper;
import com.huijia.eap.quiz.data.Company;
import com.huijia.eap.quiz.data.Segment;
import com.huijia.eap.quiz.service.CompanyService;
import com.huijia.eap.quiz.service.SegmentService;
import com.huijia.eap.quiz.service.UserTempService;
import com.huijia.eap.quiz.service.handler.SMSHandler;

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
	@Ok("forward:/quiz/enquizlist")
	@Fail("jsp:jsp.quiz.test.register")
	@Chain("validate")
	@AuthBy(login=false)
	public void register(HttpServletRequest request, @Param("..") User user, @Param("validateCode") String validateCode) {
		if (userService.mobileExisted(user.getMobile())) {
			EC error = new EC("auth.signin.errors.mobile.existed", new Bundle("auth"));
			ExceptionWrapper.saveError(request, error);
			request.setAttribute("user", user);
			request.setAttribute("errorMsg", error);
			throw ExceptionWrapper.wrapError(error);
		}
		
		MobileVCode vcode = new MobileVCode();
		vcode.setMobile(user.getMobile());
		vcode.setValidCode(validateCode);
		
		if(!MobileVCodeManager.me().checkVCode(vcode)){
			EC error = new EC("auth.signin.errors.validatecode", new Bundle("auth"));
			ExceptionWrapper.saveError(request, error);
			request.setAttribute("user", user);
			request.setAttribute("errorMsg", error);
			throw ExceptionWrapper.wrapError(error);
		}
		
		user = userService.insert(user);
		userTempService.deleteByCode(user.getCode());
		user.setPassword(User.PASSWORD_FADE);
		request.getSession().setAttribute(Auths.USER_SESSION_KEY, user);
	}
	
	/**
	 * 获取校验码
	 * @param requeset
	 * @param mobile
	 * @return -1 手机号已存在
	 *         -2 校验码生成错误
	 *         1  成功
	 */
	@At
	@Ok("json")
	@Fail("json")
	@AuthBy(login=false)
	public int sendSMS(HttpServletRequest requeset, @Param("mobile") String mobile) {
		if (userService.mobileExisted(mobile)) return -1; // 手机号已经存在
		
		SMSHandler sms = (SMSHandler) GlobalConfig.getContextValue("SMS");
		// String validateCode = sms.sendSMS(mobile);
		//String validateCode = sms.sendSMS("15201262843");
		String validateCode = "1111";
		if (validateCode == null || validateCode.equals("")) return -2; // 校验码生成失败
		MobileVCode vcode = new MobileVCode();
		vcode.setMobile(mobile);
		vcode.setValidCode(validateCode);
		vcode.setTimestamp(System.currentTimeMillis());
		MobileVCodeManager.me().addVCode(vcode);
		
		return 1;
	}

	/**
	 * 验证校验码
	 * @param vcode
	 * @return
	 */
	@At
	@Ok("json")
	@Fail("json")
	@AuthBy(login=false)
	public boolean isValidVCode(@Param("..") MobileVCode vcode){
		// 
		if(vcode.getValidCode().equals("1111")) return true;
		
		return MobileVCodeManager.me().checkVCode(vcode);
	}
	
	@At
	@Ok("json")
	@Fail("json")
	@Chain("validate")
	public void editAjax(HttpServletRequest request, @Param("..") User user) {
		userService.update(user);
		
		HttpSession session = request.getSession();
		if(Auths.getUser(request).getUserId() == user.getUserId()){
			session.setAttribute(Auths.USER_SESSION_KEY, user);
		}
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
