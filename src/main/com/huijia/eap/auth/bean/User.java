package com.huijia.eap.auth.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import com.huijia.eap.commons.bean.BaseTimedObject;
import com.huijia.eap.commons.mvc.validate.annotation.ValidateType;
import com.huijia.eap.commons.mvc.validate.annotation.ValidateType.Type;
import com.huijia.eap.commons.mvc.validate.annotation.Validations;

@Table("auth_user")
public class User extends BaseTimedObject implements Serializable {

	public static final int STATUS_ENABLE = 1;
	public static final int STATUS_DISABLED = 0;

	public static final int TYPE_ROOT = 0;
	public static final int TYPE_ADMIN = 1;
	public static final int TYPE_USER = 2;
	public static final int TYPE_SUPERUSER = 3;

	public static final String EXTS_LOGINIP = "loginip";
	public static final String EXTS_LOGINTIME = "logintime";
	public static final String EXTS_LASTACCESSTIME = "lastaccesstime";

	public static final String PASSWORD_FADE = "#PassWord#";

	@Column
	@Id(auto = false)
	private long userId;

	@Validations(rules = {
			@ValidateType(type = Type.required, errorMsg = "auth.errors.required.name", resource = true, bundle = "auth"),
			@ValidateType(type = Type.minlength, parameters = { "2" }, errorMsg = "user.add.name.span", resource = true, bundle = "auth"),
			@ValidateType(type = Type.maxlength, parameters = { "64" }, errorMsg = "user.add.name.span", resource = true, bundle = "auth"),
			@ValidateType(type = Type.regexp, parameters = { "^(?!_)(?!.*?_$)[a-zA-Z0-9_]+$" }, errorMsg = "user.add.name.span", resource = true, bundle = "auth") })
	@Column
	private String name;

	@Validations(rules = {
			@ValidateType(type = Type.required, errorMsg = "user.add.realname.span", resource = true, bundle = "auth"),
			@ValidateType(type = Type.maxlength, parameters = { "64" }, errorMsg = "user.add.realname.span", resource = true, bundle = "auth") })
	@Column
	private String realname;

	@Validations(rules = {
			@ValidateType(type = Type.required, errorMsg = "user.add.password.validate.null", resource = true, bundle = "auth"),
			@ValidateType(type = Type.pwd, errorMsg = "user.add.password.validate", resource = true, bundle = "auth") })
	@Column
	private String password;

	@Validations(rules = { @ValidateType(type = Type.email, errorMsg = "user.add.email.validate", resource = true, bundle = "auth") })
	@Column
	private String email;

	@Column
	@Validations(rules = { @ValidateType(type = Type.regexp, parameters = { "^([0-9]{2,4}\\-)?[0-9]{2,8}(\\-[0-9]{1,5})?$" }, errorMsg = "user.add.phone.validate", resource = true, bundle = "auth") })
	private String phone;

	@Column
	@Validations(rules = {
			@ValidateType(type = Type.required, errorMsg = "user.add.mobile.validate", resource = true, bundle = "auth"),
			@ValidateType(type = Type.regexp, parameters = { "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$" }, errorMsg = "user.add.mobile.validate", resource = true, bundle = "auth") })
	private String mobile;
	@Column
	private int status = STATUS_ENABLE;
	@Column
	@Validations(rules = @ValidateType(type = Type.maxlength, parameters = { "256" }, errorMsg = "user.add.description.validate", resource = true, bundle = "auth"))
	private String description;
	@Column
	private int type = TYPE_USER;
	@Column
	private long lastLoginTime;
	@Column
	private long lockedAt;
	@Column
	private String dept;
	@Column
	private String iprestrict;
	@Column
	private String authedNavs; // 授权的模块id集合

	@Column
	private long companyId;

	@Column
	@Validations(rules = { @ValidateType(type = Type.required, errorMsg = "user.add.segment.span", resource = true, bundle = "auth") })
	private long segmentId;

	// 性别: 0女 1男
	@Column
	private long gender;

