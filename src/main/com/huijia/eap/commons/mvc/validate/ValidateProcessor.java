/**
 
 * Author: Administrator
 * Created: 2011-5-3
 */
package com.huijia.eap.commons.mvc.validate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.nutz.castor.Castors;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Mirror;
import org.nutz.lang.Strings;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionInfo;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.impl.processor.AbstractProcessor;

import com.huijia.eap.commons.i18n.Bundle;
import com.huijia.eap.commons.i18n.LocalizationUtil;
import com.huijia.eap.commons.mvc.adaptor.ExtendPairAdaptor;
import com.huijia.eap.commons.mvc.adaptor.injector.MapFieldInjector;
import com.huijia.eap.commons.mvc.validate.annotation.ValidateType;
import com.huijia.eap.commons.mvc.validate.annotation.Validations;
import com.huijia.eap.commons.mvc.validate.annotation.ValidateType.Type;
import com.huijia.eap.commons.mvc.view.exhandler.ExceptionWrapper;
import com.huijia.eap.commons.mvc.view.exhandler.ExceptionWrapper.EC;

/**
 * 校验动作链处理器
 * @author liunan
 *
 */
public class ValidateProcessor extends AbstractProcessor {
	
	private class ValidationInfo {
		Validations validations;
		int argIndex;
		Field field;
		
		
		public ValidationInfo(Validations validations, int argIndex) {
			this.validations = validations;
			this.argIndex = argIndex;
		}

		public ValidationInfo(Validations validations, int argIndex, Field field) {
			this(validations, argIndex);
			this.field = field;
		}
	}
	
	private Method validateMethod;
	
	private Map<String, ValidationInfo> validateFields = new LinkedHashMap<String, ValidationInfo>();
	
	private Map<Integer, Map<String, Set<ValidateType>>> mapValidateFields = new LinkedHashMap<Integer, Map<String, Set<ValidateType>>>();
	
	@Override
	public void init(NutConfig config, ActionInfo ai) throws Throwable {
		Method method = ai.getMethod();
		if (method.getAnnotation(Validations.class) != null) {
			String methodName = method.getAnnotation(Validations.class).value();
			if (methodName == null || methodName.trim().length() == 0) {
				methodName = "validate" + Strings.capitalize(method.getName());
			}
			try {
				Method validateMethod = ai.getModuleType().getMethod(methodName, method.getParameterTypes());
				if (validateMethod != null && List.class.isAssignableFrom(validateMethod.getReturnType()))
					this.validateMethod = validateMethod;
			} catch (Exception e) {
			}
		}
		Annotation[][] annotations = method.getParameterAnnotations();
		Class<?>[] parameters = method.getParameterTypes();
		List<Object[]> params = new ArrayList<Object[]>();
		for (int i = 0; i < annotations.length; i++) {
			Annotation[] paramAnnotations = annotations[i];
			Validations annotationValidations = null;
			Param annotationParam = null;
			for (int j = 0; j < paramAnnotations.length; j++) {
				
				if (Validations.class.isAssignableFrom(paramAnnotations[j].annotationType())) {
					annotationValidations = (Validations) paramAnnotations[j];
				} else if (Param.class.isAssignableFrom(paramAnnotations[j].annotationType())) {
					annotationParam = (Param) paramAnnotations[j];
				}
			}

			if (annotationParam != null) {
				if (annotationValidations != null && annotationParam.value().startsWith(ExtendPairAdaptor.indicator) && Map.class.isAssignableFrom(parameters[i])) {
					Map<String, Set<ValidateType>> pmValidateFields = new LinkedHashMap<String, Set<ValidateType>>();
					mapValidateFields.put(i, pmValidateFields);
					String name = annotationParam.value().substring(4);
					for (ValidateType vt : annotationValidations.rules()) {
						if (Strings.isBlank(vt.value()))
							continue;
						
						String key = name + MapFieldInjector.seperator + vt.value();
						Set<ValidateType> validateTypes = pmValidateFields.get(key);
						if (validateTypes == null) {
							validateTypes = new LinkedHashSet<ValidateType>();
							pmValidateFields.put(key, validateTypes);
						}
						
						validateTypes.add(vt);
					}
				} else if (annotationValidations != null && !"..".equals(annotationParam.value())) {
					String name = annotationParam.value().trim().length() == 0 ? annotationValidations.value().trim() : annotationParam.value().trim();
					validateFields.put(name, new ValidationInfo(annotationValidations, i));
				} else
					params.add(new Object[] {
							annotationParam.value().startsWith("::") ? annotationParam.value().substring(2) : "",
							parameters[i],
							i
					});
			} //TODO: JSONAdaptor的校验
		}

		for (Object[] entry : params) {
			String name = (String) entry[0];
			Class<?> clazz = (Class<?>) entry[1];
			if (clazz.isArray())
				clazz = clazz.getComponentType();
			int argIndex = (Integer) entry[2];
			if (!clazz.isPrimitive() && clazz.getPackage().getName().startsWith("com.huijia.eap.")) {
				Field[] fields = Mirror.me(clazz).getFields();
				for (Field field : fields) {
					Validations annotationValidations = field.getAnnotation(Validations.class);
					if (annotationValidations != null) {
						
						Param annotationParam = field.getAnnotation(Param.class);
						validateFields.put(name + (annotationParam == null ? field.getName() : annotationParam.value()), new ValidationInfo(annotationValidations, argIndex, field));
						
					}
				}
			}
		}
	}

