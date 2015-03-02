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
    <link type="text/css" rel="stylesheet" href="${base }/css/ui/validate/jquery.validate.css" />
	<style type="text/css">
		body {overflow: auto;background-color: #f3f3f3}
	</style>
</head>
<body>

<c:import url="/includes/header_huijia.jsp"></c:import>
<div class="container quiz_wrapper">
	<div class="quiz_inner_wrapper">
	<c:forEach items="${quizlist}" var="quiz" varStatus="stat">
	<div class="row quiz_card">
		<div class="col-md-10">
			<div class="header">
				<h2><img src="${base}/images/quiz/icons/${quiz.icon}" />${quiz.name}</h2>
			</div>
			<p class="desc">${quiz.description}</p>

			<c:set var="dateParts" value="${fn:split(segment.expireDate, '/')}" />
			<p class="expire">有效期&nbsp;:&nbsp;${dateParts[2]}/${dateParts[0]}/${dateParts[1]}</p>			
		</div>
		<div class="col-md-2">
			<div style="margin-bottom: 10px;">
			<a href="${base}/quiz/test?quizId=${quiz.id}" class="btn btn-primary" role="button">
				<c:if test="${fn:length(quizHistoryMap[quiz.id]) == 0}">开始测试</c:if>
				<c:if test="${fn:length(quizHistoryMap[quiz.id]) > 0}">重新测试</c:if>
				</a>
			</div>
			<c:if test="${fn:length(quizHistoryMap[quiz.id]) > 0}">
			<div style="margin-bottom: 10px;">
			<a href="${base}/quiz/report?quizId=${quiz.id}" class="btn btn-primary" role="button">查看报告</a>
			</div>
			</c:if>
		</div>
	</div>
	<c:if test="${!stat.last}">
		<div class="quiz_card_seperator"></div>
	</c:if>
	</c:forEach>
	<div style="height: 60px;"></div>
	</div>
</div>
<%@ include file="/includes/footer_huijia.jsp" %>

<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${base}/js/bootstrap-modalmanager.js"></script>
<script type="text/javascript" src="${base}/js/bootstrap-modal.js"></script>

<script type="text/javascript" src="${base }/js/cupid/core.js"></script>
<!--验证脚本 -->
<script type="text/javascript" src="${base }/js/ui/validate/jquery.validate.js" ></script>
<script type="text/javascript" src="${base }/js/ui/validate/messages_cn.js" ></script>
<script type="text/javascript" src="${base }/js/ui/validate/jquery.validate.ext.methods.js" ></script>
<script type="text/javascript" src="${base}/js/huijia/index.js"></script>
</body>
</html>
