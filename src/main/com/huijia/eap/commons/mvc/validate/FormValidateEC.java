/**
 
 * Author: Administrator
 * Created: 2011-5-5
 */
package com.huijia.eap.commons.mvc.validate;

import com.huijia.eap.commons.i18n.Bundle;
import com.huijia.eap.commons.mvc.view.exhandler.ExceptionWrapper.EC;

/**
 * 针对表单校验错误的EC类
 * 增加name属性，用于与form中的元素对应显示错误信息
 * 
 * 
 * @see {@link EC}
 * @author liunan
 *
 */
public class FormValidateEC extends EC {
	
	protected String name;
	
	protected int idx;

	public String getName() {
		return name;
	}

	public FormValidateEC setName(String name) {
		this.name = name;
		return this;
	}

	public int getIdx() {
		return idx;
	}

	public FormValidateEC setIdx(int idx) {
		this.idx = idx;
		return this;
	}

	public FormValidateEC(String arg0, boolean arg1) {
		super(arg0, arg1);
	}

	public FormValidateEC(String arg0, Bundle arg1, Object... arg2) {
		super(arg0, arg1, arg2);
	}

	public FormValidateEC(String arg0, Object... arg1) {
		super(arg0, arg1);
	}

}
