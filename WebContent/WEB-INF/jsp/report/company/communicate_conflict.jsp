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
		<h1>沟通风格与冲突处理报表</h1>
		<c:import url="/WEB-INF/jsp/report/company/includes/common.jsp"></c:import>
		<h2>（二）沟通风格结果分析</h2>
		<p>此次参加测评的${commonParamSet.validUserCount}位员工中，沟通风格属于表达型的有${communicationParamSet.userCountBiaoda
			}人，占样本的${communicationParamSet.userRatioBiaoda
			}人，属于支配型的有${communicationParamSet.userCountZhipei
			}人，占样本的${communicationParamSet.userRatioZhipei
			}人，属于和蔼型的有${communicationParamSet.userCountHeai
			}人人，占样本的${communicationParamSet.userRatioHeai
			}人，属于分析型的有${communicationParamSet.userCountFenxi
			}人人，占样本的${communicationParamSet.userRatioFenxi }人。</p>
		<p>这一结果显示：该团队总体上${communicationParamSet.topCategory
			}沟通风格的人居多，说明${communicationParamSet.evaluation }</p>

		<div id="communicate_chart" style="height: 500px; width: 800px"></div>
		<script type="text/javascript">
(function(){
	var chart = echarts.init(document
			.getElementById('communicate_chart'));
	var datas = [
			<c:if test="${communicationParamSet.userCountBiaoda>0}">{value:${communicationParamSet.userCountBiaoda}, name:'表达型(${communicationParamSet.userRatioBiaoda})'},</c:if>
			<c:if test="${communicationParamSet.userCountZhipei>0}">{value:${communicationParamSet.userCountZhipei}, name:'支配型(${communicationParamSet.userRatioZhipei})'},</c:if>
			<c:if test="${communicationParamSet.userCountHeai>0}">{value:${communicationParamSet.userCountHeai}, name:'和蔼型(${communicationParamSet.userRatioHeai})'},</c:if>
			<c:if test="${communicationParamSet.userCountFenxi>0}">{value:${communicationParamSet.userCountFenxi}, name:'分析型(${communicationParamSet.userRatioFenxi})'}</c:if>
	             ];
	var legend_data = [
			<c:if test="${communicationParamSet.userCountBiaoda>0}">'表达型(${communicationParamSet.userRatioBiaoda})',</c:if>
			<c:if test="${communicationParamSet.userCountZhipei>0}">'支配型(${communicationParamSet.userRatioZhipei})',</c:if>
			<c:if test="${communicationParamSet.userCountHeai>0}">'和蔼型(${communicationParamSet.userRatioHeai})',</c:if>
			<c:if test="${communicationParamSet.userCountFenxi>0}">'分析型(${communicationParamSet.userRatioFenxi})'</c:if>
	                   ];

	var option = {
		title : {
	        text: '员工沟通风格测评分布状况',
	        x:'center'
	    },
	    renderAsImage:true,
	    color:['#ffcc3e', '#9bc45a','#21329d', '#959595','#dedede', '#9edfff', 'cc99cc'],
	    //color:['#21329d', '#959595'],
	    legend:{
	    	orient : 'vertical',
	        x : 'right',
	        y : 'center',
	        data:legend_data
	    },
	            
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

		<h2>（三）冲突处理方式结果分析</h2>
		<p>此次参加测评的${commonParamSet.validUserCount}位员工中，冲突处理方式属于支配型的有${conflictParamSet.userCountZhipei
			}人，占样本的${conflictParamSet.userRatioZhipei
			}，属于折中型的有${conflictParamSet.userCountZhezhong
			}人，占样本的${conflictParamSet.userRatioZhezhong
			}，属于回避型的有${conflictParamSet.userCountHuibi
			}人，占样本的${conflictParamSet.userRatioHuibi
			}，属于谦让型的有${conflictParamSet.userCountQianrang
			}人，占样本的${conflictParamSet.userRatioQianrang
			}，属于整合型的有${conflictParamSet.userCountZhenghe
			}人，占样本的${conflictParamSet.userRatioZhenghe }。</p>
		<p>这一结果显示：该团队总体上${conflictParamSet.topCategory
			}冲突处理方式的人居多，说明${conflictParamSet.evaluation }</p>
			
<div id="conflict_chart" style="height: 500px; width: 800px"></div>
		<script type="text/javascript">
(function(){
	var chart = echarts.init(document
			.getElementById('conflict_chart'));
	var datas = [
			<c:if test="${conflictParamSet.userCountZhipei>0}">{value:${conflictParamSet.userCountZhipei}, name:'支配型(${conflictParamSet.userRatioZhipei})'},</c:if>
			<c:if test="${conflictParamSet.userCountZhezhong>0}">{value:${conflictParamSet.userCountZhezhong}, name:'折中型(${conflictParamSet.userRatioZhezhong})'},</c:if>
			<c:if test="${conflictParamSet.userCountHuibi>0}">{value:${conflictParamSet.userCountHuibi}, name:'回避型(${conflictParamSet.userRatioHuibi})'},</c:if>
			<c:if test="${conflictParamSet.userCountQianrang>0}">{value:${conflictParamSet.userCountQianrang}, name:'谦让型(${conflictParamSet.userRatioQianrang})'},</c:if>
			<c:if test="${conflictParamSet.userCountZhenghe>0}">{value:${conflictParamSet.userCountZhenghe}, name:'整合型(${conflictParamSet.userRatioZhenghe})'},</c:if>
	             ];
	var legend_data = [
			<c:if test="${conflictParamSet.userCountZhipei>0}">'支配型(${conflictParamSet.userRatioZhipei})',</c:if>
			<c:if test="${conflictParamSet.userCountZhezhong>0}">'折中型(${conflictParamSet.userRatioZhezhong})',</c:if>
			<c:if test="${conflictParamSet.userCountHuibi>0}">'回避型(${conflictParamSet.userRatioHuibi})',</c:if>
			<c:if test="${conflictParamSet.userCountQianrang>0}">'谦让型(${conflictParamSet.userRatioQianrang})',</c:if>
			<c:if test="${conflictParamSet.userCountZhenghe>0}">'整合型(${conflictParamSet.userRatioZhenghe})'</c:if>
	                   ];

	var option = {
		title : {
	        text: '员工冲突处理测评分布状况',
	        x:'center'
	    },
	    renderAsImage:true,
	    color:['#ffcc3e', '#9bc45a','#21329d', '#959595','#dedede', '#9edfff', 'cc99cc'],
	    //color:['#21329d', '#959595'],
	    legend:{
	    	orient : 'vertical',
	        x : 'right',
	        y : 'center',
	        data:legend_data
	    },
	            
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
			
		<h1>二 员工关爱建议</h1>
		<h2>（一） 定期测评，建立团队档案</h2>
		<p>企业创立员工心理档案以及成为企业提升员工心理素质，规范心理建设队伍的一个非常有益的尝试。建立团队成员和团体的测评档案，有利于开展工作以及人事的调配工作。而沟通风格与冲突处理方式的测评结果的存档，进一步丰富了企业的员工心理档案库。通过对档案的定期梳理，企业内部或者外部咨询师能够甄选出问题员工，还能随时掌握员工的心理动态。</p>
		<h2>（二） 组织定期团体工作坊，加强团队合作精神</h2>
		<p>在团体工作坊中，个人要随着团体一起成长的，同伴的目光、自我分享会增加团队成员之间的亲密度，提升员工的之间的情感粘度。如果定期组织团体工作坊，不仅能够看到团体中每个人的身心成长，还能看到整个团体以惊人的速度在自我完善。在这样一种浓厚的感情冲击下，增强团队的士气和战斗力，提高团队成员的归属感，有利于增强团队凝聚力，从而减少人员流动率和流失率。</p>
		<h2>（三） 建立情感宣泄平台</h2>
		<p>宣泄平台，如宣泄室在国外很常见。在宣泄平台中，会配备一些宣泄工具，如宣泄人、宣泄棒等。建立宣泄平台的目的是为有情绪波动、压力大的员工提供一个在安全的、可控的空间，借助器材通过击打、呐喊等方式宣泄负面情绪和集聚的压力，体验宣泄带来的舒畅感觉，从而实现身心放松，提高心理健康水平。有了这样的宣泄品台，员工的情绪水平更容易稳定、压力水平也会得到缓解。</p>
		<h2>（四） 驻场咨询及培训课程建议</h2>
		<h3>1．驻场咨询</h3>
		<p>针对员工开展“一对一”面询，驻场咨询的主要方向为：人际沟通、心灵修行、情绪管理、创造性思维发展等。</p>
		<h3>2．培训课程</h3>
		<p>根据员工测评结果，可开展相关课程：如《接纳自己》《信任自己》《在困境中善待自己》等。</p>
	</div>

	<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${base}/js/bootstrap.min.js"></script>

</body>
</html>
