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
  	#wrap {padding:10px;}
  	em{color:red}
  </style>
</head>
<body>
	<div class="container" id="wrap">
	<%@ include file="/error/inline.jsp" %>
	<form class="form-horizontal" role="form" action="${base}/quiz/${param.operation}" method="post">
		<input type="hidden" name="id" value="${quiz.id}"/>
		<fieldset>
  			<legend>
  				<c:if test="${param.operation == 'add' }">添加试卷</c:if>
  				<c:if test="${param.operation == 'edit' }">编辑试卷</c:if>
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
				    <textarea class="form-control" rows="3" name="description" id="description">
				    ${quiz.description}
				    </textarea>
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
			    <button class="btn btn-primary">保存</button>
			    <a href="${base}/quiz/list" class="btn btn-default">取消</a>
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
	
</script>
</body>
</html>