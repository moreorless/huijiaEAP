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
					<c:import url="/error/inline.jsp" />
					<form class="form-horizontal" id="userForm"
						action="${base}/user/editAjax" method="post" role="form">
						<input type="hidden" name="userId" value="${current_user.userId }" />
						<input type="hidden" name="name" value="${current_user.name }" />
						<input type="hidden" name="type" value="${current_user.type }" />
						<input type="hidden" name="companyId" value="${current_user.companyId }" />
						<input type="hidden" name="segmentId" value="${current_user.segmentId }" />
						<input type="hidden" name="code" value="${current_user.code }" />

						<div class="form-group">
							<label class="control-label col-sm-2">真实姓名</label>
							<div class="col-sm-4">
								<input type="text" name="realname" class="form-control"
									value="${current_user.realname}" />
							</div>
							<div class="col-sm-6">
								<span><fmt:message key="user.add.realname.span"
										bundle="${i18n_auth}" /></span>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-2">密码</label>
							<div class="col-sm-4">
								<input type="password" name="password" class="form-control"
									value="${current_user.password}" />
							</div>
							<div class="col-sm-6">
								<span><fmt:message key="user.add.password.span"
										bundle="${i18n_auth}" /></span>
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-sm-2">邮箱</label>
							<div class="col-sm-4">
								<input type="text" name="email" class="form-control"
									value="${current_user.email}" />
							</div>
							<div class="col-sm-6">
								<span><fmt:message key="user.add.email.span"
										bundle="${i18n_auth}" /></span>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label">性别</label>
							<div class="col-sm-4">
								<select class="form-control" name="gender" id="sel-gender">
									<option value="0"
										<c:if test="${current_user.gender == 0}"> selected</c:if>>女</option>
									<option value="1"
										<c:if test="${current_user.gender == 1}"> selected</c:if>>男</option>
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
									value="${current_user.age}" />
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
									value="${current_user.workage}" />
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
									<option value="0"<c:if test="${current_user.education == 0}"> selected</c:if>>大专及以下</option>
									<option value="1"<c:if test="${current_user.education == 1}"> selected</c:if>>本科</option>
									<option value="2"<c:if test="${current_user.education == 2}"> selected</c:if>>硕士及以上</option>
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
										<c:if test="${current_user.jobtitle == 0}"> selected</c:if>>普通员工</option>
									<option value="1"
										<c:if test="${current_user.jobtitle == 1}"> selected</c:if>>中层管理人员</option>
									<option value="2"
										<c:if test="${current_user.jobtitle == 2}"> selected</c:if>>高层管理人员</option>
								</select>
							</div>
							<div class="col-sm-6">
								<span><fmt:message key="user.add.jobtitle.span"
										bundle="${i18n_auth}" /></span>
							</div>
						</div>

						<div class="form-group">
							<label class="control-label col-sm-2"><fmt:message
									key="user.add.mobile" bundle="${i18n_auth}" /></label>
							<div class="col-sm-4">
								<input type="text" name="mobile" class="form-control"
									value="${current_user.mobile}" readonly="readonly"/>
							</div>
							<div class="col-sm-6">
								<span><fmt:message key="user.add.mobile.span"
										bundle="${i18n_auth}" /></span>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-10">
								<button class="btn btn-primary">保存</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>
