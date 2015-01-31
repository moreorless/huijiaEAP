<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<cupid:ifNotLogin>
  <c:redirect url="/login.jsp" />
</cupid:ifNotLogin>

<c:if test="${current_user.type == 0}">
	<c:import url="admin_index.jsp" />
</c:if>
<c:if test="${current_user.type == 2}">
	<c:redirect url="/quiz/enquizlist" />
</c:if>
