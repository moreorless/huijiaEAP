/**
 
 * Author: liunan
 * Created: 2011-5-4
 */
package com.huijia.eap.commons.mvc.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.segment.CharSegment;
import org.nutz.lang.segment.Segment;

import com.huijia.eap.commons.i18n.Bundle;
import com.huijia.eap.commons.mvc.validate.annotation.ValidateType;
import com.huijia.eap.commons.mvc.validate.annotation.ValidateType.Type;
import com.huijia.eap.commons.mvc.validate.rules.DTAfterValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.DTBeforeValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.DateTimeCompleteValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.DateTimeValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.DigitsValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.EmailValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.EqualToValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.IPAfterValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.IPBeforeValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.IPValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.IPv6ValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.LengthRangeValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.MACValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.NotLocalhostIPValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.NumberRangeValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.NumberValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.PasswordValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.PortValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.RegexpValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.RequiredIfCheckValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.RequiredIfEqualToValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.RequiredIfFilledValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.RequiredValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.TimeCompleteValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.TimeValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.UrlValidateRule;
import com.huijia.eap.commons.mvc.validate.rules.ValidateRule;
import com.huijia.eap.commons.mvc.view.exhandler.ExceptionWrapper.EC;

/**
 * 注解校验器验证方法实现
 * TODO: 受限于时间紧张，这个类实现的比较烂，等以后有时间了，需要重构
 * @author liunan
 *
 */
public abstract class AnnotationValidator {
	
	private static final Map<Type, ValidateRule> rules = new HashMap<Type, ValidateRule>();
	static {
		rules.put(Type.required, new RequiredValidateRule());
		rules.put(Type.requiredIfChecked, new RequiredIfCheckValidateRule());
		rules.put(Type.requiredIfFilled, new RequiredIfFilledValidateRule());
		rules.put(Type.requiredIfEqualTo, new RequiredIfEqualToValidateRule());
		rules.put(Type.datetime, new DateTimeValidateRule());
		rules.put(Type.datetimeComplete, new DateTimeCompleteValidateRule());
		rules.put(Type.time, new TimeValidateRule());
		rules.put(Type.timeComplete, new TimeCompleteValidateRule());
		rules.put(Type.digits, new DigitsValidateRule());
		rules.put(Type.dtAfter, new DTAfterValidateRule());
		rules.put(Type.dtBefore, new DTBeforeValidateRule());
		rules.put(Type.email, new EmailValidateRule());
		rules.put(Type.equalTo, new EqualToValidateRule());
		rules.put(Type.ip, new IPValidateRule());
		rules.put(Type.ipAfter, new IPAfterValidateRule());
		rules.put(Type.ipBefore, new IPBeforeValidateRule());
		rules.put(Type.ipv6, new IPv6ValidateRule());
		rules.put(Type.notLocalhostIp, new NotLocalhostIPValidateRule());
		rules.put(Type.mac, new MACValidateRule());
		rules.put(Type.max, new NumberRangeValidateRule());
		rules.put(Type.maxlength, new LengthRangeValidateRule());
		rules.put(Type.min, new NumberRangeValidateRule());
		rules.put(Type.minlength, new LengthRangeValidateRule());
		rules.put(Type.number, new NumberValidateRule());
		rules.put(Type.port, new PortValidateRule());
		rules.put(Type.range, new NumberRangeValidateRule());
		rules.put(Type.rangelength, new LengthRangeValidateRule());
		rules.put(Type.url, new UrlValidateRule());
		rules.put(Type.regexp, new RegexpValidateRule());
		rules.put(Type.password, new PasswordValidateRule());
		rules.put(Type.gt, new NumberRangeValidateRule(false, true));
		rules.put(Type.lt, new NumberRangeValidateRule(true, false));
		rules.put(Type.ge, new NumberRangeValidateRule());
		rules.put(Type.le, new NumberRangeValidateRule());
	}

