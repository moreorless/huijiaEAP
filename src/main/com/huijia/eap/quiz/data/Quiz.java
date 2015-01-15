package com.huijia.eap.quiz.data;
import java.util.List;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import com.huijia.eap.commons.mvc.validate.annotation.ValidateType;
import com.huijia.eap.commons.mvc.validate.annotation.Validations;
import com.huijia.eap.commons.mvc.validate.annotation.ValidateType.Type;

@Table("quiz")
public class Quiz {

	@Column
	@Id(auto=false)
	private long id;
	
	/**
	 * 名称
	 */
	@Column
	@Validations(rules = {@ValidateType(type=Type.required, errorMsg="quiz.add.name.span", resource=true, bundle="auth"),
			@ValidateType(type=Type.minlength, parameters={ "1" }, errorMsg="quiz.add.name.span", resource=true, bundle="auth"),
			@ValidateType(type=Type.maxlength, parameters={ "512" }, errorMsg="quiz.add.name.span", resource=true, bundle="auth")})
	private String name;
	
	/**
	 * 描述
	 */
	@Column
	private String description;
	
	@Column
	private String icon;
	
	@Column
	private long createBy;
	
	@Column
	private long createAt;
	
	@Column
	private long updateBy;
	
	@Column
	private long updateAt;
	
	
	/**
	 * 题目
	 */
	private List<QuizItem> items;
	
	/**
	 * 评价体系
	 */
	private List<QuizEvaluation> evaluations;
	
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
	public List<QuizItem> getItems() {
		return items;
	}
	public void setItems(List<QuizItem> items) {
		this.items = items;
	}
	public long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(long createBy) {
		this.createBy = createBy;
	}
	public long getCreateAt() {
		return createAt;
	}
	public void setCreateAt(long createAt) {
		this.createAt = createAt;
	}
	public long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(long updateBy) {
		this.updateBy = updateBy;
	}
	public long getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(long updateAt) {
		this.updateAt = updateAt;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public List<QuizEvaluation> getEvaluations() {
		return evaluations;
	}
	public void setEvaluations(List<QuizEvaluation> evaluations) {
		this.evaluations = evaluations;
	}
	
	
}
