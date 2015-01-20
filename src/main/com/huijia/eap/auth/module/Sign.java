package com.huijia.eap.auth.module;

import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.View;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.view.ServerRedirectView;

import com.huijia.eap.GlobalConfig;
import com.huijia.eap.annotation.AuthBy;
import com.huijia.eap.auth.Auths;
import com.huijia.eap.auth.authentication.IAuthenticator;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.data.UserCheckBean;
import com.huijia.eap.auth.user.service.UserService;
import com.huijia.eap.commons.i18n.Bundle;
import com.huijia.eap.commons.i18n.LocalizationUtil;
import com.huijia.eap.commons.mvc.EscapeXml;
import com.huijia.eap.commons.mvc.adaptor.ExtendPairAdaptor;
import com.huijia.eap.commons.mvc.view.exhandler.ExceptionWrapper;
import com.huijia.eap.commons.mvc.view.exhandler.ExceptionWrapper.EC;
import com.huijia.eap.commons.nav.NavigatorHelper;
import com.huijia.eap.listener.UserCounterListener;

@IocBean(name="$startup_signModule")
@InjectName("$startup_signModule")
@AuthBy(module="core", check=false)
public class Sign {

	private static final String SC_KEY_LOGIN_ERROR_CNTS = "-LOGIN-ERROR-COUNTS-";
	
	private IAuthenticator authenticator;

	public IAuthenticator getAuthenticator() {
		return authenticator;
	}

	@Inject("refer:defaultAuthenticator")
	public void setAuthenticator(IAuthenticator authenticator) {
		this.authenticator = authenticator;
	}
	
