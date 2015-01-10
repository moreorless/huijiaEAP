(function($) {
	
	$.extend($.jgrid.defaults, {
		container: ".table-container"
	});
	
	var _jqGrid = $.fn.jqGrid;
	
	$.fn.jqGrid = function(options) {
		
		var jgrid = _jqGrid.apply(this, arguments);
		
		if (typeof options == "object") {
			
			if (options["autoheight"]) {
				jgrid.jqGrid("autoheight");
			}
			
			/* 不在初始化时执行autowidth方法，而是使用jqgrid原生的autowidth属性达到目的，避免IE下p.colModel为空的错误
			if (options["autowidth"]) {
				jgrid.jqGrid("autowidth");
			}*/

			$(window).resize(function(event) {
				//jgrid.trigger("resize");
				setTimeout(function() {
					jgrid.jqGrid("autoheight").jqGrid("autowidth"); //升级到jQuery 1.6.4后，jgrid.trigger会引起死循环
				}, 100);
				//由于方式变化不再判断是否在IE下调整列宽出发的事件，须继续观察效果
			});
			/*jgrid.bind("resize.jqgrid", function(event) {
				if (!$.browser.msie || $.browser.msie && !event.originalEvent) //规避IE下调整列宽度时会触发此监听
					$(this).jqGrid("autoheight").jqGrid("autowidth");
			});*/
			
		}
		
		return jgrid;
	};
	$.jgrid.extend(_jqGrid);
	
	$.extend($.fn.jqGrid, {
		
		autoheight: function() {
			return $(this).each(function() {
				if (!!!this.p.autoheight)
					return;
				
				var	body = $(this).parents(".ui-jqgrid-bdiv"),
						container = body.parents(this.p.container),
						height = container.innerHeight();
				
				body.parentsUntil(".ui-jqgrid").andSelf().add(body.parents(".ui-jqgrid")).each(function() {
					var $this = $(this);
					if ($this.css("border-top-style") != "none")
						height -= $this.cssIntValue("border-top-width");
					if ($this.css("border-bottom-style") != "none")
						height -= $this.cssIntValue("border-bottom-width");
					
					$this.siblings().each(function() {
						var $this = $(this);
						if (this != body.get(0) && $this.css("position") != "absolute" && $this.css("display") != "none") {
							height -= $this.outerHeight(true);
						}
					});
				});
				body.height(height);
			});
		},
		
		autowidth: function() {
			return $(this).each(function() {
				if (!!!this.p.autowidth)
					return;
				
				var $this = $(this);
				var parents = $this.parentsUntil(this.p.container).add($this.parents(this.p.container));
				var container = $this.parents(this.p.container);
				if ($.browser.msie)
					container.children().width(0);
				var width = container.width();
				parents.each(function() {
					var $this = $(this);
					if ($this.css("border-left-style") != "none")
						width -= $this.cssIntValue("border-left-width");
					if ($this.css("border-right-style") != "none")
						width -= $this.cssIntValue("border-right-width");
				});
				$this.jqGrid("setGridWidth", width);
				//$this.parents(".ui-jqgrid-bdiv").width(width);
			});
		}
		
	});
})(jQuery);
