 <%@ page contentType="text/html;charset=utf-8" language="java" %>
 <%@ include file="/includes/taglibs.jsp"%>
 <!-- Fixed navbar -->
<div id="nav" class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
          <span class="sr-only">Toggle navigation</span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="${base}" id="logo_bar"><fmt:message key="index.product.name" bundle="${i18n_main}"/></a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav" id="nav-items">
              <c:forEach items="${navigators_in_session}" var="nav" varStatus="status">
                <c:if test="${current_user.type <=1 || fn:contains(current_user.authedNavs, nav.id)}">
                  <li id="${nav.id}" data="${nav.url}" <c:if test="${status.first}">class="active"</c:if>>
                    <a href="javascript://"><img src="${base }/images/${nav.icon}" width="20px" height="20px"></img>&nbsp;${nav.name}</a>
                  </li>
                </c:if>
              </c:forEach>
            </ul>
            <ul class="nav navbar-nav navbar-right">
               <li>
                <p class="navbar-text pull-right">
                  当前用户
                  <a href="#" class="navbar-link" id="btn-user">${current_user.name}</a> &nbsp;
                  <a href="${base}/signout" class="navbar-link">退出</a>
                </p>
              </li>
            </ul>
        </div>
    </div>
</div>
      