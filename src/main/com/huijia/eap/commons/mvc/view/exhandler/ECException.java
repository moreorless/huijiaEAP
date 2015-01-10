package com.huijia.eap.commons.mvc.view.exhandler;

import java.util.ArrayList;
import java.util.List;

import com.huijia.eap.commons.mvc.view.exhandler.ExceptionWrapper.EC;

/**
 * 用于抛出需要向终端操作用户告知的错误
 *
 */
public class ECException extends RuntimeException {
	
	private String clazz = getClass().getName();

	private List<EC> errors;

	public ECException() {
		super();
	}

	public ECException(String message, Throwable cause) {
		super(message, cause);
	}

	public ECException(String message) {
		super(message);
	}

	public ECException(Throwable cause) {
		super(cause);
	}
	
	public ECException(List<EC> errors) {
		this.errors = errors;
	}
	
	public ECException setErrors(List<EC> errors) {
		this.errors = errors;
		return this;
	}
	
	public List<EC> getErrors() {
		return this.errors;
	}
	
	public ECException addError(EC error) {
		if (errors == null)
			errors = new ArrayList<EC>();
		
		errors.add(error);
		
		return this;
	}
	
	public ECException addErrors(List<EC> errors) {
		if (this.errors == null)
			this.errors = new ArrayList<EC>();
		
		this.errors.addAll(errors);
		
		return this;
	}
	
}
