(function($) {

	var url = window.location.href,
			paramStr = window.location.search,
			paramsMap = {};
	
	url = url.indexOf("?") > -1 ? url.substr(0, url.indexOf("?")) : url;
	
	if (paramStr) {
		paramStr = paramStr.substring(1);
		var params = paramStr.split("&");
		for(var i=0;i<params.length;i++)
		{
			/*var param = params[i].split("=");
			if(param.length > 1)
			{
				paramsMap[decodeURIComponent(param[0])] = decodeURIComponent(param[1]);
			}*/
			var idx = params[i].indexOf("=");
			if (idx > -1 && idx < params[i].length) {
				paramsMap[decodeURIComponent(params[i].substring(0, idx))] = decodeURIComponent(params[i].substring(idx + 1));
			}
		}
	}
	
	paramsMap = $.extend({
		page: 1,
		rows: 20,
		sord: "asc"
	}, paramsMap);
	
	var onPaging = function(pgButton) {
		var page = this.p.page, lastpage = this.p.lastpage;
		
		if (pgButton.startWith("first")) {
			page = 1;
		} else if (pgButton.startWith("prev")) {
			page--;
		} else if (pgButton.startWith("next")) {
			page++;
		} else if (pgButton.startWith("last")) {
			page = lastpage;
		} else if (this.p.pginput === true) {
			page = $(".ui-pg-input", this.p.pager).val();
		}
		
		paramsMap["page"] = page > lastpage ? lastpage : ( page < 1 ? 1 : page );
		
		this.p.rowNum = $('.ui-pg-selbox').val();
		paramsMap["rows"] = this.p.rowNum;
		paramsMap["sidx"] = this.p.sortname;
		paramsMap["sord"] = this.p.sortorder;
		
		window.location = url + "?" + $.param(paramsMap);
		//alert(url + "?" + $.param(paramsMap));
		
		return "stop";
	};
	
	var onSort = function(index, iCol, sortorder) {
		/** 
		 * hack for jqGrid v4.1.1
		 * if this bug[http://www.trirand.com/blog/?page_id=393/bugs/sorting-problem-when-index-missing-jqgrid-4-1/] fixed, only use index can work
		 */
		paramsMap["sidx"] = this.p.sortname || index;
		paramsMap["sord"] = sortorder;

		window.location = url + "?" + $.param(paramsMap);
		//alert(url + "?" + $.param(paramsMap));
		return "stop";
	};
	
	if (typeof $.fn.tableToGrid != "undefined") {
		var _tblToGrid = $.fn.tableToGrid;
		$.fn.tableToGrid = function(options) {
			return _tblToGrid.call(this, $.extend(options, {
				onPaging: onPaging,
				onSortCol: onSort
			}));
		};
	}
})(jQuery);