	public static List<EC> validate(String name, ValidateType[] validateTypes, Object value, HttpServletRequest request) {
		
		ValidateType required = optional(validateTypes);
		List<ValidateType> vts = Lang.array2list(validateTypes);
		
		List<EC> errors = new ArrayList<EC>();
		int[] result = null;
		if (required != null) {
			
			ValidateRule rule = rules.get(required.type());
			if (required.type() == Type.required) {
				result = rule.validate(value);
			} else if (required.type() == Type.requiredIfChecked && required.parameters()[0].startsWith("name=")) {
				String key = required.parameters()[0].substring("name=".length());
				String checked = request.getParameter(key);
				result = rule.validate(new Object[] {value, checked});
			} else if (required.type() == Type.requiredIfFilled && required.parameters()[0].startsWith("name=")) {
				String key = required.parameters()[0].substring("name=".length());
				String filled = request.getParameter(key);
				result = rule.validate(new Object[] {value, filled});
			} else if (required.type() == Type.requiredIfEqualTo && required.parameters()[0].startsWith("name=")) {
				String key = required.parameters()[0].substring("name=".length());
				String depend = request.getParameter(key);
				result = rule.validate(new Object[] {value, depend, required.parameters()[1]});
			}
			
			if (result != null && result.length > 0) {
				for (int idx : result) {
					errors.add(buildEC(name, required, idx));
				}
			}
			
			vts.remove(required);
		}
		
		if (errors.isEmpty() && (value instanceof String ? !Strings.isBlank((String) value) : value != null)) {
			for (ValidateType vt : vts) {
				ValidateRule rule = rules.get(vt.type());
				Object v;
				switch (vt.type()) {
				case dtAfter:
				case dtBefore:
				case equalTo:
				case ipAfter:
				case ipBefore:
				case lt:
				case le:
					v = new Object[] { value, parseNameParam(vt, request) };
					break;
				case gt:
				case ge:
					v = new Object[] { value, null, parseNameParam(vt, request) };
					break;
				case max:
				case maxlength:
					v = new Object[] {value, null, vt.parameters()[0]};
					break;
				case min:
				case minlength:
					v = new Object[] {value, vt.parameters()[0]};
					break;
				case range:
				case rangelength:
					v = new Object[] {value, vt.parameters()[0], vt.parameters()[1]};
					break;
				case regexp:
					if (vt.parameters() != null && vt.parameters()[0] != null) {
						v = new Object[] { Pattern.compile(vt.parameters()[0]), value };
					} else {
						v = null;
					}
					
					break;
				default:
					v = value;
				}
				
				if (rule != null) {
					result = rule.validate(v);
					if (result != null && result.length > 0) {
						for (int idx : result)
							errors.add(buildEC(name, vt, idx));
					}
				}
			}
		}
		
		return errors;
	}
	
	private static ValidateType optional(ValidateType[] validateTypes) {
		ValidateType required = null;
		for (ValidateType vt : validateTypes) {
			if (vt.type() == Type.required
					|| vt.type() == Type.requiredIfChecked
					|| vt.type() == Type.requiredIfFilled) {
				required = vt;
				break;
			}
		}
		
		return required;
	}
	
	public static Object parseNameParam(ValidateType vt, HttpServletRequest request) {
		Object value = null;
		if (vt.parameters()[0] != null && vt.parameters()[0].startsWith("name=")) {
			String key = vt.parameters()[0].substring("name=".length());
			value = request.getParameter(key);
		}
		return value;
	}
	
	private static EC buildEC(String name, ValidateType vt, int idx) {
		if (vt.resource())
			return new FormValidateEC(vt.errorMsg(), new Bundle(vt.bundle()), (Object[]) vt.parameters()).setName(name).setIdx(idx);
		else {
			Segment seg = new CharSegment(vt.errorMsg().replaceAll("\\{\\d+\\}", "\\$$0"));
			for (int i = 0; i < vt.parameters().length; i++) {
				seg.set(String.valueOf(i), vt.parameters()[i]);
			}
			
			return new FormValidateEC(seg.toString(), false).setName(name).setIdx(idx);
		}
	}
}
