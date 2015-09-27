<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>报表管理</title>
<link type="text/css" rel="stylesheet"
	href="${base}/css/bootstrap.min.css" />
<link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
<link type="text/css" rel="stylesheet" href="${base}/css/quiz/quiz.css" />

<script type="text/javascript" src="${base}/js/echarts/echarts-all.js"></script>
<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/js/bootstrap.min.js"></script>

<style type="text/css">
body {
	overflow: auto
}
</style>
</head>
<body>
	<div class="container">
		<h2>情绪倾向管理报表</h2>
		<c:import url="/WEB-INF/jsp/report/company/includes/common.jsp"></c:import>

		<p>单位 ：${commonParamSet.companyName}</p>
		<p>日期：</p>

		<h1>目录</h1>
		<h1>一 调查简介</h1>
		<h2>1.1 调查目的</h2>
		<h2>1.2 调查方法</h2>
		<h2>1.3 调查群体</h2>
		<h1>二 调查问卷的组成</h1>
		<h1>三 调查结果整体分析</h1>
		<h2>3.1 统计结果概述</h2>
		<h2>3.2 四大维度整体概述</h2>
		<h2>3.3 九个子维度的对比分析</h2>

		<h1>一 调查简介</h1>
		<h2>1.1 调查目的</h2>
		<p>${commonParamSet.companyName}与会佳心语顾问一起合作的“情绪管理倾向测评”项目，将从全方位了解企业员工情绪管理状况。这次测评的目的包括如下几点：首先，帮助企业员工了解自己的情绪管理能力；第二，帮助企业了解目前员工的情绪状况；第三，探索员工与企业提高员工情绪管理的有效方法。</p>
		<h2>1.2 调查方法</h2>
		<p>本次调研项目，采用的是问卷调查法。在问卷回收后，通过专业的统计分析方法对问卷进行综合分析，并与常模数据进行对比，从而帮助员工了解自己的情绪管理水平。</p>
		<h2>1.3 调研群体</h2>
		<p>本次参加调研项目的群体是企业的${commonParamSet.testedUserCount }名员工。</p>

		<h1>二 调查问卷的组成</h1>
		<p>该调查问卷共由共12组题组成，共涉及情绪感知、情绪控制、社交技巧和情绪利用四大维度。其中，情绪感知维度包括自我情绪感知、他人情绪感知两个二级维度；情绪控制维度包括情绪稳定性、控制力、自我激励三个二级维度；社交技巧维度包括情绪表达、适应力和感染力三个二级维度。问卷的题目包括我很清楚自己每时每刻的情绪，我认为自己很容易理解别人的身体语言，等等。</p>

		<p>图1 调查问卷的五大因素</p>
		<div
			style="width: 700px; height: 191px;background-image:url('${base}/images/report/quiz4-Fivefactors.png');"></div>
		<p>
			在上述因素中，<strong>情绪感知维度</strong>是指认识自身和他人情绪，并以此作为行动的依据，能够设身处地地为他人着想。此维度关系到个体的深层情绪和情绪表达能力，是情绪管理能力的基础维度。<strong>情绪控制维度</strong>是指员工妥善管理自身情绪、高昂和压抑的情绪中恢复过来的能力。<strong>社会技巧维度</strong>是指员工在社会中情绪的表达、情绪的灵活适应和调动周围人情绪的能力。<strong>情绪利用</strong>是指利用情绪促进问题解决，情绪促进思维的能力。
		</p>

		<h1>三 调查结果整体分析</h1>
		<h2>3.1 统计结果概述</h2>
		<p>本次调查参加人数为${commonParamSet.segmentUserCount
			}，共收回${commonParamSet.validUserCount
			}份有效问卷。问卷回收率为${commonParamSet.validUserRatio }（此处百分比为收回问卷数量÷参加人数）。</p>
		<h2>3.2 四大维度整体概述</h2>

		<!--<p>一级维度常模均值与方差如下表所示</p>
		<table class="table table table-bordered table-striped"
			style="width: 50%">
			<tr>
				<th style="width: 30%">维度名称</th>
				<th style="width: 70%">均值</th>
			</tr>
			<c:forEach items="${employeeParamSet.changmoLv1AverageScores}" var="entry">
				<tr>
					<td>${entry.key}</td>
					<td>${entry.value}</td>
				</tr>
			</c:forEach>
			</table>-->

		<!-- <tr>
				<td>赞扬</td>
				<td><fmt:formatNumber
						value="${employeeParamSet.classifyLoyaltyPraiseWholeSatisfictionScore}"
						pattern="##.##" minFractionDigits="2"></fmt:formatNumber></td>
				<td><fmt:formatNumber
						value="${employeeParamSet.classifyLoyaltyPraiseAverageSatisfictionScore}"
						pattern="##.##" minFractionDigits="2"></fmt:formatNumber></td>
			</tr>
			<tr>
				<td>留任</td>
				<td><fmt:formatNumber
						value="${employeeParamSet.classifyLoyaltyRemainWholeSatisfictionScore}"
						pattern="##.##" minFractionDigits="2"></fmt:formatNumber></td>
				<td><fmt:formatNumber
						value="${employeeParamSet.classifyLoyaltyRemainAverageSatisfictionScore}"
						pattern="##.##" minFractionDigits="2"></fmt:formatNumber></td>
			</tr>
			 -->

		<div id="emotion_management_chart_top_category"
			style="height: 500px; width: 800px"></div>
		<script type="text/javascript">
