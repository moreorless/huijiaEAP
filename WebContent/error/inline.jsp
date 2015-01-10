<%@ include file="/includes/taglibs.jsp"%>
<c:choose>
  <c:when test="${not empty huijia_EXCEPTION }">
<c:import url="/error/error.jsp" />
  </c:when>
  <c:otherwise>
<style type="text/css">
ul.error, ul.message {margin: 5px auto;padding: 2px;text-align: left;}
ul.error {background-color: #ffdfdf;border: 1px solid #f3afb5;color: #e87d86;}
ul.error a {color: #e87d86; text-decoration: underline;}
ul.message {background-color: #fffcdf;border: 1px solid #999;color: #333;}
ul.message a {color: #333; text-decoration: underline;}
ul.error li, ul.message li {height: 20px;line-height: 20px;padding-left: 25px;}
ul.message li { background: url(${base}/images/inline_message.png) no-repeat 5px 50%;}
ul.error li { background: url(${base}/images/inline_error.png) no-repeat 5px 50%;}
ul.error.empty {display: none !important;}
ul.error > .error-close, ul.message > .error-close { display:block; width:16px; height:16px; float:right; margin-top:-10px; cursor:pointer; background:url(${base}/images/inline_close.png) no-repeat; }
<c:if test="${not empty param.showErrorCloser && param.showErrorCloser == 'false' }">
ul.error > .error-close, ul.message > .error-close {display:none;}
</c:if>
</style>

<c:if test="${not empty huijia_EXCEPTION }">
  <c:set var="huijia_EXCEPTION" value="${requestScope.huijia_EXCEPTION }" scope="session" />
  <script type="text/javascript">
  window.location.replace("${base}/error/error.jsp"); <%-- MUST use javascript to redirect! If use <c:redirect>, will be ignored while using <c:import> to import this page --%>
  </script>
</c:if>

<c:choose>
<c:when test="${not empty param.errorPlacerId }">
  <c:set var="epidSuffix" value="-${param.errorPlacerId }" />
</c:when>
<c:otherwise>
  <c:set var="epidSuffix" value="" />
</c:otherwise>
</c:choose>

<c:choose>
  <c:when test="${empty requestScope.errorPlacerIdx }">
    <c:set var="errorPlacerIdx" value="0" scope="request" />
  </c:when>
  <c:otherwise>
    <c:set var="errorPlacerIdx" value="${pageScope.errorPlacerIdx + 1 }" scope="request" />
  </c:otherwise>
</c:choose>

<script type="text/javascript">
var ___validator_server_errors_for___ = "${not empty param.___validator_form_idx___ && cupid:isNumber(param.___validator_form_idx___) ? param.___validator_form_idx___ : 0}";
var ___validator_server_errors___ = {};
<c:if test="${not empty CUPID_ERRORS && (empty param.errorPlacer || param.errorPlacer == errorPlacerIdx || not empty param.errorPlacerId && param.errorPlacer == param.errorPlacerId )}">
  <c:forEach items="${CUPID_ERRORS }" var="error">
    <c:choose>
    <c:when test="${not empty param.form && param.form == 'true' && cupid:hasProperty(error, 'name')}">
      <c:set var="errorKey" value="${error.name}" />
      <c:if test="${error.idx > 0}">
        <c:set var="errorKey" value="${errorKey}:eq(${error.idx})" />
      </c:if>
      <c:choose>
        <c:when test="${error.resource }">
          <cupid:setBundle var="bundle" value="${error.bundle}" />
          <fmt:message key="${error.key }" bundle="${bundle }" var="errorMsg" >
          <c:if test="${not empty error.args }">
            <c:forEach items="${error.args }" var="arg">
              <fmt:param value="${arg }" />
            </c:forEach>
          </c:if>
          </fmt:message>        
          ___validator_server_errors___["${errorKey}"] = "${errorMsg}";
        </c:when>
        <c:otherwise>
        ___validator_server_errors___["${errorKey}"] = "<c:out value="${error.key }" escapeXml="false" />";
        </c:otherwise>
      </c:choose>
    </c:when>
    <c:otherwise>
      <c:set var="ERROR_MSG_SEG">
      	<cupid:setEscape escapeXml="false">
        ${ERROR_MSG_SEG}
        </cupid:setEscape>
        <li>
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
        </c:choose>
        </li>
      </c:set>
    </c:otherwise>
    </c:choose>
  </c:forEach>

  <c:remove scope="session" var="CUPID_ERRORS"/>
</c:if>
if (typeof window.inlineCloseHandler == "undefined") {
	window.inlineCloseHandler = function(ele) {
		$(ele).parent().addClass("empty").children("li").remove();
	};
}
</script>

<ul class="error ${empty ERROR_MSG_SEG ? 'empty' : '' }" id="includeError${epidSuffix }" ${empty param.form || param.form == 'false' ? 'data-error-together="true"' : '' }>
  <p class="error-close" onclick="window.inlineCloseHandler(this)"></p>
  <cupid:setEscape escapeXml="false">
  ${ERROR_MSG_SEG }
  </cupid:setEscape>
</ul>

<c:if test="${not empty CUPID_MESSAGES }">
<ul class="message fade-ffff00" id="includeMessage${epidSuffix }">
  <p class="error-close" onclick="window.inlineCloseHandler(this)"></p>
    <c:forEach items="${CUPID_MESSAGES }" var="message">
      <li>
      <c:choose>
        <c:when test="${message.resource }">
          <cupid:setBundle var="bundle" value="${message.bundle}" />
          <fmt:message key="${message.key }" bundle="${bundle }" >
            <c:if test="${not empty message.args }">
              <c:forEach items="${message.args }" var="arg">
                <fmt:param value="${arg }" />
              </c:forEach>
            </c:if>
          </fmt:message>
        </c:when>
        <c:otherwise>
          <c:out value="${message.key }" escapeXml="false" />
        </c:otherwise>
      </c:choose>
      </li>
    </c:forEach>
</ul>
</c:if>
<c:remove scope="session" var="CUPID_MESSAGES"/>
  </c:otherwise>
</c:choose>
