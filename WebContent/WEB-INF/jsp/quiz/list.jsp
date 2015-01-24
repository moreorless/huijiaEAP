<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>试题列表</title>
<link type="text/css" rel="stylesheet"
	href="${base}/css/bootstrap.min.css" />
<link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
<link type="text/css" rel="stylesheet" href="${base}/css/pagination.css" />

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
	<%@ include file="/error/inline.jsp"%>
		<div id="toolbar">
			<a href="${base}/quiz/prepare?operation=add" class="btn"> <img
				src="${base}/images/add_default.gif" />&nbsp;添加
			</a>
		</div>
		<table id="quiz-table"
			class="table table-striped table-bordered table-hover">
			<thead>
				<tr>
					<th id="name">试题名称</th>
					<th id="type">试题类型</th>
					<th id="name">图标</th>
					<th id="description">描述</th>
					<th id="usedcount">使用次数</th>
					<th id="createat">创建时间</th>
					<th id="operation">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${obj.data }" var="quiz">
					<tr>
						<td data-id="${company.id }">${quiz.name }</td>
						<c:if test="${quiz.type == 0}">
							<td>独立问卷</td>
						</c:if>
						<c:if test="${quiz.type == 1}">
							<td>组合问卷</td>
						</c:if>
						<td><img src="${base}/images/quiz/icons/${quiz.icon}"></img></td>
						<td>${quiz.description }</td>
						<td></td>
						<td></td>

						<td><c:if test="${quiz.type == 0}">
								<a href="${base}/quiz/viewquiz?id=${quiz.id}">查看</a>&nbsp;|&nbsp;</c:if>
							<a href="${base}/quiz/prepare?operation=edit&id=${quiz.id}">修改</a>
							&nbsp;|&nbsp; <a href="${base}/quiz/delete?id=${quiz.id}">删除</a>
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

	    window.location = '${base}/quiz/list?page=' + (page_index + 1);
	    return false;
	}
	function initPagination() {
	    $("#pager").pagination(${obj.records}, {
	        callback : pageselectCallback,
	        items_per_page : ${obj.pageSize},
	        current_page : ${obj.page -1},
	        next_text : '后一页',
	        prev_text : '前一页'
	        
	    });
	}
	$(document).ready(function(){
		initPagination();
	});
</script>
</body>
</html>
