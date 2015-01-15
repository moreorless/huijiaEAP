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
			action="${base}/company/${param.operation}" method="post">
			<input type="hidden" name="id" value="${company.id}" />
			<fieldset>
				<legend>
					<c:if test="${param.operation == 'add' }">添加公司信息</c:if>
					<c:if test="${param.operation == 'edit' }">编辑公司信息</c:if>
				</legend>

				<div class="form-group">
					<label class="col-sm-2 control-label">名称<em>*</em></label>
					<div class="col-sm-10">
						<input type="text" class="form-control" name="name" id="name"
							value="${company.name}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">编码（4位大写字母）<em>*</em></label>
					<div class="col-sm-10">
						<c:if test="${param.operation == 'add' }">
							<input type="text" class="form-control" name="code" id="code"
								value="${company.code}" />
						</c:if>
						<c:if test="${param.operation == 'edit' }">
							<input type="text" class="form-control" name="code" id="code"
								value="${company.code}" readonly="readonly"/>
						</c:if>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">描述</label>
					<div class="col-sm-10">
						<textarea class="form-control" rows="3" name="description"
							id="description">${company.description}</textarea>
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

	<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${base}/js/cupid/core.js"></script>
	<!--验证脚本 -->
	<script type="text/javascript"
		src="${base }/js/ui/validate/jquery.validate.js"></script>
	<script type="text/javascript"
		src="${base }/js/ui/validate/messages_cn.js"></script>
	<script type="text/javascript"
		src="${base }/js/ui/validate/jquery.validate.ext.methods.js"></script>
	<script type="text/javascript">
		
	</script>
</body>
</html>