(function(){
	var chart = echarts.init(document
			.getElementById('emotion_management_chart_top_category'));
		
	//var datas_std = [31,24,24,126];
	//var datas_high = [3,5,6,3,5,7];
	var datas_std = [<c:forEach var="item" items="${emotionManagementSet.topLevelStandardScoreList}" varStatus="status"><c:out value = "${item}"/><c:if test="${!status.last}">,</c:if></c:forEach>];
	var datas_average = [<c:forEach var="item" items="${emotionManagementSet.topLevelAverageScoreList}" varStatus="status"><c:out value = "${item}"/><c:if test="${!status.last}">,</c:if></c:forEach>];

	option = {
		    title : {
		    	x: "center",
		    	y: "bottom",
		        text: '四大维度整体概述'
		    },
		    renderAsImage:true,
		    legend: {
		        data:['常模分数','企业平均分']
		    },
		    color:['#21329d','#FF7F50','#ffcc3e', '#cc99cc','#9edfff','#959595', '#dedede','#9bc45a','#ffcc3e'],

		    xAxis : [
		        {
		            type : 'category',
		            name : '维度',
		            data : ['情绪感知','情绪控制','社交技巧','情绪利用']
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value',
		            min : 0,
		        	max : 100,
		        	name:'分数'
		        }
		    ],
		    series : [
				{
		            name:'常模分数',
		            type:'bar',
		            barWidth: 50,
		            itemStyle : { normal: {label : {show: true, position: 'top'}}},
		            data:datas_std,
		        },
		        {
		            name:'企业平均分',
		            type:'bar',
		            barWidth: 50,
		            itemStyle : { normal: {label : {show: true, position: 'top'}}},
		            data:datas_average,
		        }
		        
		    ]
		};
	chart.setOption(option);
})();
</script>
		<p>上图中，橙色柱状图表示本企业参加测试的团体在各个大维度上的平均分，而蓝色柱状图表示的是数据常模中各大维度的均值。${emotionManagementSet.topLevelAverageScoreEvaluation
			}。</p>


		<div id="emotion_management_chart_sub_category"
			style="height: 500px; width: 800px;"></div>
		<script type="text/javascript">
			(function() {
				
				var datas_std= [<c:forEach var="item" items="${emotionManagementSet.subLevelStandardScoreList}" varStatus="status"><c:out value = "${item}"/><c:if test="${!status.last}">,</c:if></c:forEach>];
				var datas_average = [<c:forEach var="item" items="${emotionManagementSet.subLevelAverageScoreList}" varStatus="status"><c:out value = "${item}"/><c:if test="${!status.last}">,</c:if></c:forEach>];
				
				var chart = echarts.init(document
						.getElementById('emotion_management_chart_sub_category'));
				var option = {
					title : {
						x : "center",
						y : "top",
						text : '二级维度数据分析图'
					},
					renderAsImage : true,
					color : [ '#21329d', '#FF7F50', '#959595', '#ffcc3e',
							'#9bc45a', '#dedede', '#9edfff', 'cc99cc' ],
					
					legend : {
						x : "right",
						y : "center",
						orient : "vertical",
						
						data : [ '常模数据', '本企业数据' ]
					},
					polar : [
					         {
					             indicator : [
					                 {text : '自我情绪感知', max  : 100},
					                 {text : '他人情绪感知', max  : 100},
					                 {text : '情绪稳定性', max  : 100},
					                 {text : '情绪控制力', max  : 100},
					                 {text : '自我激励', max  : 100},
					                 {text : '表达', max  : 100},
					                 {text : '适应力', max  : 100},
					                 {text : '感染力', max  : 100},
					                 {text : '问题解决', max  : 100}
					             ],
					             radius : 160
					         }
					     ],
					series : [ 
						{
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
						            value : datas_std,
						            name : '常模数据'
						        },
						        {
						            value : datas_average,
						            name : '本企业数据'
						        }
						    ]
						}  
					          
					]
				};
				// 为echarts对象加载数据 
				chart.setOption(option);
			})();
		</script>
		<p>上图中，橙色雷达图表示的本企业参加测试的员工在各个二级维度上的平均得分，而蓝色雷达图表示的数据常模中各个维度的均值。${emotionManagementSet.subLevelAverageScoreEvaluation
			}。</p>

		<h2>3.3 九个子维度的对比分析</h2>

		<div id="emotion_management_chart_ziwoqingxuganzhi"
			style="height: 500px; width: 800px"></div>
		<script type="text/javascript">
