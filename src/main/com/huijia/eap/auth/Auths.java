package com.huijia.eap.auth;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.nutz.castor.Castors;
import org.nutz.lang.Mirror;
import org.nutz.mvc.Mvcs;

import com.huijia.eap.annotation.AuthBy;
import com.huijia.eap.annotation.AuthResource;
import com.huijia.eap.annotation.DynamicAuthBy;
import com.huijia.eap.annotation.DynamicAuthItem;
import com.huijia.eap.annotation.DynamicAuthModule;
import com.huijia.eap.annotation.AuthBy.AuthType;
import com.huijia.eap.auth.bean.Permission;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.matcher.PermissionMatcher;

public abstract class Auths {
	
	private static final Logger logger = Logger.getLogger(Auths.class);
	
	private static final Pattern intpattern = Pattern.compile("^\\d+$");

	public static final String USER_SESSION_KEY = "current_user";
	
	public static final String DEFAULT_LOGIN_PATH = "/login.jsp";
	public static final String DEFAULT_LOGIN_ENTRY = "/signin";
	
	public static final String AUTH_MODULE_MAPPING_CONTEXT_KEY = "cupid.AUTH_MODULE_MAPPING_CONTEXT_KEY";
	
	/**
	 * 获取当前用户
	 * @param request
	 * @return
	 */
	public static final User getUser(HttpServletRequest request) {
		if (request != null) {
			HttpSession session = request.getSession(false);
			return getUser(session);
		}
		
		return null;
	}
	
	public static final User getUser(HttpSession session) {
		User user = null;
		if (session != null)
			user = (User) session.getAttribute(USER_SESSION_KEY);
		
		return user;
	}
	
