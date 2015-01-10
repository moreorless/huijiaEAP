/**
 * dependency: layout.js
 */
(function($) {
	
	var CSS = "layout",
		IGNORECSS = "no-resize";
	
	$.ui.plugin.add("draggable", "containmentAdjust", {
		start: function(event, ui) {
			var draggable = $(this).data('draggable'), o = draggable.options;
			if (draggable.containment) {
				switch (o.type) {
				case "we":
					draggable.containment[0] += 20;
					draggable.containment[2] -= 20;
					break;
				case "ns":
					draggable.containment[1] += 20;
					draggable.containment[3] -= 20;
					break;
				}
			}
		},
		stop: function(event, ui) {}
	});
	
	var methods = {
			
		init: function(width, height) {
		},
		
		initSplitter: function(position) {
			var self = $(this), parent = self.parent();
			var splitter = parent.find(".layout-splitter-handler");
			if (splitter.length == 0) {
				splitter = $("<div/>", {
					"class": "layout-splitter-handler",
					"css": {
						"top": position.top,
						"left": position.left,
						"width": position.width,
						"height": position.height
					}
				}).append($("<a />", { click:position.click })).appendTo(parent);
				
				splitter.draggable({
					containment: "parent",
					iframeFix: true,
					stop: function(event, ui) {
						if (typeof position.stop == "function")
							position.stop.call(this, event, ui);
						parent.trigger("layoutresize");
					},
					
					containmentAdjust: true,
					type: position.type
				});
			} else {
				splitter.css(position);
			}
			
			var btn = splitter.find("a");
			btn.css({
				top: (splitter.height() - btn.height()) / 2,
				left: (splitter.width() - btn.width()) / 2
			});
		},
		
		west: function() {

			var $this = $(this);
			var position = $this.offset();
			methods.initSplitter.call(this, {
				top: 0,
				left: $this.outerWidth(),
				height: "100%",
				width: "4px",
				type: "we",
				stop: function(event, ui) {
					var next = $this.next(".east").width(0);
					$this.width(ui.position.left);
					next.spread("horizon");
				},
				click: function() {
					var btn = $(this), splitter = btn.parent(), container = splitter.parent(), west = splitter.parent().find(".west"), east = west.next(".east");
					
					container.toggleClass("closed");
					if (!container.hasClass("closed")) {
						east.width(0);
						splitter.css({
							left: west.outerWidth(),
							top: 0
						}).draggable("enable");
					} else {
						splitter.css("left", 0).draggable("disable");
					}
					east.spread("horizon");

					container.trigger("layout" + (container.hasClass("closed") ? "closed" : "open"));
				}
			});

		},
		
		east: function() {
			var $this = $(this);
			if (!$this.hasClass("auto-spread"))
				$this.addClass("auto-spread horizon");
		},
		
		north: function() {

			var $this = $(this);
			var position = $this.offset();
			methods.initSplitter.call(this, {
				top: $this.outerHeight(),
				left: 0,
				width: "100%",
				height: "4px",
				type: "ns",
				stop: function(event, ui) {
					var next = $this.next(".south").height(0);
					$this.height(ui.position.top);
					next.spread("vertical");
				},
				click: function() {
					var btn = $(this), splitter = btn.parent(), container = splitter.parent(), north = splitter.parent().find(".north"), south = north.next(".south");
					
					container.toggleClass("closed");
					if (!container.hasClass("closed")) {
						south.height(0);
						splitter.css({
							left: 0,
							top: north.outerHeight()
						}).draggable("enable");
					} else {
						splitter.css("top", 0).draggable("disable");
					}
					south.spread("vertical");
					
					container.trigger("layout" + (container.hasClass("closed") ? "closed" : "open"));
				}
			});
		},
		
		south: function() {
			var $this = $(this);
			if (!$this.hasClass("auto-spread"))
				$this.addClass("auto-spread vertical");
		}
			
	};
	
	$.fn.layout = function() {
		return this.each(function() {
			var ele = this, $this = $(this);
			var panels = $this.find("." + CSS + ":not(." + IGNORECSS + ")").children();
			if (panels.length > 0) {
				
				panels.each(function() {
					var panel = this;
					$.each(this.className.split(/\s+/), function(index, classname) {
						if (typeof methods[classname] == "function")
							methods[classname].call(panel);
					});
				});
				
			}
		});
	};
	
	$(document).ready(function() {
		var _this = $(this);
		setTimeout(function() { _this.layout(); }, 100);
	});
	
	$(window).bind("resize.layout.resize", function() {
		$("." + CSS + ":not(." + IGNORECSS + ")").each(function() {
			var $this = $(this);
			var splitter = $(this).find(".layout-splitter-handler");
			var target, position;
			if ($this.hasClass("we")) {
				target = $this.find(".west");
				position = {left: target.css("display") == "none" ? 0 : target.outerWidth()};
			} else if ($this.hasClass("ns")) {
				target = $this.find(".north");
				position = {top: target.css("display") == "none" ? 0 : target.outerHeight()};
			}
			
			if (position)
				setTimeout(function() {methods.initSplitter.call(target, position);$this.trigger("layoutresize") }, 100);
		});
	});
	
})(jQuery);