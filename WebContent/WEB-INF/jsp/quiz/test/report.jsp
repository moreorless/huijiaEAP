<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>测验报告</title>
    <link type="text/css" rel="stylesheet" href="${base}/css/bootstrap.min.css" />
    <link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
    <link type="text/css" rel="stylesheet" href="${base}/css/quiz/report.css" />
    <link type="text/css" rel="stylesheet" href="${base}/css/huijia.css"/>
    <style type="text/css">
        body {overflow: auto;background-color: #f3f3f3}
    </style>
</head>
<body>
    <c:import url="/includes/header_huijia.jsp"></c:import>
    <div class="container report-wrapper">
        <h2 class="report-caption">您的测试结果 ${quiz.reporttpl}</h2>
        <div>
        <c:import url="/includes/report/${quiz.reporttpl}_person.jsp"></c:import>
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
                trigger: 'axis'
            },
            toolbox: {
                show : false
            },
            calculable : true,
            xAxis : [
                {
                    type : 'value',
                    splitArea : {show : true}
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
         'echarts/chart/bar',
         'echarts/theme/green'
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
