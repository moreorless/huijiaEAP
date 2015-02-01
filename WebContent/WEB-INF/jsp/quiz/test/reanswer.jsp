<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title><fmt:message key="index.product.name" bundle="${i18n_main}"/></title>
	<link type="text/css" rel="stylesheet" href="${base}/css/bootstrap.min.css" />
	<link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
	<link type="text/css" rel="stylesheet" href="${base}/css/quiz/quiz.css" />
	<link type="text/css" rel="stylesheet" href="${base}/css/huijia.css"/>
	<style type="text/css">
		body {background-color: #f3f3f3}
		#scrollWrapper {overflow: auto;height: 100%}
		
		#tip-dialog {display:none;position:absolute;z-index:9999;width: 250px;}
		#tip-dialog .content {float:left}
		#tip-dialog .content span {color: green; font-weight: bold; font-size: 18px;}
		#tip-dialog button {float:right; margin:5px;}
	</style>
</head>
<body>
<div id="scrollWrapper">
<c:import url="/includes/header_huijia.jsp"></c:import>

<div class="container quiz_wrapper">
	<p class="quiz-tip">请仔细作答。</p>
	<p style="text-align:center"><a href="${base}/quiz/test?id=${param.id}" class="btn btn-primary"></a></p>
</div>

<%@ include file="/includes/footer_huijia.jsp" %>
</div>

</body>
</html>
