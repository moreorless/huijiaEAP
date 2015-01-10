(function($) {
	
	$.extend($.ui.tabs.prototype, {
		
		addClosableTab: function(id, label, options) {
			var self = this, o = this.options,
				iframe = options.iframe, url = options.url, index = options.index || this.length() + 1, 
				reset = typeof options.reset == "undefined" ? true : options.reset;
			
			$.extend(o, {
				tabTemplate: "<li><a href='#{href}'>#{label}</a> <span class='ui-icon ui-icon-close'>Remove Tab</span></li>",
				select: function(event, ui) {
					var iframe = $("iframe.ui-tabs-frame", ui.panel);
					if (!iframe.attr("src") || reset && iframe.length && iframe.attr("data-src"))
						iframe.attr("src", iframe.attr("data-src"));
				}
			});
			
			var panel = $("#" + id);
			self.add("#" + id, label, index);
			
			if (iframe && url) {
				var frame = $("<iframe />", {
					"class": "ui-tabs-frame",
					"data-src": url,
					"frameBorder": "0"
				});
				$("#" + id, this.element).append(frame);
			} else if (url) {
				self.url(index, url);
			}
			
			return this;
		}
		
	});
	
	$(".ui-tabs span.ui-icon-close").live("click", function() {
		var $tabs = $(this).parents(".ui-tabs");
		var index = $( "li", $tabs ).index( $( this ).parent() );
		$tabs.tabs( "remove", index );
	});
	
})(jQuery);