<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><fmt:message key="index.product.name" bundle="${i18n_main}"/></title>
    <style type="text/css">
    	html body{margin: 0; background-color: #565656;}

    	.pdf-page{page-break-before: always;width: 210mm; height: 297mm; background-color: #565656; margin: 0 auto;}
        .chart{min-width: 310px; height: 400px; margin: 0 auto}
        
        .pdf-header{height: 40mm; background-color: #9edfff}
        .pdf-body  {height: 300mm;padding: 20px;overflow: hidden;}
        .pdf-footer{height: 10mm; background-color: #1f2f9a}


        #page1 .pdf-body{background-color: #a4c2e9}
        #page2 .pdf-body{background-color: #b18dc0}
        #page3 .pdf-body{background-color: #bef57c}
    </style>
</head>
<body>



        
    <div class="pdf-page" id="page1">
    	<div class="pdf-header">标题</div>
    	<div class="pdf-body">
    		<h1>第一页</h1>
	    	<h2>标题1.1</h2>
	    	<h2>标题1.2</h2>
	    	<c:forEach  var="i" begin="1" end="3" step="1">
	    		<p> p${i} : 本文用识别由域名生成算法Domain Generation Algorithm: DGA生成的C&C域名作为例子，目的是给白帽安全专家们介绍一下机器学习在安全领域的应用，演示一下机器学习模型的一般流程。机器的力量可以用来辅助白帽专家们更有效率的工作。</p>
	    	</c:forEach>
	    	
	    	<div class="chart"></div>
	    	
	    	<h1>标题2</h1>
	    	<h2>标题2.1</h2>
    	</div>
    	
    	<div class="pdf-footer"></div>
    </div>    	

    <div class="pdf-page" id="page2">
    	<div class="pdf-header">标题</div>
    	<div class="pdf-body">
    		<h1>第二页</h1>
    	</div>
    	<div class="pdf-footer"></div>
    </div>

    <div class="pdf-page" id="page3">
    	<div class="pdf-header">标题</div>
    	<div class="pdf-body">
    		<h1>第三页</h1>
    	</div>
    	<div class="pdf-footer"></div>
    </div>


<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/js/highcharts/highcharts.js"></script>

<script type="text/javascript">
$(function(){
	$('.chart').highcharts({
		plotOptions: {
        	line: {
	          animation: false,
	          shadow: false,
	          enableMouseTracking: false
	        }
	    },
        title: {
            text: '每月平均气温',
            x: -20 //center
        },
        subtitle: {
            text: 'Source: WorldClimate.com',
            x: -20
        },
        xAxis: {
            categories: ['一月', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
        },
        yAxis: {
            title: {
                text: '温度 (°C)'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            enable: false
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },
        series: [{
            name: '东京',
            data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
        }, {
            name: 'New York',
            data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
        }, {
            name: 'Berlin',
            data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
        }, {
            name: 'London',
            data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
        }]
    });
});

</script>
</body>
</html>
