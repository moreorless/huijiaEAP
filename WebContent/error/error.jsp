<%@ page contentType="text/html;charset=utf-8" language="java" isErrorPage="true" %>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" media="screen" href="${base }/css/reset.css" />
<title>错误</title>
<style type="text/css">
#container { height:100%; margin:0 auto; width:90%; text-align:center; overflow:auto; }
.contents {text-align:left;}
.stacktrace {padding-left:2em;}
p.errorcode {word-wrap:break-word;text-align:left;}
</style>
</head>
<body>
<div class="wrap">
<div id="container">
  <c:choose>
  <c:when test="${not empty CUPID_ERRORS && (empty err || cupid:hasProperty(err, 'errors')) }">
    <img src="<c:url value="/images/error_user.png"/>" style="margin:10px;" />
    <div class="contents">
    <c:forEach items="${CUPID_ERRORS }" var="error">
      <c:choose>
        <c:when test="${error.resource }">
          <cupid:setBundle var="bundle" value="${error.bundle}" />
          <fmt:message key="${error.key }" bundle="${bundle }" >
            <c:if test="${not empty error.args }">
              <c:forEach items="${error.args }" var="arg">
                <fmt:param value="${arg }" />
              </c:forEach>
            </c:if>
          </fmt:message>
        </c:when>
        <c:otherwise>
          <c:out value="${error.key }" escapeXml="false" />
        </c:otherwise>
      </c:choose><br/>
    </c:forEach>
    </div>
    <c:remove scope="session" var="CUPID_ERRORS"/>
  </c:when>
  <c:when test="${not empty huijia_EXCEPTION}">
    <c:choose>
      <c:when test="${cupid:hasProperty(huijia_EXCEPTION, 'permission') }">
        <img src="<c:url value="/images/error_auth.png"/>" style="margin:10px;" />
        <p><b>没有操作权限</b></p>
      </c:when>
      <c:otherwise>
        <img src="<c:url value="/images/error_common.png"/>" style="margin:10px;" /><br />
        <p>服务器发生内部错误，请将如下信息反馈给管理员：</p>
        <p class="errorcode">${huijia_EXCEPTION }</p>
      </c:otherwise>
    </c:choose>
    <c:remove scope="session" var="huijia_EXCEPTION"/>
  </c:when>
  <c:when test="${cupid:hasProperty(err, 'stackTrace') }">
    <img src="<c:url value="/images/error_common.png"/>" style="margin:10px;" /><br />
    <b><c:out value="${err}" escapeXml="true" /></b><br/>
    <div class="contents">
    <c:forEach items="${err.stackTrace }" var="trace">
      <div class="stacktrace">at&nbsp;<c:out value="${trace }" escapeXml="true" /></div>
    </c:forEach>
    </div>
  </c:when>
  <c:otherwise>
    ${err }
  </c:otherwise>
  </c:choose>
  <p />
  <c:if test="${empty huijia_errorpage_showback || huijia_errorpage_showback == true }">
  <a href="javascript:history.go(-1);void 0;"><fmt:message key="button.back"/></a>
  </c:if>
</div>
</div>
</body>
</html>