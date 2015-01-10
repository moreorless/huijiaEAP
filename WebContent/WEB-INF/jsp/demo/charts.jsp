<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; UTF-8" />
<title>charts</title>
<link rel="stylesheet" type="text/css" media="screen" href="${base }/css/reset.css" />
<link rel="stylesheet" type="text/css" media="screen" href="${base }/css/ui/chart/chart.css" />
<style type="text/css">
#pie-chart-wrapper {
  width: 300px;
  height: 300px;
  float: left;
  margin: 5px 15px 5px;
}
#radar-chart-wrapper {
  width: 300px;
  height: 300px;
  display: inline-block;
}

</style>
</head>
<body>

<div ></div>

<div id="bar-chart" data-save-name="柱图"></div>

<div id="bar-chart-with-legend"></div>

<div id="hbar-chart"></div>

<div id="pie-chart-wrapper" class="ui-widget-content">
  <div id=""></div>
</div>
<div id="radar-chart-wrapper" class="ui-widget-content">
  <div id="line-chart"></div>
</div>

<div id="area-chart"></div>

<div id="radar-chart"></div>

<div id="stacked-chart"></div>

<div id="h-stacked-chart"></div>

<div id="gauge-chart"></div>

<div id="another-gauge-chart"></div>

<div id="sonar-chart"></div>

<div id="dual-axis-chart"></div>

<script type="text/javascript" src="${base }/js/jquery.min.js"></script>
<script type="text/javascript" src="${base }/js/ui/jquery-ui.min.js"></script>
<script type="text/javascript" src="${base }/js/commons/flashutil/swfobject.js"></script>
<script type="text/javascript" src="${base }/js/cupid/core.js"></script>
<script type="text/javascript" src="${base }/js/ui/chart/chart.js"></script>
<script type="text/javascript">
$("div:not(.ui-widget-content, .pane)").chart([
                      {width: 650, height: 200, options: "/demo/chart/viewColors?param1=param1&param2=123"},
                      {width: 550, height: 200, options: "/demo/chart/barChart"},
                      {width: 650, height: 200, options: "/demo/chart/barChartWithLegend"},
                      {width: 650, height: 300, options: "/demo/chart/hBarChart"},
                      {width: "100%", height: "100%", options: "/demo/chart/pieChart"},
                      {width: "100%", height: "100%", options: "/demo/chart/lineChart"},
                      {width: 650, height: 200, options: "/demo/chart/areaChart"},
                      {width: 550, height: 200, options: "/demo/chart/radarChart"},
                      {width: 550, height: 200, options: "/demo/chart/stackbarChart"},
                      {width: 550, height: 200, options: "/demo/chart/hStackbarChart"}
                    ]);

$("#gauge-chart").gauge(300, 300, "/demo/chart/gaugeChart");
$("#another-gauge-chart").gauge(300, 300, "/demo/chart/anotherGaugeChart");

$("#sonar-chart").sonar(300, 300, "/demo/chart/sonarChart");

$("#dual-axis-chart").chart(800, 400, "/demo/chart/dualAxis");
/*
swfobject.embedSWF(
		"${base}/flash/open-flash-chart.swf", "pie-chart",
		"100%", "100%", "9.0.0", "expressInstall.swf",
		{"data-file":"${base}/demo/chart/pieChart"}, {"wmode": "opaque"} );
		
swfobject.embedSWF(
		"${base}/flash/open-flash-chart.swf", "line-chart",
		"550", "200", "9.0.0", "expressInstall.swf",
		{"data-file":"${base}/demo/chart/lineChart"}, {"wmode": "opaque"} );

swfobject.embedSWF(
		"${base}/flash/open-flash-chart.swf", "stacked-chart",
		"550", "200", "9.0.0", "expressInstall.swf",
		{"data-file":"${base}/demo/chart/stackbarChart"}, {"wmode": "opaque"} );

swfobject.embedSWF(
		"${base}/flash/open-flash-chart.swf", "radar-chart",
		"100%", "100%", "9.0.0", "expressInstall.swf",
		{"data-file":"${base}/demo/chart/radarChart"}, {"wmode": "opaque"} );
*/
$(document).ready(function(){
	$("#pie-chart-wrapper").resizable();
	$("#radar-chart-wrapper").resizable();
});

function click_handler(element_idx, text) {
	alert("u r clicked " + text);
}

function done(id) {
	alert(id);
}

setInterval(function() {
	$("#bar-chart").reloadchart();
}, 5000);
setInterval(function() {
	$("#gauge-chart").reloadgauge();
}, 5000);

setInterval(function() {
	$("#line-chart").reloadchart();
}, 1000);

function stackedClick(id, ext) {
	alert("key " + id + " -> " + ext);
}

function pie_click_handler(id, ext) {
	alert("slice " + id + " -> " + ext);
}

function hstack_click_handler(id, ext) {
	alert("key " + id + " -> " + ext);
}

function hbar_click_handler(id, ext) {
	alert("key " + id + " -> " + ext);
}

function sonar_click_handler(id, ext) {
	alert("key " + id + " -> " + ext);
}
/*
function save_image(id) {
	$("#" + id).get(0).post_image("${base}/demo/chart/save", "done", true);
}
*/
</script>
</body>
</html>