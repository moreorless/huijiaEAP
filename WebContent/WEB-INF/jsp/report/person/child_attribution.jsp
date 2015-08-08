<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><fmt:message key="index.product.name" /></title>
    <link type="text/css" rel="stylesheet" href="${base}/css/quiz/person.css" />
    <style type="text/css">
        .chart{width: 600px; height: 400px; margin: 0 auto}
        table {margin-bottom: 20px;border-spacing: 0;border-collapse: collapse;}
        thead {  display: table-header-group; vertical-align: middle; border-color: inherit;}

        tr {
          display: table-row;
          vertical-align: inherit;
          border-color: inherit;
        }
        table > thead > tr > th {
          vertical-align: bottom;
          border-bottom: 1px solid #ddd;
          padding: 8px;
          line-height: 1.42857143;
          text-align: left;
          font-weight: bold;
        }
        table tr td {width: 300px;line-height: 1.42857143;}
        .table1 label {width: 80px;display:inline-block;}
        .table2 {border-top: 2px solid #ccc; border-bottom: 2px solid #ccc}
        .table3 td {border: 1px solid #ccc; padding-left: 8px;}
        .table3 th {border: 1px solid #ccc}
    </style>
</head>
<body>
    <c:import url="/WEB-INF/jsp/report/person/includes/cover.jsp"></c:import>
    
    <div class="pdf-page">
        <div class="pdf-header"><img src="${base}/images/report/logo.png" width="100px" ></img></div>
        <div class="pdf-body">
            <h1>Ⅰ 儿童归因形态风格得分</h1>
            <table class="table1">
                <tr>
                    <td><label>PMB:</label> <span>${PMB}</span></td>
                    <td><label>PMG:</label> <span>${PMG}</span></td>
                </tr>
                <tr>
                    <td><label>PVB:</label> <span>${PVB}</span></td>
                    <td><label>PVG:</label> <span>${PVG}</span></td>
                </tr>
                <tr>
                    <td><label>HoB:</label> <span>${HoB}</span></td>
                    <td></td>
                </tr>
                <tr>
                    <td><label>PSB:</label> <span>${PSB}</span></td>
                    <td><label>PSG:</label> <span>${PSG}</span></td>
                </tr>
                <tr>
                    <td><label>Total&nbsp;B:</label> <span>${scoreB}</span></td>
                    <td><label>Total&nbsp;G:</label> <span>${scoreG}</span></td>
                </tr>
                <tr>
                    <td><label>G-B:</label> <span>${scoreG_B}</span></td>
                    <td></td>
                </tr>
            </table>
            <table class="table1">
                <tr>
                    <td><label>PMB:</label> <span>永久的-坏事</span></td>
                    <td><label>PMG:</label> <span>永久的-好事</span></td>
                </tr>
                <tr>
                    <td><label>PVB:</label> <span>普遍的-坏事</span></td>
                    <td><label>PVG:</label> <span>普遍的-好事</span></td>
                </tr>
                <tr>
                    <td><label>HoB:</label> <span>悲观的-坏事</span></td>
                    <td></td>
                </tr>
                <tr>
                    <td><label>PSB:</label> <span>个人化-坏事</span></td>
                    <td><label>PSG:</label> <span>个人化-好事</span></td>
                </tr>
                <tr>
                    <td><label>B:</label> <span>坏事</span></td>
                    <td><label>G:</label> <span>好事</span></td>
                </tr>
            </table>

            <h1>Ⅱ 儿童归因形态风格分析</h1>
            <p>下面是孩子得分的意义以及他与其他做过这个测验的小孩得分的比较结果。</p>
            <p>女孩在青春期以前明显比男孩乐观。8~l2岁女孩的平均分数(G-B) 大约是6.5； 8~12岁男孩的平均分数大约是5.05。假如女孩平均分数低于5.0，她就有点悲观；假如她的分数低于4.0；那她就非常悲观,有患抑症的风险; 假如男孩分数低于3.0, 他属于有些悲观；假如他的分数低于l.5，那他属于非常悲观，有患抑郁症的风险。表6-1显示8~12岁孩子的标准。利用此表, 将你所作的猜测与测验后实际呈现的乐观程度进行比较。</p>
            <div style="font-weight:bold;padding:5px;">表6-1&nbsp;&nbsp;G-B分数</div>
            <table class="table2">
                <thead>
                    <tr>
                        <th>百分比</th>
                        <th>女孩</th>
                        <th>男孩</th>
                    </tr>
                </thead>
                <tbody>
                    <tr><td>90th(最乐观)</td>  <td>11.31</td>  <td>10.30</td></tr>
                    <tr><td>80th</td>  <td>9.67</td>  <td>8.16</td></tr>
                    <tr><td>70th</td>  <td>8.35</td>  <td>7.14</td></tr>
                    <tr><td>60th</td>  <td>7.22</td>  <td>6.07</td></tr>
                    <tr><td>50th(平均)</td>  <td>6.50</td>  <td>5.05</td></tr>
                    <tr><td>40th</td>  <td>5.86</td>  <td>4.04</td></tr>
                    <tr><td>30th</td>  <td>5.00</td>  <td>2.86</td></tr>
                    <tr><td>20th</td>  <td>3.80</td>  <td>1.46</td></tr>
                    <tr><td>10th(最悲观)</td>  <td>2.27</td>  <td>0.43</td></tr>

                </tbody>
            </table>

            <p>至于B的总分,女孩平均为7.1，男孩平均为8.6,女孩比男孩更为乐观(B的分数越高,表示越悲观), B的总分比平均分高出1.5以上则表示十分乐观。<b>B的总分表示你的孩子平常是如何对不好的事件作出反应的。</b> HoB(悲观—坏事分数)指出孩子不会从挫折中爬起,将挫折从一个领域泛化到另一个领域, 并且在事情不顺利时过分责怪自己。</p>
            <p>坏事方面(PMB、PVB及PSB), 每一项的平均分大约是女孩为2.4, 男孩为2.9(坏事的分数越高, 孩子就越悲观)。3.5分或更高即显示有抑郁的风险。悲观的PMB分数代表孩子会变得消极并且失败后不能重新振作;有乐观 PMB分数的孩子将挫折视为挑战, 失败后卷起袖子, 准备重新再来。有悲观 PVB分数的孩子, 从一件不好的事件中泛化结论而使事情灾难化, 当他们同家庭或同辈间的关系不顺时, 学校表现就开始出现问题;乐观PVB分数的孩子可将问题局部化,即使他的成绩下降,他仍然可与朋友相处得很好。悲观PSB的孩子是个自责者,当事情不顺利时,虽然不是他的错, 他仍感到内疚、 羞耻及失去自尊, 而失败之后, 他觉得自己毫无价值;乐观 PSB分数的孩子责怪他人,在失败时,他的自尊没有下降,并且时常认为是他人使自己失败,他会感到气愤。男孩与女孩的G (好事)总分的平均数都是13.8 ( G总分越高,孩子越乐观),比平均数低2.0以上的分数, 代表十分悲观。<b>好事乐观分数 G显示孩子会受胜利所激励,</b></p>
        </div>
        <div class="pdf-footer"></div>
    </div>
    <div class="pdf-page">
        <div class="pdf-header"><img src="${base}/images/report/logo.png" width="100px" ></img></div>
        <div class="pdf-body">
            <p><b>并且争取成功的功劳。</b></p>
            <p>好事方面(PMG、PVG及PSG), 女孩与男孩的平均分数都是4.6。分数低于4.0,就是极度悲观。乐观的PMB分数表明孩子会在一两次成功之后,继续成功; 悲观的 PMG分数表明孩子不能从成功中得利, 即一次成功并不会带来更多成功。乐观的PMG分数表明可同时具有不同范围内的成功,即孩子与朋友交往顺利时,他的功课也会更好;相反,悲观的PVG表明孩子无法因某一范围内的成功, 而使其他范围得利。乐观的PSG分数表明孩子将成功归因于自己的内部品质，并且感受有自尊；但是悲观孩子的PSG分数，甚至在做得很好时，也时常有自尊问题；而将成功视为外部因素所致，如运气或环境等。</p>

            <p>表6-2和表6-3总结了孩子得分的意义。</p>
            <div style="font-weight:bold;padding:5px;">表6-2&nbsp;&nbsp;女孩</div>
            <table class="table2 table3">
                <thead>
                    <tr>
                        <th width="100px">分数</td>
                        <th width="100px">价值</td>
                        <th width="400px">解释</td>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td rowspan="3">PMB（永久的-坏事）</td>
                        <td>乐观：&lt;1.5</td>
                        <td>能很快从失败中恢复斗志，继续努力</td>
                    </tr>
                    <tr>
                        <td>普通：1.5～4.0</td>
                        <td>短时间会丧失斗志</td>
                    </tr>
                    <tr>
                        <td>悲观：&gt;4.0</td>
                        <td>被挫折击垮，无法继续奋斗，脆弱</td>
                    </tr>

                    <tr>
                        <td rowspan="3">PVB（普遍的-坏事）</td>
                        <td>乐观：&lt;1.5</td>
                        <td>不会将挫折普遍化</td>
                    </tr>
                    <tr>
                        <td>普通：1.5～4.0</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>悲观：&gt;4.0</td>
                        <td>变成普遍的无助，将事件灾难化</td>
                    </tr>

                    <tr>
                        <td rowspan="3">PSB（个人化-坏事）</td>
                        <td>乐观：&lt;1.5</td>
                        <td>怪别人，有较强的自尊</td>
                    </tr>
                    <tr>
                        <td>普通：1.5～4.0</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>悲观：&gt;4.0</td>
                        <td>怪自己，觉得内疚，降低自尊</td>
                    </tr>

                    <tr>
                        <td rowspan="3">PMG（永久的-好事）</td>
                        <td>乐观：&gt;6</td>
                        <td>受成功激励</td>
                    </tr>
                    <tr>
                        <td>普通：3.0～6.0</td>
                        <td>可受益于成功</td>
                    </tr>
                    <tr>
                        <td>悲观：&lt;3.0</td>
                        <td>成功反倒引起倦怠</td>
                    </tr>

                    <tr>
                        <td rowspan="3">PVG（普遍的-好事）</td>
                        <td>乐观：&gt;6</td>
                        <td>将成功广泛地普遍化</td>
                    </tr>
                    <tr>
                        <td>普通：3.0～6.0</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>悲观：&lt;3.0</td>
                        <td>成功消失无形</td>
                    </tr>

                    <tr>
                        <td rowspan="3">PSG（个人化-好事）</td>
                        <td>乐观：&gt;6.0</td>
                        <td>因成功而自豪</td>
                    </tr>
                    <tr>
                        <td>普通：3.0～6.0</td>
                        <td>愿意接受赞扬</td>
                    </tr>
                    <tr>
                        <td>悲观：&lt;3.0</td>
                        <td>不愿接受赞扬</td>
                    </tr>

                    <tr>
                        <td rowspan="3">HoB（悲观的-坏事）</td>
                        <td>乐观：&lt;3.0</td>
                        <td>不屈不挠，充满活力</td>
                    </tr>
                    <tr>
                        <td>普通：3.0～8.0</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>悲观：&gt;8.0</td>
                        <td>脆弱，将事件灾难化</td>
                    </tr>

                    <tr>
                        <td rowspan="3">B总数（坏事）</td>
                        <td>乐观：&lt;6.25</td>
                        <td>几乎不会被抑郁伤害</td>
                    </tr>
                    <tr>
                        <td>普通：6.25～8.10</td>
                        <td>有些抑郁</td>
                    </tr>
                    <tr>
                        <td>悲观：&gt;8.10</td>
                        <td>有罹患抑郁症的风险</td>
                    </tr>

                    <tr>
                        <td rowspan="3">G总数（好事）</td>
                        <td>乐观：&gt;15.27</td>
                        <td>积极，容易成功</td>
                    </tr>
                    <tr>
                        <td>普通：12.84～15.27</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>悲观：&lt;12.84</td>
                        <td>消极，容易失败</td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="pdf-footer"></div>
    </div>

    <div class="pdf-page">
        <div class="pdf-header"><img src="${base}/images/report/logo.png" width="100px" ></img></div>
        <div class="pdf-body">
        <div style="font-weight:bold;padding:5px;">表6-3&nbsp;&nbsp;男孩</div>
            <table class="table2 table3">
                <thead>
                    <tr>
                        <th width="100px">分数</td>
                        <th width="100px">价值</td>
                        <th width="400px">解释</td>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td rowspan="3">PMB（永久的-坏事）</td>
                        <td>乐观：&lt;1.5</td>
                        <td>能很快从失败中恢复斗志，继续努力</td>
                    </tr>
                    <tr>
                        <td>普通：1.5～4.0</td>
                        <td>短时间会丧失斗志</td>
                    </tr>
                    <tr>
                        <td>悲观：&gt;4.0</td>
                        <td>被挫折击垮，无法继续奋斗，脆弱</td>
                    </tr>

                    <tr>
                        <td rowspan="3">PVB（普遍的-坏事）</td>
                        <td>乐观：&lt;1.5</td>
                        <td>不会将挫折普遍化</td>
                    </tr>
                    <tr>
                        <td>普通：1.5～4.0</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>悲观：&gt;4.0</td>
                        <td>变成普遍的无助，将事件灾难化</td>
                    </tr>

                    <tr>
                        <td rowspan="3">PSB（个人化-坏事）</td>
                        <td>乐观：&lt;1.5</td>
                        <td>怪别人，有较强的自尊</td>
                    </tr>
                    <tr>
                        <td>普通：1.5～4.0</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>悲观：&gt;4.0</td>
                        <td>怪自己，觉得内疚，降低自尊</td>
                    </tr>

                    <tr>
                        <td rowspan="3">PMG（永久的-好事）</td>
                        <td>乐观：&gt;6.0</td>
                        <td>受成功激励</td>
                    </tr>
                    <tr>
                        <td>普通：3.0～6.0</td>
                        <td>可受益于成功</td>
                    </tr>
                    <tr>
                        <td>悲观：&lt;3.0</td>
                        <td>成功反倒引起倦怠</td>
                    </tr>

                    <tr>
                        <td rowspan="3">PVG（普遍的-好事）</td>
                        <td>乐观：&gt;6</td>
                        <td>将成功广泛地普遍化</td>
                    </tr>
                    <tr>
                        <td>普通：3.0～6.0</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>悲观：&lt;3.0</td>
                        <td>成功消失无形</td>
                    </tr>

                    <tr>
                        <td rowspan="3">PSG（个人化-好事）</td>
                        <td>乐观：&gt;6</td>
                        <td>因成功而自豪</td>
                    </tr>
                    <tr>
                        <td>普通：3.0～6.0</td>
                        <td>愿意接受赞扬</td>
                    </tr>
                    <tr>
                        <td>悲观：&lt;3.0</td>
                        <td>不愿接受赞扬</td>
                    </tr>

                    <tr>
                        <td rowspan="3">HoB（悲观的-坏事）</td>
                        <td>乐观：&lt;3.0</td>
                        <td>不屈不挠，充满活力</td>
                    </tr>
                    <tr>
                        <td>普通：3.0～8.0</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>悲观：&gt;8.0</td>
                        <td>脆弱，将事件灾难化</td>
                    </tr>

                    <tr>
                        <td rowspan="3">B总数（坏事）</td>
                        <td>乐观：&lt;7.26</td>
                        <td>几乎不会被抑郁伤害</td>
                    </tr>
                    <tr>
                        <td>普通：7.26～10.0</td>
                        <td>有些抑郁</td>
                    </tr>
                    <tr>
                        <td>悲观：&gt;10.0</td>
                        <td>有罹患抑郁症的风险</td>
                    </tr>

                    <tr>
                        <td rowspan="3">G总数（好事）</td>
                        <td>乐观：&gt;15.0</td>
                        <td>积极，容易成功</td>
                    </tr>
                    <tr>
                        <td>普通：12.5～15.0</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>悲观：&lt;12.5</td>
                        <td>消极，容易失败</td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="pdf-footer"></div>
    </div>
 </body>