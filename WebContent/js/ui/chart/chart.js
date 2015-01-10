(function($) {
	
	var count = 1;
	
	var texts = $.messages([
	                        {key: "text.ui.chart.saveasimage"},
	                        {key: "text.ui.chart.loading"},
	                        {key: "text.ui.chart.savename"}
	                        ]);
	var textMap = {
		saveas: texts[0],
		loading: texts[1],
		savename: texts[2]
	};
	
	$.extend({
		chart : function(selector, width, height, options, swf) {
			
			var render = $(selector);
			
			if (render.length == 0)
				return;
			else if (!render.attr("id")) {
				render.attr("id", "ofc2-chart-" + count++);
				typeof console != "undefined" && console.log("OFC2 rendering element MUST has ID attribute, auto generate id: " + render.attr("id") + " instead.");
			}

			var chartId = render.attr("id");
			var opt = {
					id: chartId,
					save_image_message: textMap.saveas,
					loading: textMap.loading
			};
			if (typeof options == "string")
				opt["data-file"] = $.context + (options.charAt(0) == '/' ? options : "/" + options);
			else
				$.extend(opt, options);

			/*if (render.is("object")) {
				$.get(("data-file" in opt) ? opt["data-file"] : $.context + (width.charAt(0) == '/' ? width : "/" + width), function(data) {
					render[0].load(data);
				});
			} else {*/
				var plainDataFile = opt["data-file"];
				opt["data-file"] = encodeURIComponent(plainDataFile);
				swfobject.embedSWF(
						$.context + (swf ? "/flash/" + swf : "/flash/open-flash-chart.swf"), chartId,
						width + "", height + "", "10.2.0", $.context + "/flash/expressInstall.swf",
						opt, {"wmode": "transparent"}, {"data-save-name": render.attr("data-save-name") ? render.attr("data-save-name") : encodeURIComponent(textMap.savename)},
						function(data) {
							if (data.success) {
								$("#" + data.id).addClass("chart").attr("data-file", plainDataFile);
							}
						});
			//}
			
		}
	});
	
	$.fn.chart = function(width, height, options, swf) {
		if ($.isArray(width)) {
			return this.each(function(idx) {
				var opt = idx < width.length ? width[idx] : undefined;
				if (opt)
					$.chart(this, opt.width, opt.height, opt.options, swf);
			});
		}
		
		return this.each(function() {
			
			$.chart(this, width, height, options, swf);
			
		});
	};
	
	$.fn.reloadchart = function(url) {
		return this.each(function() {
			
			if (typeof this.reload != "function")
				return;
			
			var datafile = $(this).attr("data-file");
			if (typeof url == "string" && !/^\s*$/.test(url)) {
				url = url.charAt(0) == '/' ? url : "/" + url;
				url = url.indexOf($.context) == 0 ? url : $.context + url;
				this.reload(url);
				$(this).attr("data-file", url);
			} else if (datafile)
				this.reload(datafile);
			
		});
	}
	
	
	$.fn.gauge = function(width, height, options) {
		return this.chart(width, height, options, "gauge-chart.swf");
	}
	
	$.fn.reloadgauge = function(url, repaint) {
		return this.each(function() {
			
			if (typeof repaint == "undefined")
				repaint = false;
			
			if (typeof this.reload != "function" || typeof this.setValue != "function")
				return;
			
			var datafile = $(this).attr("data-file");
			if (repaint) 
				$(this).reloadchart(url);
			else {
				var _chart = this;
				if (url) {
					url = url.charAt(0) == '/' ? url : "/" + url;
					url = url.indexOf($.context) == 0 ? url : $.context + url;
				}
				$.getJSON(url || datafile, function(data) {
					if (data && typeof data["value"] != "undefined")
						_chart.setValue(data["value"]);
				});
			}
			
		});
	}
	
	$.fn.sonar = function(width, height, options) {
		return this.chart(width, height, options, "sonar-chart.swf");
	}
	
	$.fn.reloadsonar = function(url) {
		return this.reloadchart(url);
	}
	
	if (typeof window.save_image == "undefined") {
		window.save_image = function(id) {
			var chart = $("#" + id);
			chart.get(0).post_image($.context + "/chart/save?name=" + chart.attr("data-save-name"), null, true);
		}
	}
	
})(jQuery);