package com.huijia.eap.quiz.data;
import java.util.List;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("quiz")
public class Quiz {

	@Column
	@Id
	private long id;
	
	/**
	 * 名称
	 */
	@Column
	private String name;
	
	/**
	 * 描述
	 */
	@Column
	private String description;
	
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
	private List<Quiz> items;
	
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
	public List<Quiz> getItems() {
		return items;
	}
	public void setItems(List<Quiz> items) {
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
	
	
}
