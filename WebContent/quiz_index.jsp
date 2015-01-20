<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<cupid:ifNotLogin>
  <c:redirect url="/login.jsp" />
</cupid:ifNotLogin>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title><fmt:message key="index.product.name" bundle="${i18n_main}"/></title>
    <link type="text/css" rel="stylesheet" href="${base}/css/bootstrap.min.css" />
    <link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
    <link type="text/css" rel="stylesheet" href="${base}/css/huijia.css"/>
    <link type="text/css" rel="stylesheet" href="${base }/css/ui/validate/jquery.validate.css" />
    
  </head>
  <body>
    <c:import url="/includes/header_huijia.jsp"></c:import>
    <div id="wrap" class="container auto-spread">
    	<iframe id="main-iframe" src="${base}/quiz/showtest" frameBorder="0" border="0" width="100%" height="100%"></iframe>
    </div>
	<%@ include file="/includes/footer.jsp" %>
  </body>
</html>