	// 年龄
	@Validations(rules = {
			@ValidateType(type = Type.required, errorMsg = "user.add.age.span", resource = true, bundle = "auth"),
			@ValidateType(type = Type.min, parameters = { "10" }, errorMsg = "user.add.age.span", resource = true, bundle = "auth"),
			@ValidateType(type = Type.max, parameters = { "100" }, errorMsg = "user.add.age.span", resource = true, bundle = "auth") })
	@Column
	private long age;

	// 工作年限
	@Validations(rules = {
			@ValidateType(type = Type.required, errorMsg = "user.add.workage.span", resource = true, bundle = "auth"),
			@ValidateType(type = Type.min, parameters = { "0" }, errorMsg = "user.add.workage.span", resource = true, bundle = "auth"),
			@ValidateType(type = Type.max, parameters = { "50" }, errorMsg = "user.add.workage.span", resource = true, bundle = "auth") })
	@Column
	private long workage;

	// 教育程度 0:大专及以下 1:本科 2:硕士及以上
	@Column
	@Validations(rules = { @ValidateType(type = Type.required, errorMsg = "user.add.education.span", resource = true, bundle = "auth") })
	private long education;

	// 职位信息 0:普通员工 1:中层管理 2:高层管理
	@Column
	@Validations(rules = { @ValidateType(type = Type.required, errorMsg = "user.add.jobtitle.span", resource = true, bundle = "auth") })
	private long jobtitle;

	// 用户编码
	@Column
	private String code;

	@Validations(rules = {
			@ValidateType(type = Type.required, errorMsg = "auth.signin.errors.validatecode", resource = true, bundle = "auth"),
			@ValidateType(type = Type.regexp, parameters = { "[0-9][0-9][0-9][0-9]" }, errorMsg = "auth.signin.errors.validatecode", resource = true, bundle = "auth") })
	private String validateCode;

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public long getGender() {
		return gender;
	}

	public void setGender(long gender) {
		this.gender = gender;
	}

	public long getAge() {
		return age;
	}

	public void setAge(long age) {
		this.age = age;
	}

	public long getWorkage() {
		return workage;
	}

	public void setWorkage(long workage) {
		this.workage = workage;
	}

	public long getJobtitle() {
		return jobtitle;
	}

	public void setJobtitle(long jobtitle) {
		this.jobtitle = jobtitle;
	}

	public long getEducation() {
		return education;
	}

	public void setEducation(long education) {
		this.education = education;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public long getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(long segmentId) {
		this.segmentId = segmentId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 保存额外信息，不进行持久化，仅作为内存中的保存
	 */
	private transient Map<String, Object> exts = new HashMap<String, Object>();

	public Map<String, Object> getExts() {
		return exts;
	}

	public Object getExt(String key) {
		return exts.get(key);
	}

	public void setExt(String key, Object value) {
		exts.put(key, value);
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getIprestrict() {
		return iprestrict;
	}

	public void setIprestrict(String iprestrict) {
		this.iprestrict = iprestrict;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public long getLockedAt() {
		return lockedAt;
	}

	public void setLockedAt(long lockedAt) {
		this.lockedAt = lockedAt;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getAuthedNavs() {
		return authedNavs;
	}

	public void setAuthedNavs(String authedNavs) {
		this.authedNavs = authedNavs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dept == null) ? 0 : dept.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((iprestrict == null) ? 0 : iprestrict.hashCode());
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result
				+ ((realname == null) ? 0 : realname.hashCode());
		result = prime * result + status;
		result = prime * result + type;
		result = prime * result + (int) (userId ^ (userId >>> 32));
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
		User other = (User) obj;
		if (dept == null) {
			if (other.dept != null)
				return false;
		} else if (!dept.equals(other.dept))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (iprestrict == null) {
			if (other.iprestrict != null)
				return false;
		} else if (!iprestrict.equals(other.iprestrict))
			return false;
		if (mobile == null) {
			if (other.mobile != null)
				return false;
		} else if (!mobile.equals(other.mobile))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (realname == null) {
			if (other.realname != null)
				return false;
		} else if (!realname.equals(other.realname))
			return false;
		if (status != other.status)
			return false;
		if (type != other.type)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

}
