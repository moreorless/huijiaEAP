<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>


<c:choose>
	<c:when test="${quiz.reporttpl == 'mental_checkup'}">
		<c:redirect url="/report/company/mentalCheckup?segmentId=${param.segmentId}&quizId=${param.quizId }" />
	</c:when>
	<c:when test="${quiz.reporttpl == 'communicate_conflict'}">
		<c:redirect url="/report/company/communicateConflict?segmentId=${param.segmentId}&quizId=${param.quizId }" />
	</c:when>
	<c:when test="${quiz.reporttpl == 'employee_survey'}">
		<c:redirect url="/report/company/employeeSurvey?segmentId=${param.segmentId}&quizId=${param.quizId }" />
	</c:when>
	<c:when test="${quiz.reporttpl == 'emotion_management'}">
		<c:redirect url="/report/company/emotionManagement?segmentId=${param.segmentId}&quizId=${param.quizId }" />
	</c:when>
</c:choose>
