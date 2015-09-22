<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><fmt:message key="index.product.name" /></title>
    <link type="text/css" rel="stylesheet" href="${base}/css/quiz/person.css" />
    <style type="text/css">
        .chart{width: 600px; height: 400px; margin: 0 auto}
        .progress {
            height: 20px;
        margin-bottom: 20px;
        overflow: hidden;
        background-color: #f5f5f5;
        border-radius: 4px;
        -webkit-box-shadow: inset 0 1px 2px rgba(0,0,0,.1);
        box-shadow: inset 0 1px 2px rgba(0,0,0,.1);
      }
      .progress-bar-info { background-color: #5bc0de;}
      .progress-bar {
        float: left;
        width: 0;
        height: 100%;
        font-size: 12px;
        line-height: 20px;
        color: #fff;
        text-align: center;
        background-color: #337ab7;
        -webkit-box-shadow: inset 0 -1px 0 rgba(0,0,0,.15);
        box-shadow: inset 0 -1px 0 rgba(0,0,0,.15);
        -webkit-transition: width .6s ease;
        -o-transition: width .6s ease;
        transition: width .6s ease;
      }
      .annotation {font-size: 12px; color: #666}

      table {
        border-spacing: 0;
        border-collapse: collapse;
      }
      td, th {
        padding: 0; border: 1px solid #ddd !important;
      }
      th {
        text-align: left;
      }
      .table {
        width: 100%;
        max-width: 100%;
        margin-bottom: 20px;
      }
      .table > thead > tr > th,
      .table > tbody > tr > th,
      .table > tfoot > tr > th,
      .table > thead > tr > td,
      .table > tbody > tr > td,
      .table > tfoot > tr > td {
        padding: 8px;
        line-height: 1.42857143;
        vertical-align: top;
        border-top: 1px solid #ddd;
      }
      .table > thead > tr > th {
        vertical-align: bottom;
        border-bottom: 2px solid #ddd;
      }
      .table > caption + thead > tr:first-child > th,
      .table > colgroup + thead > tr:first-child > th,
      .table > thead:first-child > tr:first-child > th,
      .table > caption + thead > tr:first-child > td,
      .table > colgroup + thead > tr:first-child > td,
      .table > thead:first-child > tr:first-child > td {
        border-top: 0;
      }
      .table > tbody + tbody {
        border-top: 2px solid #ddd;
      }
      .table .table {
        background-color: #fff;
      }
    </style>
</head>
<body>
    <c:import url="/WEB-INF/jsp/report/person/includes/cover.jsp"></c:import>
    <div class="pdf-page">
        <div class="pdf-header"><img src="${base}/images/report/logo.png" width="100px" ></img></div>
        <div class="pdf-body">
            <h1>Ⅰ 问卷的组成</h1>
            <p>该调查问卷共由共12组题组成，共涉及情绪感知、情绪控制、社交技巧和情绪利用四大维度。其中，情绪感知维度包括自我情绪感知、他人情绪感知两个二级维度；情绪控制维度包括情绪稳定性、控制力、自我激励三个二级维度；社交技巧维度包括情绪表达、适应力和感染力三个二级维度。问卷的题目包括我很清楚自己每时每刻的情绪，我认为自己很容易理解别人的身体语言，等等。</p>
            <p style="text-align:center"><img src="${base}/images/report/quiz4-factor.png" ></img></p>
            <p>在上述因素中，情绪感知维度是指认识自身和他人情绪，并以此作为行动的依据，能够设身处地地为他人着想。此维度关系到个体的深层情绪和情绪表达能力，是情绪管理能力的基础维度。情绪控制维度是指员工妥善管理自身情绪、高昂和压抑的情绪中恢复过来的能力。社会技巧维度是指员工在社会中情绪的表达、情绪的灵活适应和调动周围人情绪的能力。情绪利用是指利用情绪促进问题解决，情绪促进思维的能力。</p>
        
          <h1>Ⅱ 整体结论</h1>
          <h2>情绪管理倾向指数${emotionIndex}分</h2>
          <div class="progress" style="width:600px;">
            <div class="progress-bar" style="width: ${emotionIndex * 20}%;"></div>
          </div>
          <p><c:choose><c:when test="${emotionIndex == 1}">此分数说明，您的个体情绪管理倾向整体上远低于一般人。</c:when>
              <c:when test="${emotionIndex == 2}">此分数说明，您的个体情绪管理倾向整体上低于一般人。</c:when>
              <c:when test="${emotionIndex == 3}">此分数说明，您的个体情绪管理倾向整体上与一般人相近。</c:when>
              <c:when test="${emotionIndex == 4}">此分数说明，您的个体情绪管理倾向整体上高于一般人。</c:when>
              <c:when test="${emotionIndex == 5}">此分数说明，您的个体情绪管理倾向整体上远高于一般人。</c:when>
              <c:otherwise>[Error 指数超出范围]</c:otherwise></c:choose></p>
          <p class="annotation">注：上述得分的意义<br/>
          1分 -- 个体情绪管理倾向远低于一般人 <br/>
          2分 -- 个体情绪管理倾向低于一般人 <br/>
          3分 -- 个体情绪管理倾向与一般人相近 <br/>
          4分 -- 个体情绪管理倾向高于一般人 <br/>
          5分 -- 个体情绪管理倾向远高于一般人</p>
      
      <h2>特征</h2>
      <p>如下图所示</p>
      
    
      </div>
        <div class="pdf-footer"></div>
    </div>

    <div class="pdf-page">
        <div class="pdf-header"><img src="${base}/images/report/logo.png" width="100px" ></img></div>
        <div class="pdf-body">
        <div class="chart" id="score-by-category"></div>
      <p>上图中，红色柱状图表示你本人在各个大维度上的平均分，而蓝色柱状图表示的是数据常模中各大维度的均值。由上图可知，**维度比常模得分高，**维度比常模得分低。也就是说，您在**、**（维度名称）方面比一般人要强，而在**方面（维度名称）比较一般人要弱一些。</p>
       
      <h2>重点关注</h2>
      <div class="chart" id="radar-chart"></div>
      <p>上图中，红色雷达图表示的您在各个二级维度上的得分，而蓝色雷达图表示的数据常模中各个维度的均值。由上图可知，**维度比常模得分低，属于重点关注区域。</p>
      
      <h1>Ⅲ 作答可靠性评估</h1>
      <h2>社会赞许性</h2>
      <p>社会赞许性处于正常水平。测试的可靠性较强。</p>
      
      <h2>评分一致性</h2>
      <p>评分一致性的得分：3分</p>
      <div class="progress" style="width:600px;">
        <div class="progress-bar" style="width: 60%;"></div>
      </div>
      <p>此分数说明，您的评分一致性与一般人相近。就是说，您的个性特征排序有跟大多数人接近，对于出现在不同位置的相同或相似题目作答的一致性程度处于中等水平。</p>
      <p class="annotation">注：上述得分的意义<br/>
      1分 -- 个体情绪管理倾向远低于一般人 <br/>
      2分 -- 个体情绪管理倾向低于一般人 <br/>
      3分 -- 个体情绪管理倾向与一般人相近 <br/>
      4分 -- 个体情绪管理倾向高于一般人 <br/>
      5分 -- 个体情绪管理倾向远高于一般人</p>
      
      <h1>Ⅳ 具体维度解析</h1>

      </div>
        <div class="pdf-footer"></div>
    </div>

    <div class="pdf-page">
        <div class="pdf-header"><img src="${base}/images/report/logo.png" width="100px" ></img></div>
        <div class="pdf-body">
      <p>情绪管理倾向测评从情绪感知、情绪控制、社交技巧、情绪利用四大维度分析，其中，情绪感知包括自我情绪感知、他人情绪感知两个维度；情绪控制包括情绪稳定性、控制力、自我激励三个维度；社交技巧包括情绪表达、适应力、感染力三个维度；情绪利用维度主要考察问题解决能力。</p>
      
      <table class="table">
        <thead>
          <tr>
            <th></th>
            <th></th>
            <th>得分</th>
            <th>常模得分</th>
            <th>低分倾向的人的特征</th>
            <th></th>
            <th>高分倾向的人的特征</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td></td>
            <td>自我情绪感知</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td>自我情绪感知</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
        </tbody>
      </table>
      <p class="annotation">说明：各个常模分数的意义<br/>
      1=高于 1% 的常模人群； 2=高于 5% 的常模人群； 3=高于 10% 的常模人群；4=高于 25% 的常模人群；5=高于 40% 的常模人群；6=高于 60% 的常模人群；7=高于 75% 的常模人群；8=高于 90% 的常模人群；9=高于 95% 的常模人群；10=高于 99% 的常模人群。</p>
        </div>
        <div class="pdf-footer"></div>
    </div>


    
    <script type="text/javascript" src="${base}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${base}/js/highcharts/highcharts.js"></script>
    <script type="text/javascript" src="${base}/js/highcharts/highcharts-more.js"></script>

    <script type="text/javascript">
      $(document).ready(function(){
        // 特征柱状图
        var categoryNames = [<c:forEach var="feature" items="${featureList}" varStatus="status">'${feature.categoryName}'<c:if test="${!status.last}">,</c:if></c:forEach>];
        var featureOptions = {
            chart: {
                    type: 'column'
                },
                colors : [
                  '#21329d', '#ffcc3e'
                ],
                xAxis: {
                    categories:categoryNames
          },
          plotOptions: {
                  column: {
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
                align: 'right',
                verticalAlign: 'top',
                y: 70,
                layout: 'vertical'
              },
              series: [{
                  name: '常模分数',
                  data: [<c:forEach var="feature" items="${featureList}" varStatus="status">${feature.normalScore * 10}<c:if test="${!status.last}">,</c:if></c:forEach>]
                  }, {
                  name: '你的分数',
                  data: [<c:forEach var="feature" items="${featureList}" varStatus="status">${feature.averageScore}<c:if test="${!status.last}">,</c:if></c:forEach>]
              }]
        };
        $('#score-by-category').highcharts(featureOptions);
        
        // 蛛网图
        var polarOption = {
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
            colors : [
                        '#21329d', '#ffcc3e'
                      ],
            xAxis: {
                categories: [<c:forEach var="result" items="${anwerResultList}" varStatus="status">'${result.categoryName}'<c:if test="${!status.last}">,</c:if></c:forEach>],
                tickmarkPlacement: 'on',
                lineWidth: 0
            },
           yAxis: {
                gridLineInterpolation: 'polygon',
                lineWidth: 0,
                min: 0
            },
           legend: {
              align: 'right',
              verticalAlign: 'top',
              y: 70,
              layout: 'vertical'
          },
         series: [{
              name: '常模分数',
              data: [<c:forEach var="result" items="${anwerResultList}" varStatus="status">${result.normalAver}<c:if test="${!status.last}">,</c:if></c:forEach>],
              pointPlacement: 'on'
          }, {
              name: '你的分数',
              data: [<c:forEach var="result" items="${anwerResultList}" varStatus="status">${result.averageScore}<c:if test="${!status.last}">,</c:if></c:forEach>],
              pointPlacement: 'on'
          }]
        }
        $('#radar-chart').highcharts(polarOption);
      });
    </script>
  </body>
</html>
