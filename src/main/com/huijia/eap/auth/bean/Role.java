package com.huijia.eap.auth.bean;

import java.io.Serializable;
import java.util.Map;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.Table;

import com.huijia.eap.commons.bean.BaseTimedObject;

@Table("auth_role")
public class Role extends BaseTimedObject implements Serializable {

	@Column
	@Id(auto=false)
	private long roleId;

	@Column
	private String name;

	@Column
	private String description;
	
	@Many(target=Permission.class, field="roleId", key="permissionId")
	private Map<Long, Permission> permissions;
	
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Map<Long, Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(Map<Long, Permission> permissions) {
		this.permissions = permissions;
	}
	
}
