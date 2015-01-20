<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>查看问卷</title>
  <link type="text/css" rel="stylesheet" href="${base}/css/bootstrap.min.css" />
  <link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
  <link type="text/css" rel="stylesheet" href="${base }/css/ui/validate/jquery.validate.css" />
  
  <style type="text/css">
  	#wrap {padding:10px;}
  	em{color:red}
  </style>
</head>
<body>
	<div class="container" id="wrap">
	<%@ include file="/error/inline.jsp" %>
	<form class="form-horizontal" role="form" method="post">
		<input type="hidden" name="id" value="${quiz.id}"/>
		<fieldset>
  			<legend>
  				试卷信息
  			</legend>
		
			<div class="form-group">
				<label class="col-sm-2 control-label">名称<em>*</em></label>
				<div class="col-sm-10">
				    <input type="text" class="form-control" name="name" id="name" value="${quiz.name}" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">描述</label>
				<div class="col-sm-10">
				    <textarea class="form-control" rows="3" name="description" id="description">${quiz.description}</textarea>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">图标</label>
				<div class="col-sm-10">
					<input type="hidden" name="icon" id="quiz_icon" value="default.png"/>
				</div>
			</div>
			
			<div class="form-group">
			  <div class="col-sm-offset-2 col-sm-10">
			    <a href="${base}/quiz/list" class="btn btn-default">返回试卷列表</a>
			  </div>
			</div>
		</fieldset>
	</form>
	</div>
	
<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/js/cupid/core.js"></script>
<script type="text/javascript">
</script>
</body>
</html>