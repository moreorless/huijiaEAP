<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<p>单位 ：${commonParamSet.companyName}</p>
<p>注册人数 ： ${commonParamSet.registeredUserCount}</p>

<h1>一、心理健康体检数据分析</h1>
<h2>（一）心理健康体检样本构成</h2>

<p>
	本次参加心理健康体检的对象为${commonParamSet.companyName}的部分员工。由于样本量较小，所以本次测评结果仅作为设计后续服务方案的参考依据之一。
	对测谎维度的分析结果显示，${commonParamSet.testedUserCount}名员工的测评数据中有${commonParamSet.liedUserCount}位员工的测谎得分超标，因此这${commonParamSet.liedUserCount}个数据在团体报告时作为无效数据剔除。
	因此，${commonParamSet.companyName}本次测评的最终有效数据为${commonParamSet.validUserCount}名员工，以下的统计分析基于此${commonParamSet.validUserCount}份有效样本的数据之上。
</p>

<h3>1．性别构成</h3>

<p>
	在${commonParamSet.validUserCount}份有效数据中，样本性别分布为：男性有${commonParamSet.genderMaleUserCount}人，约占总人数的${commonParamSet.genderMaleRatio}；女性有${commonParamSet.genderFemaleUserCount}人，约占总人数的${commonParamSet.genderMaleUserCount}。男女性别比例大约为${commonParamSet.genderRatio}。图1显示了有效样本的性别构成比例。
</p>