	@At
	@Ok(">>:/index.jsp")
	@Fail("error:/login.jsp")
	@AuthBy(login=false)
	@AdaptBy(type=ExtendPairAdaptor.class)
	public View signin(@Param("username") String username, @Param("password") String password, @Param("map:infos") Map<String, Object> infos, @Param("lang") String lang, HttpServletRequest request, Ioc ioc, @Param("redirect") String redirect,
			HttpServletResponse response) {
		
		//兼容性处理，如果map:infos和username/password同时存在则以map:infos为准
		if (!infos.containsKey(IAuthenticator.INFO_KEY_USERNAME) && !infos.containsKey(IAuthenticator.INFO_KEY_PASSWORD)) {
			infos.put(IAuthenticator.INFO_KEY_USERNAME, username);
			infos.put(IAuthenticator.INFO_KEY_PASSWORD, password);
			infos.put(IAuthenticator.INFO_KEY_PWD_ENCRYPTED, false);
		}
		
		boolean login = false;
		HttpSession session = request.getSession();
		
		UserService service = ioc.get(UserService.class, "userService");
		User currentUser = Auths.getUser(session);
		Map<String, Object> reloginAttributes = null;
		if (currentUser != null && currentUser.getName().equals(infos.get(IAuthenticator.INFO_KEY_USERNAME))) {
			//如果已经登录，且已登录的用户与当前使用用户相同则不再重复登录
			reloginAttributes = new HashMap<String, Object>();
			for (Enumeration<String> enu = session.getAttributeNames(); enu.hasMoreElements();) {
				String key = enu.nextElement();
				reloginAttributes.put(key, session.getAttribute(key));
			}
		}
		
		infos.put(HttpServletRequest.class.getName(), request);
		infos.put(HttpServletResponse.class.getName(), response);
		boolean authen = false;
		try {
			authen = authenticator.authenticate(infos);
		} finally {
			session.invalidate();
			session = request.getSession(true);
		}

		UserCheckBean uc = GlobalConfig.getContextValueAs(UserCheckBean.class, UserCheckBean.CUPID_CONTEXT_KEY);
		if (uc == null) {
			uc = new UserCheckBean();
			uc.setCountLock(3);
			uc.setTimeLock(5);
		}

		if (authen) {
			User user = service.fetchByName((String) infos.get(IAuthenticator.INFO_KEY_USERNAME));
			authenticator.ok(user, infos);
			if (user.getLockedAt() > 0 && System.currentTimeMillis() - user.getLockedAt() <= uc.getTimeLock() * 60000) {
				EC error = new EC("auth.signin.errors.locked", new Bundle("auth"), user.getName());
				throw ExceptionWrapper.wrapError(error);
			} else {
				
				if (uc.getSessionLock() > 0) {
					int online = 0;
					Map<String, Map<String, User>> onlineUsers = (Map<String, Map<String, User>>) request.getServletContext().getAttribute(UserCounterListener.USER_KEY);
					if (onlineUsers != null) {
						Collection<Map<String, User>> valueSet = onlineUsers.values();
						synchronized (valueSet) {
							Iterator<Map<String, User>> ite = valueSet.iterator();
							while (ite.hasNext()) {
								Map<String, User> hostUsers = ite.next();
								if (hostUsers != null) {
									Collection<User> users = hostUsers.values();
									synchronized (users) {
										Iterator<User> userIte = users.iterator();
										while (userIte.hasNext()) {
											User ou = userIte.next();
											if (user.getUserId() == ou.getUserId())
												online++;
										}
									}
								}
							}
						}
					}

					if (online >= uc.getSessionLock()) {
						EC error = new EC("auth.signin.errors.onlinelimit", new Bundle("auth"), uc.getSessionLock());
						throw ExceptionWrapper.wrapError(error);
					}
				}
				
				if (reloginAttributes != null) {
					for (Map.Entry<String, Object> entry : reloginAttributes.entrySet())
						session.setAttribute(entry.getKey(), entry.getValue());
				} else {

					user.setExt(User.EXTS_LOGINIP, Auths.getUserAddr(request));
					user.setExt(User.EXTS_LOGINTIME, new Date());
				}
				LocalizationUtil.setSessionLocale(request, response, session, lang);
				
				// 登录成功后，加载导航配置
				session.setAttribute(NavigatorHelper.NAV_SESSION_KEY, NavigatorHelper.loadNavigator());
				
				login = true;
				updateLoginErrorCounts(request.getServletContext(), username, true);
				updateLockedAt(service, user, 0);
				
				user.setPassword(User.PASSWORD_FADE);
				session.setAttribute(Auths.USER_SESSION_KEY, user);
				
				if(user.getType() > 0){
					// 普通用户，跳转至测试页面
					return new ServerRedirectView("/quiz/enquizlist");
				}
			}
		} else {//用户名或密码错误
			authenticator.fail(username, infos);
			int cnt = updateLoginErrorCounts(request.getServletContext(), username, false);
			if (cnt >= uc.getCountLock()) {
				
				User loginUser = service.fetchByName(username);
				if (loginUser != null)
					updateLockedAt(service, loginUser, System.currentTimeMillis());
				
				EC error = new EC("auth.signin.errors.locked", new Bundle("auth"), username);
				throw ExceptionWrapper.wrapError(error);
			}
		}
		
		if (login) {	
			if (redirect != null && redirect.trim().length() > 0) {
				int idx = redirect.indexOf("?");
				String path = idx > -1 ? redirect.substring(0, idx) : redirect;
				if (path.startsWith("/") && ( path.endsWith(".jsp") || path.indexOf(".") < 0 ))
					return new ServerRedirectView(EscapeXml.unescape(redirect));
			}
			
			return new ServerRedirectView("/index.jsp");
		}
		
		EC error = new EC("auth.signin.errors.signfailed", new Bundle("auth"));
		throw ExceptionWrapper.wrapError(error);
	}
	
	@At
	@Ok(">>:/login.jsp")
	@Fail("->:/login.jsp")
	@AuthBy(check=false, returnable=false)
	public void signout(HttpServletRequest request, HttpSession session) {
		session.invalidate();
	}
	
	
	
	
	private static synchronized int updateLoginErrorCounts(ServletContext sc, String username, boolean clear) {
		Map<String, Integer> counts = (Map<String, Integer>) sc.getAttribute(SC_KEY_LOGIN_ERROR_CNTS);
		if (counts == null) {
			counts = new HashMap<String, Integer>();
			sc.setAttribute(SC_KEY_LOGIN_ERROR_CNTS, counts);
		}
		Integer cnt = counts.get(username);

		if (clear)
			cnt = 0;
		else if (cnt == null)
			cnt = 1;
		else
			cnt++;
		counts.put(username, cnt);
		
		return cnt;
	}
	
	private static synchronized void updateLockedAt(UserService service, User user, long lockedAt) {
		user.setPassword(User.PASSWORD_FADE);
		user.setLockedAt(lockedAt);
		service.update(user);
	}
	
	public static final synchronized void unlockUser(ServletContext sc, String username) {
		updateLoginErrorCounts(sc, username, true);
		UserService service = Mvcs.getIoc().get(UserService.class, "userService");
		User user = service.fetchByName(username);
		if (user != null) {
			updateLockedAt(service, user, 0);
		}
	}
	
	
	
	
}
