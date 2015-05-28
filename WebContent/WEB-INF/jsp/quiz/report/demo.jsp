<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><fmt:message key="index.product.name" bundle="${i18n_main}"/></title>
    <link type="text/css" rel="stylesheet" href="${base}/css/bootstrap.min.css" />
    <link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
    <link type="text/css" rel="stylesheet" href="${base}/css/huijia.css"/>
    <link type="text/css" rel="stylesheet" href="${base}/css/quiz/report.css" />
    <style type="text/css">
        html {overflow: auto !important;}
        body {overflow-y: auto !important; }
        
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
        	<h1>标题1</h1>
        	<h2>标题1.1</h2>
        	<h2>标题1.2</h2>
        	<c:forEach  var="i" begin="1" end="9" step="1">
        		<p> p${i} : 本文用识别由域名生成算法Domain Generation Algorithm: DGA生成的C&C域名作为例子，目的是给白帽安全专家们介绍一下机器学习在安全领域的应用，演示一下机器学习模型的一般流程。机器的力量可以用来辅助白帽专家们更有效率的工作。</p>
        	</c:forEach>
        	<div class="chart"></div>
        	
        	<h1>标题2</h1>
        	<h2>标题2.1</h2>
        	<c:forEach  var="i" begin="1" end="8" step="1">
        		<p> p${i} : 本文用识别由域名生成算法Domain Generation Algorithm: DGA生成的C&C域名作为例子，目的是给白帽安全专家们介绍一下机器学习在安全领域的应用，演示一下机器学习模型的一般流程。机器的力量可以用来辅助白帽专家们更有效率的工作。</p>
        	</c:forEach>
        	<div class="chart"></div>
        	
        	<h2>标题2.2</h2>
        	<div class="chart"></div>
        	<c:forEach  var="i" begin="1" end="7" step="1">
        		<p> p${i} : 本文用识别由域名生成算法Domain Generation Algorithm: DGA生成的C&C域名作为例子，目的是给白帽安全专家们介绍一下机器学习在安全领域的应用，演示一下机器学习模型的一般流程。机器的力量可以用来辅助白帽专家们更有效率的工作。</p>
        	</c:forEach>
        	<h1>标题3</h1>
        	<c:forEach  var="i" begin="1" end="6" step="1">
        		<p> p${i} : 本文用识别由域名生成算法Domain Generation Algorithm: DGA生成的C&C域名作为例子，目的是给白帽安全专家们介绍一下机器学习在安全领域的应用，演示一下机器学习模型的一般流程。机器的力量可以用来辅助白帽专家们更有效率的工作。</p>
        	</c:forEach>
        	<h2>标题3.1</h2>
        	<c:forEach  var="i" begin="1" end="5" step="1">
        		<p> p${i} : 本文用识别由域名生成算法Domain Generation Algorithm: DGA生成的C&C域名作为例子，目的是给白帽安全专家们介绍一下机器学习在安全领域的应用，演示一下机器学习模型的一般流程。机器的力量可以用来辅助白帽专家们更有效率的工作。</p>
        	</c:forEach>
        	<h2>标题3.2</h2>
        </div>
    </div>
    <c:if test="${param.exportpdf != 'true'}">
    <%@ include file="/includes/footer_huijia.jsp" %>
    </c:if>
<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${base}/js/echarts/echarts.js"></script>
<script type="text/javascript" src="${base }/js/cupid/core.js"></script>

<c:if test="${param.exportpdf != 'true'}">
<!--验证脚本 -->
<script type="text/javascript" src="${base }/js/ui/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${base }/js/ui/validate/messages_cn.js"></script>
<script type="text/javascript" src="${base }/js/ui/validate/jquery.validate.ext.methods.js"></script>

<script type="text/javascript" src="${base}/js/huijia/index.js"></script>
</c:if>
<script type="text/javascript">

var chartOption = {
	    title : {
	        text: '某地区蒸发量和降水量',
	        subtext: '纯属虚构'
	    },
	    tooltip : {
	        trigger: 'axis'
	    },
	    legend: {
	        data:['蒸发量','降水量']
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            mark : {show: true},
	            dataView : {show: true, readOnly: false},
	            magicType : {show: true, type: ['line', 'bar']},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    calculable : true,
	    xAxis : [
	        {
	            type : 'category',
	            data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value'
	        }
	    ],
	    series : [
	        {
	            name:'蒸发量',
	            type:'bar',
	            data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3],
	            markPoint : {
	                data : [
	                    {type : 'max', name: '最大值'},
	                    {type : 'min', name: '最小值'}
	                ]
	            },
	            markLine : {
	                data : [
	                    {type : 'average', name: '平均值'}
	                ]
	            }
	        },
	        {
	            name:'降水量',
	            type:'bar',
	            data:[2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3],
	            markPoint : {
	                data : [
	                    {name : '年最高', value : 182.2, xAxis: 7, yAxis: 183, symbolSize:18},
	                    {name : '年最低', value : 2.3, xAxis: 11, yAxis: 3}
	                ]
	            },
	            markLine : {
	                data : [
	                    {type : 'average', name : '平均值'}
	                ]
	            }
	        }
	    ]
	};
	                    
    
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
            $('.chart').each(function(){
                var myChart = echarts.init($(this).get(0), theme);
                myChart.setOption(chartOption);
            });
         });

</script>
</body>
</html>
