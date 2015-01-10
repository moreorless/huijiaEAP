/*
 * Translated default messages for the jQuery validation plugin.
 * Locale: CN
 */
jQuery.extend(jQuery.validator.messages, $.messages([
               {key: "text.ui.validate.messages.required", id: "required"},
               {key: "text.ui.validate.messages.remote", id: "remote"},
               {key: "text.ui.validate.messages.email", id: "email"},
               {key: "text.ui.validate.messages.url", id: "url"},
               {key: "text.ui.validate.messages.date", id: "date"},
               {key: "text.ui.validate.messages.dateISO", id: "dateISO"},
               {key: "text.ui.validate.messages.number", id: "number"},
               {key: "text.ui.validate.messages.digits", id: "digits"},
               {key: "text.ui.validate.messages.creditcard", id: "creditcard"},
               {key: "text.ui.validate.messages.equalTo", id: "equalTo"},
               {key: "text.ui.validate.messages.accept", id: "accept"},
               {key: "text.ui.validate.messages.maxlength", id: "_maxlength"},
               {key: "text.ui.validate.messages.minlength", id: "_minlength"},
               {key: "text.ui.validate.messages.rangelength", id: "_rangelength"},
               {key: "text.ui.validate.messages.range", id: "_range"},
               {key: "text.ui.validate.messages.max", id: "_max"},
               {key: "text.ui.validate.messages.min", id: "_min"}
               ]));
jQuery.extend(jQuery.validator.messages, {
	maxlength: jQuery.validator.format(jQuery.validator.messages._maxlength),
	minlength: jQuery.validator.format(jQuery.validator.messages._minlength),
	rangelength: jQuery.validator.format(jQuery.validator.messages._rangelength),
	range: jQuery.validator.format(jQuery.validator.messages._range),
	max: jQuery.validator.format(jQuery.validator.messages._max),
	min: jQuery.validator.format(jQuery.validator.messages._min)
});