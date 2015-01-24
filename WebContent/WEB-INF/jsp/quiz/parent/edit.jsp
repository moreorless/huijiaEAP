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
	href="${base }/css/ui/validate/jquery.validate.css" />

<style type="text/css">
#wrap {
	padding: 10px;
}

em {
	color: red
}

.icon-selector {
	padding: 5px;
	border: dashed 1px #ccc;
}

.icon-selector a {
	width: 50px;
	height: 50px;
	display: inline-block;
	line-height: 50px;
	text-align: center;
	padding-top: 2px;
}

.icon-selector a.active {
	border: 2px solid #428bca;
}
</style>
</head>
<body>
	<div class="container" id="wrap">
		<%@ include file="/error/inline.jsp"%>
		<form class="form-horizontal" role="form"
			action="${base}/quiz/${param.operation}" method="post"
			enctype="multipart/form-data">
			<input type="hidden" name="id" value="${quiz.id}" />
			<fieldset>
				<legend>
					<c:if test="${param.operation == 'add' }">添加试卷</c:if>
					<c:if test="${param.operation == 'edit' }">编辑试卷</c:if>
				</legend>

				<div class="form-group">
					<label class="col-sm-2 control-label">名称<em>*</em></label>
					<div class="col-sm-8">
						<input type="text" class="form-control" name="name" id="name"
							value="${quiz.name}" />
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">描述</label>
					<div class="col-sm-10">
						<textarea class="form-control" rows="3" name="description"
							id="description">${quiz.description}</textarea>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">指导语</label>
					<div class="col-sm-10">
						<textarea class="form-control" rows="3" name="description"
							id="description">${quiz.guideline}</textarea>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">图标</label> <input
						type="hidden" name="icon" id="quiz_icon" value="default.png" />
					<div class="col-sm-2">
						<img id="quiz_icon_repl"
							src="${base}/images/quiz/icons/default.png" alt="" />
						&nbsp;&nbsp;<a href="javascript://" id="btn-icon-selector">选择</a>
					</div>
					<div id="icon-selector"
						class="col-sm-offset-2 col-sm-10 icon-selector"
						style="display: none">
						<c:forEach items="${iconNames}" var="iconName">
							<a href="javascript://" data="${iconName}"> <img
								src="${base}/images/quiz/icons/${iconName}" alt="${iconName}"
								width="36" height="36" />
							</a>
						</c:forEach>
					</div>
				</div>

				<!-- 当组合问卷进行编辑时，不显示试题上传 -->
				<div class="form-group" id="upload_div">
					<label class="col-sm-2 control-label">试题文件</label>
					<div class="col-sm-10">
						<input type="file" class="form-control" name="quiz_file"
							id="quiz_file" />
						<p class="help-block">上传试题文件</p>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<button class="btn btn-primary">保存</button>
						<a href="${base}/quiz/list" class="btn btn-default">取消</a>
					</div>
				</div>

			</fieldset>
		</form>
	</div>

	<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${base}/js/cupid/core.js"></script>
	<script type="text/javascript">
		var IconPicker = {
			init : function() {
				$('#btn-icon-selector').click(function() {
					IconPicker.show();
				});

				$('#icon-selector').find('a')
						.click(
								function() {
									$('#icon-selector').find('a').removeClass(
											'active');
									$(this).addClass('active');
									$('#quiz_icon_repl').attr(
											'src',
											'${base}/images/quiz/icons/'
													+ $(this).attr('data'));
									$('#quiz_icon').val($(this).attr('data'));
									$('#icon-selector').hide();
								});
			},
			show : function() {
				$('#icon-selector').show();
			}
		};

		$(document).ready(function() {
			IconPicker.init();

		});
	</script>
</body>
</html>