	@Override
	public void process(ActionContext ac) throws Throwable {
		
		HttpServletRequest request = ac.getRequest();
		
		if ("fetch-validate-confs".equals(request.getParameter("validate-type"))) {
			
			Map<String, Map<String, Object>> confs = new HashMap<String, Map<String, Object>>();
			Map<String, Object> rules = new HashMap<String, Object>();
			confs.put("rules", rules);
			Map<String, Object> messages = new HashMap<String, Object>();
			confs.put("messages", messages);
			
			for (Map.Entry<String, ValidationInfo> vi : validateFields.entrySet()) {
				ValidateType[] validateTypes = vi.getValue().validations.rules();
				HashMap<String, Object> fieldRules = new LinkedHashMap<String, Object>();
				HashMap<String, Object> fieldMessages = new LinkedHashMap<String, Object>();
				for (ValidateType vt : validateTypes) {
					Pair pair = getRulePair(vt);
					fieldRules.put(pair.key, pair.value);
					if (vt.errorMsg().trim().length() > 0)
						fieldMessages.put(pair.key, vt.resource() ? LocalizationUtil.getMessage(request, new Bundle(vt.bundle()), vt.errorMsg(), vt.parameters()) : vt.errorMsg());
				}
				rules.put(vi.getKey(), fieldRules);
				messages.put(vi.getKey(), fieldMessages);
			}
			for (Map<String, Set<ValidateType>> form : mapValidateFields.values()) {
				for (Map.Entry<String, Set<ValidateType>> entry : form.entrySet()) {
					ValidateType[] validateTypes = entry.getValue().toArray(new ValidateType[entry.getValue().size()]);
					for (ValidateType vt : validateTypes) {
						if (Strings.isBlank(vt.value()))
							continue;
						HashMap<String, Object> fieldRules = (HashMap<String, Object>) rules.get(entry.getKey());
						if (fieldRules == null) {
							fieldRules = new LinkedHashMap<String, Object>();
							rules.put(entry.getKey(), fieldRules);
						}
						HashMap<String, Object> fieldMessages = (HashMap<String, Object>) messages.get(entry.getKey());
						if (fieldMessages == null) {
							fieldMessages = new LinkedHashMap<String, Object>();
							messages.put(entry.getKey(), fieldMessages);
						}

						Pair pair = getRulePair(vt);
						fieldRules.put(pair.key, pair.value);
						if (vt.errorMsg().trim().length() > 0)
							fieldMessages.put(pair.key, vt.resource() ? LocalizationUtil.getMessage(request, new Bundle(vt.bundle()), vt.errorMsg(), vt.parameters()) : vt.errorMsg());
					}
				}
			}
			
			if (!confs.isEmpty()) {
				Mvcs.write(ac.getResponse(), confs, JsonFormat.compact());
			}
			
			return;
		}
		
		Object[] args = ac.getMethodArgs();
		List<EC> errors = new ArrayList<EC>();
		for (Map.Entry<String, ValidationInfo> entry : validateFields.entrySet()) {
			ValidationInfo vi = entry.getValue();
			
			List<EC> ers;
			Object arg = args[vi.argIndex];
			if (vi.field == null) {
				ers = AnnotationValidator.validate(entry.getKey(), vi.validations.rules(), arg, request);
			} else {
				ers = AnnotationValidator.validate(entry.getKey(), vi.validations.rules(), Mirror.me(arg).getValue(arg, vi.field), request);
			}
			
			if (ers != null && !ers.isEmpty()) errors.addAll(ers);
		}
		for (Map.Entry<Integer, Map<String, Set<ValidateType>>> entry : mapValidateFields.entrySet()) {
			Object map = args[entry.getKey()];
			Map<String, Set<ValidateType>> fieldRules = entry.getValue();
			for (Map.Entry<String, Set<ValidateType>> fieldEntry : fieldRules.entrySet()) {
				ValidateType[] validateTypes = fieldEntry.getValue().toArray(new ValidateType[fieldEntry.getValue().size()]);
				String[] keys = validateTypes[0].value().split(":");
				Object value = map;
				for (String key : keys) {
					if (value instanceof Map) {
						value = ((Map) value).get(key);
					}
				}
				List<EC> ers = AnnotationValidator.validate(fieldEntry.getKey(), validateTypes, value, request);
				if (ers != null && !ers.isEmpty()) errors.addAll(ers);
			}
		}
		if (validateMethod != null) {
			List<?> methodErrors = (List<?>) validateMethod.invoke(ac.getModule(), args);
			if (methodErrors != null) {
				for (Object e : methodErrors) {
					if (e instanceof EC)
						errors.add((EC) e);
				}
			}
		}
		
		if (injectArg(ac, errors))
			doNext(ac);
		else {
			throw ExceptionWrapper.wrapErrors(errors);
		}
	}
	
