<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
{
    title : {
        text: ''
    },
    tooltip : {
        trigger: 'axis'
    },
    calculable : true,
    polar : [
        {
            indicator : [
            	<c:forEach var="categoryname" items="${categorieNames}" varStatus="status">{text : '${categoryname}', max  : 100}<c:if test="${!status.last}">,</c:if></c:forEach>
            ],
            radius : 130
        }
    ],
    series : [
        {
            name: '各维度得分',
            type: 'radar',
            itemStyle: {
                normal: {
                    areaStyle: {
                        type: 'default'
                    }
                }
            },
            data : [
                {
                    value : [<c:forEach var="score" items="${scoreArray}" varStatus="status">${score}<c:if test="${!status.last}">,</c:if></c:forEach>],
                    name : ''
                }
            ]
        }
    ]
}