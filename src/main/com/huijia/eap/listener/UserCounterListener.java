package com.huijia.eap.listener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.huijia.eap.auth.Auths;
import com.huijia.eap.auth.bean.User;


/**
 * 用户登录、注销监听器，维护在线用户列表
 * @author liunan
 *
 */
public class UserCounterListener implements ServletContextListener,
		HttpSessionAttributeListener, HttpSessionListener, HttpSessionActivationListener
{
    public static final String COUNT_KEY = "CURRENT_ONLINE_USERS_COUNT";
    public static final String USER_KEY = "ONLINE_USERS";
    private transient ServletContext ctx;
	private int userCount;
	private Map<String, Map<String, User>> users;

	public synchronized void contextInitialized(ServletContextEvent sce)
	{
		ctx = sce.getServletContext();
		ctx.setAttribute(COUNT_KEY, Integer.toString(userCount));
	}

	public synchronized void contextDestroyed(ServletContextEvent event)
	{
		ctx = null;
		userCount = 0;
		users = null;
	}
	
	/**
	 * 用户登录
	 * @param user
	 */
	private synchronized void addUser(User user, String sessionId)
	{
		String ip = (String) user.getExt(User.EXTS_LOGINIP);
		if (ip != null && ip.trim().length() > 0)
		{
			users = (Map) ctx.getAttribute(USER_KEY);
			if (users == null)
				users = Collections.synchronizedMap(new HashMap<String, Map<String, User>>());
			
			Map<String, User> hostUsers = users.get(ip);
			if (hostUsers == null)
			{
				hostUsers = new HashMap<String, User>();
				users.put(ip, hostUsers);
			}
			
			boolean increase = true;
			if (hostUsers.containsKey(sessionId))
				increase = false;
			hostUsers.put(sessionId, user);
			ctx.setAttribute(USER_KEY, users);
			
			if (increase)
			{
				userCount = Integer.parseInt((String) ctx.getAttribute(COUNT_KEY));
				userCount++;
				ctx.setAttribute(COUNT_KEY, Integer.toString(userCount));
			}
		}
	}
	
	/**
	 * 登录用户信息修改
	 * @param user
	 */
	private synchronized void updateUser(User user, String sessionId)
	{
		String ip = (String) user.getExt(User.EXTS_LOGINIP);
		users = (Map) ctx.getAttribute(USER_KEY);
		if (users != null && users.containsKey(ip))
		{
			Map<String, User> hostUsers = users.get(ip);
			if (hostUsers == null)
				return;
			
			if (hostUsers.containsKey(sessionId))
			{
				hostUsers.put(sessionId, user);
				users.put(ip, hostUsers);
				ctx.setAttribute(USER_KEY, users);
			}
		}
	}
	
	/**
	 * 注销用户
	 * @param user
	 */
	private synchronized void removeUser(User user, String sessionId)
	{
		String ip = (String) user.getExt(User.EXTS_LOGINIP);
		users = (Map) ctx.getAttribute(USER_KEY);
		if (users != null && users.containsKey(ip))
		{
			Map<String, User> hostUsers = users.get(ip);
			if (hostUsers == null)
				return;
			
			if (hostUsers.containsKey(sessionId))
			{
				User removed = hostUsers.remove(sessionId);
				users.put(ip, hostUsers);
				ctx.setAttribute(USER_KEY, users);
				
				userCount = Integer.parseInt((String) ctx.getAttribute(COUNT_KEY));
				userCount--;
				ctx.setAttribute(COUNT_KEY, Integer.toString(userCount));
			}
		}
	}
	
	private synchronized void removeUserBySessionId(String sessionId)
	{
		if (users == null || users.isEmpty())
			return;
		
		Map.Entry<String, Map<String, User>> entry;
		String ip;
		Map<String, User> hostUsers;
		Set<Map.Entry<String, Map<String, User>>> entrySet = users.entrySet();
		synchronized (entrySet) {
			Iterator<Map.Entry<String, Map<String, User>>> ite = entrySet.iterator();
			while (ite.hasNext())
			{
				entry = ite.next();
				ip = entry.getKey();
				hostUsers = entry.getValue();
				if (hostUsers.containsKey(sessionId))
				{
					User removed = hostUsers.remove(sessionId);
					users.put(ip, hostUsers);
					ctx.setAttribute(USER_KEY, users);
					
					userCount = Integer.parseInt((String) ctx.getAttribute(COUNT_KEY));
					userCount--;
					ctx.setAttribute(COUNT_KEY, Integer.toString(userCount));
				}
			}
		}
	}

	public void attributeAdded(HttpSessionBindingEvent event)
	{
		if (event.getName().equals(Auths.USER_SESSION_KEY) && event.getValue() instanceof User)
		{
			addUser((User) event.getValue(), event.getSession().getId());
		}
	}

	public void attributeRemoved(HttpSessionBindingEvent event)
	{
		if (event.getName().equals(Auths.USER_SESSION_KEY) && event.getValue() instanceof User)
			removeUser((User) event.getValue(), event.getSession().getId());
	}

	public void attributeReplaced(HttpSessionBindingEvent event)
	{
		if (event.getName().equals(Auths.USER_SESSION_KEY) && event.getValue() instanceof User)
			updateUser((User) event.getValue(), event.getSession().getId());
	}

	public void sessionCreated(HttpSessionEvent arg0)
	{
	}

	public void sessionDestroyed(HttpSessionEvent event)
	{//session过期
		HttpSession session = event.getSession();
		User user = Auths.getUser(session);
		if (user != null)
			removeUser(user, session.getId());
		else if (session != null)
			removeUserBySessionId(session.getId());
	}

	public void sessionDidActivate(HttpSessionEvent arg0)
	{
	}

	public void sessionWillPassivate(HttpSessionEvent event)
	{//手工退出注销用户
		HttpSession session = event.getSession();
		User user = Auths.getUser(session);
		if (user != null)
			removeUser(user, session.getId());
		else if (session != null)
			removeUserBySessionId(session.getId());
	}


}
