<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>编辑用户</title>
    <link type="text/css" rel="stylesheet" href="${base}/css/bootstrap.min.css" />
    <link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
    <link type="text/css" rel="stylesheet" href="${base }/css/ui/validate/jquery.validate.css" />
    
    <style type="text/css">
    	#wrap{
    		height:100%;
    		padding:10px;
    		overflow-x:hidden;
    		overflow-y:auto;
    	}
    	
    	em{color:red}
    </style>
  </head>
  <body>
  
  <div id="wrap" class="container">
  	<%@ include file="/error/inline.jsp" %>
  	<form action="${base }/user/${param.operation}" class="form-horizontal" role="form" method="post">
  		<input type="hidden" name="userId" value="${user.userId }"/>
  		<fieldset>
  			<legend>
  				<c:if test="${param.operation == 'add' }">添加用户</c:if>
  				<c:if test="${param.operation == 'edit' }">编辑用户</c:if>
  			</legend>
  			
  			<div class="form-group">
			  <label class="col-sm-2 control-label">用户名<em>*</em></label>
			  <div class="col-sm-4">
			    <input type="text" class="form-control" name="name" id="name" value="${user.name}" />
			  </div>
			  <div class="col-sm-6">
			    <span class="help-inline"><fmt:message key="user.add.name.span" bundle="${i18n_auth}"/></span>
		      </div>
			</div>
			
			<div class="form-group">
			  <label class="col-sm-2 control-label">真实姓名<em>*</em></label>
			  <div class="col-sm-4">
			    <input type="text" class="form-control" name="realname" value="${user.realname}" />
			  </div>
			  <div class="col-sm-6">
			    <span><fmt:message key="user.add.realname.span" bundle="${i18n_auth}"/></span>
			  </div>
			</div>
			
			<c:if test="${user.type > 0 }">
			<div class="form-group">
			  <label class="col-sm-2 control-label">所属企业<em>*</em></label>
			  <div class="col-sm-4">
			    <select class="form-control" name="companyid" id="sel-company">
			    	<c:forEach items="${companyList}" var="company">
			    		<option value="${company.id}"<c:if test="${user.companyid == company.id}"> selected</c:if>>${company.name}</option>
			    	</c:forEach>
			    </select>
			  </div>
			  <div class="col-sm-6">
			    <span><fmt:message key="user.add.company.span" bundle="${i18n_auth}"/></span>
			  </div>
			</div>
			</c:if>
			
			<div class="form-group">
			  <label class="col-sm-2 control-label">密码<em>*</em></label>
			  <div class="col-sm-4">
			    <input type="password" class="form-control" name="password" id="password" value="${user.password}" />
			  </div>
			  <div class="col-sm-6">
			    <span><fmt:message key="user.add.password.span" bundle="${i18n_auth}"/></span>
			  </div>
			</div>
			
			
			<div class="form-group">
			  <label class="col-sm-2 control-label"><fmt:message key="user.add.repassword" bundle="${i18n_auth}"/><em>*</em></label>
			  <div class="col-sm-4">
			    <input type="password" class="form-control" name="repassword" id="repassword" value="${not empty user ? '#PassWord#' : user.password }"/>
			  </div>
			  <div class="col-sm-6">
			    <span><fmt:message key="user.add.repassword.span" bundle="${i18n_auth}"/></span>
		      </div>
			</div>
	   				
			
			<div class="form-group">
			  <label class="col-sm-2 control-label">邮箱</label>
			  <div class="col-sm-4">
			    <input type="text" class="form-control" name="email" value="${user.email}" />
			  </div>
			  <div class="col-sm-6">
			    <span><fmt:message key="user.add.email.span" bundle="${i18n_auth}"/></span>
			  </div>
			</div>
			
			<div class="form-group">
			  <label class="col-sm-2 control-label"><fmt:message key="user.add.mobile" bundle="${i18n_auth}"/></label>
			  <div class="col-sm-4">
			    <input type="text" class="form-control" name="mobile" value="${user.mobile}" />
			  </div>
			  <div class="col-sm-6">
			    <span><fmt:message key="user.add.mobile.span" bundle="${i18n_auth}"/></span>
			  </div>
			</div>
			
			<%-- 
			<div class="form-group">
			  <label class="col-sm-2 control-label">授权模块</label>
			  <div class="col-sm-10">
			  	<input type="hidden" name="authedNavs" value="${user.authedNavs }" />
			    <c:forEach items="${navigators }" var="navObj">
			    	<input type="checkbox" name="chk-navigator" id="chk_${navObj.id}" value="${navObj.id}" />${navObj.name}
			    	&nbsp;&nbsp;&nbsp;&nbsp;
			    </c:forEach>
			  </div>
			</div>
			 --%>
			 
			<div class="form-group">
			  <div class="col-sm-offset-2 col-sm-10">
			    <button class="btn btn-primary">保存</button>
			    <a href="${base}/user/list" class="btn btn-default">取消</a>
			  </div>
			</div>
  		</fieldset>
  	</form>
  </div>
   <script type="text/javascript" src="${base}/js/jquery.min.js"></script>
   
   	<script type="text/javascript" src="${base}/js/cupid/core.js"></script>
	<!--验证脚本 -->
<script type="text/javascript" src="${base }/js/ui/validate/jquery.validate.js" ></script>
<script type="text/javascript" src="${base }/js/ui/validate/messages_cn.js" ></script>
<script type="text/javascript" src="${base }/js/ui/validate/jquery.validate.ext.methods.js" ></script>
   
  <script type="text/javascript">
  
  var ValidateHandler = {
			init : function () {
				$("[name=name]").rules("add", {
					remote:{
						type: "POST",
						async: false,
						url: "${base}/user/isValidName",
						dataType:"json",
						data : {
							operation : "${param.operation}",
							name : function(){return $('#name').val()},
							userId : $("[name=userId]").val()
						}
					},
					messages: {
						remote: $.message("auth","user.add.rename")
					}
				});
				$("[name=repassword]").rules("add", {
					required:true,
					equalTo:"#password",
					messages: {
						required:$.message("auth","user.add.repassword.span"),
						equalTo:$.message("auth","user.add.repassword.span.pass")
					}
				});
			}}
  
  $(document).ready(function(){
	  
	  $('[name="chk-navigator"]').click(function(){
		  var authedNavs = "";
		  $('[name=chk-navigator]:checked').each(function(){
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