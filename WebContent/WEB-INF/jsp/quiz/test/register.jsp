<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><fmt:message key="index.product.name" bundle="${i18n_main}"/></title>
<link type="text/css" rel="stylesheet"
	href="${base}/css/bootstrap.min.css" />
<link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
<link type="text/css" rel="stylesheet" href="${base}/css/quiz/quiz.css" />
<link type="text/css" rel="stylesheet" href="${base}/css/huijia.css" />
<link type="text/css" rel="stylesheet"
	href="${base }/css/ui/validate/jquery.validate.css" />
<style type="text/css">
body {
	overflow: auto;	background-color: #f3f3f3
}
</style>
</head>
<body>
	<c:import url="/includes/header_huijia.jsp">
		<c:param name="hideBtn" value="true"></c:param>
	</c:import>
	<c:import url="/error/inline.jsp"></c:import>
	<div class="container quiz_wrapper">
		<div class="quiz_inner_wrapper">
			<form action="${base}/user/register" class="form-horizontal" role="form" method="post" id="register-form">
				<input type="hidden" name="userId" value="${user.userId }" /> 
				<input type="hidden" name="name" value="${user.name }" /> 
				<input type="hidden" name="type" value="${user.type }" /> 
				<input type="hidden" name="companyId" value="${user.companyId }" /> 
				<input type="hidden" name="segmentId" value="${user.segmentId }" /> 
				<input type="hidden" name="code" value="${user.code }" />

				<fieldset>
					<div class="form-group">
						<label class="col-sm-2 control-label">真实姓名<em>*</em></label>
						<div class="col-sm-4">
							<input type="text" class="form-control" name="realname" value="${user.realname}" />
						</div>
						<div class="col-sm-6">
							<span><fmt:message key="user.add.realname.span" bundle="${i18n_auth}" /></span>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">密码<em>*</em></label>
						<div class="col-sm-4">
							<input type="password" class="form-control" name="password" id="password" value="" />
						</div>
						<div class="col-sm-6">
							<span><fmt:message key="user.add.password.span" bundle="${i18n_auth}" /></span>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label"><fmt:message key="user.add.repassword" bundle="${i18n_auth}" /><em>*</em></label>
						<div class="col-sm-4">
							<input type="password" class="form-control" name="repassword" id="repassword" value="" />
						</div>
						<div class="col-sm-6">
							<span><fmt:message key="user.add.repassword.span" bundle="${i18n_auth}" /></span>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">性别<em>*</em></label>
						<div class="col-sm-4">
							<select class="form-control" name="gender" id="sel-gender">
								<option value="1">男</option>
								<option value="0">女</option>
							</select>
						</div>
						<div class="col-sm-6">
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">年龄<em>*</em></label>
						<div class="col-sm-4">
							<input type="text" class="form-control" name="age" />
						</div>
						<div class="col-sm-6">
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">工作年限<em>*</em></label>
						<div class="col-sm-4">
							<input type="text" class="form-control" name="workage" />
						</div>
						<div class="col-sm-6">
						</div>
					</div>




					<div class="form-group">
						<label class="col-sm-2 control-label">教育程度<em>*</em></label>
						<div class="col-sm-4">
							<select class="form-control" name="education" id="sel-education">
								<option value="0">大专及以下</option>
								<option value="1" selected>本科</option>
								<option value="2">硕士及以上</option>
							</select>
						</div>
						<div class="col-sm-6"></div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">职位<em>*</em></label>
						<div class="col-sm-4">
							<select class="form-control" name="jobtitle" id="sel-jobtitle">
								<option value="0" selected>普通员工</option>
								<option value="1">中层管理人员</option>
								<option value="2">高层管理人员</option>
							</select>
						</div>
						<div class="col-sm-6">
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">邮箱</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" name="email" />
						</div>
						<div class="col-sm-6">
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label"><fmt:message
								key="user.add.mobile" bundle="${i18n_auth}" /><em>*</em></label>
						<div class="col-sm-4">
							<input type="text" class="form-control" name="mobile" />
						</div>
						<div class="col-sm-6">
						</div>
					</div>
					 
					<div class="form-group">
						<label class="col-sm-2 control-label"><fmt:message
								key="user.add.validatecode" bundle="${i18n_auth}" /><em>*</em></label>
						<div class="col-sm-2">
							<input type="text" class="form-control" name="validateCode" />
						</div>
						<div class="col-sm-6">
							 <button type="button" class="btn btn-primary" disabled="disabled">获取校验码</button>  
						</div>
					</div>

					<div class="form-group">
						<div class="col-sm-offset-5 col-sm-10">
							<button type="button" class="btn btn-primary" id="btn-submit">提交</button>  
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

	<script type="text/javascript" src="${base }/js/cupid/core.js"></script>
	<!--验证脚本 -->
	<script type="text/javascript" src="${base }/js/ui/validate/jquery.validate.js"></script>
	<script type="text/javascript" src="${base }/js/ui/validate/messages_cn.js"></script>
	<script type="text/javascript" src="${base }/js/ui/validate/jquery.validate.ext.methods.js"></script>

	<script type="text/javascript">
		function validateMobileCode() {
			var mobile = $('input[name=mobile]').val();
			var userCode = $('input[name=code]').val();
			//var obj = false;
			$.ajax({
				async : false, //使用同步请求，因为异步请求不能将返回值传给全局变量；这里很重要  
				url : "${base}/user/sendSMS",
				type : "post",
				dataType : "json",
				data : "mobile=" + mobile,
				//data: "{mobile:'" + mobile + "',userCode:'" + userCode + "'}",
				success : function(data) {
					alert("ok:"+data);  
					//obj = data;
				}
			});
		}
	</script>

	<script type="text/javascript">
		
	$(document).ready(function() {
		$('#btn-submit').click(function(){
			$('#register-form').submit();
		});

	});
	</script>
</body>
</html>
