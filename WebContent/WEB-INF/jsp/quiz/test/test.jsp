<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>答题测验</title>
	<link type="text/css" rel="stylesheet" href="${base}/css/bootstrap.min.css" />
	<link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
</head>
<body>

<div class="container">
	<h2>答题测验</h2>
	<c:forEach items="${questionlist}" var="question">
	<div class="row">
		${question.name}
	</div>
		
	</c:forEach>

</div>

<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/js/bootstrap.min.js"></script>

  </body>
</html>
