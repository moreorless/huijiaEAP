<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><fmt:message key="index.product.name" bundle="${i18n_main}"/></title>
    <link type="text/css" rel="stylesheet" href="${base}/css/bootstrap.min.css" />
    <link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
    <c:if test="${param.exportpdf != 'true'}">
    <link type="text/css" rel="stylesheet" href="${base}/css/huijia.css"/>
    </c:if>
    <link type="text/css" rel="stylesheet" href="${base}/css/quiz/report.css" />
    <style type="text/css">
        html {overflow: auto !important;}
        body {overflow: auto !important;background-color: #fff}
        <c:if test="${param.exportpdf == 'true'}">
        	#export-btn-wrappwer {display:none}
        	#nav{display:none}
        	#footer{display:none}
        	@media print{
            	p h1 h2 h3 h4 {page-break-before:always;}
        	}
        </c:if>        
    </style>
</head>
<body>
    <c:import url="/includes/header_huijia.jsp"></c:import>
    <div class="container report-wrapper">
        
      	<div style="text-align:right" id="export-btn-wrappwer">
      		<a href="${base}/quiz/enquizlist" class="btn btn-link"><img src="${base}/images/quiz/go_home.png" />&nbsp;返回首页</a>
      		<a href="${base}/quiz/reportexport?quizId=${param.quizId}" class="btn btn-link" id="btn-export" target="_blank"><img src="${base}/images/quiz/pdf.png" />&nbsp;导出报告</a>
      	</div>
        
        <div>
        <c:import url="/includes/report/${quiz.reporttpl}_person.jsp">
            <c:param name="exportpdf" value="${param.exportpdf}" />
        </c:import>
        </div>
    </div>
    <%@ include file="/includes/footer_huijia.jsp" %>
<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/js/echarts/echarts.js"></script>
<script type="text/javascript">
var quizMap = {};
<c:forEach items="${quizlist}" var="_quiz" varStatus="stat">
  var categories = [];
  <c:forEach items="${_quiz.categoryList}" var="category">
  categories.push("${category.name}");
  </c:forEach>
  quizMap[${_quiz.id}] = {categories : categories};
</c:forEach>

var column_h_Options = [];
<c:forEach items="${resultlist}" var="result" varStatus="stat">
    var scoreArray${stat.index} = [];
    <c:forEach items="${result.scoreMap}" var="_obj">
    scoreArray${stat.index}.push(${_obj.value});
    </c:forEach>
    
    column_h_Options[${stat.index}] = {
    	    tooltip : {
                show: true
            },
            xAxis : [
                {
                    type : 'value'
                }
            ],
            yAxis : [
                {
                    type : 'category',
                    data : quizMap[${result.quizId}].categories
                }
            ],
            
            series : [
                {
                    name:'分数',
                    type:'bar',
                    data:scoreArray${stat.index}
                }
            ]
        };
</c:forEach>

    
    require.config({
        paths: {
            echarts: '${base}/js/echarts'
        }
    });

    
    require([
         'echarts',
         'echarts/theme/green',
         'echarts/chart/bar'
         ],
         function(echarts, theme){
            $('.eap-report').each(function(){
                var data = $(this).attr('data');
                if(data.indexOf('report:column_h') != -1){
                    var quizIndex = data.substr(data.lastIndexOf(':') + 1);
                    quizIndex = parseInt(quizIndex);
                    var myChart = echarts.init($(this).get(0), theme);
                    myChart.setOption(column_h_Options[quizIndex]);
                }
            });
         });

</script>
</body>
</html>
