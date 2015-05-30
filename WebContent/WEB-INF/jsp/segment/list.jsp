<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>号段管理</title>
<link type="text/css" rel="stylesheet"
	href="${base}/css/bootstrap.min.css" />
<link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
<link type="text/css" rel="stylesheet" href="${base}/css/pagination.css" />
<link type="text/css" rel="stylesheet" href="${base}/css/quiz/quiz.css" />
<style type="text/css">
body {
	overflow: auto
}

#toolbar {
	height: 30px;
	line-height: 30px;
	margin-top: 10px;
}

.pagination {
	margin: 0
}
</style>
</head>
<body>
	<div class="container">
		<div id="toolbar">
			<a
				href="${base}/segment/prepare?operation=add&companyId=${company.id}"
				class="btn"> <img src="${base}/images/add_default.gif" />&nbsp;添加
			</a>
		</div>
		<table id="segment-table"
			class="table table-striped table-bordered table-hover">
			<thead>
				<tr>
					<th id="description">描述</th>
					<th id="startId">号段范围</th>
					<th id="size">号段长度</th>
					<th id="status">状态</th>
					<th id="operation">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${obj.data }" var="segment">
					<tr>
						<td>${segment.description }</td>
						<td>${segment.startId }-${segment.endId }</td>
						<td>${segment.size }</td>
						<c:if test="${segment.status == '0' }">
							<td>已失效</td>
						</c:if>
						<c:if test="${segment.status == '1' }">
							<td>使用中</td>
						</c:if>
						<td><a href="${base}/segment/exportUsers?id=${segment.id}">下载号段</a>&nbsp;|&nbsp;
							<a href="${base}/segment/listUsers?id=${segment.id}">查看用户</a>&nbsp;|&nbsp;
							<a href="${base}/segment/quizlist?id=${segment.id}">查看报告</a>&nbsp;|&nbsp;
							<a href="${base}/segment/prepare?operation=edit&id=${segment.id}&companyId=${company.id}">修改</a>
							&nbsp;|&nbsp; <a href="${base}/segment/delete?id=${segment.id}&companyId=${company.id}">删除</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div id="pager"></div>
	</div>

	<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${base}/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${base}/js/jquery.pagination.js"></script>
	<script type="text/javascript">
	/**
	 * Callback function that displays the content.
	 *
	 * Gets called every time the user clicks on a pagination link.
	 *
	 * @param {int} page_index New Page index
	 * @param {jQuery} jq the container with the pagination links as a jQuery object
	 */
	function pageselectCallback(page_index, jq){
		if((page_index + 1) == ${obj.page}) {
			return false;
		}

	    window.location = '${base}/company/list?page=' + (page_index + 1);
	    return false;
	}
	function initPagination() {
	    $("
							#pager").pagination(${obj.records}, {
	        callback :
							pageselectCallback,
	        items_per_page
							: ${obj.pageSize},
	        current_page
							: ${obj.page -1},
	        next_text
							: '后一页',
	        prev_text: '前一页'
	        
	    });
	}
	$(document).ready(function(){
		initPagination();
	});
</script>
</body>
</html>
