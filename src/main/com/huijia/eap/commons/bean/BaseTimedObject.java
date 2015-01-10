package com.huijia.eap.commons.bean;

import java.io.Serializable;

import org.nutz.dao.entity.annotation.Column;

public abstract class BaseTimedObject implements Serializable {

	@Column("created")
	protected long created;
	@Column("modified")
	protected long modified;
	
	public long getCreated() {
		return created;
	}
	public void setCreated(long created) {
		this.created = created;
	}
	public long getModified() {
		return modified;
	}
	public void setModified(long modified) {
		this.modified = modified;
	}
	
}
