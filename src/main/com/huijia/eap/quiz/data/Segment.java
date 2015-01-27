package com.huijia.eap.quiz.data;

import java.util.LinkedList;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("segment")
public class Segment {

	@Column
	@Id(auto = false)
	private long id;

	@Column
	private long companyId;

	@Column
	private String description;

	@Column
	private short status;

	@Column
	private String expireDate;

	@Column
	private long startId;

	@Column
	private long endId;

	@Column
	private long size;

	@Column
	private String initPassword;

	private LinkedList<Quiz> myQuizList = new LinkedList<Quiz>();

	private String myQuizIds;

	public String getMyQuizIds() {
		return myQuizIds;
	}

	public void setMyQuizIds(String myQuizIds) {
		this.myQuizIds = myQuizIds;
	}

	public String getInitPassword() {
		return initPassword;
	}

	public void setInitPassword(String initPassword) {
		this.initPassword = initPassword;
	}

	public LinkedList<Quiz> getMyQuizList() {
		return myQuizList;
	}

	public void setMyQuizList(LinkedList<Quiz> myQuizList) {
		this.myQuizList = myQuizList;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public long getStartId() {
		return startId;
	}

	public void setStartId(long startId) {
		this.startId = startId;
	}

	public long getEndId() {
		return endId;
	}

	public void setEndId(long endId) {
		this.endId = endId;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