(function(){
	var chart = echarts.init(document
			.getElementById('emotion_management_chart_ziwoqingxuganzhi'));
	var datas = ${emotionManagementSet.dataZiwoqingxuganzhi};
	var option = {
		title : {
	        text: '自我情绪感知',
	        x:'center'
	    },
	    renderAsImage:true,
	    //color:['#21329d', '#959595'],
	    color:['#21329d', '#cc99cc','#9bc45a','#ffcc3e',  '#959595', '#9edfff'],
		series : [ {
			type : 'pie',
			radius : '55%',
	        center: ['50%', '50%'],
			data : datas
		} ]
	};
	chart.setOption(option);
})();
</script>
		<p>自我情绪感知维度上，${emotionManagementSet.evaluationZiwoqingxuganzhi }</p>


		<div id="emotion_management_chart_tarenqingxuganzhi"
			style="height: 500px; width: 800px"></div>
		<script type="text/javascript">
(function(){
	var chart = echarts.init(document
			.getElementById('emotion_management_chart_tarenqingxuganzhi'));
	var datas = ${emotionManagementSet.dataTarenqingxuganzhi};
	var option = {
		title : {
	        text: '他人情绪感知',
	        x:'center'
	    },
	    renderAsImage:true,
	    //color:['#21329d', '#959595'],
	    color:['#21329d', '#959595','#ffcc3e', '#cc99cc','#9bc45a','#ffcc3e',  '#959595', '#9edfff'],
		series : [ {
			type : 'pie',
			radius : '55%',
	        center: ['50%', '50%'],
			data : datas
		} ]
	};
	chart.setOption(option);
})();
</script>
		<p>他人情绪感知维度上，${emotionManagementSet.evaluationTarenqingxuganzhi }</p>
		
		
<div id="emotion_management_chart_wendingxing"
			style="height: 500px; width: 800px"></div>
		<script type="text/javascript">
(function(){
	var chart = echarts.init(document
			.getElementById('emotion_management_chart_wendingxing'));
	var datas = ${emotionManagementSet.dataWendingxing};
	var option = {
		title : {
	        text: '情绪稳定性',
	        x:'center'
	    },
	    renderAsImage:true,
	    //color:['#21329d', '#959595'],
	    color:['#21329d', '#959595','#ffcc3e', '#cc99cc','#9bc45a','#ffcc3e',  '#959595', '#9edfff'],
		series : [ {
			type : 'pie',
			radius : '55%',
	        center: ['50%', '50%'],
			data : datas
		} ]
	};
	chart.setOption(option);
})();
</script>
		<p>情绪稳定性维度上，${emotionManagementSet.evaluationWendingxing }</p>


<div id="emotion_management_chart_kongzhili"
			style="height: 500px; width: 800px"></div>
		<script type="text/javascript">
(function(){
	var chart = echarts.init(document
			.getElementById('emotion_management_chart_kongzhili'));
	var datas = ${emotionManagementSet.dataKongzhili};
	var option = {
		title : {
	        text: '情绪控制力',
	        x:'center'
	    },
	    renderAsImage:true,
	    //color:['#21329d', '#959595'],
	    color:['#21329d', '#959595','#ffcc3e', '#cc99cc','#9bc45a','#ffcc3e',  '#959595', '#9edfff'],
		series : [ {
			type : 'pie',
			radius : '55%',
	        center: ['50%', '50%'],
			data : datas
		} ]
	};
	chart.setOption(option);
})();
</script>
		<p>在情绪控制维度上，${emotionManagementSet.evaluationKongzhili }</p>


