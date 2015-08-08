<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><fmt:message key="index.product.name" /></title>
<link type="text/css" rel="stylesheet"
	href="${base}/css/bootstrap.min.css" />
<link type="text/css" rel="stylesheet" href="${base}/css/common.css" />

<style type="text/css">
#wrap {
	padding: 10px;
}

em {
	color: red
}
</style>
</head>
<body>
	<div class="container" id="wrap">
		<%@ include file="/error/inline.jsp"%>

		<h5>个人报告查看 -- ${user.realname }</h5>

		<table class="table table-striped table-bordered table-hover">
			<thead>
				<tr>
					<th>试题</th>
					<!-- th>答题时间</th -->
					<th>个人报告</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${quizlist}" var="quiz">
					<tr>
						<td>${quiz.name }</td>
						<!-- td></td -->
						<td><a href="${base}/quiz/reportexport?quizId=${quiz.id}&userId=${user.userId}">下载</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

	</div>
</body>
</html>