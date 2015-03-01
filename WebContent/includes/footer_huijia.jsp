<%@ page contentType="text/html;charset=utf-8" language="java"%>
<div id="footer">
	<div>
		<p>
			版权所有@2015 <a href="#" target="_blank"></a>
		</p>
	</div>
</div>

<div id="user-dialog" class="modal fade bs-example-modal-lg"
	role="dialog">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h3>个人信息</h3>
			</div>
			<div class="modal-body">
				<div class="alert alert-success" id="save-message"
					style="display: none">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
					<strong></strong>
				</div>
				<div>
					<c:import url="/error/inline.jsp">
						<c:param name="errorPlacerId">error-for-userdialog</c:param>
					</c:import>
					<form class="form-horizontal" id="userForm" role="form">
						<input type="hidden" name="userId" value="${current_user.userId }" /> 
						<input type="hidden" name="name" value="${current_user.name }" /> 
						<input type="hidden" name="type" value="${current_user.type }" /> 
						<input type="hidden" name="companyId" value="${current_user.companyId }" /> 
						<input type="hidden" name="segmentId" value="${current_user.segmentId }" /> 
						<input type="hidden" name="code" value="${current_user.code }" />
		
						<div class="form-group">
							<label class="col-sm-2 control-label">真实姓名<em>*</em></label>
							<div class="col-sm-4">
								<input type="text" class="form-control" name="realname" value="${current_user.realname}" />
							</div>
							<div class="col-sm-6">
							</div>
						</div>
		
						<div class="form-group">
							<label class="col-sm-2 control-label">密码<em>*</em></label>
							<div class="col-sm-4">
								<input type="password" class="form-control" name="password" id="password" value="#PassWord#" />
							</div>
							<div class="col-sm-6">
								<span><fmt:message key="user.add.password.span" bundle="${i18n_auth}" /></span>
							</div>
						</div>
		
						<div class="form-group">
							<label class="col-sm-2 control-label"><fmt:message key="user.add.repassword" bundle="${i18n_auth}" /><em>*</em></label>
							<div class="col-sm-4">
								<input type="password" class="form-control" name="repassword" id="repassword" value="#PassWord#" />
							</div>
							<div class="col-sm-6">
								<span><fmt:message key="user.add.repassword.span" bundle="${i18n_auth}" /></span>
							</div>
						</div>
		
						<div class="form-group">
							<label class="col-sm-2 control-label">性别<em>*</em></label>
							<div class="col-sm-10">
								<label class="radio-inline">
									<input type="radio" value="1" name="gender"  <c:if test="${current_user.gender == 1}">checked</c:if>/>男
								</label>
								<label class="radio-inline">
									<input type="radio" value="0" name="gender" <c:if test="${current_user.gender == 0}">checked</c:if>/>女
								</label>
							</div>
						</div>
		
						<div class="form-group">
							<label class="col-sm-2 control-label">年龄<em>*</em></label>
							<div class="col-sm-4">
								<input type="text" class="form-control" name="age" <c:if test='${current_user.age > 0}'>value="${current_user.age}"</c:if> />
							</div>
							<div class="col-sm-6">
							</div>
						</div>
		
						<div class="form-group">
							<label class="col-sm-2 control-label">工作年限<em>*</em></label>
							<div class="col-sm-4">
								<input type="text" class="form-control" name="workage" <c:if test='${current_user.workage > 0}'>value="${current_user.workage}"</c:if> />
							</div>
							<div class="col-sm-6">
							</div>
						</div>
		
		
		
		
						<div class="form-group">
							<label class="col-sm-2 control-label">教育程度<em>*</em></label>
							<div class="col-sm-10">
								<label class="radio-inline">
									<input type="radio" value="0" name="education"  <c:if test="${current_user.education == 0}">checked</c:if>/>大专及以下
								</label>
								<label class="radio-inline">
									<input type="radio" value="1" name="education"  <c:if test="${current_user.education == 1}">checked</c:if>/>本科
								</label>
								<label class="radio-inline">
									<input type="radio" value="2" name="education"  <c:if test="${current_user.education == 2}">checked</c:if>/>硕士及以上
								</label>
							</div>
						</div>
		
						<div class="form-group">
							<label class="col-sm-2 control-label">职位<em>*</em></label>
							<div class="col-sm-10">
								<label class="radio-inline">
									<input type="radio" value="0" name="jobtitle"  <c:if test="${current_user.jobtitle == 0}">checked</c:if>/>普通员工
								</label>
								<label class="radio-inline">
									<input type="radio" value="1" name="jobtitle"  <c:if test="${current_user.jobtitle == 1}">checked</c:if>/>中层管理人员
								</label>
								<label class="radio-inline">
									<input type="radio" value="2" name="jobtitle"  <c:if test="${current_user.jobtitle == 2}">checked</c:if>/>高层管理人员
								</label>
							</div>
						</div>
		
						<div class="form-group">
							<label class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-4">
								<input type="text" class="form-control" name="email" value="${current_user.email}"/>
							</div>
							<div class="col-sm-6">
							</div>
						</div>
		
						<div class="form-group">
							<label class="col-sm-2 control-label"><fmt:message
									key="user.add.mobile" bundle="${i18n_auth}" /><em>*</em></label>
							<div class="col-sm-4">
								<input type="text" class="form-control" name="mobile" id="mobile" value="${current_user.mobile}"/>
							</div>
							<div class="col-sm-6" id="mobile-help">
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-10">
								<a class="btn btn-primary" id="btn-save-user">保存</a>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>
