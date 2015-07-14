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
<style type="text/css">
body {
	overflow: auto
}
</style>
</head>
<body>
	<div class="container">
		<h1>企业员工调查问卷报表</h1>
		<!-- <c:import url="/WEB-INF/jsp/report/company/includes/common.jsp"></c:import> -->

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
		<h2>3.2 优势劣势问题</h2>
		<h2>3.3 满意度整体分析</h2>
		<h2>3.4 员工忠诚度分析</h2>
		<h2>3.5满意度、忠诚度综合分析</h2>

		<h1>一 调查简介</h1>
		<h2>1.1 调查目的</h2>
		<p>${commonParamSet.companyName
			}与会佳心语顾问一起合作的“企业员工调研”项目，将从全方位了解企业员工对公司的满意度状况以及员工的敬业度状况。可以为企业带来以下几方面的价值：首先，帮助企业了解目前员工的状况；第二，找出目前在本企业中影响员工满意度和敬业度的核心因素；第三，探索企业激发员工工作热情的有效方法。</p>
		<h2>1.2 调查方法</h2>
		<p>本次调研项目，采用的是问卷调查法。在问卷回收后，通过专业的统计分析方法对问卷进行综合分析，并与市场中最佳雇主的员工满意度指标进行对比，从而发现促进公司提升的解决方案。</p>
		<h2>1.3 调研群体</h2>
		<p>本次参加调研项目的群体是企业的${commonParamSet.testedUserCount }名员工。</p>

		<h1>二 调查问卷的组成</h1>
		<p>该调查问卷共由22道题组成，共涉及管理、工作职责、环境、薪酬和职业发展五大因素，企业文化、同事关系、制度流程等17个二级因素。问卷的题目包括，我很愿意介绍别人到我们公司工作，公司的福利总让我觉得很暖心，等等。</p>
		<p>图1 调查问卷的五大因素</p>
		<p>
			在上述因素中，<strong>职业发展</strong>因素是指员工在企业提供服务的同时，也会在工作中不断地提高自己的<strong>能力</strong>、在职业发展中有升职的<strong>机会</strong>，以及在公司中对自己的职业<strong>前景</strong>有一个很好的预期。<strong>管理</strong>因素则包括了企业的制度流程、对员工的<strong>培养、认可、信任</strong>等方面。<strong>工作职责</strong>是企业人力资源部门管理员工的基本，跟员工的福利和薪酬是挂钩的，只有员工<strong>明确</strong>自己的工作职能，并能在工作中发现自己的<strong>工作意义</strong>，才能更有效地工作，为企业服务。<strong>薪酬福利</strong>是员工在企业创造价值所获得的相应回报的重要体现，共包括<strong>薪酬、福利、绩效</strong>三方面。<strong>环境因素</strong>是企业为员工提供的物理环境（<strong>工作场所</strong>）、企业文化环境、解决问题时可用<strong>资源</strong>和人际环境（<strong>同事关系</strong>）的总和，员工只有在自己满意的环境中工作，才能发挥自己最优水平，创造更高价值。
		</p>

		<h1>三 调查结果整体分析</h1>
		<h2>3.1 统计结果概述</h2>
		<p>本次调查参加人数为${commonParamSet.segmentUserCount
			}，共收回${commonParamSet.validUserCount
			}份有效问卷。问卷回收率为${commonParamSet.validUserRatio }（此处百分比为收回问卷数量÷参加人数）。</p>
		<h2>3.2 优势劣势问题</h2>
		<p>问卷中的各个题目的得分可以在某种程度上反应企业管理中存在的优势与问题。所以，将问卷中得分高和得分低的题目收集汇总，通过分析，企业能够初步了解目前管理中存在的问题。</p>
		<p>（1）满意度最高的3道题（选得分最高的前三名）</p>
		<table class="table table-bordered table-striped" style="width: 50%">
			<tr>
				<th>题目</th>
				<th style="width: 50%">因素</th>
			</tr>
			<tr>
				<td>${employeeParamSet.top1QuestionIds }</td>
				<td>${employeeParamSet.top1QuestionFactors}</td>
			</tr>
			<tr>
				<td>${employeeParamSet.top2QuestionIds}</td>
				<td>${employeeParamSet.top2QuestionFactors}</td>
			</tr>
			<tr>
				<td>${employeeParamSet.top3QuestionIds}</td>
				<td>${employeeParamSet.top3QuestionFactors}</td>
			</tr>
		</table>
		<p>从以上得分最高的几个问题中，我们可以看出，在企业整体的管理运营中，${employeeParamSet.topEvaluation}。</p>
		<p>（2）满意度最低的3道题（选得分最高的前三名）</p>
		<table class="table table table-bordered table-striped"
			style="width: 50%">
			<tr>
				<th>题目</th>
				<th style="width: 50%">因素</th>
			</tr>
			<tr>
				<td>${employeeParamSet.low1QuestionIds}</td>
				<td>${employeeParamSet.low1QuestionFactors}</td>
			</tr>
			<tr>
				<td>${employeeParamSet.low2QuestionIds}</td>
				<td>${employeeParamSet.low2QuestionFactors}</td>
			</tr>
			<tr>
				<td>${employeeParamSet.low3QuestionIds}</td>
				<td>${employeeParamSet.low3QuestionFactors}</td>
			</tr>
		</table>
		<h2>3.3 满意度整体分析</h2>
		<p>（1）整体选择频率分析</p>
		<p>图2 满意度整体选择频率分析图（注：下图中最佳雇主数据为均值，非极端值）</p>
		<table class="table table-bordered table-striped" style="width: 80%">
			<tr>
				<th></th>
				<th>非常不同意</th>
				<th>不同意</th>
				<th>比较不同意</th>
				<th>非常同意</th>
				<th>同意</th>
				<th>非常同意</th>
			</tr>
			<tr>
				<td>${commonParamSet.companyName}</td>
				<td>${employeeParamSet.satisfactionRank6Ratio }</td>
				<td>${employeeParamSet.satisfactionRank5Ratio }</td>
				<td>${employeeParamSet.satisfactionRank4Ratio }</td>
				<td>${employeeParamSet.satisfactionRank3Ratio }</td>
				<td>${employeeParamSet.satisfactionRank2Ratio }</td>
				<td>${employeeParamSet.satisfactionRank1Ratio }</td>
			</tr>
			<tr>
				<td>最佳雇主</td>
				<td>2.3%</td>
				<td>1.8%</td>
				<td>3.6%</td>
				<td>12.9%</td>
				<td>45.2%</td>
				<td>34.1%</td>
			</tr>
		</table>
		<p>上图中，蓝色线条为商业报告中最佳雇主的数据。从上图可以看出，在满意度上，满意度非常高的员工比例比最佳雇主企业员工比例${employeeParamSet.satisfactionRank1Evaluation}；满意度比较高的员工比例比最佳雇主企业员工比例${employeeParamSet.satisfactionRank2Evaluation}；满意度极低的的员工比例比最佳雇主企业员工比例${employeeParamSet.satisfactionRank6Evaluation}。</p>
		<p>（2）整体满意度分析</p>
		<p>图3 整体满意度维度分析图</p>
		<p>从上图可以看出，${employeeParamSet.wholeSatisfactionEvaluation}。</p>
		<p>（3）子维度分析</p>
		<p>图4 满意度子维度分析图</p>
		<p>在五大因素下，共细分了17个二级因素，从细分的因素来看，${employeeParamSet.subSatisfactionEvaluation}</p>
		<h2>3.4 员工忠诚度分析</h2>
		<p>员工的忠诚度划分为两个层次：（1）表现为赞扬：员工会乐于向亲朋好友夸奖和赞扬自己的企业，此层次反应的是员工对于企业的整体喜爱程度。（2）表现为留任：会留在企业中继续效力，此层次反应的是员工对于继续在企业中工作的意向。</p>
		<p>（1）整体忠诚度</p>
		<p>参加此次调查的员工共${commonParamSet.testedUserCount
			}人，其中，${employeeParamSet.wholeLoyaltyPraiseRatio}的人会表现出赞扬；${employeeParamSet.wholeLoyaltyRemainRatio}的员工正在考虑继续留任；${employeeParamSet.wholeLoyaltyNoPraiseRatio}的员工很少或者根本没有在外赞扬过公司；${employeeParamSet.wholeLoyaltyNoRemainRatio}的员工很少或者根本没有考虑过继续留任。</p>
		<p>
			其中，有极高离职倾向的人有${employeeParamSet.wholeLoyaltyLeaveCount}个，分别为工号&nbsp;&nbsp;&nbsp;&nbsp;
			<c:forEach items="${employeeParamSet.wholeLoyaltyLeaveUserList}"
				var="username">
				<c:out value="${username}"></c:out>&nbsp;&nbsp;&nbsp;&nbsp;
			</c:forEach>
		</p>
		<table class="table table table-bordered table-striped"
			style="width: 50%">
			<tr>
				<th style="width: 30%">表现</th>
				<th style="width: 35%">经常、很多</th>
				<th style="width: 35%">很少、没有</th>
			</tr>
			<tr>
				<td>赞扬</td>
				<td>${employeeParamSet.wholeLoyaltyPraiseRatio}</td>
				<td>${employeeParamSet.wholeLoyaltyNoPraiseRatio}</td>
			</tr>
			<tr>
				<td>留任</td>
				<td>${employeeParamSet.wholeLoyaltyRemainRatio}</td>
				<td>${employeeParamSet.wholeLoyaltyNoRemainRatio}</td>
			</tr>
		</table>
		<p>图5 整体忠诚度分析</p>
		<p>${employeeParamSet.wholeLoyaltyEvaluation}</p>
		<p>（2）分类分析</p>
		<p>图6 员工忠诚度分类分析</p>
		<table class="table table table-bordered table-striped"
			style="width: 50%">
			<tr>
				<th style="width: 30%">表现</th>
				<th style="width: 35%">整体满意度</th>
				<th style="width: 35%">平均满意度</th>
			</tr>
			<tr>
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
		</table>
		<p>上图中，红色虚线代表的两类员工的整体满意度得分，蓝色方框表示的两类员工子在五大因素上的平均满意度得分情况。</p>
		<p>${employeeParamSet.classifyLoyaltyEvaluation}</p>
		<p>（3）员工忠诚度驱力因素分析</p>
		<p>图7 员工忠诚度驱力因素分析</p>
		<table class="table table table-bordered table-striped"
			style="width: 80%">
			<tr>
				<th style="width: 20%"></th>
				<th style="width: 16%">管理</th>
				<th style="width: 16%">工作职责</th>
				<th style="width: 16%">环境</th>
				<th style="width: 16%">薪酬福利</th>
				<th style="width: 16%">职业发展</th>
			</tr>
			<tr>
				<td>对于表现出留任员工的重要程度</td>
				<td>${employeeParamSet.drivingLoyaltyRemainRatioGuanli}</td>
				<td>${employeeParamSet.drivingLoyaltyRemainRatioGongzuozhize}</td>
				<td>${employeeParamSet.drivingLoyaltyRemainRatioHuanjing}</td>
				<td>${employeeParamSet.drivingLoyaltyRemainRatioXinchoufuli}</td>
				<td>${employeeParamSet.drivingLoyaltyRemainRatioZhiyefazhan}</td>
			</tr>
			<tr>
				<td>对于表现出表扬员工的重要程度</td>
				<td>${employeeParamSet.drivingLoyaltyPraiseRatioGuanli}</td>
				<td>${employeeParamSet.drivingLoyaltyPraiseRatioGongzuozhize}</td>
				<td>${employeeParamSet.drivingLoyaltyPraiseRatioHuanjing}</td>
				<td>${employeeParamSet.drivingLoyaltyPraiseRatioXinchoufuli}</td>
				<td>${employeeParamSet.drivingLoyaltyPraiseRatioZhiyefazhan}</td>
			</tr>
		</table>
		<p>从上图可以看出，${employeeParamSet.drivingLoyaltyEvaluation}</p>
		<p>（4）员工忠诚度驱力因素细分</p>
		<p>图8 员工忠诚度驱力因素细分</p>
		<table class="table table table-bordered table-striped"
			style="width: 50%">
			<tr>
				<th style="width: 40%"></th>
				<th style="width: 20%">表扬</th>
				<th style="width: 20%">留任</th>
				<th style="width: 20%">差值</th>
			</tr>
			<c:forEach var="datalist"
				items="${employeeParamSet.detailDrivingLoyaltyRatioList}">
				<tr>
					<c:forTokens items="${datalist }" delims="," var="name">
						<td>${name}</td>
					</c:forTokens>
				</tr>
			</c:forEach>
		</table>
		<p>在五大因素中共划分出17个二级因素。将员工的忠诚度从这17个细分二级因素中一一划分开，便于更加准确地找到企业管理中改进的方向。</p>
		<p>从上图可以看出，${employeeParamSet.detailDrivingLoyaltyEvaluation}</p>
		<h2>3.5 满意度、忠诚度综合分析</h2>
		<p>图9 满意度、忠诚度综合分析</p>
		<table class="table table table-bordered table-striped"
			style="width: 50%">
			<tr>
				<th style="width: 30%"></th>
				<th style="width: 20%">满意度得分</th>
				<th style="width: 20%">忠诚度驱动力得分</th>
				<th style="width: 30%">所属区域</th>
			</tr>
			<c:forEach var="datalist"
				items="${employeeParamSet.compositeAnalysisAreaScoreList}">
				<tr>
					<c:forTokens items="${datalist }" delims="," var="name">
						<td>${name}</td>
					</c:forTokens>
				</tr>
			</c:forEach>
		</table>
		<p>满意度平均得分为${employeeParamSet.compositeAnalysisSatisfactionAverageScore},忠诚度平均得分为${employeeParamSet.compositeAnalysisLoyaltyAverageScore}。</p>
		<p>根据员工满意度与忠诚度情况，可以将五大因素中的17个二级因素分为四大区域，分别为：</p>
		<p>
			优势区域：员工满意度较高，不过忠诚度驱力较低的区域。落入该区域的因素有
			<c:forEach var="name"
				items="${employeeParamSet.compositeAnalysisCategoryNameListYoushi}">
				${name }，
				</c:forEach>
			这表明，这部分已经做得不错，员工满意度较高，但是，因为这些二级因素对员工的忠诚度（特别是离职率）影响较小，所以属于优势区域，而非强势区域。
		</p>
		<p>
			强势区域：员工满意度较高，忠诚度驱力也较高的区域。落入该区域的因素有
			<c:forEach var="name"
				items="${employeeParamSet.compositeAnalysisCategoryNameListQiangshi}">
				${name }，
				</c:forEach>
			这表明，这些二级因素属于企业的强势地带，因为这些因素不仅满意度高，而且对员工的忠诚度（特别是离职率）影响力较大。
		</p>
		<p>
			忍痛区域：员工满意度较低，忠诚度驱力也较低的区域。落入该区域的因素有
			<c:forEach var="name"
				items="${employeeParamSet.compositeAnalysisCategoryNameListRentong}">
				${name }，
				</c:forEach>
			这表明，这些二级因素虽然满意度较低，但是其对员工的忠诚度（特别是离职率）影响较小，所以需要暂时忍痛，等改进区域因素调整得当后，再来改善的因素。
		</p>
		<p>
			改进区域：员工满意度较低，不过忠诚度驱力较高的区域。落入该区域的因素有
			<c:forEach var="name"
				items="${employeeParamSet.compositeAnalysisCategoryNameListGaijin}">
				${name }，
				</c:forEach>
			这表明，这些二级因素应该是我们将要重点改进的区域。因为这部分二级因素对员工的忠诚度（特别是离职率）影响力较大，一旦改进，可以大大降低员工的离职率，提高员工留任的概率。
		</p>

	</div>

	<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${base}/js/bootstrap.min.js"></script>

</body>
</html>
