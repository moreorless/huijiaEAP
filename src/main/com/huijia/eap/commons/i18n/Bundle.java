package com.huijia.eap.commons.i18n;

public class Bundle {

	public static final String PREFIX = "i18n_";
	
	public static final String DEFAULT = "main";
	
	public static final Bundle MAIN_BUNDLE = new Bundle(DEFAULT);
	
	private String name;
	
	public Bundle(String name) {
		setName(name);
	}

	public String getName() {
		return PREFIX + name;
	}

	public void setName(String name) {
		this.name = (name == null || name.trim().length() == 0) ? DEFAULT : ( name.startsWith(PREFIX) ? name.substring(PREFIX.length(), name.length()) : name );
	}
	
	public boolean isDefault() {
		return isDefaultBundle(this.name);
	}
	
	public static final boolean isDefaultBundle(String bundle) {
		return DEFAULT.equalsIgnoreCase(bundle);
	}

	@Override
	public String toString() {
		return getName();
	}
}
