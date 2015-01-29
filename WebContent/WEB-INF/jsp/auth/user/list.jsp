<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>用户列表</title>
    <link type="text/css" rel="stylesheet" href="${base}/css/bootstrap.min.css" />
	<link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
	<style type="text/css">
		#wrap {padding:10px;}
		#toolbar{height:30px; line-height:30px; margin-top:10px;}
	</style>
  </head>
  <body>

<div id="wrap" class="container">
	<div id="toolbar">
		<a href="${base}/user/prepare?operation=add" class="btn" >
			<img src="${base}/images/add_default.gif" />&nbsp;添加用户</a>
	</div>
	<table class="table table-striped table-bordered table-hover">
		<thead>
			<tr>
				<th>用户名</th>
				<th>真实姓名</th>
				<th>用户编码</th>
				<th><fmt:message key="user.add.mobile" bundle="${i18n_auth}"/></th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${userList}" var="user">
			<tr>
				<td>${user.name }</td>
				<td>${user.realname }</td>
				<td>${user.code }</td>
				<td>${user.mobile }</td>
				<td><a href="${base}/user/prepare?userId=${user.userId}&operation=edit" title="修改用户"><img src="${base}/images/edit_default.gif"/></a>
					&nbsp;|&nbsp;
					<a href="${base}/user/delete?userId=${user.userId}" title="删除用户"><img src="${base}/images/del_default.gif"/></a>
				</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>


</div>

<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/js/bootstrap.min.js"></script>

  </body>
</html>
