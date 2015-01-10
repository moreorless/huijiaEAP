package com.huijia.eap.auth.cache;

import java.util.List;
import java.util.Set;

import com.huijia.eap.auth.bean.Permission;
import com.huijia.eap.auth.bean.Role;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.bean.UserRoleRelation;

public interface AuthCache {
	
	public boolean isValidUser(long userId);
	public boolean isValidRole(long roleId);
	public User checkUser(long userId, String pwd, boolean encrypted);
	public User checkUser(String username, String pwd, boolean encrypted);
	public User getUser(long userId);
	public List<Long> getAvailableUserIds();
	public List<User> getUsers();
	public List<Role> getRoles();
	public List<Role> getRolesByUser(long userId);
	public List<Permission> getPermissionsByUser(long userId);
	public List<Permission> getPermissionsByRole(long roleId);

	public void updateUserListener(User user);
	public void deleteUserListener(long userId);
	
	public void upddateRoleListener(Role role);
	public void deleteRoleListener(long roleId);
	
	public void updateRelationByUserListener(long userId, Set<Long> roleIds);
	public void updateRelationByRoleListener(long roleId, Set<Long> userIds);
	public void updateRelationListener(UserRoleRelation relation);
	public void deleteRelationListener(long relationId);
}
