;(function($) {
	
	$.fn.eventcloud = function(settings) {
		
		var cloudSize = this.length;
		
		return this.each(function() {
			var aLi = $(">li", this);
			var iLi = aLi.length;
			aLi.first().attr("data-quantity", "2147483648").attr("data-nodetype", "dest");
			aLi.each(function() {
				$(this).attr("title", $(this).text());
				$(this).mouseover(function(e) {
			       	this._title = this.title;
					this.title = "";
					$("body").append($("<div />", {
						"class": "eventcloud-tooltip",
						css: {
							"top": (e.pageY + 20) + "px",
							"left": (e.pageX + 10) + "px"
						}
					}).append($("<div>" + this._title + "</div>")));
					$(".eventcloud-tooltip").show("fast");
			    }).mouseout(function(){		
					this.title = this._title;
					$(".eventcloud-tooltip").remove(); 
			    }).mousemove(function(e){
					$(".eventcloud-tooltip").css({
						"top": (e.pageY+20) + "px",
						"left": (e.pageX+10)  + "px"
					});
				});
			});
			$(this).addClass("eventcloud").tagcloud({type:"sphere",sizemin:8,sizemax:20,power:.3,ullength:cloudSize}).draggable();
			
		});
	};
	
})(jQuery);