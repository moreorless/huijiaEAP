<%@ page contentType="text/html;charset=utf-8" language="java" %>
<div id="footer">
  <div>
    <p>版权所有@2015 <a href="#" target="_blank"></a></p>
  </div>
</div>

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
		<form class="form-horizontal" id="userForm" action="${base}/user/editAjax" method="post" role="form">
			<input type="hidden" name="userId" value="${current_user.userId }"/>
			<input type="hidden" name="name" value="${current_user.name }"/>
			<input type="hidden" name="type" value="${current_user.type }"/>
			
			<div class="form-group">
			    <label class="control-label col-sm-2">真实姓名</label>
			    <div class="col-sm-4">
			      <input type="text" name="realname" class="form-control" value="${current_user.realname}"  />
			    </div>
			    <div class="col-sm-6">
			      <span><fmt:message key="user.add.realname.span" bundle="${i18n_auth}"/></span>
			    </div>
			  </div>
			  <div class="form-group">
			  <label class="control-label col-sm-2">密码</label>
			  <div class="col-sm-4">
			    <input type="password" name="password" class="form-control" value="${current_user.password}" />
			  </div>
			  <div class="col-sm-6">
			    <span><fmt:message key="user.add.password.span" bundle="${i18n_auth}"/></span>
			  </div>
			</div>
			
			<div class="form-group">
			  <label class="control-label col-sm-2">邮箱</label>
			  <div class="col-sm-4">
			    <input type="text" name="email" class="form-control" value="${current_user.email}" />
			  </div>
			  <div class="col-sm-6">
			    <span><fmt:message key="user.add.email.span" bundle="${i18n_auth}"/></span>
			  </div>
			</div>
			
			<div class="form-group">
			  <label class="control-label col-sm-2"><fmt:message key="user.add.mobile" bundle="${i18n_auth}"/></label>
			  <div class="col-sm-4">
			    <input type="text" name="mobile" class="form-control" value="${current_user.mobile}" />
			  </div>
			  <div class="col-sm-6">
			    <span><fmt:message key="user.add.mobile.span" bundle="${i18n_auth}"/></span>
			  </div>
			</div>
			<div class="form-group">
			  <div class="col-sm-offset-2 col-sm-10">
			    <a href="javascript://" class="btn btn-primary" id="btn-save">保存</a>
			  </div>
			</div>
		</form>
		</div>
  	</div>
  	</div>
  	</div>
</div>
