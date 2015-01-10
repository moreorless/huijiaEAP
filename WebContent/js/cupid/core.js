; 
(function($) {
	/*
	 * jQuery JSON Plugin
	 * version: 2.1 (2009-08-14)
	 *
	 * This document is licensed as free software under the terms of the
	 * MIT License: http://www.opensource.org/licenses/mit-license.php
	 *
	 * Brantley Harris wrote this plugin. It is based somewhat on the JSON.org 
	 * website's http://www.json.org/json2.js, which proclaims:
	 * "NO WARRANTY EXPRESSED OR IMPLIED. USE AT YOUR OWN RISK.", a sentiment that
	 * I uphold.
	 *
	 * It is also influenced heavily by MochiKit's serializeJSON, which is 
	 * copyrighted 2005 by Bob Ippolito.
	 */
    /** jQuery.toJSON( json-serializble )
        Converts the given argument into a JSON respresentation.

        If an object has a "toJSON" function, that will be used to get the representation.
        Non-integer/string keys are skipped in the object, as are keys that point to a function.

        json-serializble:
            The *thing* to be converted.
     **/
    $.toJSON = function(o)
    {
        if (typeof(JSON) == 'object' && JSON.stringify)
            return JSON.stringify(o);
        
        var type = typeof(o);
    
        if (o === null)
            return "null";
    
        if (type == "undefined")
            return undefined;
        
        if (type == "number" || type == "boolean")
            return o + "";
    
        if (type == "string")
            return $.quoteString(o);
    
        if (type == 'object')
        {
            if (typeof o.toJSON == "function") 
                return $.toJSON( o.toJSON() );
            
            if (o.constructor === Date)
            {
                var month = o.getUTCMonth() + 1;
                if (month < 10) month = '0' + month;

                var day = o.getUTCDate();
                if (day < 10) day = '0' + day;

                var year = o.getUTCFullYear();
                
                var hours = o.getUTCHours();
                if (hours < 10) hours = '0' + hours;
                
                var minutes = o.getUTCMinutes();
                if (minutes < 10) minutes = '0' + minutes;
                
                var seconds = o.getUTCSeconds();
                if (seconds < 10) seconds = '0' + seconds;
                
                var milli = o.getUTCMilliseconds();
                if (milli < 100) milli = '0' + milli;
                if (milli < 10) milli = '0' + milli;

                return '"' + year + '-' + month + '-' + day + 'T' +
                             hours + ':' + minutes + ':' + seconds + 
                             '.' + milli + 'Z"'; 
            }

            if (o.constructor === Array) 
            {
                var ret = [];
                for (var i = 0; i < o.length; i++)
                    ret.push( $.toJSON(o[i]) || "null" );

                return "[" + ret.join(",") + "]";
            }
        
            var pairs = [];
            for (var k in o) {
                var name;
                var type = typeof k;

                if (type == "number")
                    name = '"' + k + '"';
                else if (type == "string")
                    name = $.quoteString(k);
                else
                    continue;  //skip non-string or number keys
            
                if (typeof o[k] == "function") 
                    continue;  //skip pairs where the value is a function.
            
                var val = $.toJSON(o[k]);
            
                pairs.push(name + ":" + val);
            }

            return "{" + pairs.join(", ") + "}";
        }
    };

    /** jQuery.evalJSON(src)
        Evaluates a given piece of json source.
     **/
    $.evalJSON = function(src)
    {
        if (typeof(JSON) == 'object' && JSON.parse)
            return JSON.parse(src);
        return eval("(" + src + ")");
    };
    
    /** jQuery.secureEvalJSON(src)
        Evals JSON in a way that is *more* secure.
    **/
    $.secureEvalJSON = function(src)
    {
        if (typeof(JSON) == 'object' && JSON.parse)
            return JSON.parse(src);
        
        var filtered = src;
        filtered = filtered.replace(/\\["\\\/bfnrtu]/g, '@');
        filtered = filtered.replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, ']');
        filtered = filtered.replace(/(?:^|:|,)(?:\s*\[)+/g, '');
        
        if (/^[\],:{}\s]*$/.test(filtered))
            return eval("(" + src + ")");
        else
            throw new SyntaxError("Error parsing JSON, source is not valid.");
    };

    /** jQuery.quoteString(string)
        Returns a string-repr of a string, escaping quotes intelligently.  
        Mostly a support function for toJSON.
    
        Examples:
            >>> jQuery.quoteString("apple")
            "apple"
        
            >>> jQuery.quoteString('"Where are we going?", she asked.')
            "\"Where are we going?\", she asked."
     **/
    $.quoteString = function(string)
    {
        if (string.match(_escapeable))
        {
            return '"' + string.replace(_escapeable, function (a) 
            {
                var c = _meta[a];
                if (typeof c === 'string') return c;
                c = a.charCodeAt();
                return '\\u00' + Math.floor(c / 16).toString(16) + (c % 16).toString(16);
            }) + '"';
        }
        return '"' + string + '"';
    };
    
    var _escapeable = /["\\\x00-\x1f\x7f-\x9f]/g;
    
    var _meta = {
        '\b': '\\b',
        '\t': '\\t',
        '\n': '\\n',
        '\f': '\\f',
        '\r': '\\r',
        '"' : '\\"',
        '\\': '\\\\'
    };
})(jQuery);

jQuery.extend(String.prototype, {
	
	endWith: function(end, case_insensitive) {
		if (typeof case_insensitive == "undefined")
			case_insensitive = false;
		var self = this,
			match = end;
		if (case_insensitive) {
			self = this.toLowerCase();
			match = end ? end.toLowerCase() : end;
		}
		var index = self.indexOf(match);
		return match && index == self.length - match.length;
	},
	
	startWith: function(start, case_insensitive) {
		if (!start)
			return false;
		if (typeof case_insensitive == "undefined")
			case_insensitive = false;
		var self = this,
			match = start;
		if (case_insensitive) {
			self = this.toLowerCase();
			match = start.toLowerCase();
		}
		var startWith = self.substring(0, match.length);
		return match == startWith;
	}
	
});

(function($) {
	
	var methods = {
			indexOf: function(obj) {
				for (var i = 0; i < this.length; i++) {
					if (this[i] == obj) {
						return i;
					}
				}
				
				return -1;
			}
	};
	
	Array.prototype.hasOwnProperty = function(property) { //avoid hasOwnProperty mis-understand when handle extend methods by prototype
		if (methods.hasOwnProperty(property))
			return false;
		return {}.hasOwnProperty.call(this, property);
	};
	
	if (!Array.prototype.indexOf) { //modern browsers have native indexOf method for Array
		Array.prototype.indexOf = methods.indexOf;
	}
	
})(jQuery);

var SKIN = {
	COOKIE_KEY: "SKIN"
};

var cupid = {
	MessageCache : {
		"main": {}
	}
};

jQuery.extend({
	
	context: (function() {
	    /*
		var href = jQuery("script[type='text/javascript'][src]").attr("src");
		var ctx = location.pathname.substring(0, location.pathname.indexOf("/", 1));
		return href.substring(href.indexOf(ctx), href.indexOf("/js"));
		*/
		var ctx = location.pathname.substring(0, location.pathname.indexOf("/", 1));
		return ctx;
	})(),
	
	isException : function(ex) {
		var isPlainObject = jQuery.isPlainObject(ex);
		var clazz = (isPlainObject && "clazz" in ex) ? ex["clazz"].toLowerCase() : "";
		
		return clazz.indexOf("exception") > -1 || clazz.indexOf("throwable") > -1;
	},
	
	isEmpty: function(obj) {
		if (typeof obj["length"] != "undefined") {
			return !obj["length"];
		}else if (jQuery.isPlainObject(obj)) {
			return jQuery.isEmptyObject(obj);
		} else if (jQuery.isArray(obj) || jQuery.type(obj) == "string") {
			return obj.length == 0;
		} else {
			return !!!obj;
		}
	},
	
	rgbToHex: function(rgb) {
		if (!rgb || rgb == "transparent")
			return rgb;
		var re = /^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/;
		var group = re.exec(rgb);
		return group ? "#" + parseInt(group[1]).toString(16) + parseInt(group[2]).toString(16) + parseInt(group[3]).toString(16) : rgb;
	},
	
	getLang: function() {
		return jQuery.cookie("lang");
	},
	
	getSkin: function() {
		var skin;
		if (typeof SKIN != "undefined")
			skin = $.cookie(SKIN.COOKIE_KEY);
		return skin || "default";
	},
	
	changeSkin: function(skin, deep) {
		if (typeof SKIN == "undefined") return;
		
		if(typeof skin == "undefined") skin = "default";
		if(typeof deap == "undefined") deap = true;
		
		var currentSkin = jQuery.getSkin();
		if(currentSkin == skin) return;
		
		var replacement = skin == "default" ? ".css" : ("!" + skin + ".css");
		var prefix = $.context + "/css";
		if(skin == "default")
		{
			jQuery.cookie(SKIN.COOKIE_KEY, null);
		}
		else
		{
			jQuery.cookie(SKIN.COOKIE_KEY, skin, {
				expires: 365,
				path: $.context + "/"
			});
		}

		function doChangeSkin(prefix, replacement, deap, win)
		{
			if(!win||!win.document) return;
			
			var links = $("link[" + SKIN.SKINABLE_ATTR + "]");
			links.each(function() {
				if (this.type == "text/css" && this.href.indexOf(prefix) > -1) {
					
					var type = $(this).attr(SKIN.SKINABLE_ATTR);
					var href = this.href.replace(/(![^!\.]+)?\.css$/, replacement);
					if (type == "remove") {
						$(this).remove();
					} else if (type == "replace") {
						this.href = href;
					} else if (type == "append") {
						$(this).after($('<link ' + SKIN.SKINABLE_ATTR + '="remove" type="text/css" rel="stylesheet" href="' + href + '" />'));
					}
					
				}
			});
			
			if(deap)
			{
				for(var i=0; i<win.frames.length; i++)
				{
					doChangeSkin(prefix, replacement, deap, win.frames[i]);
				}
			}
		}

		//doChangeSkin(prefix, replacement, deap, window);
		
		window.location = jQuery.context + "/signout";
	},
	
	cacheMessage: function(key, text, bundle) {
		if (key && $.trim(key) != '') {
			cupid.MessageCache[key] = text;
			var topWin = window;
			while (topWin.parent && topWin != topWin.parent) {
				try {
					var parentHostname = topWin.parent.location.hostname;
					if (topWin.location.hostname != parentHostname)
						break;
				} catch (e) {
					break;
				}
				if (typeof topWin.parent.cupid == "undefined")
					break;
				topWin = topWin.parent;
			}
			if (!topWin.cupid.MessageCache[bundle]) {
				topWin.cupid.MessageCache[bundle] = {};
			}
			topWin.cupid.MessageCache[bundle][key] = text;
		}
	},
	
	message : function(bundle, key, args) {
		if (!bundle)
			bundle = "main";
		var text;
		var topWin = window;
		while (topWin.parent && topWin != topWin.parent) {
			try {
				var parentHostname = topWin.parent.location.hostname;
				if (topWin.location.hostname != parentHostname)
					break;
			} catch (e) {
				break;
			}
			if (typeof topWin.parent.cupid == "undefined") {
				break;
			}
			topWin = topWin.parent;
		}
		if (topWin && topWin.cupid.MessageCache[bundle] && topWin.cupid.MessageCache[bundle][key]) {
			text = topWin.cupid.MessageCache[bundle][key];
		}
		if (!text) {
			text = jQuery.parseJSON(jQuery.ajax({
				url: jQuery.context + "/util/message",
				data: {
					"bundle" : bundle,
					"key" : key
				},
				async: false,
				dataType: "json",
				traditional: true
			}).responseText);
			if (!cupid.MessageCache[bundle])
				cupid.MessageCache[bundle] = {};
			cupid.MessageCache[bundle][key] = text;
			if (topWin != window) {
				if (!topWin.cupid.MessageCache[bundle])
					topWin.cupid.MessageCache[bundle] = {};
				topWin.cupid.MessageCache[bundle][key] = text;
			}
		}
		
		if (args) {
			for (var i = 0; i < args.length; i++) {
				text = text.replace(new RegExp("\\{" + i + "\\}", "g"), args[i]);
			}
		}
		return text;
	},
	
	messages: function(messages) {
		var results;
		var topWin = window;
		while (topWin.parent && topWin != topWin.parent) {
			try {
				var parentHostname = topWin.parent.location.hostname;
				if (topWin.location.hostname != parentHostname)
					break;
			} catch (e) {
				break;
			}
			if (typeof topWin.parent.cupid == "undefined") {
				break;
			}
			topWin = topWin.parent;
		}
		if (messages.constructor == Array) {
			var results = {}, fetch = [];
			for (var i = 0; i < messages.length; i++) {
				var message = messages[i];
				if (typeof message.bundle == "undefined" || jQuery.trim(message.bundle).length == 0)
					message.bundle = "main";
				if (topWin && topWin.cupid.MessageCache[message.bundle] && topWin.cupid.MessageCache[message.bundle][message.key]) {
					results[message.id ? message.id : i] = topWin.cupid.MessageCache[message.bundle][message.key];
				} else
					fetch.push({bundle:message.bundle, key:message.key});
			}
			
			try {
				var texts = jQuery.parseJSON(jQuery.ajax({
					url: jQuery.context + "/util/messages",
					data: {
						"messages" : jQuery.map(fetch, function(message) { return jQuery.toJSON(message); })
					},
					async: false,
					dataType: "json",
					traditional: true,
					/*
					 * 必须使用POST方式，以避免超过URL的长度限制，
					 * IE 的 URL 长度上限是 2083 字节，其中纯路径部分不能超过 2048 字节。
				     * Firefox 浏览器的地址栏中超过 65536 字符后就不再显示。
				     * Safari 浏览器一致测试到 80000 字符还工作得好好的。
				     * Opera 浏览器测试到 190000 字符的时候，还正常工作。
					 */
					type: "POST",
					clearError: false
				}).responseText);
			} catch (e) {
				
			}
			
			if (texts) {
				for (var i = 0; i < fetch.length; i++) {
					var message = fetch[i];
					var text = texts[i];
					if (!cupid.MessageCache[message.bundle])
						cupid.MessageCache[message.bundle] = {};
					cupid.MessageCache[message.bundle][message.key] = text;
					if (topWin != window) {
						if (!topWin.cupid.MessageCache[message.bundle])
							topWin.cupid.MessageCache[message.bundle] = {};
						topWin.cupid.MessageCache[message.bundle][message.key] = text;
					}
				}
				
				var idx = 0;
				for (var i = 0; i < messages.length; i++) {
					var message = messages[i];
					var id = message.id ? message.id : i;
					if (!results[id] && idx < texts.length) {
						results[id] = texts[idx++];
					}
					
					if (typeof message.args != "undefined") {
						for (var j = 0; j < message.args.length; j++) {
							results[id] = results[id].replace(new RegExp("\\{" + j + "\\}", "g"), message.args[j]);
						}
					}
				}
			}
		}
		
		return results;
	},
	
	REST : {
		message: function(bundle, key, args) {
			var url = [];
			url.push(jQuery.context);
			url.push("util/rest/message");
			url.push(encodeURIComponent(bundle));
			url.push(encodeURIComponent(key));
			if (args && args.length > 0) {
				var arr = [];
				jQuery.each(args, function(idx, value) {
					if (value)
						//url.push(encodeURIComponent(value));
						arr.push(encodeURIComponent(value));
				});
				url.push(arr.join(","));
			} else {
				url.push("");
			}
			
			return jQuery.parseJSON(jQuery.ajax({
				url: url.join("/"),
				async: false,
				dataType: "json",
				clearError: false
			}).responseText);
		}
	},
	
	errorPlacer: {
		show: function(message, idx) {
			if (typeof idx == "undefined")
				idx = 0;
			
			if (idx.constructor === Number && $("ul.error").length > 0) {
				$("ul.error:eq(" + idx + ")").append($("<li />", {text: message})).removeClass("empty");
			} else if ($("ul#includeError-" + idx).length > 0) {
				$("ul#includeError-" + idx).append($("<li />", {text: message})).removeClass("empty");
			} else
				alert(message);
		},
		
		showHtml: function(message, idx) {
			if (typeof idx == "undefined")
				idx = 0;
			
			if (idx.constructor === Number && $("ul.error").length > 0) {
				$("ul.error:eq(" + idx + ")").append($("<li />", {html: message})).removeClass("empty");
			} else if ($("ul#includeError-" + idx).length > 0) {
				$("ul#includeError-" + idx).append($("<li />", {html: message})).removeClass("empty");
			} else
				alert(message);
		},
		
		list: function(idx) {
			if (typeof idx == "undefined")
				idx = 0;
			
			if (idx.constructor === Number && $("ul.error").length > 0) {
				var messages = [];
				$("ul.error:eq(" + idx + ") li").each(function() {
					messages.push($(this).text());
				});
				return messages;
			} else if ($("ul#includeError-" + idx + ".error").length > 0) {
				var messages = [];
				$("ul#includeError-" + idx + ".error li").each(function() {
					messages.push($(this).text());
				});
				return messages;
			}
		},
		
		remove: function(idx, containerIdx) {
			if (typeof containerIdx == "undefined")
				containerIdx = 0;
			
			if (containerIdx.constructor === Number && $("ul.error").length > 0) {
				$("ul.error:eq(" + containerIdx + ") li:eq(" + idx + ")").remove();
				
				$("ul.error:eq(" + containerIdx + "):not(:has(li))").addClass("empty");
			} else if ($("ul#includeError-" + containerIdx + ".error").length > 0) {
				$("ul#includeError-" + containerIdx + ".error li:eq(" + idx + ")").remove();
				$("ul#includeError-" + containerIdx + ".error:not(:has(li))").addClass("empty");
			}
		},
		
		clear: function(idx) {
			if (typeof idx == "undefined")
				idx = 0;
			
			if (idx.constructor === Number && $("ul.error").length > 0) {
				$("ul.error:eq(" + idx + ")").addClass("empty").children("li").remove();
			} else if ($("ul#includeError-" + idx + ".error").length > 0) {
				$("ul#includeError-" + idx + ".error").addClass("empty").children("li").remove();
			}
		},
		
		clearAll: function() {
			if ($("ul.error").length > 0) {
				$("ul.error").addClass("empty").children("li").remove();
			}
		}
	},
	
	globalMarker: function() {
		var t = window;
		if (window != top) {
			while (t.parent && t != t.parent) {
				if (typeof t.parent.cupid == "undefined") {
					break;
				}
				t = t.parent;
			}
		}
		var marker = t.$("#server-down-marker");
		if (marker.length == 0) {
			marker = t.$("<div />", {
				id: "server-down-marker",
				css: {
					position: "absolute",
					top: 0,
					right: 0,
					"float": "left",
					color: "#fff",
					background: "#f00",
					padding: "10px",
					"z-index": 100000,
					display: "none"
				}
			}).appendTo("body");
		}
		return marker;
	}
	
});

var tmp = $.messages([
                      {key:"errors.noperssmion"},
                      {key:"errors.global.termination"},
                      {key:"errors.global.netunstable"},
                      {key:"errors.global.sessioninvalid"},
                      {key:"text.notify.closeall"},
                      {key:"errors.global.interalexception"}
                      ]);
var messages = {
	NoPermission: tmp[0],
	Terminate: tmp[1],
	NetUnstable: tmp[2],
	SessionInvalid: tmp[3],
	NotifyCloseAll: tmp[4],
	InternalException: tmp[5]
};

(function($) {
	
	$.fn.cssColor = function(propertyName) {
		var color = this.css(propertyName);
		return propertyName.toLowerCase().indexOf("color") > -1 ? $.rgbToHex(color) : color;
	};
	
	$.fn.cssIntValue = function(propertyName) {
		var value = this.css(propertyName);
		return parseInt(value, 10) || 0;
	};
	
	$.fn.getBorderWidth = function(which) {
		var sides = [];
		switch (which) {
			case "top":
			case "bottom":
			case "left":
			case "right":
				sides.push(which);
				break;
			case "vertical":
				sides.push("top");
				sides.push("bottom");
				break;
			case "horizon":
				sides.push("left");
				sides.push("right");
				break;
		}
		
		var _this = this;
		return function(sides) {
			var width = 0;
			$.each(sides, function() {
				if (_this.css("border-" + this + "-style") != "none")
					width += _this.cssIntValue("border-" + this + "-width");
			});
			return width;
		}(sides);
	};
	
})(jQuery);

jQuery.browser={}
jQuery.browser.mozilla = /firefox/.test(navigator.userAgent.toLowerCase());
jQuery.browser.webkit = /webkit/.test(navigator.userAgent.toLowerCase());
jQuery.browser.opera = /opera/.test(navigator.userAgent.toLowerCase());
jQuery.browser.msie = /msie/.test(navigator.userAgent.toLowerCase());

jQuery.xhrPool = [];
jQuery.xhrPool.abortAll = function() {
	jQuery(this).each(function(idx, jqXHR) {
		jqXHR.abort();
	});
	jQuery.xhrPool.length = 0;
}
jQuery(window).bind(jQuery.browser.msie ? "unload.ajaxabort" : "beforeunload.ajaxabort", function() {
	jQuery.xhrPool.abortAll();
});
jQuery.ajaxSetup({
	traditional: true
});
jQuery(document).ajaxSend(function(event, xhr, settings) {
	jQuery.xhrPool.push(xhr);
	if (typeof settings.clearError == "undefined" || settings.clearError === true)
		$.errorPlacer.clear(settings.errorPlacer);
});
jQuery(document).ajaxComplete(function(event, xhr, settings) {
	var index = jQuery.xhrPool.indexOf(xhr);
	if (index > -1)
		jQuery.xhrPool.splice(index, 1);
});
jQuery(document).ajaxError(function(event, xhr, settings, exception) {
	if (typeof xhr.globalError != "undefined" && !xhr.globalError)
		return;
	
	var mime = xhr.getResponseHeader("content-type");
	if (mime && mime.indexOf("text/html") < 0) {
		
		var buildMessageFromEC = function(errors) {
			var messages = [];
			$.each(errors, function() {
				typeof this == "string" ? messages.push(this) : ($.isPlainObject(this) && !this.resource && messages.push(this.key));
			});
			return messages.join("\n");
		};
		
		var method = "show";
		var message;
		if (typeof xhr.errorCodes != "undefined")
			message = buildMessageFromEC(xhr.errorCodes);
		else {
			var e = jQuery.parseJSON(xhr.responseText);
			if ($.isException(e)) {
				if (typeof e.errors != "undefined") {
					message = buildMessageFromEC(e.errors);
				} else if (e.clazz.indexOf("PermissionException") > -1) {
					message = messages.NoPermission;
				} else
					message = e.detailMessage;
			} else if (typeof e == "string") {
				var errincode = $("#errincode");
				if (errincode.length == 0) {
					$("<form/>", {
						id: "errincode",
						method: "POST",
						action: $.context + "/commons/dlerrlog"
					}).append($("<input/>", {
						type: "hidden",
						name: "err",
						value: e
					})).appendTo("body");
				} else {
					errincode.find("input[name=err]").val(e);
				}
				message = messages.InternalException.replace("{0}", "$('#errincode').submit()");
				method = "showHtml";
			} else {
				message = (exception);
			}
		}
		
		$.errorPlacer[method](message, settings.errorPlacer);
	} else if ([0, 12029].indexOf(xhr.status) > -1) {
		if (xhr.statusText != "abort") {//过滤掉由于页面切换造成的ajax请求中断错误，依赖$.xhrPool.abortAll
			$.globalMarker().text(messages.Terminate).show();
		}
		window.console && console.log($.toJSON(xhr));
	} else if ([12002, 12030, 12031, 12152].indexOf(xhr.status) > -1) {
		//ref: http://msdn.microsoft.com/en-us/library/windows/desktop/aa383770%28v=vs.85%29.aspx
		//ref: http://stackoverflow.com/questions/872206/http-status-code-0-what-does-this-mean-in-ms-xmlhttp
		$.globalMarker().text(messages.NetUnstable).show();
		setTimeout(function() {
			$.globalMarker().hide();
		}, 60000);
		window.console && console.log($.toJSON(xhr));
	} else {
		if (xhr.status == 401) {
			$.globalMarker().text(messages.SessionInvalid).show();
		} else if (xhr.status == 403) {
			window.location.replace($.context + "/error/unauth.jsp");
		} else {
			window.console && console.log(xhr.status);
			document.write(xhr.responseXML ? xhr.responseXML : xhr.responseText);
		}
	}
}).ajaxSuccess(function(event, xhr, settings) {
	$.globalMarker().hide();
});

if (typeof jQuery.jgrid != "undefined") {
	if ($.browser.msie) {
		var _stripHtml = jQuery.jgrid.stripHtml;
		jQuery.jgrid.stripHtml = function(v) {
			var str = _stripHtml(v);
			return str.replace(/^\s*$/, "");
		};
	}
	jQuery.extend(jQuery.jgrid.defaults, {
		rowNum: 20,
		rowList: [10, 20, 30, 40],
		altRows: true,
		altclass: "even",
		beforeSelectRow: function(rowid, e) {
			
			if (typeof this.p._beforeSelectRow == "function")
				return this.p._beforeSelectRow.call(this, rowid, e);
			
			if (this.p.multiselect && this.p.selectboxonly) {
				return $(e.target).hasClass("cbox");
			}
			
			return true;
		}
	}, $.messages([
                   {key: "text.ui.grid.recordtext", id: "recordtext"},
                   {key: "text.ui.grid.emptyrecords", id: "emptyrecords"},
                   {key: "text.ui.grid.loadtext", id: "loadtext"},
                   {key: "text.ui.grid.pgtext", id: "pgtext"}
                   ]));

	var _jqGrid = $.fn.jqGrid;
	$.fn.jqGrid = function(options) {
		if (typeof options == "object" && typeof options.colModel != "undefined") {
			/** 
			 * hack for jqGrid v4.1.1
			 * if this bug[http://www.trirand.com/blog/?page_id=393/bugs/sorting-problem-when-index-missing-jqgrid-4-1/] fixed, delete this code
			 */
			$.each(options.colModel, function() {
				if (typeof this.index == "undefined")
					this.index = this.name;
			});
		} else if (typeof options == "object" && typeof options.beforeSelectRow == "function" && options.multiselect && options.selectboxonly) {
			options._beforeSelectRow = options.beforeSelectRow;
			delete options.beforeSelectRow;
		}
		var jqgrid = _jqGrid.apply(this, arguments);
		if (typeof options == "object" && options.caption && !/^\s*$/.test(options.caption))
			jqgrid.parents(".ui-jqgrid-view").addClass("ui-jqgrid-view-captioned");
		return jqgrid;
	}
	var _setCaption = _jqGrid.setCaption;
	_jqGrid.setCaption = function(caption) {
		_setCaption.call(this, caption).parents(".ui-jqgrid-view")[caption && !/^\s*$/.test(caption) ? "addClass" : "removeClass"]("ui-jqgrid-view-captioned");
	};
	$.jgrid.extend(_jqGrid);
	
	if (typeof $.fn.jqGrid.setGroupHeaders != "undefined") {
		var setGroupHeaders = $.fn.jqGrid.setGroupHeaders;
		$.fn.jqGrid.setGroupHeaders = function() {//修正使用Group Header时autoheight计算的
			return setGroupHeaders.apply(this, arguments).jqGrid("autoheight");
		}
	}
	
	/*if ($.browser.msie)
		$.jgrid.extend({
			emptyRows : function (parent, scroll, locdata) {
				if(ts.p.deepempty) {$("#"+$.jgrid.jqID(ts.p.id)+" tbody:first tr:gt(0)").remove();}
				else {
					var tbody = $("#"+$.jgrid.jqID(ts.p.id)+" tbody:first");
					var trf = tbody.find("tr:first").detach();
					var smashcan = $("<table></table>").appendTo("body").append(tbody.contents());
					smashcan[0].innerHTML = "";
					smashcan.remove();
					trf.appendTo(tbody);
				}
				if (scroll && ts.p.scroll) {
					$(">div:first", parent).css({height:"auto"}).children("div:first").css({height:0,display:"none"});
					parent.scrollTop = 0;
				}
				if(locdata === true) {
					if(ts.p.treeGrid === true ) {
					ts.p.data = []; ts.p._index = {};
					}
				}
			}
		});*/
	//bugfix for v4.2 to fix Date formatter parse date-in-milliseconds error
	if (typeof $.fn.fmatter != "undefined") {
		var _formatter = $.fn.fmatter.date;
		$.fn.fmatter.date = function(cellval, opts, rwd, act) {
			if (!$.fmatter.isEmpty(cellval) && cellval.constructor === Number)
				cellval = new Date(cellval);
			return _formatter(cellval, opts, rwd, act);
		}
	}
}

if (typeof jQuery.tools != "undefined" && typeof jQuery.tools.overlay != "undefined") {
	jQuery.fn.dialog = function(options) {
		
		var o = jQuery.extend(true, {
			mask: {
				loadSpeed: 200,
				opacity: 0.9
			},
		
			closeOnClick: false
		}, options);
		return this.overlay(o);
	};
}

if (typeof jQuery.ui != "undefined" && typeof jQuery.ui.datepicker) {
	jQuery(window).bind("unload.bugfix", function() {//bugfix: jQueryUI's DatePicker widget create a DIV element on initialize, cause IE memleak
		if (jQuery.datepicker)
			jQuery.datepicker.dpDiv.remove();
	});
	if (jQuery.browser.msie && jQuery("body > .wrap").length > 0) {//bad hack, but the only way worked on IE
		jQuery("body > .wrap").scroll(function() {
			if (jQuery.datepicker._curInst) {
				var offset = jQuery.datepicker._curInst.input.offset();
				jQuery.datepicker._curInst.dpDiv.offset({left: offset.left, top: offset.top + jQuery.datepicker._curInst.input.outerHeight()});
			}
		});
	}
	
	jQuery.extend(jQuery.datepicker._defaults, {
		changeYear: true,
		changeMonth: true
	});
}

if (typeof jQuery.ui != "undefined" && typeof jQuery.ui.accordion != "undefined") {
	jQuery.ui.accordion.prototype.options.fillSpace = true;
	
	jQuery.ui.progressbar.prototype.options.showValue = true;
	jQuery.ui.progressbar.prototype.options.create = function() {
		if ($(this).progressbar("option", "showValue") == true)
			$(this).find(">div:first").css("line-height", $(this).height() + "px").text($(this).progressbar("value") + "%");
	};
	
	jQuery.ui.progressbar.prototype.options.change = function() {
		if ($(this).progressbar("option", "showValue") == true)
			$(this).find(">div:first").text($(this).progressbar("value") + "%");
	}
}

if (typeof jQuery.ui != "undefined" && typeof jQuery.ui.tabs != "undefined") {
	var tabify = jQuery.ui.tabs.prototype._tabify;
	jQuery.ui.tabs.prototype._tabify = function(init) {
		tabify.call(this, init);
		this.anchors.unbind("click.tabs", false).bind("click.tabs", function() {
			$(this).parent().trigger("click");
			return false;
		});
	}
}

if (typeof jQuery.jGrowl != "undefined") {
	$.extend($.jGrowl.defaults, {
		position: "bottom-right",
		append: "html",
		closerTemplate: "<div>[ " + messages.NotifyCloseAll + " ]</div>"
	});
	var _render = $.fn.jGrowl.prototype.render;
	$.extend($.fn.jGrowl.prototype, {
		render: function(notification) {
			
			if (typeof notification.message == "object") {
				if (notification.options.append == "original") {
					notification.options.m = notification.message;
					notification.message = "";
					var _beforeOpen = notification.options.beforeOpen;
					$.extend(notification.options, {
						beforeOpen: function(e, m, o) {
							e.append($(o.m).contents());
							_beforeOpen.apply(this, arguments);
						}
					});
				} else {
					notification.message = $(notification.message).html();
				}
			
			}
			
			if ($.browser.msie && parseInt($.browser.version) < 9) {
				notification.message += '</div><div class="jGrowl-group-icon">';
			}
			
			_render.call(this, notification);
			
		}
	});
}

jQuery(function() {
	
	//将输入焦点定位在页面上第一个处于可见状态的表单中的第一个可见的text输入框
	if (jQuery(".first-focus:visible:first").eq(0).trigger("focus").length == 0)
		jQuery("form:visible").find(":text:visible:first").eq(0).trigger("focus");
	
	//对没有提交按钮的表单，不响应回车提交事件
	$("form").each(function() {
		var _this = $(this);
		if (_this.find("input, button").filter(":submit").length == 0)
			_this.keydown(function(e) {
				if (e.which == 13) {
					$.data(this, "pressEnter", true);
				}
			});
			_this.submit(function(e) {
				if ($.data(this, "pressEnter") && !e.isDefaultPrevented()) {
					$.removeData(this, "pressEnter");
					e.preventDefault();
					window.console && console.log("Prevented submit form by press Enter in input text field.");
				}
			});
	});
	
	if (jQuery.browser.msie && parseInt(jQuery.browser.version) < 8) {
		jQuery("a, button").attr("hidefocus", "true");
	}
	
});

/**
 * 为FireFox添加outerHTML和innerText
 */
if(typeof HTMLElement != "undefined" && !$.browser.msie)
{
	HTMLElement.prototype.__defineGetter__("outerHTML", function() 
    { 
        var a=this.attributes, str="<"+this.tagName, i=0;
		for(;i<a.length;i++) 
        if(a[i].specified) 
            str+=" "+a[i].name+'="'+a[i].value+'"'; 
        if(!this.canHaveChildren) 
            return str+" />"; 
        return str+">"+this.innerHTML+"</"+this.tagName+">"; 
    });

    HTMLElement.prototype.__defineSetter__("outerHTML", function(s) 
    { 
        var r = this.ownerDocument.createRange(); 
        r.setStartBefore(this); 
        var df = r.createContextualFragment(s); 
        this.parentNode.replaceChild(df, this); 
        return s; 
    });
	
	HTMLElement.prototype.__defineGetter__("innerText", function()
	{
		return this.textContent;
	});
	
	HTMLElement.prototype.__defineSetter__("innerText", function(sText)
	{
		this.textContent = sText;
	});
}

/*jQuery(function() {
	if (jQuery("script[src$='jquery.tzSelect.js']").length == 0)
		jQuery("head").append(jQuery("<script></script>", {
			type: "text/javascript",
			src: jQuery.context + "/js/ui/tzselect/jquery.tzSelect.js"
		}));
	if (jQuery("link[href$='jquery.tzSelect.css']").length == 0)
		jQuery("head").append(jQuery("<link/>", {
			type: "text/css",
			rel: "stylesheet",
			href: jQuery.context + "/css/ui/tzselect/jquery.tzSelect.css"
		}));
});*/