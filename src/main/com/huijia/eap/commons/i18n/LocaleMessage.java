package com.huijia.eap.commons.i18n;

public class LocaleMessage {
	protected String bundle;
	protected String key;
	protected Object[] args;
	protected String def;
	public String getBundle() {
		return bundle;
	}
	public void setBundle(String bundle) {
		this.bundle = bundle;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
	public String getDef() {
		return def;
	}
	public void setDef(String def) {
		this.def = def;
	}

}
