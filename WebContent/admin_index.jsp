<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<cupid:ifNotLogin>
  <c:redirect url="/login.jsp" />
</cupid:ifNotLogin>

<c:if test="${current_user.type == 2}">
	<c:redirect url="/quiz/list" />
</c:if>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title><fmt:message key="index.product.name" bundle="${i18n_main}"/></title>
    <link type="text/css" rel="stylesheet" href="${base}/css/bootstrap.min.css" />
    <link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
    <link type="text/css" rel="stylesheet" href="${base}/css/index.css"/>
    <link type="text/css" rel="stylesheet" href="${base }/css/ui/validate/jquery.validate.css" />
    
  </head>
  <body>
    <c:import url="/includes/header.jsp"></c:import>
    <div id="wrap" class="container auto-spread">
    	<iframe id="main-iframe" src="about:blank" frameBorder="0" border="0" width="100%" height="100%"></iframe>
    </div>
	<%@ include file="/includes/footer.jsp" %>

<div id="user-dialog" class="modal fade bs-example-modal-lg" role="dialog">
	<div class="modal-dialog modal-lg">
	 <div class="modal-content">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    	<h3>个人信息</h3>
	</div>
	<div class="modal-body">
		<div class="alert alert-success" id="save-message" style="display:none">
		  <button type="button" class="close" data-dismiss="alert">&times;</button>
		  <strong></strong>
		</div>
		<div>
		<c:import url="/error/inline.jsp" />
		<form class="form-horizontal" id="userForm" action="${base}/user/editAjax" method="post">
			<input type="hidden" name="userId" value="${current_user.userId }"/>
			<input type="hidden" name="name" value="${current_user.name }"/>
			<input type="hidden" name="type" value="${current_user.type }"/>
			
			<div class="control-group">
			    <label class="control-label">真实姓名</label>
			    <div class="controls">
			      <input type="text" name="realname" value="${current_user.realname}"  />
			      <span><fmt:message key="user.add.realname.span" bundle="${i18n_auth}"/></span>
			    </div>
			  </div>
			  <div class="control-group">
			  <label class="control-label">密码</label>
			  <div class="controls">
			    <input type="password" name="password" value="${current_user.password}" />
			    <span><fmt:message key="user.add.password.span" bundle="${i18n_auth}"/></span>
			  </div>
			</div>
			
			<div class="control-group">
			  <label class="control-label">邮箱</label>
			  <div class="controls">
			    <input type="text" name="email" value="${current_user.email}" />
			    <span><fmt:message key="user.add.email.span" bundle="${i18n_auth}"/></span>
			  </div>
			</div>
			
			<div class="control-group">
			  <label class="control-label"><fmt:message key="user.add.mobile" bundle="${i18n_auth}"/></label>
			  <div class="controls">
			    <input type="text" name="mobile" value="${current_user.mobile}" />
			    <span><fmt:message key="user.add.mobile.span" bundle="${i18n_auth}"/></span>
			  </div>
			</div>
			<div class="control-group">
			  <label class="control-label"></label>
			  <div class="controls">
			    <a href="javascript://" class="btn btn-primary" id="btn-save">保存</a>
			  </div>
			</div>
		</form>
		</div>
  	</div>
  	</div>
  	</div>
</div>


    <script type="text/javascript" src="${base}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${base}/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${base}/js/bootstrap-modalmanager.js"></script>
	<script type="text/javascript" src="${base}/js/bootstrap-modal.js"></script>
    
    <script type="text/javascript" src="${base }/js/cupid/core.js"></script>
    <script type="text/javascript" src="${base}/js/cupid/layout.js"></script>
	<script type="text/javascript" src="${base}/js/cupid/plugins/layout.autospread.js"></script>
	<script type="text/javascript" src="${base}/js/index.js"></script>
	<!--验证脚本 -->
	<script type="text/javascript" src="${base }/js/ui/validate/jquery.validate.js" ></script>
	<script type="text/javascript" src="${base }/js/ui/validate/messages_cn.js" ></script>
	<script type="text/javascript" src="${base }/js/ui/validate/jquery.validate.ext.methods.js" ></script>
	
	<script type="text/javascript">
		var UserDialog = {
			show : function(){
				$("#user-dialog").modal('show');	
			}
		}
		$(document).ready(function(){
			$("#nav-items li").click(function(){
				$("#nav-items").find('li').removeClass('active');
				$(this).addClass('active');
				$("#main-iframe").attr('src', '${base}' + $(this).attr('data'));
			});
			
			$("#nav-items li").first().click();
			
			$('#btn-save').click(function(){
				if(!$('#userForm').valid()) return;
				$.ajax({
					url : '${base}/user/editAjax',
					dataType : 'json',
					type : 'post',
					data : $('#userForm').serialize(),
					success : function(){
						$('#save-message').show();
						$('#save-message strong').text('保存成功');
						
						setTimeout(function(){$('#save-message').hide();}, 5000);
					}
				});
			});
			
			$('#btn-user').click(function(){
				UserDialog.show();
			});
		});
	</script>
  </body>
</html>
