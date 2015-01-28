package com.huijia.eap.quiz.data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

/**
 * 临时用户，第一次添加号段时插入，用户注册后将用户信息加入auth_user表，并在该表中删除用户
 * 
 * @author jianglei
 * 
 */
@Table("user_temp")
public class UserTemp {

	@Column
	private String code;

	@Column
	private long segmentId;

	@Column
	private long companyId;

	@Column
	private String password;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(long segmentId) {
		this.segmentId = segmentId;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
