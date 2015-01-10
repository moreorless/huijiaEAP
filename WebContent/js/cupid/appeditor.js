/**
 * 应用编辑器
 * 
 * @author liunan
 */
(function($) {
	
	var options = {
		width: /*3*/0,
		height: {
							min: "30px",
							max: "70px"
						},
		cssName: "ul.navis li",
		statusCss: "selected",
		movingClass: "move",
		cornerColor: $("#other-navis").cssColor("background-color")
	};
	
	var texts_tmp = $.messages([
	                            {key:"button.update"},
	                            {key:"button.delete"}
	                            ]);
	var texts = {
		update: texts_tmp[0],
		remove: texts_tmp[1]
	};
	
	var methods = {
		init: function(opts) {
			var ops = $.extend(opts, options);
			return this.each(function() {
				var $this = $(this);
				if (!$this.is(ops.cssName)) {
					return true;
				}
				
				$(this).bind("click.appeditor", function() {
					//$("#" + $(this).attr("data-id")).trigger("click");
					var $this = $(this);
					var dataId = $this.attr("data-id");
					if (!dataId) {
						return;
					}
					var icon = $this.find("img");
					var label = $this.find("label.name");
					PortalUtils.create({id: $this.attr("data-id"), icon: icon.attr("src").substring(icon.attr("src").lastIndexOf("/") + 1), name: label.text()});
					$("#navi-custom-trigger").data("overlay").close();
				});
				
				/*var mouseover = function() {
					var app = $(this);
					if (app.hasClass(ops.movingClass))
						return;
					var pos = app.position();
					var w = app.parent();
					pos.left += w.scrollLeft();
					pos.top += w.scrollTop();
					pos.position = "absolute";
					app.css(pos);
					var holder = $(".placeholder");
					holder.show();
					if (holder.length == 0)
						holder = $('<li class="placeholder" />');
					app.after(holder);
				};
				var mouseout = function(event, sender) {
					var app = $(this);
					app.css({
						position: "relative",
						top: "",
						left: ""
					});
					holder = $(".placeholder");
					if (sender)
						holder.add(sender.find(".placeholder"));
					if (sender)
						holder.hide();
					else
						holder.remove();
				};
				$this.bind("mouseover.appeditor", mouseover);
				$this.bind("mouseout.appeditor", mouseout);*/
				
				var name = $this.children(".name");
				if ($.isEmpty(name)) {
					name = $('<label class="name">' + $this.attr("title") + '</label>');
					name.appendTo(this);
					//name.corner("5px");
				}
				//name.show();
				
				/*var edit = $this.children(".edit");
				if (edit.length == 0) {
					edit = $('<a class="edit" title="' + texts.update + '"></a>');
					edit.click({appId: $this.attr("data-id")}, methods.edit);
					edit.appendTo(this);
				}
				//edit.show();
				
				var del = $this.children(".delete");
				if (del.length == 0) {
					del = $('<a class="delete" title="' + texts.remove + '"></a>');
					del.click({appId: $this.attr("data-id")}, methods.del);
					del.appendTo(this);
				}
				//del.show();
				
				$this.data("appeditor", {
					moveStart: function(event, ui) {
						mouseout.call(ui.item, event, $(ui.sender));
						$("ul.navis li").unbind(".appeditor");
						$this.addClass(ops.movingClass);
					},
					
					moveStop: function(event, ui) {
						$this.removeClass(ops.movingClass);
						var apps = $("ul.navis li");
						apps.bind("mouseover.appeditor", mouseover);
						apps.bind("mouseout.appeditor", mouseout);
					}
				});*/
			});
		},
		//implement by peizl 2011/6/3 
		edit: function(event) {
			event.stopPropagation();
			//alert("编辑 app-id:" + event.data.appId + " 功能还没有实现哦...");
			var id = event.data.appId;		
			PortalEditor.create(id);
		},
		//implement by peizl 2011/6/3 
		del: function(event) {
			event.stopPropagation();
			//alert("删除 app-id:" + event.data.appId + "功能还没有实现哦...");
			var id = event.data.appId;
			var app = $(this).closest('li');
			PortalUtils.remove(id, app);
		},
		
		select: function() {
			return this.each(function() {
				var $this = $(this);
				var app;
				if ($this.is(options.cssName))
					app = $this;
				else if ($this.is(options.cssName + " img"))
					app = $this.parent();
				
				if (app && !$this.hasClass(options.statusCss)) {
					app.stop(true, true);
					app.corner("bottom dog cc:" + options.cornerColor);
					app.animate({
						width: "+=" + options.width,
						marginRight: "-" + options.width + "px",
						lineHeight: options.height.min
					}, "slow").queue(function() {
						var $this = $(this);
						var name = $this.children(".name");
						if ($.isEmpty(name)) {
							name = $('<label class="name">' + $this.attr("title") + '</label>');
							name.appendTo(this);
							name.corner("5px");
						}
						name.show();
						
						var edit = $this.children(".edit");
						if (edit.length == 0) {
							edit = $('<a class="edit" title="' + texts.update + '"></a>');
							edit.click({appId: $this.attr("data-id")}, methods.edit);
							edit.appendTo(this);
						}
						edit.show();
						
						var del = $this.children(".delete");
						if (del.length == 0) {
							del = $('<a class="delete" title="' + texts.remove + '"></a>');
							del.click({appId: $this.attr("data-id")}, methods.del);
							del.appendTo(this);
						}
						del.show();
						$(this).dequeue();
					}).addClass(options.statusCss);
				}
			});
		},
	
		release: function() {
			return this.each(function() {
				var $this = $(this);
				if ($this.hasClass(options.statusCss)) {
					$this.stop(true, true);
					$this.children(".name").hide();
					$this.children(".edit").hide();
					$this.children(".delete").hide();
					$this.animate({
						width: "-=" + options.width,
						marginRight: "0",
						lineHeight: options.height.max
					}, "slow").removeClass(options.statusCss);
					$this.uncorner();
				}
			});
		}
	};
	
	$.fn.appeditor = function(method) {
		if (methods[method]) {
			return methods[method].apply(this, Array.prototype.slice(arguments, 1));
		} else if (typeof method == "object" || !method) {
			return methods.init.apply(this, arguments);
		}
	};
})(jQuery);
