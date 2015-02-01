 <%@ page contentType="text/html;charset=utf-8" language="java" %>
 <%@ include file="/includes/taglibs.jsp"%>
 <!-- Fixed navbar -->
<div id="nav">
    <div>
      <div class="huijia-header-bg">
      </div>
      <div class="huijia-header-nav">
            <div class="col-md-8 brand"><h4><c:if test="${param.hideBtn != 'true' }">${current_user.realname}，</c:if>欢迎您使用会佳心语测评系统</h4></div>
            <c:if test="${param.hideBtn != 'true' }">
            <div class="col-md-2 brand-right" style="text-align:right">
            	<label>个人设置</label>
            	<a href="javascript://" id="btn-user"><img src="${base}/images/config.jpg"></img></a>
            </div>
            </c:if>
            <div class="col-md-2 brand-right" style="text-align:right">
            	<label>退出</label>
            	<a href="${base}/signout"><img src="${base}/images/quit.jpg"></img></a>
            </div>
       </div>
    </div>
</div>
      