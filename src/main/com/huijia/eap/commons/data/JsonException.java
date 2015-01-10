package com.huijia.eap.commons.data;

public abstract class JsonException extends Exception {

	protected Class<?> clazz = getClass();
	
	public Class<?> getClazz() {
		return clazz;
	}

	public JsonException() {
		super();
	}

	public JsonException(String message, Throwable cause) {
		super(message, cause);
	}

	public JsonException(String message) {
		super(message);
	}

	public JsonException(Throwable cause) {
		super(cause);
	}
}
