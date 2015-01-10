<%@ page contentType="text/html;charset=utf-8" language="java" isErrorPage="true" %>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" media="screen" href="${base }/css/reset.css" />
<title>错误</title>
<style type="text/css">
#container { height:100%; margin:0 auto; width:90%; text-align:center; }
</style>
</head>
<body>

<div id="container">
  <img src="<c:url value="/images/error_404.png"/>" style="margin:10px;" />
  <p><b>404 页面不存在</b></p>
  <a href="javascript:history.go(-1);void 0;"><fmt:message key="button.back"/></a>
</div>
</body>
</html>