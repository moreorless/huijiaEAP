<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>试题列表</title>
	<link type="text/css" rel="stylesheet" href="${base}/css/bootstrap.min.css" />
	<link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
	<link type="text/css" rel="stylesheet" href="${base}/css/quiz/quiz.css" />
	<style type="text/css">
		body {overflow: auto}
	</style>
</head>
<body>

<div class="container">
	<c:forEach items="${quizlist}" var="quiz" varStatus="stat">
	<div class="row quiz_card">
		<div class="header">
			<h2><img src="${base}/images/quiz/icons/${quiz.icon}" />${quiz.name}</h2>
			<a href="${base}/quiz/test?id=${quiz.id}" class="btn btn-primary active" role="button">开始测试</a>
		</div>
		<p class="desc">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${quiz.description}</p>
		<p class="expire">有效期为yyyy-mm-dd - yyyy-mm-dd</p>
		<c:if test="${!stat.last}">
			<div class="quiz_card_seperator"></div>
		</c:if>
	</div>
	
	</c:forEach>

</div>

<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/js/bootstrap.min.js"></script>

  </body>
</html>
