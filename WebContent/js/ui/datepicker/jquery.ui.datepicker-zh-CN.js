/* Chinese initialisation for the jQuery UI date picker plugin. */
/* Written by Cloudream (cloudream@gmail.com). */

jQuery(function($){
	if (typeof $.messages == "function") {
		var texts = $.messages([
		                        {key: "button.close"},
		                        {key: "text.ui.datepicker.lastmonth"},
		                        {key: "text.ui.datepicker.nextmonth"},
		                        {key: "text.ui.datepicker.today"},
		                        {key: "text.ui.datepicker.jan"},
		                        {key: "text.ui.datepicker.feb"},
		                        {key: "text.ui.datepicker.mar"},
		                        {key: "text.ui.datepicker.apr"},
		                        {key: "text.ui.datepicker.may"},
		                        {key: "text.ui.datepicker.jun"},
		                        {key: "text.ui.datepicker.jul"},
		                        {key: "text.ui.datepicker.aug"},
		                        {key: "text.ui.datepicker.sep"},
		                        {key: "text.ui.datepicker.oct"},
		                        {key: "text.ui.datepicker.nov"},
		                        {key: "text.ui.datepicker.dec"},
		                        {key: "text.ui.datepicker.sunday"},
		                        {key: "text.ui.datepicker.monday"},
		                        {key: "text.ui.datepicker.tuesday"},
		                        {key: "text.ui.datepicker.wednesday"},
		                        {key: "text.ui.datepicker.thursday"},
		                        {key: "text.ui.datepicker.friday"},
		                        {key: "text.ui.datepicker.saturday"},
		                        {key: "text.ui.datepicker.sunday.short"},
		                        {key: "text.ui.datepicker.monday.short"},
		                        {key: "text.ui.datepicker.tuesday.short"},
		                        {key: "text.ui.datepicker.wednesday.short"},
		                        {key: "text.ui.datepicker.thursday.short"},
		                        {key: "text.ui.datepicker.friday.short"},
		                        {key: "text.ui.datepicker.saturday.short"},
		                        {key: "text.ui.datepicker.sunday.min"},
		                        {key: "text.ui.datepicker.monday.min"},
		                        {key: "text.ui.datepicker.tuesday.min"},
		                        {key: "text.ui.datepicker.wednesday.min"},
		                        {key: "text.ui.datepicker.thursday.min"},
		                        {key: "text.ui.datepicker.friday.min"},
		                        {key: "text.ui.datepicker.saturday.min"},
		                        {key: "text.ui.datepicker.week"},
		                        {key: "text.ui.datepicker.dateformat"},
		                        {key: "text.ui.datepicker.year"},
		                        {key: "text.ui.datetimepicker.timeselect"},
		                        {key: "text.ui.datetimepicker.time"},
		                        {key: "text.ui.datetimepicker.hour"},
		                        {key: "text.ui.datetimepicker.minute"},
		                        {key: "text.ui.datetimepicker.second"},
		                        {key: "text.ui.datetimepicker.now"},
		                        {key: "text.ui.datetimepicker.finish"}
		                        ]);
		
		$.datepicker.regional['zh-CN'] = {
			closeText: texts[0],
			prevText: texts[1],
			nextText: texts[2],
			currentText: texts[3],
			monthNames: [texts[4],texts[5],texts[6],texts[7],texts[8],texts[9],texts[10],texts[11],texts[12],texts[13],texts[14],texts[15]],
			monthNamesShort: ['1','2','3','4','5','6',
			'7','8','9','10','11','12'],
			dayNames: [texts[16],texts[17],texts[18],texts[19],texts[20],texts[21],texts[22]],
			dayNamesShort: [texts[23],texts[24],texts[25],texts[26],texts[27],texts[28],texts[29]],
			dayNamesMin: [texts[30],texts[31],texts[32],texts[33],texts[34],texts[35],texts[36]],
			weekHeader: texts[37],
			dateFormat: texts[38],
			firstDay: 1,
			isRTL: false,
			showMonthAfterYear: true,
			yearSuffix: texts[39]};
		$.datepicker.setDefaults($.datepicker.regional['zh-CN']);

		if (typeof $.timepicker != "undefined") {
			$.timepicker.regional['zh-CN'] = {
					timeOnlyTitle: texts[40],
					timeText: texts[41],
					hourText: texts[42],
					minuteText: texts[43],
					secondText: texts[44],
					currentText: texts[45],
					closeText:  texts[46],
					ampm: false
				};
			$.timepicker.setDefaults($.timepicker.regional['zh-CN']);
		}
	}
});
