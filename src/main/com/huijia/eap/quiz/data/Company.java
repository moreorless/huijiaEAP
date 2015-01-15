package com.huijia.eap.quiz.data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import com.huijia.eap.commons.mvc.validate.annotation.ValidateType;
import com.huijia.eap.commons.mvc.validate.annotation.Validations;
import com.huijia.eap.commons.mvc.validate.annotation.ValidateType.Type;

/**
 * 企业
 * @author leon
 *
 */
@Table("company")
public class Company {
	@Column
	@Id(auto=false)
	private long id;
	
	@Column
	@Validations(rules = {@ValidateType(type=Type.required, errorMsg="company.add.name.span", resource=true, bundle="quiz"),
			@ValidateType(type=Type.minlength, parameters={ "1" }, errorMsg="company.add.name.span", resource=true, bundle="quiz"),
			@ValidateType(type=Type.maxlength, parameters={ "512" }, errorMsg="company.add.name.span", resource=true, bundle="quiz")})
	private String name;

	@Column
	@Validations(rules = {@ValidateType(type=Type.required, errorMsg="company.add.code.span", resource=true, bundle="quiz"),
			@ValidateType(type=Type.minlength, parameters={ "4" }, errorMsg="company.add.code.span", resource=true, bundle="quiz"),
			@ValidateType(type=Type.maxlength, parameters={ "4" }, errorMsg="company.add.code.span", resource=true, bundle="quiz"),
			@ValidateType(type=Type.regexp, parameters={ "[A-Z][A-Z][A-Z][A-Z]" }, errorMsg="company.add.code.span", resource=true, bundle="quiz")})
	private String code;

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column
	private String description;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
	
	
}