	/**
	 * 获取用户IP地址
	 * @param request
	 * @return
	 */
	public static final String getUserAddr(HttpServletRequest request) {
		if (request != null) {
			String ip = request.getHeader("x-forwarded-for"); 
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		        ip = request.getHeader("Proxy-Client-IP"); 
		    } 
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		        ip = request.getHeader("WL-Proxy-Client-IP"); 
		    } 
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		        ip = request.getRemoteAddr(); 
		    } 
		    return ip;
		}
		return null;
	}
	
	
	public static final AuthByChain getAuthByChain(HttpServletRequest request, Method method, Object[] args) {
		AuthBy typeAuthBy = method.getDeclaringClass().getAnnotation(AuthBy.class);
		AuthBy methodAuthBy = method.getAnnotation(AuthBy.class);
		
		DynamicAuthBy typeDynamicAuthBy = method.getDeclaringClass().getAnnotation(DynamicAuthBy.class);
		DynamicAuthBy methodDynamicAuthBy = method.getAnnotation(DynamicAuthBy.class);
		if (methodDynamicAuthBy != null || methodAuthBy == null && typeDynamicAuthBy != null) {
			String mod = null;
			DynamicAuthModule dynamicMod = method.getAnnotation(DynamicAuthModule.class);
			if (dynamicMod == null) {
				Object value = null;
				Annotation[][] paramAnnotations = method.getParameterAnnotations();
				if (paramAnnotations != null) {
					for (int i = 0; dynamicMod == null && i < paramAnnotations.length; i++) {
						if (paramAnnotations[i] == null)
							continue;
						for (int j = 0; j < paramAnnotations[i].length; j++) {
							if (paramAnnotations[i][j] instanceof DynamicAuthModule) {
								dynamicMod = (DynamicAuthModule) paramAnnotations[i][j];
								value = (args != null) ? args[i] : value;
								
								i = paramAnnotations.length;
								break;
							}
						}
					}
				}
				
				if (dynamicMod != null && value != null) {
					if (dynamicMod.value() != null && dynamicMod.value().trim().length() > 0) {
						String key = dynamicMod.value();
						if (value instanceof Map) {
							value = ((Map) value).get(key);
						} else if (value.getClass().isArray() && intpattern.matcher(key).matches()) {
							int idx = Integer.parseInt(key);
							Object[] array = (Object[]) value;
							value = array.length <= idx ? null : array[idx];
						} else
							value = Mirror.me(value).getValue(value, key);
					}
				}
				
				mod = value == null ? null : String.valueOf(value);
			} else if (dynamicMod.value() != null && dynamicMod.value().trim().length() > 0) {
				mod = request.getParameter(dynamicMod.value());
				if (mod == null || mod.trim().length() == 0)
					mod = String.valueOf(request.getAttribute(dynamicMod.value()));
			}
			
			if (dynamicMod == null) {
				throw new IllegalArgumentException("Using @DynamicAuthBy(...), but no @DynamicAuthModule found!");
			} else	if (dynamicMod != null && mod == null) {
				throw new IllegalArgumentException("Using @DynamicAuthBy(...), but @DynamicAuthModule not point to a valid value!");
			}
			
			if (typeDynamicAuthBy != null)
				for (DynamicAuthItem item : typeDynamicAuthBy.value()) {
					if (item.key().equals(mod)) {
						typeAuthBy = item.by();
						break;
					}
				}

			if (methodDynamicAuthBy != null)
				for (DynamicAuthItem item : methodDynamicAuthBy.value()) {
					if (item.key().equals(mod)) {
						methodAuthBy = item.by();
						break;
					}
				}
			
//			if (typeAuthBy == null && methodAuthBy == null) {
//				throw new IllegalArgumentException("Using @DynamicAuthBy(...), but no mapping @AuthBy found by " + dynamicMod.toString());
//			}
		}
		
		return new AuthByChain(methodAuthBy, typeAuthBy);
	}
	
	/**
	 * 根据调用的方法和调用的URL获取Permission对象，即权限验证所需的Module, Action和Resource<br/>
	 * Resource的获取支持从request.parameter、request.attribute和方法参数中获取，详见手册
	 * 
	 * @param request
	 * @param method
	 * @param module
	 * @return
	 */
	public static final Permission[] getPermission(HttpServletRequest request, AuthByChain chain, Method method, Object[] args) {

		String module = chain.getModule();
		if (module == null)
			module = method.getDeclaringClass().getSimpleName();
		AuthType type = chain.getType();

		Object resourceValue = null;
		AuthResource resource = method.getAnnotation(AuthResource.class);
		if (resource == null) {
			Object value = null;
			Annotation[][] paramAnnotations = method.getParameterAnnotations();
			if (paramAnnotations != null) {
				for (int i = 0; resource == null && i < paramAnnotations.length; i++) {
					if (paramAnnotations[i] == null)
						continue;
					for (int j = 0; j < paramAnnotations[i].length; j++) {
						if (paramAnnotations[i][j] instanceof AuthResource) {
							resource = (AuthResource) paramAnnotations[i][j];
							value = (args != null) ? args[i] : value;
							
							i = paramAnnotations.length;
							break;
						}
					}
				}
			}
			
			if (resource != null && value != null) {
				if (resource.value() == null || resource.value().trim().length() == 0) {
					resourceValue = value;
				} else {
					String key = resource.value();
					if (value instanceof Map) {
						resourceValue = ((Map) value).get(key);
					} else if (value.getClass().isArray() && intpattern.matcher(key).matches()) {
						int idx = Integer.parseInt(key);
						Object[] array = (Object[]) value;
						resourceValue = array.length <= idx ? null : array[idx];
					} else
						resourceValue = Mirror.me(value).getValue(value, key);
				}
			}
		} else if (resource.value() != null && resource.value().trim().length() > 0) {
			resourceValue = request.getParameter(resource.value());
			if (resourceValue == null)
				resourceValue = request.getAttribute(resource.value());
		}
		
		if (resource != null && resourceValue == null) {
			throw new IllegalArgumentException("Annotate @AuthResource, got NULL by, PLEASE check if annotation is right!");
		} else if (type == AuthType.TREE && resource == null) {
			throw new IllegalArgumentException("Annotate @AuthBy(type=AuthType.TREE), but no @AuthResource found!");
		}
		
		String moduleName = module == null ? method.getDeclaringClass().getSimpleName() : module;
		List<Module> mapping = (List<Module>) request.getServletContext().getAttribute(AUTH_MODULE_MAPPING_CONTEXT_KEY);
		if (mapping != null) {
			for (Module main : mapping) {
				if (main.search(moduleName) != null) {
					moduleName = main.getId();
					break;
				}
			}
		}
		String action = method.getName();
		
		ArrayList<Permission> permissions = new ArrayList<Permission>();
		if (type == AuthType.NORMAL) {
			Permission permission = new Permission();
			permission.setModule(moduleName);
			permission.setAction(action);
			permission.setResource("*");
			permissions.add(permission);
		} else if (resourceValue.getClass().isArray()) {
			int length = Array.getLength(resourceValue);
			for (int i = 0; i < length; i++) {
				Permission permission = new Permission();
				permission.setModule(moduleName);
				permission.setAction(action);
				permission.setResource(Castors.me().castToString(Array.get(resourceValue, i)));
				permissions.add(permission);
			}
		} else {
			Permission permission = new Permission();
			permission.setModule(moduleName);
			permission.setAction(action);
			permission.setResource(Castors.me().castToString(resourceValue));
			permissions.add(permission);
		}

		return permissions.toArray(new Permission[permissions.size()]);
	}
	
	public static final Permission checkPermissions(User user, Permission[] permissions, Object module, List<PermissionMatcher> matchers, DefaultAuthorizerImpl authenticator) {
		boolean hasPermission = false;
		for (Permission permission : permissions) {
			String resource = permission.getResource();
			if (resource != null && resource.trim().length() > 0 && !resource.trim().equals("*")) {
				
				resource = resource.trim();
				if (module instanceof TreeModule) {
					resource = ((TreeModule) module).getPath(resource);
				} else if (module instanceof DynamicTreeModule) {
					resource = ((DynamicTreeModule) module).getPath(permission.getModule(), resource);
				} else {
					logger.error("Controller[" + module.getClass() + "] is annotated @AuthBy(type=AuthType.TREE), but not implemented TreeModule interface.");
					resource = "/" + resource;
				}
				
				permission.setResource(resource);
			}
			
			if (matchers != null) {
				boolean and = true;
				for (PermissionMatcher matcher : matchers) {//与关系
					if (!authenticator.hasPermission(user, permission, matcher)) {
						and = false;
						break;
					}
				}
				hasPermission = and;
			} else {
				hasPermission = authenticator.hasPermission(user, permission);
			}
			
			if (!hasPermission)
				return permission;
		}
		return null;
	}
		
	
	public static class Module {
		private String id;
		private String name;
		private List<Module> subs;
		private Module parent;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Module getParent() {
			return parent;
		}
		public void setParent(Module parent) {
			this.parent = parent;
		}
		public List<Module> getSubs() {
			return subs;
		}
		public Module addSub(Module sub) {
			if (sub != null) {
				sub.setParent(this);
				if (subs == null) {
					subs = new ArrayList<Module>();
				}
				subs.add(sub);
			}
			return this;
		}
		public Module search(String id) {
			if (this.id.equals(id))
				return this;
			else if (subs != null) {
				for (Module sub : subs) {
					Module found = sub.search(id);
					if (found != null)
						return found;
				}
			}
			
			return null;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Module other = (Module) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}
		
	}
}