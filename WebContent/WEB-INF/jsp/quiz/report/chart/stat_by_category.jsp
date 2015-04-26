<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
{
    title : {
        text: '各维度得分'
    },
    tooltip : {
        trigger: 'axis'
    },
    calculable : true,
    xAxis : [
        {
            type : 'value',
            boundaryGap : [0, 0.01]
        }
    ],
    yAxis : [
        {
            type : 'category',
            data : [<c:forEach var="categoryname" items="${categorieNames}" varStatus="status">'${categoryname}'<c:if test="${!status.last}">,</c:if></c:forEach>]
        }
    ],
    series : [
        {
            name:'分数',
            type:'bar',
            data: [<c:forEach var="score" items="${scoreArray}" varStatus="status">${score}<c:if test="${!status.last}">,</c:if></c:forEach>]
        }
    ]
}