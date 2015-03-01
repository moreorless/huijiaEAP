<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><fmt:message key="index.product.name" bundle="${i18n_main}"/></title>
<link type="text/css" rel="stylesheet"
	href="${base}/css/bootstrap.min.css" />
<link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
<link type="text/css" rel="stylesheet" href="${base}/css/huijia.css" />
<link type="text/css" rel="stylesheet" href="${base }/css/ui/validate/jquery.validate.css" />
<style type="text/css">
body {
	overflow-y: auto;	background-color: #f3f3f3;
}
#container {width:1000px;}
</style>
</head>
<body>
	<c:import url="/includes/header_huijia.jsp">
		<c:param name="hideBtn" value="true"></c:param>
	</c:import>
	
	<c:import url="/error/inline.jsp"></c:import>
	
	<div class="container">
	<div class="row" style="clear:both;padding-top:40px;">
		<form action="${base}/user/register" class="form-horizontal" role="form" method="post" id="register-form">
				<input type="hidden" name="userId" value="${user.userId }" /> 
				<input type="hidden" name="name" value="${user.name }" /> 
				<input type="hidden" name="type" value="${user.type }" /> 
				<input type="hidden" name="companyId" value="${user.companyId }" /> 
				<input type="hidden" name="segmentId" value="${user.segmentId }" /> 
				<input type="hidden" name="code" value="${user.code }" />

				<div class="form-group">
					<label class="col-sm-2 control-label">真实姓名<em>*</em></label>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="realname" value="${user.realname}" />
					</div>
					<div class="col-sm-6">
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
					<div class="col-sm-10">
						<label class="radio-inline">
							<input type="radio" value="1" name="gender"  <c:if test="${user.gender == 1}">checked</c:if>/>男
						</label>
						<label class="radio-inline">
							<input type="radio" value="0" name="gender" <c:if test="${user.gender == 0}">checked</c:if>/>女
						</label>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">年龄<em>*</em></label>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="age" value="${user.age != 0 ? user.age : ''}"/>
					</div>
					<div class="col-sm-6">
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">工作年限<em>*</em></label>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="workage" value="${user.workage != 0 ? user.workage : ''}"/>
					</div>
					<div class="col-sm-6">
					</div>
				</div>




				<div class="form-group">
					<label class="col-sm-2 control-label">教育程度<em>*</em></label>
					<div class="col-sm-10">
						<label class="radio-inline">
							<input type="radio" value="0" name="education"  <c:if test="${user.education == 0}">checked</c:if>/>大专及以下
						</label>
						<label class="radio-inline">
							<input type="radio" value="1" name="education"  <c:if test="${user.education == 1}">checked</c:if>/>本科
						</label>
						<label class="radio-inline">
							<input type="radio" value="2" name="education"  <c:if test="${user.education == 2}">checked</c:if>/>硕士及以上
						</label>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">职位<em>*</em></label>
					<div class="col-sm-10">
						<label class="radio-inline">
							<input type="radio" value="0" name="jobtitle"  <c:if test="${user.jobtitle == 0}">checked</c:if>/>普通员工
						</label>
						<label class="radio-inline">
							<input type="radio" value="1" name="jobtitle"  <c:if test="${user.jobtitle == 1}">checked</c:if>/>中层管理人员
						</label>
						<label class="radio-inline">
							<input type="radio" value="2" name="jobtitle"  <c:if test="${user.jobtitle == 2}">checked</c:if>/>高层管理人员
						</label>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">邮箱</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="email" value="${user.email}"/>
					</div>
					<div class="col-sm-6">
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label"><fmt:message
							key="user.add.mobile" bundle="${i18n_auth}" /><em>*</em></label>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="mobile" id="mobile" value="${user.mobile}"/>
					</div>
					<div class="col-sm-6" id="mobile-help">
					</div>
				</div>
				 
				<div class="form-group">
					<label class="col-sm-2 control-label"><fmt:message
							key="user.add.validatecode" bundle="${i18n_auth}" /><em>*</em></label>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="validateCode" id="validateCode" disabled/>
					</div>
					<div class="col-sm-6">
						 <button type="button" class="btn btn-primary" id="btn-validatecode">获取校验码</button>
					</div>
					<div class="col-sm-offset-2 col-sm-10">
					<p class="help-block" id="validate-code-help" style="display: none">校验码发送成功，60秒内未收到请重新获取。</p>
					</div>
				</div>

				<div class="form-group">
					<div class="col-sm-offset-5 col-sm-10">
						<button type="button" class="btn btn-primary" id="btn-submit">提交</button>  
						<a href="${base}/signout" class="btn btn-default">取消</a>
					</div>
				</div>
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
	var ValidateHandler = {
			init : function() {
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
				$("[name=validateCode]").rules("add", {
							remote : {
								type : "POST",
								url : "${base}/user/isValidVCode",
								dataType : "json",
								data : {
									mobile : function(){ return $('input[name=mobile]').val()},
									validCode : function(){ return $('#validateCode').val()}
								}
							},
							messages : {
								remote : $.message("auth", "auth.signin.errors.validatecode")
							}
						});
			}
		};

	var ValidateCodeHandler = {
		wait : 60,
		timer : function(){

			if(ValidateCodeHandler.wait == 0){
				$('#btn-validatecode').text('获取校验码');
				$('#btn-validatecode').prop("disabled", false);
				ValidateCodeHandler.wait = 60;
			}else {
				$('#btn-validatecode').prop("disabled", true);
				$('#btn-validatecode').text(ValidateCodeHandler.wait + '秒后重新获取');
				console.log(new Date() + ' -- ' + ValidateCodeHandler.wait)
				ValidateCodeHandler.wait--;
				setTimeout(ValidateCodeHandler.timer, 1000);
			}
		},
		getValidateCode : function(){
			if(!$("#register-form").valid()) return;

			$.ajax({
				url : "${base}/user/sendSMS",
				type : 'post',
				dataType : "json",
				data : {
					mobile : $('input[name=mobile]').val()
				},
				success : function(result){
					if(result == -1){  // 手机号已使用
						$('#mobile-help').text('手机号已被使用').addClass('alert-danger');
						return;
					}else if(result == -2){  // 生成校验码出错
						return;
					}
					$('#mobile-help').text('').removeClass('alert-danger');
					ValidateCodeHandler.timer();
				},
				error : function(){
					$('#validate-code-help').text('获取校验码失败!请稍后重试');
				}
			});
			$('#validate-code-help').show();
		}
	};
		
	$(document).ready(function() {
		$('#btn-submit').click(function(){
			$('#validate-code-help').hide();
			
			$('#register-form').submit();
		});
		
		$('#btn-validatecode').click(function(){
			ValidateCodeHandler.getValidateCode();
			$('#validateCode').prop("disabled", false);
		});
		ValidateHandler.init();
	});
	</script>
</body>
</html>
