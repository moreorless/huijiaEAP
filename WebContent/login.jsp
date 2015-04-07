<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title><fmt:message key="index.product.name" /></title>
    <link type="text/css" rel="stylesheet" href="${base}/css/bootstrap.min.css" />
    
    <link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
    <link type="text/css" rel="stylesheet" href="${base}/css/login.css"/>
	
  </head>
  <body>

    <div id="container">
    	<div class="logo">
    		<img src="${base}/images/logo.jpg"></img>
    		<strong>心理测评系统</strong>
    	</div>
    	<div class="loginWrap">
    	<div class="loginBox">
		<form class="form-signin form-horizontal" role="form" action="${base}/signin" method="post">
			<header>用户登录</header>
			<div class="form-body">
			
		        <div class="form-group">
		        	<%@ include file="/error/inline.jsp" %>
		        </div>
		         <div class="form-group">

		         <div class="input-group">
		         	<span class="input-group-addon" id="basic-addon1">
		         		<span class="glyphicon glyphicon-user" aria-hidden="true" ></span>
		         	</span>
		         	<input type="text" id="inputEmail" class="form-control" placeholder="用户名" name="username" aria-describedby="basic-addon1" />
		         </div>
		         </div>

		         <div class="form-group">
			         <div class="input-group">
			         	<span class="input-group-addon" id="basic-addon2">
			         		<span class="glyphicon glyphicon-lock" aria-hidden="true" ></span>
			         	</span>
			         	<input type="password" id="inputPassword" class="form-control" placeholder="密码" name="password" aria-describedby="basic-addon2"/>
			         </div>
		         	<!--div class="note"><a href="${base}/user/findpwd" target="_blank">忘记密码?</a></div-->
	     		</div>
	     	</div>
	         <footer>
	         	&nbsp;
	         	<button class="btn btn-large btn-primary" type="submit">登录</button>
	         </footer>
      	</form>
      	</div>
      	</div>
    </div>


    <script type="text/javascript" src="${base}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${base}/js/bootstrap.min.js"></script>

  </body>
</html>
