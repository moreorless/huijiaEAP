(function($) {
	
	$.cupid = $.cupid || {};
	
	var l = $.cupid.layout || {};
	var DEFAULT_OPTIONS = $.extend(l, { className: "auto-spread" });
	
	$(document).ready(function(event) {
		
		$("." + DEFAULT_OPTIONS.className).adaptivespread({recusive:false});
		
	});
	
	$(window).bind("resize.layout", function() {
		var selector = "." + DEFAULT_OPTIONS.className + ($.browser.msie && parseFloat($.browser.version) < 9 ? ":visible" : "");
		$(selector).adaptivespread({recusive:false});
	});
})(jQuery);