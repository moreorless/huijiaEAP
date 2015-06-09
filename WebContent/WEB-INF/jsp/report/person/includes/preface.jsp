<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>

<div class="pdf-page">
	<div id="preface">
		<div class="header">
			<div class="logo"><img src="${base}/images/report/logo_w.png" width="140px" ></img></div>
			<div class="report">报&nbsp;&nbsp;告</div>
			<div class="split1"></div>
			<div class="split2"></div>
		</div>
		
		<div class="welcome">
			<p>恭喜您已经完成了“${quiz.name}”测评。</p>
			<p>本报告的结果基于您对问卷的回答，经过计算机专家系统的分析而得出。</p>
			<p>本测评报告主要由以下几部分组成：</p>
		</div>
		<div class="catalog">
			<ul>
			<c:choose>
				<c:when test="${quiz.reporttpl == 'mental_checkup' }">
					<li>Ⅰ  心理健康概念 </li>
					<li>Ⅱ  心理健康的标准</li>
					<li>Ⅲ  影响心理健康的因素</li>
					<li>Ⅳ  心理健康体检测评说明</li>
					<li>Ⅴ  您的心理健康总体状况详解</li>
					<li>Ⅵ  您的心理健康各维度状况详解</li>
					<li>Ⅶ  心理测评师给您的建议</li>
					<li>Ⅷ  本测评的注意事项</li>
				</c:when>
				<c:when test="${quiz.reporttpl == 'communicate_conflict' }">
					<li>Ⅰ  沟通风格与冲突处理方式的概念</li>
					<li>Ⅱ  影响沟通风格与冲突处理方式的因素</li>
					<li>Ⅲ  沟通风格与冲突处理方式测评说明</li>
					<li>Ⅳ  您的沟通风格状况详解</li>
					<li>Ⅴ  您的冲突处理方式状况详解</li>
					<li>Ⅵ  心理测评师给您的建议</li>
				</c:when>
			</c:choose>
			</ul>
		</div>
		<div class="footer">
			
		</div>
	</div>
</div>