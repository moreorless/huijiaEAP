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
	</style>
</head>
<body>
<div id="scrollWrapper">
<c:import url="/includes/header_huijia.jsp"></c:import>

<div class="container quiz_wrapper">
	<h3 style="color:green;">恭喜您已完成答题。</h3>
	<p>您可以点击下面的“查看报告”按钮查看测试结果。</p>
	<div style="text-align:center">
		<a href="${base}/quiz/enquizlist" class="btn primary btn-lg"><img src="${base}/images/quiz/go_home.png" />&nbsp;返回首页</a>
	</div>
	<div style="text-align:center">
		<a href="${base}/quiz/reportexport?quizId=${param.quizId}" target="_blank" class="btn primary btn-lg" id="btn-export" target="_blank"><img src="${base}/images/quiz/pdf.png" />&nbsp;查看报告</a>
	</div>
            
</div>

<%@ include file="/includes/footer_huijia.jsp" %>
</div>

</body>
</html>
