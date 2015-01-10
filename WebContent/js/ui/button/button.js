/**
 * button - CUPID UI
 * 
 * modifed from EasyUI linkbutton 
 */
(function($){
	
	function createButton(target) {
		var opts = $.data(target, 'btn').options;
		
		var t = $(target);
		t.empty();
		t.addClass('l-btn');
		if (opts.id){
			t.attr('id', opts.id);
		} else {
			t.removeAttr('id');
		}
		if (opts.plain){
			t.addClass('l-btn-plain');
		} else {
			t.removeClass('l-btn-plain');
		}
		
		if ( opts.text){
			t.html(opts.text).wrapInner(
					'<span class="l-btn-left">' +
					'<span class="l-btn-text">' +
					'</span>' +
					'</span>'
			);
			if (opts.iconCls){
				t.find('.l-btn-text').addClass(opts.iconCls).css('padding-left', '20px');
			}
		} else {
			t.html('&nbsp;').wrapInner(
					'<span class="l-btn-left">' +
					'<span class="l-btn-text">' +
					'<span class="l-btn-empty"></span>' +
					'</span>' +
					'</span>'
			);
			if (opts.iconCls){
				t.find('.l-btn-empty').addClass(opts.iconCls);
			}
		}
		
		t.bind("click.btn", function(event) {
			var opt = t.data("btn").options;
			if (opt.disabled) {
				event.stopImmediatePropagation();
				return false;
			}
		});
		
		setDisabled(target, opts.disabled);
	}
	
	function setDisabled(target, disabled){
		var state = $.data(target, 'btn');
		if (disabled){
			state.options.disabled = true;
			var href = target.href;
			if (href){
				state.href = href;
				target.href = 'javascript://';
			}
			var onclick = target.onclick;
			if (onclick) {
				state.onclick = onclick;
				target.onclick = null;
			}
			$(target).addClass('l-btn-disabled');
		} else {
			state.options.disabled = false;
			if (state.href) {
				target.href = state.href;
			}
			if (state.onclick) {
				target.onclick = state.onclick;
			}
			$(target).removeClass('l-btn-disabled');
		}
	}
	
	$.fn.btn = function(options) {
		if (typeof options == 'string') {
			var btn = this && this.length > 0 ? $.data(this[0], "btn") : undefined, opt = btn ? btn.options : undefined, inited = btn && opt;
			switch(options){
			case 'options':
				return inited ? opt : undefined;
			case 'enable':
				return inited ? this.each(function(){
					setDisabled(this, false);
				}) : this;
			case 'disable':
				return inited ? this.each(function(){
					setDisabled(this, true);
				}) : this;
			case "isEnable":
				return inited ? !opt.disabled : undefined;
			case "text":
				return inited ? $(this[0]).find(".l-btn-text").text() : undefined;
			}
		}
		
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'btn');
			if (state){
				$.extend(state.options, options);
			} else {
				var t = $(this);
				$.data(this, 'btn', {
					options: $.extend({}, $.fn.btn.defaults, {
						id: t.attr('id'),
						disabled: (t.attr('disabled') ? true : undefined),
						plain: (t.attr('plain') ? t.attr('plain') == 'true' : undefined),
						text: $.trim(t.html()),
						iconCls: t.attr('icon'),
						compact: t.hasClass("compact")
					}, options)
				});
				t.removeAttr('disabled');
			}
			
			createButton(this);
		});
	};
	
	$.fn.btn.defaults = {
			id: null,
			disabled: false,
			plain: false,
			text: '',
			iconCls: null,
			compact: false
	};
	
})(jQuery);
