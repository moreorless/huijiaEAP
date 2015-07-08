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
		<h1>个人心理分析报表</h1>
		<c:import url="/WEB-INF/jsp/report/company/includes/common.jsp"></c:import>


		<h2>（二）心理健康体检结果分析</h2>
		<h3>1．员工心理健康总体概况</h3>
		<p>
			此次参加测评的${commonParamSet.validUserCount}位员工的整体心理健康得分为${mentalParamSet.wholeScore} ，处于心理健康${mentalParamSet.wholeEvaluation}水平。心理体检结果显示，
			<c:if test="${mentalParamSet.jiaoChaUserCount} >0}"> ${mentalParamSet.jiaoChaUserCount }名员工心理健康水平较差，占有效样本的${mentalParamSet.jiaoChaRatio }，其中最低得分为${mentalParamSet.jiaoChaLowestScore }分；</c:if>
			<c:if test="${mentalParamSet.buLiangUserCount} >0}">${mentalParamSet.buLiangUserCount }人心理健康水平不良，占有效样本的${mentalParamSet.buLiangRatio }；</c:if>
			<c:if test="${mentalParamSet.zhongDengUserCount} >0}">${mentalParamSet.zhongDengUserCount }人心理健康状况处于中等水平，占有效样本的${mentalParamSet.zhongDengRatio }；</c:if>
			<c:if test="${mentalParamSet.jiaoHaoUserCount} >0}">${mentalParamSet.jiaoHaoUserCount }人心理健康水平较好，占有效样本的${mentalParamSet.jiaoHaoRatio }；</c:if>
			<c:if test="${mentalParamSet.henHaoUserCount} >0}">${mentalParamSet.henHaoUserCount }人心理健康水平非常好，占有效样本的${mentalParamSet.henHaoRatio }</c:if>（如图6所示）。
		</p>

		<p>这一结果显示：心理健康状况需要特别引起警惕的员工比例为${mentalParamSet.needJingTiRatio }，心理健康
			水平有待提高的员工为${mentalParamSet.needTiGaoRatio }。</p>

		<p>
			图7所示为本次参与测评的${commonParamSet.validUserCount}位员工的心理健康总分分布图。K-S正态性检验发现，整个分布为标准正态分布，与全国常模保持一致。即：心理健康处于中等程度的人最多，越往两端人数越少。（K-S正态性检验发现，整个分布不是标准正态分布，与全国常模不一致，可能是由于样本量太小导致。）
		</p>

		<h3>2．心理健康各维度结果分析</h3>
		<h4>（1）各维度得分的平均分</h4>

		<p>
			体检结果显示，${commonParamSet.companyName}员工心理健康状况的六个维度——认知维度、情绪维度、意识行为维度、生理症状维度、社会交往维度和自我防御维度的各维度得分都处于中等范围，总体差异程度不大。如图8所示。
		</p>
		<p>
			上图中，白色区域为高分区，灰色区域为低分区，天蓝色区域为中分区，并显示了各维度的高低分临界值。总体上心理健康的六个维度得到了均衡发展，各维度的得分都处于中等及以上的程度。
		</p>
		<h4>（2）各维度得分高低程度的人数比较</h4>
		<p>
			六个维度中，社会支持是支持员工心理健康的主要因素，自我防御是影响员工心理健康水平的主要因素，生理症状是反映员工心理健康状况的重要指标，而积极心态、情绪管理和行为表现则是从“知、情、意”的三个角度全面评价当前员工的心理健康水平。
		</p>
		<p>维度高低分的具体人数如图9所示。</p>
		<p>
			在心理健康的各个因素中，得分处于中等分数的员工占整个团体的多数部分。统计分析结果显示，所选样本员工在6个维度中，低分人数最多的是${mentalParamSet.lowScoreCategories}，而高分人数最多的是${mentalParamSet.highScoreCategories}。如图9所示。<c:if test="${mentalParamSet.percent30CategoryNum} >0}">其中，${mentalParamSet.percent30Categories}的低分人群均超过总有效样本的30%，这说明这${mentalParamSet.percent30CategoryCount}个维度是企业特别需要企业关注的部分。</c:if>
		</p>

		<h1>二 员工关爱建议</h1>
		<h2>（一）定期测评，建立全员心理档案，指导企业管理</h2>
		<h3>1．扩大测评范围，定期心理体检，建立全员心理健康模型</h3>
		<p>
			受不同地域、从事不同工作等因素影响，员工会呈现出不同心理特点。本次测评是对${commonParamSet.companyName}部分员工开展，并不能完全代表${commonParamSet.companyName}整体员工的特点。因此建议${commonParamSet.companyName}扩大测评范围，对全员进行心理健康体检，评估员工心理健康状况，建立全员心理健康模型。并通过心理健康体检查找问题产生的原因，针对性辅导，及时将问题解决于萌芽状态。
		</p>
		<h3>2．建立员工心理档案，指导企业人力资源管理</h3>
		<p>
			${commonParamSet.companyName}可以借助全面的员工心理健康关爱行动的契机，建立员工的心理健康档案，对员工心理健康状况进行跟踪。一方面可以对相关人群进行针对性的测评，考察其发展潜力；同时也可以发现人才，为企业发展建立适宜的人才储备。
		</p>
		<h2>（二）加强传递关爱信息，为员工提供各种形式的心理咨询</h2>
		<p>
			对于受心理问题困扰的员工，通过心理咨询热线、网上咨询、个人面询等形式提供有效的个性化心理辅导服务，使员工更易接受心理咨询，及时解决心理困扰和烦恼，使其能够保持较好的心理状态去生活和工作。
		</p>
		<h2>（三）在组织内建设减压室，全面关爱员工身心健康</h2>
		<p>
			人们的心理健康与身体健康相辅相成。企业可以从身心两个方面关爱员工的健康问题。在企业内建设减压室，借助一定的物理放松设备——生物反馈放松仪器、催眠仪、芳香图书馆、负离子氧吧，宣泄室等，帮助员工有效放松身心，提高心理健康水平。
		</p>
		<h2>（四）完善社会支持系统，全面关注员工未来发展</h2>
		<p>人的发展不只是职业的发展，也包括生活、成长的各个方面。建议企业从员工的这些发展需求出发，帮助员工完善社会支持系统，比如：</p>
		<p>一是关爱员工家庭生活，解决后顾之忧；</p>
		<p>二是关注员工亲子教育，提供相应服务；</p>
		<p>三是做好职业生涯规划，帮助员工成长。</p>
		<h2>（五）提供培训学习机会，辅助员工个人成长</h2>
		<p>根据员工所关注的培训课程，建议XX组织以下培训：</p>
		<p>一是沟通与人际协作方面的培训课程；</p>
		<p>二是了解自我与潜能开发方面的培训；</p>
		<p>三是职场适应与规则的培训课程；</p>
		<p>四是亲子教育方面的培训课程；</p>
		<p>五是婚姻情感方面的培训课程。</p>
		<h2>（六）针对基层管理者加强其管理能力训练</h2>
		<p>建议${commonParamSet.companyName}针对基层管理者加强基本管理技能培训，创造良好的工作环境：</p>
		<h3>1．对员工及时、具体的鼓励</h3>
		<p>
			当发现员工工作中做出了一些成绩或者取得进步时要给予及时、具体的表扬。作为一名基层管理者，应能做到每周对每个员工都能发现他们的闪光点，并进行表扬。可以表扬的员工的地方包括：当员工做了本职工作以外的工作时，当员工完成工作的质量超出了该项工作规定的标准时，当员工发表了一些有价值的想法或观点时等。
		</p>
		<h3>2．营造员工成长空间，使员工在工作中发挥所长</h3>
		<p>
			当一个人做自己擅长的事情时，会提升投入程度，并且发挥应有的绩效水平。让员工在工作中发挥自身所长，这需要管理者首先了解员工擅长的事情，通过工作规划使员工发挥自己的专长，在工作中获得自我效能感。建议做以下方面的工作：</p>
		<p>一是了解每个员工的业余爱好，公司针对多数员工的兴趣组织各类兴趣小组活动；</p>
		<p>二是对员工做MBTI测评，了解员工的职业性格特点，使主管了解员工职业性格中的沟通模式以潜在的优势，为沟通铺垫基础；</p>
		<p>三是进行系统的工作规划，使员工的工作与自己的优势和爱好相匹配。</p>
		<h3>3．加强关注员工的发展与进步</h3>
		<p>作为管理者，要加强对员工进步的关注，及时地与员工沟通其取得的进步，能够很大程度上鼓励其信心，使其更加专注、投入地工作。</p>
		<h3>4. 提升员工心理危机识别能力</h3>
		<p>
			在调研过程中，不乏出现心理健康状态较差的员工，表现为情绪低落，周期较长，对生活缺乏信心，自我认同感较差等等症状。这些症状不同程度的影响到员工工作，因此加强管理者对员工心理危机识别的训练也是必不可少的，帮助管理有策略地处理危机状况，在意外事件之后能确保员工及其家属得到及时援助，处理创伤后应激症状。同时降低危机事件对公司及其员工的影响。</p>
		<h2>（七）驻场咨询及培训课程建议</h2>
		<h3>1．驻场咨询</h3>
		<p>“一对一”面询的主要方向为：</p>
		<p>人际互动中的沟通问题、自我成长、职业生涯规划、亲子关系咨询、两性情感咨询。</p>
		<h3>2．培训课程</h3>
		<p>综合管理层、员工层对EAP的期待及课程需求，全年4次课程设置主题依次为：</p>
		<p>《沟通，人际互动的核心影响力》</p>
		<p>《做一棵枝叶繁茂的树—管理者心理资本提升》</p>
		<p>《从容跨越—自我情绪管理》</p>
		<p>《我爱我家—团队建设》</p>
	</div>
	<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${base}/js/bootstrap.min.js"></script>

</body>
</html>
