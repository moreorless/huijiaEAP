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

		<h5>团体报告查看 --
			(所属企业:${company.name}&nbsp;|&nbsp;号段范围:${segment.startId}-${segment.endId}&nbsp;|&nbsp;号段总人数:${segment.size})</h5>

		<table class="table table-striped table-bordered table-hover">
			<thead>
				<tr>
					<th>试卷名</th>
					<th>已注册人数</th>
					<th>已答题人数</th>
					<th>查看</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${quizList}" var="quiz">
					<tr>
						<td>${quiz.name }</td>
						<td>${segment.userCountRegistered}</td>
						<td>${quiz.userCountFinished}</td>
						<td><c:choose>
								<c:when test="${quiz.userCountFinished==0}">无团体报告   </c:when>
								<c:otherwise>
									<a
										href="${base}/segment/report?segmentId=${segment.id}&quizId=${quiz.id}"
										target="_blank" title="查看报告"><img
										src="${base}/images/report.png" /></a>
								</c:otherwise>
							</c:choose></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

	</div>

	<script type="text/javascript" src="${base}/js/cupid/core.js"></script>

	<script type="text/javascript">
		
	</script>
</body>
</html>