<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><fmt:message key="index.product.name" /></title>
    <link type="text/css" rel="stylesheet" href="${base}/css/quiz/person.css" />
    <style type="text/css">
        .chart{width: 600px; height: 400px; margin: 0 auto}
        .progress {height: 50px; width: 500px;}
    </style>
    
</head>
<body>
    <c:import url="/WEB-INF/jsp/report/person/includes/cover.jsp"></c:import>
    <c:import url="/WEB-INF/jsp/report/person/includes/preface.jsp"></c:import>
    
    <div class="pdf-page" id="page1">
        <div class="pdf-header"><img src="${base}/images/report/logo.png" width="100px" ></img></div>
        <div class="pdf-body">
            <h1>Ⅰ 心理健康的概念</h1>
            <p>心理健康是指个体在适应环境的过程中，生理、心理和社会性方面达到协调一致，保持一种持续的、积极的心理功能状态，能良好的适应环境，充分发挥其身心的潜能，较好妥善处理和适应人与人之间、人与社会环境之间的相互关系。</p>
            <p>一般心理健康包括智力正常、情绪健康、意志健全、行为协调、人际关系适应、反应适度、心理特点符合年龄等七个方面。</p>
            <p>心理健康在人的健康概念中占据重要的位置，它能直接影响和制约人的身体健康，同时也直接影响着其社会适应能力。人的心理健康水平大致可以分为三个等级：</p>
            <p>一是一般常态心理，表现为心情经常愉快、适应能力强、善于与别人相处、能较好地完成同龄人发展水平应做的活动、具有调节控制自己情绪的能力；</p>
            <p>二是轻度失调心理，表现出不具有同龄人所应有的愉快、和他人相处略感困难、生活自理有些吃力，若主动调节或通过专业人员帮助，可恢复常态；</p>
            <p>三是严重病态心理，表现为严重的适应失调，不能维持正常的生活和工作，如不及时治疗可能进一步发展成为精神病患者。</p>

            <h1>Ⅱ  心理健康的标准</h1>
            <h2>(一)  国外心理健康标准</h2>
            <p>对于心理健康的定义，英格里士认为：“心理健康是指一种持续的心理情况，当事者在那种情况下能作良好适应，具有生命的活力，而能充分发展其身心的潜能；这乃是一种积极的丰富情况。不仅是免于心理疾病而已”。麦灵格尔认为：“心理健康是指人们对于环境及相互间具有最高效率及快乐的适应情况。不仅是要有效率，也不仅是要能有满足之感，或是能愉快地接受生活的规范，而是需要三者具备。心理健康的人应能保持平静的情绪，敏锐的智能，适于社会环境的行为和愉快的气质。”</p>

            <h2>(二) 国内心理健康标准</h2>
            <p>我国学者季浏认为心理健康至少应包括4个方面：</p>
            <p>①智力正常；</p>
            <p>②适当的情绪控制能力；</p>
            <p>③对自己能作出恰当的评价；</p>
            <p>④能保持良好的人际关系。</p>
            <p>他更注重心理健康中的情感因素，这与当今流行的“情商”理论是契合的。</p>
            <p>北京大学王登峰、张伯源在综合了以上观点后，提出心理健康的8条标准更为全面：</p>
            <p>①了解自我，悦纳自我；</p>
            <p>②接受他人，善与人处；</p>
            <p>③正视现实，接受现实；</p>
            <p>④热爱生活，乐于工作；</p>
            <p>⑤能协调与控制情绪，心境良好；</p>
            

        </div>
        <div class="pdf-footer"></div>
    </div>

    <div class="pdf-page" id="page2">
        <div class="pdf-header"><img src="${base}/images/report/logo.png" width="100px" ></img></div>
        <div class="pdf-body">
            <p>⑥人格完整和谐；</p>
            <p>⑦智力正常，智商在80以上；</p>
            <p>⑧心理行为符合年龄特征。</p>
            <p>这8条标准，更多地考虑了心理健康的个性（人格）因素，提出了完整和谐的人格，以及心理健康的年龄特征问题。</p>
            <p>此外，台湾心理学家张春兴认为，一个心理健康的人，应能符合下列条件：</p>
            <p>①情绪较稳定，无长期焦虑，少心理冲突；</p>
            <p>②乐于工作，能在工作中表现自己的能力；</p>
            <p>③能与他人建立和谐的关系，而且乐于和他人交往；</p>
            <p>④对于自己有适当的了解，并且有自我悦纳的态度；</p>
            <p>⑤对于生活的环境有适切的认识，能切实有效地面对问题、解决问题，而不逃避。他特别强调了心理健康者应具备对生活、工作及自己的积极态度。</p>

            <h1>Ⅲ  影响心理健康的因素</h1>
            <h2>(一) 个人因素</h2>
            <p>个人因素对个人的心理健康发展有着重要的影响，主要包括先天遗传因素、身体状况、个人经历、性格、能力、情绪等。</p>
            <h3>1 先天因素</h3>
            <p>先天遗传因素在很大程度上决定一个人的生物学特性，同时对人的心理健康有着直接的影响。一个人的气质、能力、性格都要受到遗传因素的影响。 </p>
            <p>从孩子们的身上，我们可以看到其父母的影子，同时他们还有一些相似之处。即使分开生活的双胞胎，他们在不同的环境下成长，也会有相似的地方。随着现代科学技术的发展，有的遗传性疾病还是难以避免，但是我们要尽量避免人为造成的遗传疾病。遗传性疾病会导致生下来的孩子语言、思维能力差，对环境的适应能力很差，生理和心理都不健康，因此优生优育是心理健康的基础。</p>

            <h3>2 身体状况</h3>
            <p>健康的身体是心理健康的前提。营养不良或是营养过剩都会影响大脑的发育，可以导致智力发育迟缓，不适应社会环境，影响心理健康。身体缺陷会导致人不能正常地发挥机体功能，比常人敏感，对社会环境的变化很难适应，并且有自卑等不健康心理。同时疾病也会导致心理健康受到影响，疾病会导致生理功能受到损害，使承受压力的能力减弱，从而影响个体的心理健康。例如，患有艾滋病的患者，他们不仅要承受病痛的折磨，还要遭到社会上一些人的歧视，他们的心理就会出现问题。有的可能产生报复心理，有的产生自暴自弃的想法，严重影响他们的心理健康。 </p>

            <h3>3 个人经历</h3>
            <p>个人经历对个体的心理健康有着重要的影响，尤其是早期的个人经历对今后的心理成长有着决定性的作用。</p>
            <p>研究表明，早期在单调、贫乏的环境中生长的孩子，心理的成长往往会出现障碍，长大后会出现冷漠、冲动的性格，而且行为往往偏激。孩子在早期一般都以父母作为模仿的对象，如果父母身上有不良的行为和缺陷，孩子就会模仿并内化，导致出现不适应社会的行为。而从小受到长辈和父母溺爱的孩子，独立性比一般孩子要差，往往会依赖别人，自己没有独立处理问题的能力。</p>

        </div>
        <div class="pdf-footer"></div>
    </div>
    
    <div class="pdf-page">
        <div class="pdf-header"><img src="${base}/images/report/logo.png" width="100px" ></img></div>
        <div class="pdf-body">
            <h3>4 性格能力</h3>
            <p>一个人的性格可以决定一生的命运，是因为性格是人格中最重要的核心，它可以决定一个人的心理特点及处理问题的态度和能力。内向的人不能很快地适应环境的变化；冲动型的性格很难控制自己的行为；活泼型的很难集中注意力来长时间专注到一个问题上。这些性格都有自己的优势和劣势，如果碰到不适应的环境，可以导致心理失调，出现障碍。</p>

            <h3>5 情绪影响</h3>
            <p>积极或消极的情绪都会影响心理健康，只有适度的情绪有利于心理健康。适度的愉悦可以减少心理的压力和紧张感，使人放松，身心和谐。过度的兴奋和高兴可能会导致神经系统出现障碍，严重的可能导致精神性疾病。</p>

            <h2>(二) 社会因素</h2>
            <h3>1 生活环境因素</h3>
            <p>生活中的物质条件恶劣，生活习惯不当如摄取烟、酒、食物的过量等，都会影响和损害身心健康。其次，不良的工作环境、劳动时间过长、工作不胜任、工作单调以及居住条件、经济收入差等，都会使人产生焦虑、烦躁、愤怒、失望等紧张心理状态从而影响人的心理健康。此外，生活环境的巨大变迁也会使个体产生心理应激，由此带来心理的不适。</p>
            
            <h3>2 重大生活事件与突变因素</h3>
            <p>生活中遇到的各种各样的变化尤其是一些突然变化的事件，常常是导致心理失常或精神疾病的原因，比如家人死亡、失恋、离婚、天灾、疾病等。由于个体每经历一次生活事件，都会给其带来压力，都要付出精力去调整、适应，因此，如果在一段时间内发生的不幸事件太多或事件较严重、突然，个体的身心健康就很容易受到影响。</p>
            <h3>3 文化教育因素</h3>
            <p>教育因素包含家庭教育和学校教育。对个人心理发展而言，早期教育和家庭环境是影响心理健康的重要因素之一。研究表明，个体早期环境如果单调、贫乏，其心理发展将会受到阻碍，并会抑制其潜能的发展，而受到良好照顾，接受丰富刺激的个体则可能在成年后成为佼佼者。另外，儿童与父母的关系，父母的教养态度、方式，家庭的类型等也会对个体以后的心理健康产生影响。早期与父母建立和保持良好关系，得到充分父母爱，受到支持、鼓励的儿童，容易获得安全感和信任感，并对成年后的人格良好发展、人际交往、社会适应等方面有着积极的促进作用。</p>

            <h1>Ⅳ  心理健康体检测评说明</h1>
            <p>心理疾病具有生理疾病所常有的“潜伏期”，如果能够通过心理测评等手段及早发现，及时寻求专业的心理咨询师、心理医生适当疏导或对症诊治，就可以收到良好的预防效果，避免心理问题演变成严重的心理疾病。因此，关注心理健康就必须建立心理预防机制，通过一些专业的心理学方法在早期发现问题，从而有利于采取有效的干预措施。</p>
            <p>本心理健康体检就像一支“心灵温度计”一样，帮您为自己的心理“测温”，及时把握自我的心理健康状况。心理健康体检测评，是一份测查企业员工心理健康状况的自评问卷。其目的是通过心理健康的测查，对目前心理健康目前状态做一个衡量，以便及早发现并及早敢于，避免更加严重的问题发生。</p>
            <p>该心理健康体检问卷共54题，是会佳心语研究院研发的一套测查心理健康状况的专业测评工具，具体条目大部分为自主研发，少部分根据需要取自国内和国外的权威量表。</p>
            <p>本套测评系统主要测查个人在心理健康六个维度上的表现状况，分别是：积极心态维度、情绪管理维度、行为表现维度、生理症状维度、社会支持维度和自我防御维度，每个维度通过多道题目考察，采取李克特五点量表计分。每个人可以得到一个心理健康总分，此分数范围在48-240分之间，根据常模，可以判断一个人的心理健康总分处于“较差、不良、中等、较好、非常好”五个等级；而在每个人心理健康的6个维度上，根据常模标准不同，可判断其分别处于“较低、中等、较高”三个级别。</p>
            

        </div>
        <div class="pdf-footer"></div>
    </div>
    <div class="pdf-page">
        <div class="pdf-header"><img src="${base}/images/report/logo.png" width="100px" ></img></div>
        <div class="pdf-body">
            <p>此心理健康体检测评的内部一致性信度a=0.89，分半信度r=0.85；与另一心理健康体检测评的效标关联效度R=0.74。除此之外，该问卷设有6个测谎题目，以考察测试者做测评的认真程度，如测谎分超过标准，则其问卷作废，以此保证测试结果的真实有效。由此可见，该心理健康体检测评是一个专业的、高效的心理测评工具。</p>
            <p>该心理健康体检测评的适用对象为16岁以上的成人（具有初中以上文化程度的人群），尤其适用于职场中的各类人群，可以帮助个人及时了解自己的心理健康状况，并详细分析发现可能存在的心理问题及其表现。</p>
    
            <h1>Ⅴ  您的心理健康总体状况详解</h1>
            <h2>(一) 作答有效性分析</h2>  
            <p>我们根据您此次测评花费的时间，以及您在测谎题目上的分数来评价您的作答有效性。</p>

            <p>测谎总分：您在测谎题目上的得分为${quizResult.lieScore}分，您的测谎分数
                <c:if test="quizResult.valid == 1">介于正常范围，表明您的作答比较真实可靠。</c:if>
                <c:if test="quizResult.valid == 0">超出正常范围。</c:if></p>

            <p>上述指标说明，您此次回答的测评是真实、有效的。因此，您此次测评的结果也能够比较真实有效地反映您的真实状况，请仔细阅读下列报告。</p>


            <h2>(二) 心理健康总分</h2>
            <p>你的心理健康总分为${quizResult.score}分，该得分说明此次测试所显示出来的健康总体状态是“${total_evaluation.healthStatus}”。</p>
            <p>下图所示的心理健康雷达图可以用来反映您现在的心理健康状态。心理健康雷达图是按照您在每个维度上的得分画出来的。</p>
            <div class="chart" id="radar-chart"></div>
            <p>上述得分表明，${total_evaluation.evaluation}</p>

            <p>正如上图所示，${total_evaluation.suggestion}</p>
        </div>
        <div class="pdf-footer"></div>
    </div>

     <div class="pdf-page">
        <div class="pdf-header"><img src="${base}/images/report/logo.png" width="100px" ></img></div>
        <div class="pdf-body">
            <h1>Ⅵ  您的心理健康各维度状况详解</h1>
            <h2>(一) ${categorieNames[0]}维度</h2>
            <p>在${categorieNames[0]}维度上，您的得分是${scoreArray[0]}分。这个分值所表达的意思是，您在心理健康的${categorieNames[0]}维度上的水平“${evaluationList[0].healthStatus}”。下图的“${categorieNames[0]}剖面图”形象地表示了您在该维度上的状况。</p>
            <p>您的“${categorieNames[0]}维度”的得分如下图所示：</p>
            <div class="progress" data="${scoreArray[0]}"></div>
            <h3>1 结果说明</h3>
            <p>您在“${categorieNames[0]}维度”上的得分“${evaluationList[0].healthStatus}”，这表明：${evaluationList[0].evaluation}</p>

            <h3>2 建议</h3>
            <p>${evaluationList[0].suggestion}</p>

            <h2>(二) ${categorieNames[1]}维度</h2>
            <p>在${categorieNames[1]}维度上，您的得分是${scoreArray[1]}分。这个分值所表达的意思是，您在心理健康的${categorieNames[1]}维度上的水平“${evaluationList[1].healthStatus}”。下图的“${categorieNames[1]}剖面图”形象地表示了您在该维度上的状况。</p>
            <p>您的“${categorieNames[1]}维度”的得分如下图所示：</p>
            <div class="progress" data="${scoreArray[1]}"></div>
            <h3>1 结果说明</h3>
            <p>您在“${categorieNames[1]}维度”上的得分“${evaluationList[1].healthStatus}”，这表明：${evaluationList[1].evaluation}</p>

            <h3>2 建议</h3>
            <p>${evaluationList[1].suggestion}</p>

            
            
        </div>
        <div class="pdf-footer"></div>
    </div>

    <div class="pdf-page">
        <div class="pdf-header"><img src="${base}/images/report/logo.png" width="100px" ></img></div>
        <div class="pdf-body">
            <h2>(三) ${categorieNames[2]}维度</h2>
            <p>在${categorieNames[2]}维度上，您的得分是${scoreArray[2]}分。这个分值所表达的意思是，您在心理健康的${categorieNames[2]}维度上的水平“${evaluationList[2].healthStatus}”。下图的“${categorieNames[2]}剖面图”形象地表示了您在该维度上的状况。</p>
            <p>您的“${categorieNames[2]}维度”的得分如下图所示：</p>
            <div class="progress" data="${scoreArray[2]}"></div>
            <h3>1 结果说明</h3>
            <p>您在“${categorieNames[2]}维度”上的得分“${evaluationList[2].healthStatus}”，这表明：${evaluationList[2].evaluation}</p>

            <h3>2 建议</h3>
            <p>${evaluationList[2].suggestion}</p>

            <h2>(四) ${categorieNames[3]}维度</h2>
            <p>在${categorieNames[3]}维度上，您的得分是${scoreArray[3]}分。这个分值所表达的意思是，您在心理健康的${categorieNames[3]}维度上的水平“${evaluationList[3].healthStatus}”。下图的“${categorieNames[3]}剖面图”形象地表示了您在该维度上的状况。</p>
            <p>您的“${categorieNames[3]}维度”的得分如下图所示：</p>
            <div class="progress" data="${scoreArray[3]}"></div>
            <h3>1 结果说明</h3>
            <p>您在“${categorieNames[3]}维度”上的得分“${evaluationList[3].healthStatus}”，这表明：${evaluationList[3].evaluation}</p>

            <h3>2 建议</h3>
            <p>${evaluationList[3].suggestion}</p>

            <h2>(五) ${categorieNames[4]}维度</h2>
            <p>在${categorieNames[4]}维度上，您的得分是${scoreArray[4]}分。这个分值所表达的意思是，您在心理健康的${categorieNames[4]}维度上的水平“${evaluationList[4].healthStatus}”。下图的“${categorieNames[4]}剖面图”形象地表示了您在该维度上的状况。</p>
            <p>您的“${categorieNames[4]}维度”的得分如下图所示：</p>
            <div class="progress" data="${scoreArray[4]}"></div>
            
        </div>
        <div class="pdf-footer"></div>
    </div>

    <div class="pdf-page">
        <div class="pdf-header"><img src="${base}/images/report/logo.png" width="100px" ></img></div>
        <div class="pdf-body">
            <h3>1 结果说明</h3>
            <p>您在“${categorieNames[4]}维度”上的得分“${evaluationList[4].healthStatus}”，这表明：${evaluationList[4].evaluation}</p>

            <h3>2 建议</h3>
            <p>${evaluationList[4].suggestion}</p>

            <h2>(六) ${categorieNames[5]}维度</h2>
            <p>在${categorieNames[5]}维度上，您的得分是${scoreArray[5]}分。这个分值所表达的意思是，您在心理健康的${categorieNames[5]}维度上的水平“${evaluationList[5].healthStatus}”。下图的“${categorieNames[5]}剖面图”形象地表示了您在该维度上的状况。</p>
            <p>您的“${categorieNames[5]}维度”的得分如下图所示：</p>
            <div class="progress" data="${scoreArray[5]}"></div>
            <h3>1 结果说明</h3>
            <p>您在“${categorieNames[5]}维度”上的得分“${evaluationList[5].healthStatus}”，这表明：${evaluationList[5].evaluation}</p>

            <h3>2 建议</h3>
            <p>${evaluationList[5].suggestion}</p>

            <h1>Ⅶ  心理测评师给您的建议</h1>
            <h2>(一) 学习减压放松技巧</h2>
            <h3>1.音乐冥想</h3>
            <p>冥想是这样一种状态：意识变得意识到它本身超越心智/感官印象的体验。冥想的技巧有使心念和情绪平静下来的效果。有规律的练习会唤醒你与生俱来的平静和快乐。冥想的心理和生理利益有很好的文件为证。即使一个人没有灵性的动机来练习冥想，他或她也能通过有规律的冥想练习来极大地改善身体和情绪的健康。</p>

            <p>放一些轻松、缓慢的背景音乐，整个人找个安静的地方坐好、放松。调整好自己的姿势后，仔细观察自然的呼吸过程，在任何情况下都不要改变呼吸的节奏。也可以把注意力集中在每一次呼气上。通过每次呼吸来缓解紧张感，放松心情。特别是注意呼气结束与吸气开始之间的那一短暂间隙，眼睛张开或闭上。如果眼睛张开，可将视线罗在身前几米的地面上，眼睛稍垂视，视点不要离身体太远。</p>

            <h3>2.绘画减压</h3>
        </div>
        <div class="pdf-footer"></div>
    </div>

    <script type="text/javascript" src="${base}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${base}/js/highcharts/highcharts.js"></script>
    <script type="text/javascript" src="${base}/js/highcharts/highcharts-more.js"></script>
    <script type="text/javascript">
    $(function(){
        $('#radar-chart').highcharts({

            chart: {
                polar: true,
                type: 'line'
            },

            plotOptions: {
                line: {
                  animation: false,
                  shadow: false,
                  enableMouseTracking: false
                }
            },

            title: {
                text: '',
                x: -80
            },

            legend: {
                enabled: false
            },

            pane: {
                size: '80%'
            },

            xAxis: {
                categories: [<c:forEach var="categoryname" items="${categorieNames}" varStatus="status">'${categoryname}'<c:if test="${!status.last}">,</c:if></c:forEach>],
                tickmarkPlacement: 'on',
                lineWidth: 0
            },

            yAxis: {
                gridLineInterpolation: 'polygon',
                lineWidth: 0,
                min: 0
            },

            series: [{
                name: '各维度得分',
                data: [<c:forEach var="score" items="${scoreArray}" varStatus="status">${score}<c:if test="${!status.last}">,</c:if></c:forEach>],
                pointPlacement: 'on'
            }]

        });
    });
    </script>

  </body>
</html>
