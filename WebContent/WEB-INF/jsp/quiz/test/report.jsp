<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>测验报告</title>
	<link type="text/css" rel="stylesheet" href="${base}/css/bootstrap.min.css" />
	<link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
	<link type="text/css" rel="stylesheet" href="${base}/css/quiz/report.css" />
	<link type="text/css" rel="stylesheet" href="${base}/css/huijia.css"/>
	<style type="text/css">
		body {overflow: auto;background-color: #f3f3f3}
	</style>
</head>
<body>
	<c:import url="/includes/header_huijia.jsp"></c:import>
	<div class="container report-wrapper">
		<h2 class="report-caption">您的测试结果</h2>
		<div>
		<c:import url="/includes/report/communication_person.jsp"></c:import>
		</div>
	</div>
	<%@ include file="/includes/footer_huijia.jsp" %>
<script type="text/javascript" src="${base}/js/echarts/echarts-all.js"></script>
<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/js/huijia/report.js"></script>
</body>
</html>
