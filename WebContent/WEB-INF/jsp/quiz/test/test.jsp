<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>会佳心语测评系统</title>
	<link type="text/css" rel="stylesheet" href="${base}/css/bootstrap.min.css" />
	<link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
	<link type="text/css" rel="stylesheet" href="${base}/css/quiz/quiz.css" />
	<link type="text/css" rel="stylesheet" href="${base}/css/huijia.css"/>
	<style type="text/css">
		body {overflow: auto;background-color: #f3f3f3}
	</style>
</head>
<body>
<c:import url="/includes/header_huijia.jsp"></c:import>
<div class="container quiz_wrapper">
	<p class="quiz-tip">温馨提示：请注意保护个人测评信息。</p>
<div class="quiz_inner_wrapper">
	<form class="form-horizontal" role="form" action="${base}/quiz/answer" method="post">
	<input type="hidden" name="quizId" value="${quiz.id}"/>
	<c:forEach items="${quiz.items}" var="quizitem" varStatus="stat">
		<div class="row question-card <c:if test="${stat.index % 2 ==0}">light</c:if>">
			<h4>${stat.index + 1}.&nbsp;${quizitem.question}</h4>
			<c:forEach items="${quizitem.options}" var="qOption">
			<div class="radio">
			  <label>
			    <input type="radio" name="answer[${quizitem.id}]" id="answerOption${quizitem.id}${qOption.index}" value="${qOption.value}" />
			    	${qOption.index}.&nbsp;${qOption.content}
			  </label>
			</div>
			</c:forEach>
		</div>
	</c:forEach>
	
	<div class="row">
    	<div style="text-align: center;height: 60px;line-height: 60px;padding-top: 20px;">
      	<button type="submit" class="btn btn-primary">提交</button>
      	<%--
      	<a href="${base}/quiz/enquizlist" class="btn btn-default">返回</a>
      	 --%>
    	</div>
  	</div>
	</form>
</div>
</div>

<%@ include file="/includes/footer_huijia.jsp" %>

<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${base}/js/bootstrap-modalmanager.js"></script>
<script type="text/javascript" src="${base}/js/bootstrap-modal.js"></script>

<script type="text/javascript" src="${base }/js/cupid/core.js"></script>
<script type="text/javascript" src="${base}/js/huijia/index.js"></script>
</body>
</html>
