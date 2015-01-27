<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户列表</title>
<link type="text/css" rel="stylesheet"
	href="${base}/css/bootstrap.min.css" />
<link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
<style type="text/css">
#wrap {
	padding: 10px;
}

#toolbar {
	height: 30px;
	line-height: 30px;
	margin-top: 10px;
}
</style>
</head>
<body>

	<div id="wrap" class="container">

		<table class="table table-striped table-bordered table-hover">
			<thead>
				<tr>
					<th>用户名</th>
					<th>真实姓名</th>
					<th>邮箱</th>
					<th><fmt:message key="user.add.mobile" bundle="${i18n_auth}" /></th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${userList}" var="user">
					<tr>
						<td>${user.name }</td>
						<td>${user.realname }</td>
						<td>${user.email }</td>
						<td>${user.phone }</td>
						<td><a href="#">个人报告</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>


	</div>

	<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${base}/js/bootstrap.min.js"></script>

</body>
</html>
