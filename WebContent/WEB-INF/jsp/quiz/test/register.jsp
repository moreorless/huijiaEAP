<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>新用户注册</title>
<link type="text/css" rel="stylesheet"
	href="${base}/css/bootstrap.min.css" />
<link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
<link type="text/css" rel="stylesheet" href="${base}/css/quiz/quiz.css" />
<link type="text/css" rel="stylesheet" href="${base}/css/huijia.css" />
<link type="text/css" rel="stylesheet"
	href="${base }/css/ui/validate/jquery.validate.css" />
<style type="text/css">
body {
	overflow: auto;
	background-color: #f3f3f3
}
</style>
</head>
<body>
	<c:import url="/includes/header_huijia.jsp"></c:import>
	<div class="container quiz_wrapper">
		<div class="quiz_inner_wrapper">
			<%@ include file="/error/inline.jsp"%>
			<form action="${base }/user/register" class="form-horizontal"
				role="form" method="post">
				<input type="hidden" name="userId" value="${user.userId }" /> <input
					type="hidden" name="name" value="${user.name }" /> <input
					type="hidden" name="type" value="${user.type }" /> <input
					type="hidden" name="companyId" value="${user.companyId }" /> <input
					type="hidden" name="segmentId" value="${user.segmentId }" /> <input
					type="hidden" name="code" value="${user.code }" />

				<fieldset>

					<div class="form-group">
						<label class="col-sm-2 control-label">真实姓名<em>*</em></label>
						<div class="col-sm-4">
							<input type="text" class="form-control" name="realname"
								value="${user.realname}" />
						</div>
						<div class="col-sm-6">
							<span><fmt:message key="user.add.realname.span"
									bundle="${i18n_auth}" /></span>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">密码<em>*</em></label>
						<div class="col-sm-4">
							<input type="password" class="form-control" name="password"
								id="password" value="${user.password}" />
						</div>
						<div class="col-sm-6">
							<span><fmt:message key="user.add.password.span"
									bundle="${i18n_auth}" /></span>
						</div>
					</div>


					<div class="form-group">
						<label class="col-sm-2 control-label"><fmt:message
								key="user.add.repassword" bundle="${i18n_auth}" /><em>*</em></label>
						<div class="col-sm-4">
							<input type="password" class="form-control" name="repassword"
								id="repassword"
								value="${not empty user ? '#PassWord#' : user.password }" />
						</div>
						<div class="col-sm-6">
							<span><fmt:message key="user.add.repassword.span"
									bundle="${i18n_auth}" /></span>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">性别<em>*</em></label>
						<div class="col-sm-4">
							<select class="form-control" name="gender" id="sel-gender">
								<option value="0"
									<c:if test="${user.gender == 0}"> selected</c:if>>女</option>
								<option value="1"
									<c:if test="${user.gender == 1}"> selected</c:if>>男</option>
							</select>
						</div>
						<div class="col-sm-6">
							<span><fmt:message key="user.add.gender.span"
									bundle="${i18n_auth}" /></span>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">年龄<em>*</em></label>
						<div class="col-sm-4">
							<input type="text" class="form-control" name="age"
								value="${user.age}" />
						</div>
						<div class="col-sm-6">
							<span><fmt:message key="user.add.age.span"
									bundle="${i18n_auth}" /></span>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">工作年限<em>*</em></label>
						<div class="col-sm-4">
							<input type="text" class="form-control" name="workage"
								value="${user.workage}" />
						</div>
						<div class="col-sm-6">
							<span><fmt:message key="user.add.workage.span"
									bundle="${i18n_auth}" /></span>
						</div>
					</div>




					<div class="form-group">
						<label class="col-sm-2 control-label">教育程度<em>*</em></label>
						<div class="col-sm-4">
							<select class="form-control" name="education" id="sel-education">
								<option value="0"
									<c:if test="${user.education == 0}"> selected</c:if>>大专及以下</option>
								<option value="1"
									<c:if test="${user.education == 1}"> selected</c:if>>本科</option>
								<option value="2"
									<c:if test="${user.education == 2}"> selected</c:if>>硕士及以上</option>
							</select>
						</div>
						<div class="col-sm-6">
							<span><fmt:message key="user.add.education.span"
									bundle="${i18n_auth}" /></span>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">职位<em>*</em></label>
						<div class="col-sm-4">
							<select class="form-control" name="jobtitle" id="sel-jobtitle">
								<option value="0"
									<c:if test="${user.jobtitle == 0}"> selected</c:if>>普通员工</option>
								<option value="1"
									<c:if test="${user.jobtitle == 1}"> selected</c:if>>中层管理人员</option>
								<option value="2"
									<c:if test="${user.jobtitle == 2}"> selected</c:if>>高层管理人员</option>
							</select>
						</div>
						<div class="col-sm-6">
							<span><fmt:message key="user.add.jobtitle.span"
									bundle="${i18n_auth}" /></span>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">邮箱</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" name="email"
								value="${user.email}" />
						</div>
						<div class="col-sm-6">
							<span><fmt:message key="user.add.email.span"
									bundle="${i18n_auth}" /></span>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label"><fmt:message
								key="user.add.mobile" bundle="${i18n_auth}" /><em>*</em></label>
						<div class="col-sm-4">
							<input type="text" class="form-control" name="mobile"
								value="${user.mobile}" />
						</div>
						<div class="col-sm-6">
							<span><fmt:message key="user.add.mobile.span"
									bundle="${i18n_auth}" /></span>
						</div>
					</div>

					<div class="form-group">
						<div class="col-sm-offset-5 col-sm-10">
							<input type="submit" class="btn btn-primary"></input>
							<a href="${base}/signout" class="btn btn-default">取消</a>
						</div>
					</div>
				</fieldset>
			</form>
		</div>
	</div>

	<div id="footer">
		<div>
			<p>
				版权所有@2015 <a href="#" target="_blank"></a>
			</p>
		</div>
	</div>

	<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${base}/js/bootstrap.min.js"></script>

	<script type="text/javascript" src="${base}/js/bootstrap-modal.js"></script>

	<script type="text/javascript" src="${base }/js/cupid/core.js"></script>
	<!--验证脚本 -->
	<script type="text/javascript"
		src="${base }/js/ui/validate/jquery.validate.js"></script>
	<script type="text/javascript"
		src="${base }/js/ui/validate/messages_cn.js"></script>
	<script type="text/javascript"
		src="${base }/js/ui/validate/jquery.validate.ext.methods.js"></script>
	<script type="text/javascript" src="${base}/js/huijia/index.js"></script>

	<script type="text/javascript">
		var ValidateHandler = {
			init : function() {
				$("[name=name]").rules("add", {
					remote : {
						type : "POST",
						async : false,
						url : "${base}/user/isValidName",
						dataType : "json",
						data : {
							operation : "${param.operation}",
							name : function() {
								return $('#name').val()
							},
							userId : $("[name=userId]").val()
						}
					},
					messages : {
						remote : $.message("auth", "user.add.rename")
					}
				});
				$("[name=repassword]").rules(
						"add",
						{
							required : true,
							equalTo : "#password",
							messages : {
								required : $.message("auth",
										"user.add.repassword.span"),
								equalTo : $.message("auth",
										"user.add.repassword.span.pass")
							}
						});
			}
		}

		$(document).ready(function() {

			$('[name="chk-navigator"]').click(function() {
				var authedNavs = "";
				$('[name=chk-navigator]:checked').each(function() {
					authedNavs += ($(this).val() + ";");
				});
				$('[name=authedNavs]').val(authedNavs);
			});

			<c:forEach items="${authedNavIds}" var="navId">
			$('#chk_${navId}').attr('checked', true);
			</c:forEach>

			ValidateHandler.init();
		});
	</script>
</body>
</html>
