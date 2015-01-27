<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>编辑用户</title>
<link type="text/css" rel="stylesheet"
	href="${base}/css/bootstrap.min.css" />
<link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
<link type="text/css" rel="stylesheet"
	href="${base}/css/ui/validate/jquery.validate.css" />

<link rel="stylesheet" href="${base}/css/ui/jquery-ui.css" />
<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/js/ui/jquery-ui.js"></script>
<script>
	$(function() {
		//$("#expireDate").datepicker();
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
	<div class="container" id="wrap">
		<%@ include file="/error/inline.jsp"%>
		<form class="form-horizontal" role="form"
			action="${base}/segment/${param.operation}" method="post">
			<input type="hidden" name="companyId" value="${segment.companyId}" />
			<input type="hidden" name="id" value="${segment.id}" /> <input
				type="hidden" name="startId" value="${segment.startId}" /> <input
				type="hidden" name="endId" value="${segment.endId}" />
			<fieldset>
				<legend>
					<c:if test="${param.operation == 'add' }">添加号段</c:if>
					<c:if test="${param.operation == 'edit' }">编辑号段</c:if>
				</legend>


				<div class="form-group">
					<label class="col-sm-2 control-label">用户数量</label>
					<div class="col-sm-2">
						<input type="text" name="size" id="size" value="${segment.size}"
							<c:if test="${param.operation == 'edit' }">readonly="readonly"</c:if> />
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">初始密码</label>
					<div class="col-sm-2">
						<input type="text" name="initPassword" id="initPassword"
							value="${segment.initPassword}"
							<c:if test="${param.operation == 'edit' }">readonly="readonly"</c:if> />
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">是否启用</label>
					<div class="col-sm-2">
						<select class="form-control" name="status" id="status"">
							<option value="0"
								<c:if test="${segment.status == 0}"> selected</c:if>>失效</option>
							<option value="1"
								<c:if test="${segment.status == 1}"> selected</c:if>>启用</option>
						</select>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">描述</label>
					<div class="col-sm-10">
						<textarea class="form-control" rows="3" name="description"
							id="description">${segment.description}</textarea>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">到期时间</label>
					<div class="col-sm-10">
						<input type="text" name="expireDate" id="expireDate"
							value="${segment.expireDate}" readonly="readonly" />
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">可用问卷</label>
					<div class="col-sm-10">
						<c:forEach items="${allQuizList}" var="quiz">
							<input type="checkbox" name="myQuizIds" id="myQuizIds"
								value="${quiz.id}"> &nbsp; ${quiz.name} <br /> </input>
						</c:forEach>
					</div>
				</div>

				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<button class="btn btn-primary">保存</button>
						<a href="${base}/company/list" class="btn btn-default">取消</a>
					</div>
				</div>
			</fieldset>
		</form>
	</div>

	<script type="text/javascript" src="${base}/js/cupid/core.js"></script>
	<!--验证脚本 -->
	<script type="text/javascript"
		src="${base }/js/ui/validate/jquery.validate.js"></script>
	<script type="text/javascript"
		src="${base }/js/ui/validate/messages_cn.js"></script>
	<script type="text/javascript"
		src="${base }/js/ui/validate/jquery.validate.ext.methods.js"></script>
	<script type="text/javascript">
		function setCheckBox(value) {
			var boxes = document.getElementsByName("myQuizIds");

			for ( var i = 0; i < boxes.length; i++) {
				if (boxes[i].value == value)
					boxes[i].checked = true;
			}

		}

		$(document).ready(function() {
			$("#expireDate").datepicker();
			$("#expireDate").blur();
			<c:forEach items="${segment.myQuizList}" var="quiz">
			setCheckBox("${quiz.id}");
			</c:forEach>
		});
	</script>
</body>
</html>