(function($) {
	
	$.cupid = $.cupid || {};
	
	$.cupid.layout = {
			DEFAULT_OPTIONS: {
					className: "auto-spread",
					recusive: true
			},
			ignoreSelectors: "script, link, style, meta, input[type=hidden]"
	};
	
	var methods = {
		vertical: function(options) {
			return this.each(function() {
				
				var $this = $(this), el = this;
				var parent = $this.parent();
				if(parent.length == 0) {
					return;
				}
				
				var els = $this.siblings();
				var height = 0;
				
				if ($this.css("border-top-style") != "none") {
					height += $this.cssIntValue("border-top-width");
				}
				if ($this.css("border-bottom-style") != "none") {
					height += $this.cssIntValue("border-bottom-width");
				}
				
				height += $this.cssIntValue("padding-top");
				height += $this.cssIntValue("padding-bottom");
				height += $this.cssIntValue("margin-top");
				height += $this.cssIntValue("margin-bottom");
				
				els.not(options.ignoreSelectors).each(function()
				{
					var item = $(this);
					if(this != el && item.css("display") != "none"//item.is(":visible") 
						&& item.css("float") == "none" 
						&& item.css("position") != "absolute")
					{
						//if (!$.browser.msie || this.clientHeight != 0/*对IE中空元素有高度的特殊处理*/)
						var itemHeight = item.height(); //不再判断是否为IE，IE中的空元素有高度实际存在，如需处理应对空元素做处理
						if (itemHeight > 0) {
							height += itemHeight;
							height += item.cssIntValue("margin-top") + item.cssIntValue("margin-bottom");
							height += item.cssIntValue("padding-top") + item.cssIntValue("padding-bottom");
							if (item.css("border-top-style") != "none") {
								height += item.cssIntValue("border-top-width");
							}
							if (item.css("border-bottom-style") != "none") {
								height += item.cssIntValue("border-bottom-width");
							}
						}
					}
				});
				var parentHeight = parent.height();
				//不需要再对IE做特殊处理，高度缩小可以正常处理
				height = parentHeight < height ? 0 : parentHeight - height;
				$this.css("height", height + "px");
			});
		},
		
		horizon: function(options) {
			return this.each(function() {
				var $this = $(this), el = this;
				
				var parent = $this.parent();
				var els = parent.children();
				var width = 0;
				
				if ($this.css("border-left-style") != "none") {
					width += $this.cssIntValue("border-left-width");
				}
				if ($this.css("border-right-style") != "none") {
					width += $this.cssIntValue("border-right-width");
				}
				
				width += $this.cssIntValue("margin-left");
				width += $this.cssIntValue("margin-right");
				width += $this.cssIntValue("padding-left");
				width += $this.cssIntValue("padding-right");
				
				els.not(options.ignoreSelectors).each(function()
				{
					var item = $(this);
					if(this != el && item.css("display") != "none"//item.is(":visible") 
						&& item.css("float") != "none"
						&& item.css("position") != "absolute")
					{
						var itemWidth = item.width();
						if (itemWidth > 0) {
							width += itemWidth;
							width += item.cssIntValue("margin-left") + item.cssIntValue("margin-right");
							width += item.cssIntValue("padding-left") + item.cssIntValue("padding-right");
							if (item.css("border-left-style") != "none")
								width += item.cssIntValue("border-left-width");
							if (item.css("border-right-style") != "none")
								width += item.cssIntValue("border-right-width");
						}
					}
				});
				var parentWidth = parent.width();
				if (parentWidth < width)
				  width = 0;
				else
				  width = parentWidth - width;
				
				width -= 1; //简单处理，由于FF/IE9+/Chrome23+对百分比后的宽度会计算小数点后的数字，所有多留出1px
				$this.css("width", width + "px");
			});
		},
		
		fullheight: function(options) {
			return this.each(function() {
				var $this = $(this), el = this;
				var parent = $this.parent();
				var height = 0;
				
				if ($this.css("border-top-style") != "none") {
					height += $this.cssIntValue("border-top-width");
				}
				if ($this.css("border-bottom-style") != "none") {
					height += $this.cssIntValue("border-bottom-width");
				}
						
				height += $this.cssIntValue("padding-top");
				height += $this.cssIntValue("padding-bottom");
				height += $this.cssIntValue("margin-top");
				height += $this.cssIntValue("margin-bottom");
				
				if (parent.height() < height)
				  height = 0;
				else
				  height = parent.height() - height + "px";
				if (height != $this.height())
					$this.css("height", height + "px");
			});
		},
		
		fullwidth: function(options) {
			return this.each(function() {
				var $this = $(this), el = this;
				var parent = $this.parent();
				var width = 0;
				
				if ($this.css("border-left-style") != "none") {
					width += $this.cssIntValue("border-left-width");
				}
				if ($this.css("border-right-style") != "none") {
					width += $this.cssIntValue("border-right-width");
				}
						
				width += $this.cssIntValue("padding-left");
				width += $this.cssIntValue("padding-right");
				width += $this.cssIntValue("margin-left");
				width += $this.cssIntValue("margin-right");
				
				if (parent.width() < width)
				  width = 0;
				else
				  width = parent.width() - width + "px";
				
				if (width != $this.width())
					$this.css("width", width + "px");
			});
		},
		
		relatedMethods: function(method) {
			switch (method) {
				case "vertical":
				case "fullheight":
					return ["vertical", "fullheight"];
				case "horizon":
				case "fullwidth":
					return ["horzion", "fullwidth"];
			}
			return [];
		}

	};
	
	$.fn.spread = function(method) {
		var options, argus = arguments;
		
		if (typeof method == "object" || !method) {
			options = method;
			method = "vertical";
		} else if (methods[method] || methods[method.replace("-", "")]) {
			argus = $.makeArray(arguments).slice(1);
			options = argus[0];
			method = method.replace("-", "");
		} 
		
		//var _this = this;
		options = options || $.cupid.layout.DEFAULT_OPTIONS;
		var ignoreSelectors = options["ignoreSelectors"];
		options["ignoreSelectors"] = ignoreSelectors ? $.cupid.layout.ignoreSelectors + ", " + ignoreSelectors : $.cupid.layout.ignoreSelectors;
		var re = methods[method].call(this, options);
		re.trigger("spreadstop");

		if (options.recusive) {
			/*_this = this.add(this.find("." + options.className).filter(function() {
				var result = false;
				$.each(this.className.split(" "), function() {
					var _method = this.replace("-", "");
					if (typeof methods[_method] == "function" && _method == method) {
						result = true;
						return false;
					}
				});
				
				return result ? true : "vertical" == method;
			}));*/
			this.children("." + options.className).each(function() {
				var el = $(this);
				var result = false;
				var classNames = this.className.split(" ");
				for (var i = 0; i < classNames.length; i++) {
					var _method = classNames[i].replace("-", "");
					if (typeof methods[_method] == "function" && methods.relatedMethods(_method).indexOf(method) > -1) {
						methods[_method].call(el, options);
						result = true;
						break;
					}
				}
				
				if (!result && methods.relatedMethods("vertical").indexOf(method) > -1)
					methods["vertical"].call(el, options).trigger("spreadstop");
			});
		}
		
		//return methods[method].apply(_this, argus);
		return re;
	};
	
	$.fn.adaptivespread = function(options) {
		var opts = $.extend({}, $.cupid.layout.DEFAULT_OPTIONS, options || {});
		return this.each(function() {
			var $this = $(this);
			if ($this.hasClass(opts.className)) {
				var spread = false;
				var classNames = this.className.split(" ");
				for (var i = 0; i < classNames.length; i++) {
					var method = classNames[i].replace("-", "");
					if (typeof methods[method] == "function") {
						$this.spread(method, opts);
						spread = true;
						break;
					}
				}
				if (!spread)
					$this.spread(opts);
			}
		});
	};
	
})(jQuery);