	private class Pair {
		String key;
		Object value;
		
		public Pair(String key, Object value) {
			this.key = key;
			this.value = value;
		}
	}
	
	protected Pair getRulePair(ValidateType vt) {
		String key = vt.type().toString();
		String[] parameters = new String[vt.parameters().length];
		for (int i = 0; i < parameters.length; i++) {
			String param = vt.parameters()[i];
			if (param.startsWith("name="))
				parameters[i] = "[" + (param.endsWith("'") ? param : "name='" + param.substring(5) + "'") + "]";
			else
				parameters[i] = param;
		}
		Object value = parameters.length == 0 ? true : (parameters.length == 1 ? parameters[0] : parameters);
		switch (vt.type()) {
		case requiredIfChecked:
			key = Type.required.toString();
			value = parameters[0] + ":checked";
			break;
		case requiredIfFilled:
			key = Type.required.toString();
			value = parameters[0] + ":filled";
			break;
		case requiredIfEqualTo:
			key = Type.required.toString();
			value = parameters[0] + "[value=" + parameters[1] + "]";
			break;
		case password:
			key = Type.pwd.toString();
			break;
		}
		
		return new Pair(key, value);
	}
	
	protected boolean injectArg(ActionContext ac, List<EC> errors) {
		boolean inject = false;
		
		if (errors == null || errors.isEmpty())
			return true;
		
		Class<?>[] classes = ac.getMethod().getParameterTypes();
		for (int i = 0; i < classes.length; i++) {
			if (classes[i].isArray() && EC.class.isAssignableFrom(classes[i].getComponentType()) && noParamInjector(ac.getMethod().getParameterAnnotations()[i])) {
				inject = true;
				
				Object[] args = ac.getMethodArgs();
				args[i] = errors.toArray(new EC[errors.size()]);
			}
		}
		
		return inject;
	}
	
	private boolean noParamInjector(Annotation[] annos) {
		if (annos != null) {
			for (Annotation anno : annos) {
				if (Param.class.isAssignableFrom(anno.annotationType()))
					return false;
			}
		}
		
		return true;
	}

}
