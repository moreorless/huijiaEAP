package com.huijia.eap.auth.matcher.method;

import java.io.Serializable;
import java.util.regex.Pattern;

public class PermissionMethod implements Serializable {

	private static final long serialVersionUID = 1L;

	protected char value;
	
	protected String name;
	
	protected String pattern;
	
	protected transient Pattern _pattern;
	
	protected String depends;

	public PermissionMethod() {
		super();
	}

	public PermissionMethod(char value, String name, String pattern) {
		super();
		this.value = value;
		this.name = name;
		setPattern(pattern);
	}

	public PermissionMethod(char value, String name, String pattern, String depends) {
		this(value, name, pattern);
		this.depends = depends;
	}

	public char getValue() {
		return value;
	}

	public void setValue(char value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPattern() {
		return pattern;
	}
	
	public Pattern getPatternObject() {
		if (_pattern == null && pattern != null) {
			_pattern = Pattern.compile(pattern);
		}
		return _pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getDepends() {
		return depends;
	}

	public void setDepends(String depends) {
		this.depends = depends;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PermissionMethod other = (PermissionMethod) obj;
		if (value != other.value)
			return false;
		return true;
	}
	
}
