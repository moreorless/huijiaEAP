/*
 * Created by liunan
 * inspired from jqGrid TableToGrid plugin by Peter Romianowski
 * added features:
 * 1. if 'options' contains 'colModel', merge it with colModel parsed from table header
 * 2. fetch rowid from 'data-id' attribute of the first TD 
 */
(function($) {
	
	$.fn.tableToGrid = function(options) {
		return this.each(function() {
			if(this.grid) {return;} //Adedd from Tony Tomov
			// This is a small "hack" to make the width of the jqGrid 100%
			$(this).width("99%");
			var w = $(this).width();

			// Text whether we have single or multi select
			var inputCheckbox = $('input[type=checkbox]:first', $(this));
			var inputRadio = $('input[type=radio]:first', $(this));
			var selectMultiple = inputCheckbox.length > 0;
			var selectSingle = !selectMultiple && inputRadio.length > 0;
			var selectable = selectMultiple || selectSingle;
			//var inputName = inputCheckbox.attr("name") || inputRadio.attr("name");

			// Build up the columnModel and the data
			var colModel = [];
			var colNames = [];
			$('th', $(this)).each(function(idx) {
				if (colModel.length === 0 && selectable) {
					colModel.push({
						name: '__selection__',
						index: '__selection__',
						width: 0,
						hidden: true
					});
					colNames.push('__selection__');
				} else {
					var name = index = $(this).attr("id") || $.trim($.jgrid.stripHtml($(this).html())).split(' ').join('_');
					colModel.push({
						name: name,
						index: index,
						width: $(this).width() || 150
					});
					if (options.colModel && idx < options.colModel.length) {
						$.extend(true, colModel[idx], options.colModel[idx]);
						if (options.colModel[idx].name) {
							colModel[idx].index = colModel[idx].name;
						}
					}
					colNames.push($(this).html());
				}
				
				if ($(this).attr("class")) 
					colModel[idx].classes = $(this).attr("class"); 
			});
			options.colModel = colModel;
			var localReader = {
					repeatitems: false,
					page: function() {return 1;},
					total: function() {return 1;},
					records: function() {return 0;}
				};
			
			var data = [];
			var rowIds = [];
			var rowChecked = [];
			$('tbody > tr', $(this)).each(function(index) {
				var row = {};
				var rowPos = 0;
				$('td', $(this)).each(function() {
					if (rowPos === 0 && selectable) {
						var input = $('input', $(this));
						var rowId = input.attr("value");
						rowIds.push(rowId || data.length);
						if (input.attr("checked")) {
							rowChecked.push(rowId);
						}
						row[colModel[rowPos].name] = input.attr("value");
					} else {
						row[colModel[rowPos].name] = $(this).html();
						if (rowPos === 0) {
							var rowId = $(this).attr("data-id");
							rowIds.push(rowId || index);
						} else if (colModel[rowPos].formatter == "date") {//为Date formatter转换类型和添加缺省的reformatAfterEdit设置
							row[colModel[rowPos].name] = parseInt(row[colModel[rowPos].name], 10);
							$.extend(true, colModel[rowPos].formatoptions, {reformatAfterEdit:true});
						}
					}
					
					rowPos++;
				});
				if(rowPos >0) { data.push(row); }
			});

			if (options.localReader) {
				if (typeof options.localReader.page == "function")
					localReader.page = options.localReader.page;
				else
					localReader.page = function() { return parseInt(options.localReader.page || 1, 10); };
				
				if (typeof options.localReader.total == "function")
					localReader.total = options.localReader.total;
				else
					localReader.total = function() { return parseInt(options.localReader.total || 1, 10); };
				
				if (typeof options.localReader.records == "function")
					localReader.records = options.localReader.records;
				else
					localReader.records = function() { return parseInt(options.localReader.records || 0, 10) - data.length; };
			}

			// Clear the original HTML table
			$(this).empty();

			// Mark it as jqGrid
			$(this).addClass("scroll");

			$(this).jqGrid($.extend({
				datatype: "local",
				width: w,
				colNames: colNames,
				colModel: colModel,
				multiselect: selectMultiple
				//inputName: inputName,
				//inputValueCol: imputName != null ? "__selection__" : null
			}, options || {}, {
				localReader: localReader
			}));
			
			if (typeof this.formatCol) {
				var _formatCol = this.formatCol, _this = this;
				this.formatCol = function() {
					var length = arguments.length;
					for (var i = 0; i < length; i++) {
						if (typeof arguments[i] == "string")
							arguments[i] = $.trim(arguments[i]);
					}
					return _formatCol.apply(_this, arguments);
				}
			}

			// Add data
			var a;
			for (a = 0; a < data.length; a++) {
				var id = null;
				if (rowIds.length > 0) {
					id = rowIds[a];
					
					//comment it by liunan
					//because this will cause uuid failed, and it seems avoid nothing.
					/*
					if (id && id.replace) {
						// We have to do this since the value of a checkbox
						// or radio button can be anything 
						id = encodeURIComponent(id).replace(/[.\-%]/g, "_");
					}
					*/
				}
				if (id === null) {
					id = a + 1;
				}
				$(this).jqGrid("addRowData",id, data[a]);
			}

			// Set the selection
			for (a = 0; a < rowChecked.length; a++) {
				$(this).jqGrid("setSelection",rowChecked[a]);
			}
			
			if (typeof $.fn.btnlazyinit != "undefined") {
				$(this).btnlazyinit();
			}
		});
	}
	
})(jQuery);
