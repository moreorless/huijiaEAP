package com.huijia.eap;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.nutz.ioc.Ioc;
import org.nutz.lang.Mirror;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionInfo;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.impl.processor.AbstractProcessor;

import com.huijia.eap.annotation.MatchBy;
import com.huijia.eap.auth.AuthByChain;
import com.huijia.eap.auth.Auths;
import com.huijia.eap.auth.DefaultAuthorizerImpl;
import com.huijia.eap.auth.PermissionException;
import com.huijia.eap.auth.authentication.IAuthenticator;
import com.huijia.eap.auth.authentication.WebAuthenticatorWrapper;
import com.huijia.eap.auth.bean.Permission;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.matcher.PermissionMatcher;
import com.huijia.eap.commons.mvc.view.ExceptionJspView;

/**
 * 访问控制
 * @author gaoxl
 *
 */
public class AccessControlProcessor extends AbstractProcessor{

	private static final Logger logger = Logger.getLogger(AccessControlProcessor.class);
	
	protected String path = Auths.DEFAULT_LOGIN_PATH;
	protected String loginEntry = Auths.DEFAULT_LOGIN_ENTRY;
	
	protected DefaultAuthorizerImpl authorizer;
	protected WebAuthenticatorWrapper authenticator;

	@Override
	public void init(NutConfig config, ActionInfo ai) throws Throwable {
		authorizer = config.getIoc().get(DefaultAuthorizerImpl.class, "authorizer");
		authenticator = config.getIoc().get(WebAuthenticatorWrapper.class, "defaultAuthenticator");
	}

	@Override
	public void process(ActionContext ac) throws Throwable {
		HttpServletRequest request = ac.getRequest();
		Method method = ac.getMethod();
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Auths.USER_SESSION_KEY);
		
		AuthByChain authByChain = Auths.getAuthByChain(request, method, ac.getMethodArgs());
		if (authByChain.isEmpty()) {//未设置@AuthBy
			ac.getResponse().setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		List<PermissionMatcher> matchers = getMatchers(authByChain.getMatchBys(), ac.getIoc());
		boolean login = authByChain.isLogin();
		boolean check = authByChain.isCheck();
		boolean returnable = authByChain.isReturnable();
		
		
		if (!login) {
			doNext(ac);
		} else if(user != null){
			// 设置最后一次访问时间
			user.setExt(User.EXTS_LASTACCESSTIME, new Date(session.getLastAccessedTime()));
			
			// 暂时不进行权限校验
			/*
			if (user.getType() != User.TYPE_ROOT && authorizer != null && check) {
				
				Permission[] permissions = Auths.getPermission(request, authByChain, method, ac.getMethodArgs());
				Permission permission = Auths.checkPermissions(user, permissions, ac.getModule(), matchers, authorizer);
				if (permission != null) {
					ExceptionJspView view = new ExceptionJspView(null);
					PermissionException e = new PermissionException(CupidCoreMessages.getString(this.getClass(), "permissionexmsg"));
					e.setUser(user);
					e.setPermission(permission);
					ac.getResponse().setStatus(HttpServletResponse.SC_FORBIDDEN);
					throw e;
				}
			}
			
			if (logger.isDebugEnabled()) {
				logger.debug("User [" + user.getName() + "] is getting through AuthFilter");
			}
			*/
			
			doNext(ac);
		}else{
			// 尚未登录， 跳转至登录页面
			String path = this.path;
			String redirect = null, encodeRedirect = null;
			
			if(!request.isRequestedSessionIdFromURL()){
				// 登录后重定向至登录前页面
				if ("POST".equals(request.getMethod())) {
					String refer = request.getHeader("referer");
					if (refer != null) {
						redirect = refer.substring(refer.indexOf(request.getContextPath()) + request.getContextPath().length());
						try {
							encodeRedirect = URLEncoder.encode(redirect, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							encodeRedirect = URLEncoder.encode(redirect);
						}
						
						path += "?redirect=" + encodeRedirect;
					}
				} else{
					redirect = request.getRequestURI().substring(request.getContextPath().length());
					try {
						encodeRedirect = URLEncoder.encode(redirect, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						encodeRedirect = URLEncoder.encode(redirect);
					}
					
					/*
					String query = request.getQueryString();
					if (query != null && query.trim().length() > 0)
						redirect += "?" + query;
					*/
					Map<String, String> restParams = new LinkedHashMap<String, String>();
					Enumeration<String> enu = request.getParameterNames();
					StringBuilder sb = new StringBuilder();
					while (enu.hasMoreElements())
					{
						String key = enu.nextElement();
						String value = request.getParameter(key);
//						if (!"u".equals(key) && !"p".equals(key))
						if (!authenticator.isRESTParam(key))
						{
							if (sb.length() > 0)
								sb.append("&");
							else sb.append("?");
							sb.append(key + "=" + value);
						} else {
							restParams.put(key, value);
						}
					}	
					redirect = redirect + sb.toString();

					
					// 访问页面中带有用户名 密码信息，直接带入登录页面；采集器远程登录使用
					if(!restParams.isEmpty()){
						StringBuilder queryParams = new StringBuilder();
						for (Map.Entry<String, String> entry : restParams.entrySet()) {
							queryParams.append("&").append(entry.getKey()).append("=").append(entry.getValue());
						}
						
						path = this.loginEntry + "?redirect=" + encodeRedirect + queryParams.toString();
						
						ac.getResponse().sendRedirect(ac.getRequest().getContextPath() + path);
						ac.getResponse().addHeader(IAuthenticator.TYPE_KEY, authenticator.getType());
						return;
					}
				}
			}
			
			/*new ServerRedirectView(path) {

				@Override
				public void render(HttpServletRequest req,
						HttpServletResponse resp, Object obj) throws Exception {
					super.render(req, resp, obj);
					resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				}
			
			}.render(request, ac.getResponse(), null);*/
			
			if (redirect != null)
				request.setAttribute("redirect", redirect);
			ac.getResponse().sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
	}
	
	protected List<PermissionMatcher> getMatchers(MatchBy[] matchBy, Ioc ioc) {
		List<PermissionMatcher> matchers = null;
		if (matchBy != null && matchBy.length > 0) {
			matchers = new ArrayList<PermissionMatcher>();
			for (MatchBy match : matchBy) {
				if (match.ioc() != null && match.ioc().trim().length() > 0)
					matchers.add(ioc.get(match.value(), match.ioc()));
				else
					matchers.add(Mirror.me(match.value()).born());
				
			}
		}
		
		return matchers;
	}
}
