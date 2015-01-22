<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>查看问卷</title>
<link type="text/css" rel="stylesheet"
	href="${base}/css/bootstrap.min.css" />
<link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
<link type="text/css" rel="stylesheet"
	href="${base }/css/ui/validate/jquery.validate.css" />

<link rel="stylesheet" href="${base}/css/ui/jquery-ui.css" />
<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/js/ui/jquery-ui.js"></script>

<script>
	$(function() {
		$("#tabs").tabs();
	});
</script>



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

	<div class="container" id="wrap" style="height: 580px; overflow: auto">
		<div id="tabs">
			<ul>
				<li><a href="#tabs-1">基本信息</a></li>
				<li><a href="#tabs-2">題目信息</a></li>
				<li><a href="#tabs-3">个人评价信息</a></li>
				<li><a href="#tabs-4">团体评价信息</a></li>
			</ul>
			<div id="tabs-1">
				<table class="table table-striped table-hover table-bordered">
					<tr>
						<td style="width: 15%">名称</td>
						<td>${quiz.name}</td>
					</tr>
					<tr>
						<td style="width: 15%">描述</td>
						<td>${quiz.description}</td>
					</tr>
					<tr>
						<td style="width: 15%">题目个数</td>
						<td>${quiz.itemNum}</td>
					</tr>
					<tr>
						<td style="width: 15%">测谎总分分界线</td>
						<td>${quiz.lieBorder}</td>
					</tr>
					<tr>
						<td style="width: 15%">维度Json</td>
						<td>${quiz.categoryJson}</td>
					</tr>
					<tr>
						<td style="width: 15%">维度个数</td>
						<td>${quiz.categoryNum}</td>
					</tr>
				</table>
			</div>
			<div id="tabs-2">
				<table class="table table-striped table-hover table-bordered">
					<thead>
						<tr>
							<th id="question" style="width: 5%">id</th>
							<th id="question" style="width: 20%">问题</th>
							<th id="isLie" style="width: 5%">测谎</th>
							<th id="optionJson" style="width: 70%">选项Json</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${quizItems}" var="item">
							<tr>
								<td>${item.id}</td>
								<td>${item.question}</td>
								<td>${item.lieFlag }</td>
								<td>${item.optionJson}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div id="tabs-3">
				<table class="table table-striped table-hover table-bordered">
					<thead>
						<tr>
							<th style="width: 8%">维度</th>
							<th style="width: 8%">分数区间</th>
							<th style="width: 8%">健康度</th>
							<th style="width: 20%">评语</th>
							<th style="width: 20%">建议</th>
							<th style="width: 18%">解释</th>
							<th style="width: 18%">特征</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${quizEvaluationsSingle}" var="item">
							<tr>
								<td>${item.categoryName}</td>
								<td>${item.minScore}-${item.maxScore}</td>
								<td>${item.healthStatus}</td>
								<td>${item.evaluation}</td>
								<td>${item.suggestion}</td>
								<td>${item.explanation}</td>
								<td>${item.feature}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div id="tabs-4">
				<table class="table table-striped table-hover table-bordered">
					<thead>
						<tr>
							<th style="width: 8%">维度</th>
							<th style="width: 8%">分数区间</th>
							<th style="width: 8%">健康度</th>
							<th style="width: 20%">评语</th>
							<th style="width: 20%">建议</th>
							<th style="width: 18%">解释</th>
							<th style="width: 18%">特征</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${quizEvaluationsTeam}" var="item">
							<tr>
								<td>${item.categoryName}</td>
								<td>${item.minScore}-${item.maxScore}</td>
								<td>${item.healthStatus}</td>
								<td>${item.evaluation}</td>
								<td>${item.suggestion}</td>
								<td>${item.explanation}</td>
								<td>${item.feature}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>


	</div>



</body>
</html>