<div id="emotion_management_chart_ziwojili"
			style="height: 500px; width: 800px"></div>
		<script type="text/javascript">
(function(){
	var chart = echarts.init(document
			.getElementById('emotion_management_chart_ziwojili'));
	var datas = ${emotionManagementSet.dataZiwojili};
	var option = {
		title : {
	        text: '自我激励',
	        x:'center'
	    },
	    renderAsImage:true,
	    //color:['#21329d', '#959595'],
	    color:['#21329d', '#959595','#ffcc3e', '#cc99cc','#9bc45a','#ffcc3e',  '#959595', '#9edfff'],
		series : [ {
			type : 'pie',
			radius : '55%',
	        center: ['50%', '50%'],
			data : datas
		} ]
	};
	chart.setOption(option);
})();
</script>
		<p>在自我激励维度上，${emotionManagementSet.evaluationZiwojili }</p>


<div id="emotion_management_chart_biaoda"
			style="height: 500px; width: 800px"></div>
		<script type="text/javascript">
(function(){
	var chart = echarts.init(document
			.getElementById('emotion_management_chart_biaoda'));
	var datas = ${emotionManagementSet.dataBiaoda};
	var option = {
		title : {
	        text: '情绪表达',
	        x:'center'
	    },
	    renderAsImage:true,
	    //color:['#21329d', '#959595'],
	    color:['#21329d', '#959595','#ffcc3e', '#cc99cc','#9bc45a','#ffcc3e',  '#959595', '#9edfff'],
		series : [ {
			type : 'pie',
			radius : '55%',
	        center: ['50%', '50%'],
			data : datas
		} ]
	};
	chart.setOption(option);
})();
</script>
		<p>在情绪表达维度上，${emotionManagementSet.evaluationBiaoda }</p>


<div id="emotion_management_chart_shiyingli"
			style="height: 500px; width: 800px"></div>
		<script type="text/javascript">
(function(){
	var chart = echarts.init(document
			.getElementById('emotion_management_chart_shiyingli'));
	var datas = ${emotionManagementSet.dataShiyingli};
	var option = {
		title : {
	        text: '适应力',
	        x:'center'
	    },
	    renderAsImage:true,
	    //color:['#21329d', '#959595'],
	    color:['#21329d', '#959595','#ffcc3e', '#cc99cc','#9bc45a','#ffcc3e',  '#959595', '#9edfff'],
		series : [ {
			type : 'pie',
			radius : '55%',
	        center: ['50%', '50%'],
			data : datas
		} ]
	};
	chart.setOption(option);
})();
</script>
		<p>在适应力维度上，${emotionManagementSet.evaluationShiyingli }</p>


<div id="emotion_management_chart_ganranli"
			style="height: 500px; width: 800px"></div>
		<script type="text/javascript">
(function(){
	var chart = echarts.init(document
			.getElementById('emotion_management_chart_ganranli'));
	var datas = ${emotionManagementSet.dataGanranli};
	var option = {
		title : {
	        text: '感染力',
	        x:'center'
	    },
	    renderAsImage:true,
	    //color:['#21329d', '#959595'],
	    color:['#21329d', '#959595','#ffcc3e', '#cc99cc','#9bc45a','#ffcc3e',  '#959595', '#9edfff'],
		series : [ {
			type : 'pie',
			radius : '55%',
	        center: ['50%', '50%'],
			data : datas
		} ]
	};
	chart.setOption(option);
})();
</script>
		<p>在感染力维度上，${emotionManagementSet.evaluationGanranli }</p>


<div id="emotion_management_chart_wentijiejue"
			style="height: 500px; width: 800px"></div>
		<script type="text/javascript">
(function(){
	var chart = echarts.init(document
			.getElementById('emotion_management_chart_wentijiejue'));
	var datas = ${emotionManagementSet.dataWentijiejue};
	var option = {
		title : {
	        text: '问题解决',
	        x:'center'
	    },
	    renderAsImage:true,
	    //color:['#21329d', '#959595'],
	    color:['#21329d', '#959595','#ffcc3e', '#cc99cc','#9bc45a','#ffcc3e',  '#959595', '#9edfff'],
		series : [ {
			type : 'pie',
			radius : '55%',
	        center: ['50%', '50%'],
			data : datas
		} ]
	};
	chart.setOption(option);
})();
</script>
		<p>在问题解决维度上，${emotionManagementSet.evaluationWentijiejue }</p>



	</div>

	<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${base}/js/bootstrap.min.js"></script>

</body>
</html>
