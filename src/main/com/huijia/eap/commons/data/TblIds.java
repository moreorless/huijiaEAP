package com.huijia.eap.commons.data;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

@Table("sys_tblids")
public class TblIds {

	@Column("name")
	@Name(casesensitive=false)
	private String name;
	
	@Column("maxid")
	private long maxid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getMaxid() {
		return maxid;
	}

	public void setMaxid(long maxid) {
		this.maxid = maxid;
	}
}
