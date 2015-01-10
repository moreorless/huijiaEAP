var SKIN = {
	COOKIE_KEY: "SKIN",
	SKINABLE_ATTR: "data-skin"
};

(function() {
	var skin = (result = new RegExp('(?:^|; )' + SKIN.COOKIE_KEY + '=([^;]*)').exec(document.cookie)) ? decodeURIComponent(result[1]) : null;
	var replacement = "";
	if (skin)
		replacement += "!" + skin;
	replacement += ".css";

	var links = document.getElementsByTagName("LINK");
	if (links && links.length > 0) {
		for (var i = 0; i < links.length; i++) {
			var link = links[i];
			var type = link.getAttribute(SKIN.SKINABLE_ATTR);
			var href = link.href.replace(/(![^!\.]+)?\.css$/, replacement);
			if (type && type == "replace")
				link.href = href;
			else if (type && type == "append")
				document.write('<link data-skin="remove" type="text/css" rel="stylesheet" href="' + href + '" />');
		}
	}
	
})();
