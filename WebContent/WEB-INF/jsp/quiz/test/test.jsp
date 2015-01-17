<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>会佳心语测评系统</title>
	<link type="text/css" rel="stylesheet" href="${base}/css/bootstrap.min.css" />
	<link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
</head>
<body>

<div class="container">
	<h2>答题测验</h2>
	<form role="form" action="${base}/quiz/answer" method="post">
	<input type="hidden" name="quizId" value="${quiz.id}"/>
	<c:forEach items="${quiz.items}" var="question">
		<div class="row">
			${question.name}
		</div>
		<c:forEach items="${question.options}" var="qOption">
		<div class="radio">
		  <label>
		    <input type="radio" name="answer[${question.id}]" id="answerOption${question.id}${qOption.index}" value="${qOption.value}" />
		    	${qOption.index}.&nbsp;${qOption.name}
		  </label>
		</div>
		</c:forEach>
	</c:forEach>
	
	<div class="form-group">
    	<div class="col-sm-offset-2 col-sm-10">
      	<button type="submit" class="btn btn-default">提交</button>
    	</div>
  	</div>
	</form>
</div>

<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/js/bootstrap.min.js"></script>
</body>
</html>
