package com.huijia.eap.auth.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.huijia.eap.auth.bean.Permission;
import com.huijia.eap.auth.bean.Role;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.bean.UserRoleRelation;
import com.huijia.eap.util.DigestUtil;

@IocBean(name="defaultAuthCache", create="init")
public class DataSourceAuthCache implements AuthCache {
	
	private static final Logger logger = Logger.getLogger(DataSourceAuthCache.class);
	
	private static DataSourceAuthCache instance;
	
	private Dao dao;
	
	private Map<Long, User> userMap = Collections.synchronizedMap(new HashMap<Long, User>());
	private Map<Long, Role> roleMap = Collections.synchronizedMap(new HashMap<Long, Role>());
	private Map<Long, Set<Long>> userRoleRelations = Collections.synchronizedMap(new HashMap<Long, Set<Long>>());
	
	private DataSourceAuthCache() {}
	
	public synchronized static final DataSourceAuthCache getInstance() {
		if (instance == null)
			instance = new DataSourceAuthCache();
		return instance;
	}
	
	public void init() {
		List<User> users = dao.query(User.class, null, null);
		for (User user : users) {
			userMap.put(user.getUserId(), user);
		}
		List<Role> roles = dao.query(Role.class, null, null);
		dao.fetchLinks(roles, "permissions");
		for (Role role : roles) {
			roleMap.put(role.getRoleId(), role);
		}
		List<UserRoleRelation> userroles = dao.query(UserRoleRelation.class, null, null);
		for (UserRoleRelation relation : userroles) {
			Set<Long> relatedRoles = userRoleRelations.get(relation.getUserId());
			if (relatedRoles == null) {
				relatedRoles = Collections.synchronizedSet(new HashSet<Long>());
				userRoleRelations.put(relation.getUserId(), relatedRoles);
			}
				
			relatedRoles.add(relation.getRoleId());
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("users size " + userMap.size());
			logger.debug("roles size " + roleMap.size());
		}
	}

	@Inject
	public void setDao(Dao dao) {
		this.dao = dao;
	}
	
	@Override
	public void updateUserListener(User user) {
		if (user != null) {
			userMap.put(user.getUserId(), user);
			
			if (logger.isDebugEnabled())
				logger.debug("User [" + user.getName() + "] has been updated!");
		}
	}

	@Override
	public void deleteUserListener(long userId) {
		if (userMap.containsKey(userId)) {
			userMap.remove(userId);
			
			if (logger.isDebugEnabled())
				logger.debug("User [" + userId + "]" + userId + "] has been deleted!");
		}
	}

	@Override
	public void upddateRoleListener(Role role) {
		if (role != null) {
			roleMap.put(role.getRoleId(), role);
			
			if (logger.isDebugEnabled())
				logger.debug("Role [" + role.getName() + "] has been updated!");
		}
	}

	@Override
	public  void deleteRoleListener(long roleId) {
		if (roleMap.containsKey(roleId)) {
			roleMap.remove(roleId);
		}
		
		if (logger.isDebugEnabled())
			logger.debug("Role [" + roleId + "] has been removed!");
	}

	@Override
	public  void updateRelationListener(UserRoleRelation relation) {
		if (relation != null) {
			Set<Long> roles = userRoleRelations.get(relation.getUserId());
			if (roles == null) {
				roles = new HashSet<Long>();
				userRoleRelations.put(relation.getUserId(), roles);
			}
			roles.add(relation.getRoleId());
			
			if (logger.isDebugEnabled())
				logger.debug("UserRoleRelation [" + relation.getUserId() + "-" + relation.getRoleId() + "] has been updated!");
		}
	}

	@Override
	public synchronized void updateRelationByUserListener(long userId, Set<Long> roleIds) {
		if (userRoleRelations.containsKey(userId))
			userRoleRelations.get(userId).clear();
		else
			userRoleRelations.put(userId, new HashSet<Long>());
		if (roleIds != null && !roleIds.isEmpty()) {
			userRoleRelations.get(userId).addAll(roleIds);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("User [" + userId + "] related to Roles " + Arrays.toString(roleIds.toArray()));
		}
	}
	
	@Override
	public synchronized void updateRelationByRoleListener(long roleId, Set<Long> userIds) {
		if (userIds == null || userIds.isEmpty())
			return;
		
		for (Map.Entry<Long, Set<Long>> entry : userRoleRelations.entrySet()) {
			Long userId = entry.getKey();
			Set<Long> roleIds = entry.getValue();
			
			if (userIds.contains(userId)) {
				if (roleIds == null) {
					roleIds = new HashSet<Long>();
					userRoleRelations.put(userId, roleIds);
				}
				
				roleIds.add(roleId);
			} else if (roleIds != null) {
				roleIds.remove(roleId);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Role [" + roleId + "] related to Users " + Arrays.toString(userIds.toArray()));
		}
	}

	@Override
	public synchronized void deleteRelationListener(long relationId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValidUser(long userId) {
		return userMap.containsKey(userId);
	}

	@Override
	public boolean isValidRole(long roleId) {
		return roleMap.containsKey(roleId);
	}

	@Override
	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		users.addAll(userMap.values());
		return users;
	}

	@Override
	public List<Role> getRoles() {
		List<Role> roles = new ArrayList<Role>();
		roles.addAll(roleMap.values());
		return roles;
	}

	@Override
	public List<Role> getRolesByUser(long userId) {
		if (isValidUser(userId)) {
			List<Role> roles = new ArrayList<Role>();
			
			Set<Long> roleIds = new HashSet<Long>();
			if (userRoleRelations.containsKey(userId))
				roleIds.addAll(userRoleRelations.get(userId));
			for (Long roleId : roleIds) {
				if (roleMap.containsKey(roleId.longValue()))
					roles.add(roleMap.get(roleId.longValue()));
			}
			return roles;
		}
		return null;
	}
	
	@Override
	public List<Permission> getPermissionsByUser(long userId) {
		List<Permission> permissions = new ArrayList<Permission>();
		
		List<Role> roles = getRolesByUser(userId);
		if (roles != null) {
			for (Role role : roles) {
				if (role.getPermissions() != null)
					permissions.addAll(role.getPermissions().values());
			}
		}
		
		return permissions;
	}

	@Override
	public List<Permission> getPermissionsByRole(long roleId) {
		List<Permission> permissions = new ArrayList<Permission>();
		
		Role role = null;
		if (roleMap.containsKey(roleId)) {
			role = roleMap.get(roleId);
		}
		if (role != null) {
			permissions.addAll(role.getPermissions().values());
		}
		
		return permissions;
	}

	@Override
	public User getUser(long userId) {
		return userMap.get(userId);
	}

	@Override
	public List<Long> getAvailableUserIds() {
		List<Long> userIds = new ArrayList<Long>();
		userIds.addAll(userMap.keySet());
		return userIds;
	}

	@Override
	public User checkUser(long userId, String pwd, boolean encrypted) {
		if (isValidUser(userId)) {
			User user = getUser(userId);
			return user.getPassword().equals(encrypted ? pwd : DigestUtil.encodeSHA(pwd)) ? user : null;
		}
		return null;
	}

	@Override
	public User checkUser(String username, String pwd, boolean encrypted) {
		List<User> users = getUsers();
		for (User user : users) {
			if (user.getName().equals(username)) {
				return checkUser(user.getUserId(), pwd, encrypted);
			}
		}
		return null;
	}

}
