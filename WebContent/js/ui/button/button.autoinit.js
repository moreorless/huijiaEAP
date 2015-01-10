(function($) {
	
	var cls = "btn";
	
	if ($.fn.menubtn) {
		$("div[data-" + cls + "=menubtn]:not(.lazy)").menubtn();
	}
	if ($.fn.btn) {
		$("a[data-" + cls + "=btn]:not(.lazy)").btn();
	}
	
	$.fn.btnlazyinit = function() {
		if ($.fn.menubtn) {
			this.find("div[data-" + cls + "=menubtn].lazy").menubtn();
		}
		if ($.fn.btn) {
			this.find("a[data-" + cls + "=btn].lazy").btn();
		}
	};
	
})(jQuery);