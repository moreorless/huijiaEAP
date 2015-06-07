<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>

<div class="pdf-page">
	<div id="cover">
	  	<div id="cover-upper">
	  		<div class="logo"><img src="${base}/images/report/logo.png" width="140px" ></img></div>
	  		<div class="title">
	  			<div class="tag">
	  				<span>The personal report</span>
	  				
	  			</div>
	  			<div class="quizname">${quiz.name}</div>
	  			<div class="quizname">测评报告</div>
	  		</div>
	  	</div>
	  	<div id="cover-middle">
	  	</div>
		<div id="cover-under">
			<p>姓名：${user.realname }</p>
			<p>
				<c:choose>
					<c:when test='${user.gender == 0}'>性别：女</c:when>
					<c:when test='${user.gender == 1}'>性别：男</c:when>
				</c:choose>
			</p>
			<p>年龄：${user.age }</p>
			<p>测试日期：${testDate}</p>
		</div>
		<div id="cover-footer">
			
		</div>
	</div>
</div>