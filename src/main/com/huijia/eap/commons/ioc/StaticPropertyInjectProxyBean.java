package com.huijia.eap.commons.ioc;

public class StaticPropertyInjectProxyBean {
	private String type;
	
	private String field;
	
	private Object value;
	
	public StaticPropertyInjectProxyBean(String type, String field, Object value) {
		this.type = type;
		this.field = field;
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