<div id="common_chart_gender" style="height: 500px; width: 800px"></div>
<script type="text/javascript">
(function(){
	var chart = echarts.init(document
			.getElementById('common_chart_gender'));
	var datas = [<c:forEach var="item" items="${commonParamSet.chartDataGender}" varStatus="status">{value:<c:out value = "${item.value}"/>,name:'<c:out value = "${item.key}"/>'}<c:if test="${!status.last}">,</c:if></c:forEach>];

	var option = {
		title : {
	        text: '员工心理体检数据性别构成',
	        x:'center'
	    },
	    renderAsImage:true,
	    //color:['#ffcc3e', '#9bc45a','#21329d', '#959595','#dedede', '#9edfff', 'cc99cc'],
	    color:['#21329d', '#959595'],
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
<h3>2．年龄构成</h3>
<p>
	${commonParamSet.validUserCount}份有效数据中，样本年龄分布为：30岁以下的有${commonParamSet.age0to30UserCount}人，约占${commonParamSet.age0to30Ratio}；在31-40岁的有${commonParamSet.age31to40UserCount}人，约占${commonParamSet.age31to40Ratio}；在41-50岁的有${commonParamSet.age41to50UserCount}人，约占${commonParamSet.age41to50Ratio}；50岁以上的有${commonParamSet.age51to99UserCount}人，约占${commonParamSet.age51to99Ratio}。相当数量员工处在${commonParamSet.ageMaxName}年龄段。图2显示了有效样本的年龄构成百分比。
</p>
<div id="common_chart_age" style="height: 500px; width: 800px"></div>
<script type="text/javascript">
(function(){
	var chart = echarts.init(document
			.getElementById('common_chart_age'));
	var datas = [<c:forEach var="item" items="${commonParamSet.chartDataAge}" varStatus="status">{value:<c:out value = "${item.value}"/>,name:'<c:out value = "${item.key}"/>'}<c:if test="${!status.last}">,</c:if></c:forEach>];

	var option = {
		title : {
	        text: '员工心理体检数据年龄构成',
	        x:'center'
	    },
	    renderAsImage:true,
	    color:['#21329d', '#959595','#ffcc3e', '#9bc45a','#dedede', '#9edfff', 'cc99cc'],
	    //color:['#21329d', '#959595'],
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

<h3>3．教育程度构成</h3>
<p>
	本次报告中按教育程度划分为：大专及以下学历、本科学历、硕士及以上学历。在${commonParamSet.validUserCount}份有效数据中，样本教育程度分布为：大专及以下学历的员工为${commonParamSet.educationDazhuanUserCount}人，占${commonParamSet.educationDazhuanRatio}；本科学历的员工为${commonParamSet.educationBenkeUserCount}人，占${commonParamSet.educationBenkeRatio}；硕士及以上学历的员工为${commonParamSet.educationShuoshiUserCount}人，占${commonParamSet.educationShuoshiRatio}。本科以上学历的员工占总有效样本的比例为${commonParamSet.educationBenkeShuoshiRatio}。图3显示了有效样本的教育程度构成百分比。
</p>
<div id="common_chart_education" style="height: 500px; width: 800px"></div>
<script type="text/javascript">
(function(){
	var chart = echarts.init(document
			.getElementById('common_chart_education'));
	var datas = [<c:forEach var="item" items="${commonParamSet.chartDataEducation}" varStatus="status">{value:<c:out value = "${item.value}"/>,name:'<c:out value = "${item.key}"/>'}<c:if test="${!status.last}">,</c:if></c:forEach>];

	var option = {
		title : {
	        text: '员工心理体检数据年龄构成',
	        x:'center'
	    },
	    renderAsImage:true,
	    color:['#21329d', '#959595','#ffcc3e', '#9bc45a','#dedede', '#9edfff', 'cc99cc'],
	    //color:['#21329d', '#959595'],
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
<h3>4．工作年限构成</h3>
<p>
	在${commonParamSet.validUserCount}有效数据中，样本工作年限分布为：工作1-3年的员工有${commonParamSet.workage1to3UserCount}人，占${commonParamSet.workage1to3Ratio}；3-5年的有${commonParamSet.workage3to5UserCount}人，占${commonParamSet.workage3to5Ratio}；5-10年的有${commonParamSet.workage5to10UserCount}人，占${commonParamSet.workage5to10Ratio}；10年以上的有${commonParamSet.workage10to99UserCount}人，占${commonParamSet.workage10to99Ratio}。
	可见，此样本中，${commonParamSet.workageComment}。具体有效样本的工作年龄构成百分比如图4所示。</p>
<div id="common_chart_workage" style="height: 500px; width: 800px"></div>
<script type="text/javascript">
(function(){
	var chart = echarts.init(document
			.getElementById('common_chart_workage'));
	var datas = [<c:forEach var="item" items="${commonParamSet.chartDataWorkage}" varStatus="status">{value:<c:out value = "${item.value}"/>,name:'<c:out value = "${item.key}"/>'}<c:if test="${!status.last}">,</c:if></c:forEach>];

	var option = {
		title : {
	        text: '员工心理体检数据年龄构成',
	        x:'center'
	    },
	    renderAsImage:true,
	    color:['#21329d', '#959595','#ffcc3e', '#9bc45a','#dedede', '#9edfff', 'cc99cc'],
	    //color:['#21329d', '#959595'],
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


<h3>5．职位状况构成</h3>
<p>
	在${commonParamSet.validUserCount}有效数据中，职位状况分布为：一般员工为${commonParamSet.jobtitleGeneralUserCount}人，占${commonParamSet.jobtitleGeneralRatio}；中层管理者为${commonParamSet.jobtitleMiddleUserCount}人，占${commonParamSet.jobtitleMiddleRatio}；高层管理者${commonParamSet.jobtitleSeniorUserCount}人，占${commonParamSet.jobtitleSeniorRatio}。
	具体有效样本的职位状况构成百分比如图5所示。</p>
<div id="common_chart_jobtitle" style="height: 500px; width: 800px"></div>
<script type="text/javascript">
(function(){
	var chart = echarts.init(document
			.getElementById('common_chart_jobtitle'));
	var datas = [<c:forEach var="item" items="${commonParamSet.chartDataJobtitle}" varStatus="status">{value:<c:out value = "${item.value}"/>,name:'<c:out value = "${item.key}"/>'}<c:if test="${!status.last}">,</c:if></c:forEach>];

	var option = {
		title : {
	        text: '员工心理体检数据年龄构成',
	        x:'center'
	    },
	    renderAsImage:true,
	    color:['#21329d', '#959595','#ffcc3e', '#9bc45a','#dedede', '#9edfff', 'cc99cc'],
	    //color:['#21329d', '#959595'],
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

<div style="width: 800px; height: 500px; position: relative;display:none">
	<div
		style="width:800px;height: 500px;background-image:url('${base}/images/report/gauss.png');repeat;position:absolute;z-index:1"></div>
	<div id="test_xxx" style="height: 500px; width: 100%;"></div>
</div>
<script type="text/javascript">
	var scores2 = [ 5, 20, 40, 10, 10, 20 ];
	var names2 = [ "衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子" ];
	//var names2 = [ "衬衫", "", "","","","羊毛衫", "-", "-","-","-", "雪纺衫", "-", "-","-","-", "裤子", "-", "-","-","-", "高跟鞋", "-", "-","-","-", "袜子" ];
	var myChart2 = echarts.init(document.getElementById('test_xxx'));
	var option2 = {
		tooltip : {
			show : true
		},
		legend : {
			data : [ '销量', '降水量' ]
		},
		toolbox : {
			show : true,
			feature : {
				mark : {
					show : false
				},

				saveAsImage : {
					show : true
				}
			}
		},
		animation : false,
		xAxis : [ {
			type : 'category',
			data : names2
		} ],
		yAxis : [ {
			type : 'value'
		} ],
		series : [ {
			"name" : "销量",
			"type" : "bar",
			"data" : scores2
		}, {
			name : '降水量',
			type : 'line',
			data : [ 2.6, 5.9, 9.0, 26.4, 28.7, 70.7 ]
		} ]
	};

	// 为echarts对象加载数据 
	myChart2.setOption(option2);
</script>
