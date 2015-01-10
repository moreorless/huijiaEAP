package com.huijia.eap.commons.ioc;

public class StaticMethodInvokingProxyBean {

	private String type;
	
	private String method;
	
	private Object[] args;

	public StaticMethodInvokingProxyBean(String type, String method, Object... args) {
		this.type = type;
		this.method = method;
		this.args = args;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}
	
}
