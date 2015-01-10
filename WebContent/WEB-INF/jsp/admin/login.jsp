<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title><fmt:message key="index.product.name" /></title>
    <link type="text/css" rel="stylesheet" href="${base}/css/bootstrap.min.css" />
    <link type="text/css" rel="stylesheet" href="${base}/css/login.css"/>
    <link type="text/css" rel="stylesheet" href="${base}/css/bootstrap-responsive.css" />
  </head>
  <body>

    <div id="container">
    	
		<form class="form-horizontal form-signin" action="${base}/signin" method="post">
	        <h2 class="form-signin-heading"><fmt:message key="index.product.name" bundle="${i18n_main}"/>后台管理</h2>
	        
	        <div class="control-group">
	        	<%@ include file="/error/inline.jsp" %>
	        </div>
	         <div class="control-group">
	         	<label class="control-label" for="inputEmail">用户名</label>
	         	<div class="controls">
	         	<input type="text" id="inputEmail" placeholder="用户名" name="username">
	         	</input>
	         	</div>
	         </div>
	         
	         <div class="control-group">
	         	<label class="control-label" for="inputPassword">密码</label>
	         	<div class="controls">
	         	<input type="password" id="inputPassword" placeholder="密码" name="password">
	         	</input>
	         	</div>
	         </div>
	         
	        <button class="btn btn-large btn-primary" type="submit">登录</button>
      	</form>
      	
      	<div style="position:fixed; right:50px;bottom:20px;font-size:8px;color:#666"><fmt:message key="build.version" bundle="${i18n_main}" /></div>
    </div>


    <script type="text/javascript" src="${base}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${base}/js/bootstrap.min.js"></script>

  </body>
</html>
