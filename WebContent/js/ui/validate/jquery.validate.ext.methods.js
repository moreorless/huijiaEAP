(function($) {
	var dt_pattern = /^(\d{4})-([01]?\d)-([0123]?\d) (([01][0-9]|2[0123]):([0-5][0-9]))$/;
	var dt_complete_pattern = /^(\d{4})-([01]?\d)-([0123]?\d) (([01][0-9]|2[0123]):([0-5][0-9]):([0-5][0-9]))$/;
	var time_pattern = /^([01][0-9]|2[0123]):([0-5][0-9])$/;
	var time_complete_pattern = /^([01][0-9]|2[0123]):([0-5][0-9]):([0-5][0-9])$/;
	var punct = "`~!@#$%^&*()\\-=_+\\[\\]\\{}|;':\",./<>?";
	var password_pattern = new RegExp("^((?=.*\\d)(?=.*[a-zA-Z])|(?=.*\\d)(?=.*[" + punct + "])|(?=.*[a-zA-Z])(?=.*[" + punct + "]))[0-9a-zA-Z" + punct + "]{1,}$");
	
	var ip2int = function(ip) {
		var ipint = 0;
		var numbers = ip.split(".");
		for (var i = 0; i < numbers.length; i++) {
			var n = parseInt(numbers[i], 10);
			if (n < 0 || n > 255) {
				return -1;
			}
			ipint += (n << ((3 - i) * 8)) >>> 0;
		}
		return ipint;
	};
	
	var texts = $.messages([
	                        {key: "text.ui.validate.messages.port", id:"port"},
	                        {key: "text.ui.validate.messages.datetime", id:"datetime"},
	                        {key: "text.ui.validate.messages.datetime.complete", id:"datetimeComplete"}, 
	                        {key: "text.ui.validate.messages.time", id:"time"},
	                        {key: "text.ui.validate.messages.time.complete", id:"timeComplete"},
	                        {key: "text.ui.validate.messages.dtbefore", id:"dtBefore"},
	                        {key: "text.ui.validate.messages.dtafter", id:"dtAfter"},
	                        {key: "text.ui.validate.messages.ip", id:"ip"},
	                        {key: "text.ui.validate.messages.iprange.starterror", id:"ipStart"},
	                        {key: "text.ui.validate.messages.ipragne.stoperror", id:"ipStop"},
	                        {key: "text.ui.validate.messages.iprange.range", id:"ipRange"},
	                        {key: "text.ui.validate.messages.ipv6", id:"ipv6"},
	                        {key: "text.ui.validate.messages.notLocalhostIp", id:"notLocalhostIp"},
	                        {key: 'text.ui.validate.messages.mac', id:"mac"},
	                        {key: "text.ui.validate.messages.regexp", id:"regexp"},
	                        {key: "text.ui.validate.messages.pwd", id:"pwd"},
	                        {key: "text.ui.validate.messages.gt", id:"gt"},
	                        {key: "text.ui.validate.messages.ge", id:"ge"},
	                        {key: "text.ui.validate.messages.lt", id:"lt"},
	                        {key: "text.ui.validate.messages.le", id:"le"},
	                        {key: "text.ui.validate.messages.compare", id:"compare"}
	                        ]);
	
	$.validator.addMethod("port", function(value, element) {
		return this.optional(element) || /^\d+$/.test(value) && parseInt(value) > 0 && parseInt(value) < 65536;
	}, texts.port);
	
	$.validator.addMethod("datetime", function(value, element) {
		return this.optional(element) || dt_pattern.test(value);
	}, texts.datetime);
	
	$.validator.addMethod("datetimeComplete", function(value, element) {
		return this.optional(element) || dt_complete_pattern.test(value);
	}, texts.datetimeComplete);
	
	$.validator.addMethod("time", function(value, element) {
		return this.optional(element) || time_pattern.test(value);
	}, texts.time);
	
	$.validator.addMethod("timeComplete", function(value, element) {
		return this.optional(element) || time_complete_pattern.test(value);
	}, texts.timeComplete);
	
	$.validator.addMethod("dtBefore", function(value, element, param) {
		var stopEl;
		if (typeof param == "string") {
			stopEl = $(param);
		} else if (typeof param == "object") {
			stopEl = $(param.selector);
		}
		if (stopEl.length > 0 && !this.optional(element) && !this.optional(stopEl.get(0))) {
			var start = dt_pattern.exec(value);
			var stop = dt_pattern.exec(stopEl.val());
			return start && stop && new Date(start[1], start[2], start[3], start[5], start[6]).getTime() < new Date(stop[1], stop[2], stop[3], stop[5], stop[6]);
		}
		
		return true;
	}, texts.dtBefore);
	
	$.validator.addMethod("dtAfter", function(value, element, param) {
		if (typeof param == "string") {
			var startEl = $(param);
			if (startEl.length > 0 && !this.optional(element) && !this.optional(startEl.get(0))) {
				var stop = dt_pattern.exec(value);
				var start = dt_pattern.exec(startEl.val());
				return start && stop && new Date(start[1], start[2], start[3], start[5], start[6]).getTime() < new Date(stop[1], stop[2], stop[3], stop[5], stop[6]);
			}
		}
		
		return true;
	}, texts.dtAfter);
	
	var numberCompare = function(value, element, param, compareType) {
		var minEle;
		if (typeof param == "string")
			minEle = $(param);
		else if (typeof param == "object")
			minEle = $(param.selector);
		if (minEle.length > 0 && !this.optional(element) && !this.optional(minEle.get(0))) {
			var compare = parseFloat(minEle.val());
			$(element).data("error-number-compare-to", compare);
			switch (compareType) {
			case "gt":
				return parseFloat(value) > compare;
			case "ge":
				return parseFloat(value) >= compare;
			case "lt":
				return parseFloat(value) < compare;
			case "le":
				return parseFloat(value) <= compare;
			default:
				return true;
			}
		}
		
		return true;
	}
	
	var numberCompareMessage = function(message) {
		return function(param, element) {
			return $.message(null, "text.ui.validate.messages.compare", message + $(element).data("error-number-compare-to"));
		}
	}
	
	$.validator.addMethod("gt", function(value, element, param) {
		return numberCompare.call(this, value, element, param, "gt");
	}, numberCompareMessage(texts.gt));
	
	$.validator.addMethod("ge", function(value, element, param) {
		return numberCompare.call(this, value, element, param, "ge");
	}, numberCompareMessage(texts.ge));
	
	$.validator.addMethod("lt", function(value, element, param) {
		return numberCompare.call(this, value, element, param, "lt");
	}, numberCompareMessage(texts.lt));
	
	$.validator.addMethod("le", function(value, element, param) {
		return numberCompare.call(this, value, element, param, "le");
	}, numberCompareMessage(texts.le));

	$.validator.addMethod("ip", function(value, element) { 
	    return this.optional(element) || /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/i.test(value);
	}, texts.ip);
	
	$.validator.addMethod("ipBefore", function(value, element, param) {
		var stopIp;
		if (typeof param == "string")
			stopIp = $(param);
		else if (typeof param == "object" && param["selector"])
			stopIp = $(param.selector);
		
		var ele = $(element);
		if (!this.optional(element) && !$.validator.methods.ip.call(this, value, element)) {
			ele.data("error-type", "startip-invalid");
			return false;
		} else if (!this.optional(stopIp.get(0)) && !$.validator.methods.ip.call(this, stopIp.val(), stopIp.get(0))) {
			ele.data("error-type", "stopip-invalid");
			return false;
		} else if (!this.optional(element) && !this.optional(stopIp.get(0))) {
			var start = ip2int(value);
			var stop = ip2int(stopIp.val());
			ele.data("error-type", "range-invalid");
			return start <= stop;
		}
		
		return true;
	}, function(params, element) {
		var errorType = $(element).data("error-type");
		switch (errorType) {
			case "startip-invalid":
				return texts.ipStart;
			case "stopip-invalid":
				return texts.ipStop;
			default:
				return texts.ipRange;
		}
	});
	
	$.validator.addMethod("ipAfter", function(value, element, param) {
		var startIp;
		if (typeof param == "string")
			startIp = $(param);
		else if (typeof param == "object" && param["selector"])
			startIp = $(param.selector);
		
		var ele = $(element);
		if (!this.optional(element) && !$.validator.methods.ip.call(this, value, element)) {
			ele.data("error-type", "stopip-invalid");
			return false;
		} else if (!this.optional(startIp.get(0)) && !$.validator.methods.ip.call(this, startIp.val(), startIp.get(0))) {
			ele.data("error-type", "startip-invalid");
			return false;
		} else if (!this.optional(element) && !this.optional(startIp.get(0))) {
			var stop = ip2int(value);
			var start = ip2int(startIp.val());
			ele.data("error-type", "range-invalid");
			return start <= stop;
		}
		
		return true;
	}, function(params, element) {
		var errorType = $(element).data("error-type");
		switch (errorType) {
			case "startip-invalid":
				return texts.ipStart;
			case "stopip-invalid":
				return texts.ipStop;
			default:
				return texts.ipRange;
		}
	});

	$.validator.addMethod("ipv6", function(value, element) { 
	    return this.optional(element) || /^((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}:([0-9A-Fa-f]{1,4}:)?[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){4}:([0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){3}:([0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){2}:([0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|(([0-9A-Fa-f]{1,4}:){0,5}:((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|(::([0-9A-Fa-f]{1,4}:){0,5}((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|([0-9A-Fa-f]{1,4}::([0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})|(::([0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:))$/i.test(value);
	}, texts.ipv6);
	
	$.validator.addMethod("notLocalhostIp", function(value, element) {
		return this.optional(element) || value != "127.0.0.1" && value != "::1" && value != "localhost";
	}, texts.notLocalhostIp);
	
	$.validator.addMethod("mac", function(value, element) {
		return this.optional(element) || /^(([0-9a-zA-Z]){1,2}[:-]){5}([0-9a-zA-Z]){1,2}$/.test(value);
	}, texts.mac);
	
	$.validator.addMethod("regexp", function(value, element, param) {
		return this.optional(element) || (param && typeof param == "string" ? new RegExp(param, "g").test(value) : false);
	}, texts.regexp);
	
	$.validator.addMethod("pwd", function(value, element) {
		return this.optional(element) || password_pattern.test(value) && value.length > 7;
	}, texts.pwd);
	
	var enhancedEval = function(type, name) {
		if (typeof name == type)
			return name;
		
		var realObj = window.eval(name);
		if (typeof name != type)
			realObj = window.eval("window." + name);
		
		return typeof realObj == type ? realObj : undefined;
	}
	
	var _customMetaMessage = $.validator.prototype.customMetaMessage;
	var _resetForm = $.validator.prototype.resetForm;
	$.extend($.validator.prototype, {
		customMetaMessage: function(element, method) {
			var message = _customMetaMessage.call(this, element, method);
			if (!message) {
				message = $(element).attr("messages");
			}
			return message;
		},
		
		showErrors: function(errors) {
			if(errors) {
				// add items to error list and map
				$.extend( this.errorMap, errors );
				this.errorList = [];
				for ( var name in errors ) {
					var re = (/^(.+):eq\((\d+)\)$/).exec(name);
					var element = this.findByName(name)[0];
					if (!$(element).is("select") && re)
						element = this.findByName(re[1])[re[2]];
					if (element && errors[name])
						this.errorList.push({
							message: errors[name],
							element: element
						});
				}
				// remove items from success list
				this.successList = $.grep( this.successList, function(element) {
					return !(element.name in errors);
				});
			}
			this.hideNoFieldError();
			this.settings.showErrors
				? this.settings.showErrors.call( this, this.errorMap, this.errorList )
				: this.defaultShowErrors();
		},
		
		resetForm: function() {
			_resetForm.apply(this, arguments);
			if (this.settings.unhighlight) {
				for ( var i = 0, elements = this.elements(); elements[i]; i++ ) {
					this.settings.unhighlight.call( this, elements[i], this.settings.errorClass, this.settings.validClass );
				}
			}
		},

		elements: function() {
			var validator = this,
				rulesCache = {};

			// select all valid inputs inside the form (no submit or reset buttons)
			return $(this.currentForm)
			.find("input, select, textarea")
			.not(":submit, :reset, :image, [disabled]")
			.not( this.settings.ignore )
			.filter(function() {
				var name = validator.idOrName(this);
				!name && validator.settings.debug && window.console && console.error( "%o has no name assigned", this);

				var form = $(validator.currentForm);
				var setting = form.attr("data-validate-type") || /** @deprecated */form.attr("data-front-end-validate");
				// select only the first element for each name, and only those with rules specified
				if ( name in rulesCache || ["server", "false"].indexOf(setting) == -1 && !validator.objectLength($(this).rules()) )
					return false;

				rulesCache[name] = true;
				return true;
			});
		},
		
		showNoFieldErrors: function(errors) {
			if (typeof errors == "undefined" || errors.length == 0)
				return;
			
			var errorContainer = $("ul.error", this.currentForm);
			if (errorContainer.length == 0)
				errorContainer = $(this[0]).prev("ul.error");
			if (errorContainer.length == 0) {
				errorContainer = $('<ul class="error error-for-no-field"/>').prependTo(this.currentForm);
			}
			
			for (var i = 0; i < errors.length; i++) {
				$('<li class="error-for-no-field" />').text(errors[i]).appendTo(errorContainer);
			}
			this.toHide = this.toHide.add($(".error-for-no-field", this.currentForm));
		},
		
		hideNoFieldError: function() {
			var form = $(this.currentForm);
			form.add(form.prev("ul.error")).find(".error-for-no-field").remove();
		},
		
		addInvalidHandler: function(handler) {
			if (typeof handler == "string")
				handler = enhancedEval("function", handler);
			
			if (typeof handler == "function") {
				$(this.currentForm).bind("invalid-form.validate", handler);
				return handler;
			}
			
			return undefined;
		},
		
		removeInvalidHandler: function(handler) {
			if (typeof handler == "function")
				$(this.currentForm).unbind("invalid-form.validate", handler);
		},
		
		getLength: function(value, element) {
			switch( element.nodeName.toLowerCase() ) {
			case 'select':
				return $("option:selected", element).length;
			case 'input':
				if( this.checkable( element) )
					return this.findByName(element.name).filter(':checked').length;
			}
			
			if (typeof value == "string") {
				return value.length + value.split("\n").length - 1;
			}
			return value.length;
		}
		
	});
		

	var ajaxError = function(xhr, errorStatus, exception) {
		var mime = xhr.getResponseHeader("content-type");
		if (mime.indexOf("text/html") < 0) {

			var e = $.parseJSON(xhr.responseText);
			if ($.isException(e)) {
				if (typeof e.errors != "undefined") {
					var errors = {}, together = [];
					var messages = $.map(e.errors,
							function(m, i) {
								if ($.isPlainObject(m)) {
									if (typeof m.name == "string" && $.trim(m.name).length > 0) {
										errors[m.name] = m.key;
										return;
									} else {
										together.push(m.key);
										return;
									}
								}
								return m;
							});
					if (!$.isEmptyObject(errors)) {
						this.showErrors(errors);
					}
					if (together.length > 0) {
						this.showNoFieldErrors(together);
					}
					
					if (messages.length > 0) {
						xhr.errorCodes = messages;
					} else {
						xhr.globalError = false;
					}
				}
			}
		}

	};
	
	var _validate = $.fn.validate;
	$.fn.validate = function(options) {
		if (!this.length) {
			options && options.debug && window.console && console.warn( "nothing selected, can't validate, returning nothing" );
			return;
		}

		var validator = $.data(this[0], "validator");
		if ( validator ) {
			return validator;
		}

		var opts = $.data(this[0], "validator.options");
		var settings = {
				errorPlacement: function(error, element) {
					var ele = element.get(0);
					var validator = $(ele.form).validate();
					if (validator) {
						var errorContainer = [], name = validator.idOrName(element);
						if (name) {
							errorContainer = $(".error-for-" + name.replace(/[:]/g, "\\:"));
						}
						if (errorContainer.length == 0)
							errorContainer = $(".error-for-" + element.attr("name").replace(/[:]/g, "\\:"));
						if (errorContainer.length > 0) {
							error.appendTo(errorContainer);
						} else if ($(ele).parents("ul li > div").length > 0) {
							//if ($(ele).is("ul li>div *")) { //fix IE bug: $(ele).is("ul li>div *") equals false on IE7
							var tip = $(ele).parents("ul li > div").next("span");
							if (tip.hasClass(validator.settings.errorClass) && validator.errorsFor(element).length == 0)
								return; //不重复在同一位置显示错误信息，丢弃后产生的错误信息
							error.insertBefore(tip);
						} else
							error.insertAfter(element);
					}
				},
				showErrors: function() {
					this.defaultShowErrors();
					this.toHide.remove(); //将要隐藏的错误提示块直接删除，避免有些无法即时验证是否正确的输入框的错误信息被隐藏从而影响提示信息的恢复显示
					//如果今后发现存在性能或其他问题，修改此函数前需要充分考虑此种情况，可参加报表组的添加时的重名检查
				},
				errorElement: "span",
				success: function(label) {
					label.remove();
				},
				highlight: function(element, errorClass) {
					var name = this.idOrName(element);
					$(element).parents("li").attr("highlightFor", function(idx, attr) {
						if (attr && !attr.startWith(name + ",") && !attr.endWith("," + name) && attr.indexOf("," + name + ",") == 0) {
							return attr + "," + name;
						} else
							return name;
					}).addClass("highlight");
				},
				unhighlight: function(element, errorClass) {
					var ele = $(element), li = ele.parents("li"), highlightFor = li.attr("highlightFor"), name = this.idOrName(element);
					if (highlightFor) {
						if (highlightFor.startWith(name + ","))
							highlightFor = highlightFor.substring(name.length + 1)
						else if (highlightFor.endWith("," + name))
							highlightFor = highlightFor.substring(0, highlightFor.lastIndexOf(","));
						else if (highlightFor.indexOf("," + name + ",") > -1)
							highlightFor = highlightFor.replace("," + name + ",", ",");
						ele.attr("highlightFor", highlightFor);
						if (highlightFor.length > 0)
							return;
					}
					li.removeClass("highlight");
				}
		};
		var ulerror = this.find("ul.error");
		if (ulerror.length == 0)
			ulerror = this.prev("ul.error");
		if (ulerror.length > 0 && ulerror.attr("data-error-together") || options && options["error-together"] || this.attr("data-error-together"))
			$.extend(settings, {
				showErrors: function() {
					this.defaultShowErrors();
					if (this.labelContainer.find("li").length > 0)
						this.labelContainer.removeClass("empty");
					else
						this.labelContainer.show().addClass("empty");
				},
				
				/*errorLabelContainer: $(this[0]).find("ul.error").length > 0 ? $(this[0]).find("ul.error") : ( $(this[0]).prev("ul.error").length > 0 ? $(this[0]).prev("ul.error") : "ul.error:eq(0)"),*/
				errorLabelContainer: ulerror,
				errorElement: "li"
			});
		
		if (opts)
			$.extend(true, settings, options, opts);
		else
			$.extend(true, settings, options);

		var _complete;
		if (typeof settings["onValidatorInitComplete"] == "function") {
			_complete = settings["onValidatorInitComplete"];
			delete settings["onValidatorInitComplete"];
		}
		
		var validator = _validate.call(this, settings);
		
		if (validator) {
			validator.ajaxError = $.proxy(ajaxError, validator);
			if (_complete)
				_complete.call(validator);
		}
		
		return validator;
	};
	
	var hasServerError = function(formIdx) {
		return typeof ___validator_server_errors___ == "object" && !$.isEmptyObject(___validator_server_errors___) && formIdx + "" == ___validator_server_errors_for___;
	}
	
	var _fetch_validate_ = function(action, form, customInit, formIdx, additionalOpts) {
		return $.ajax(action, {
			async: false,
			data: {
				"validate-type": "fetch-validate-confs"
			},
			dataType: "json",
			clearError: false,
			success: function(data) {
				
				if (!$.isEmptyObject(data)) {
					if (data["messages"]) {
						var messages = data["messages"];
						for (var key in messages) {
							var ruleMessages = messages[key];
							for (var k in ruleMessages) {
								if (/\{\d+\}/.test(ruleMessages[k]))
									ruleMessages[k] = $.validator.format(ruleMessages[k]);
							}
						}
					}
					
					if (customInit == true) {
						if (hasServerError(formIdx)) {
							$.extend(data, {
								onValidatorInitComplete: function() {
									this.showErrors(___validator_server_errors___);
								}
							});
						}
						form.data("validator.options", $.extend(data, additionalOpts || {}));
					}
					else {
						var validator = form.validate($.extend(data, additionalOpts || {}));
						if (hasServerError(formIdx)) {
							validator.showErrors(___validator_server_errors___);
						}
					}
				}
			}
		});
	}
	$("form").each(function(idx) {
		var form = $(this);
		var setting = form.attr("data-validate-type") || /** @deprecated */form.attr("data-front-end-validate");
		var additionalOpts = {};
		var ignore = form.data("validate-ignore");
		if (typeof ignore != "undefined")
			additionalOpts["ignore"] = ignore;
		var formidx = $('<input type="hidden" name="___validator_form_idx___" value="' + idx + '" />');
		formidx.appendTo(form);
		
		switch (setting) {
			/** 仅前台验证 */
			case "client":
				break;
			/**
			 * 仅后台验证 
			 * from data-validate-type
			 */
			case "server":
			/**
			 * @deprecated
			 * from data-front-end-validate
			 */
			case "false":
				form.validate($.extend({onsubmit:false}, additionalOpts));
				if (typeof ___validator_server_errors___ == "object" && !$.isEmptyObject(___validator_server_errors___) && idx + "" == ___validator_server_errors_for___) {
					form.validate().showErrors(___validator_server_errors___);
				}
				if (typeof invalidHandler != "undefined")
					$(function() {
						form.validate().setInvalidHandler(eval(invalidHandler));
					});
				break;
			/**
			 * 手动初始化
			 * from data-validate-type
			 */
			case "custom-init":
			/**
			 * @deprecated
			 * from data-front-end-validate
			 */
			case "custom":
				var customInit = true;
			/**
			 * 标准方式
			 * from data-validate-type
			 */
			case "complete":
			default:
				var action = form.attr("data-action");
				if (!action)
					this.action && _fetch_validate_.call(this, this.action, form, customInit, idx, additionalOpts);
				else {
					$(function() {
						var _form = form.get(0);
						action && (action = enhancedEval("function", action));
						(typeof action == "function") && _fetch_validate_.call(_form, _form.action + action.call(_form), form, customInit, idx, additionalOpts);
					});
				}
		}
	});
	
})(jQuery);
