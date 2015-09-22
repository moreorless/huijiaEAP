<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title><fmt:message key="index.product.name" bundle="${i18n_main}"/></title>
	<link type="text/css" rel="stylesheet" href="${base}/css/bootstrap.min.css" />
	<link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
	<link type="text/css" rel="stylesheet" href="${base}/css/quiz/quiz.css" />
	<link type="text/css" rel="stylesheet" href="${base}/css/huijia.css"/>
	<style type="text/css">
		body {background-color: #f3f3f3}
		#scrollWrapper {overflow: auto;height: 100%}
		.quiz-group {}
		.quiz-group .title {text-align: center; font-size: 20px;  color: #9bca54}
		
		#tip-dialog {display:none;position:absolute;z-index:9999;width: 250px;}
		#tip-dialog .content {float:left}
		#tip-dialog .content span {color: green; font-weight: bold; font-size: 18px;}
		#tip-dialog button {float:right; margin:5px;}
		#pager span {color: green; font-weight: bold; font-size: 18px;}
	</style>
</head>
<body>
<div id="scrollWrapper">
<c:import url="/includes/header_huijia.jsp"></c:import>
<div class="container quiz_wrapper">
	<p class="quiz-tip">温馨提示：请注意保护个人测评信息。</p>
	<div class="quiz_inner_wrapper">
		<form class="form-horizontal" role="form" action="${base}/quiz/forced/answer" method="post" id="quiz-form" data-validate-type="client">
			<input type="hidden" name="quizId" value="${quiz.id}"/>
			<input type="hidden" name="answerJson" id="answerJson" value=""/>

			<div class="quiz-page">
			<h3>&nbsp;&nbsp;&nbsp;&nbsp;${quiz.name }</h3>
			<c:forEach var="groupIndex" begin="1" end="${groupSize}">
			<div id="quiz-group-${groupIndex}" class="quiz-group" <c:if test="${groupIndex != 1}">style="display:none"</c:if>>
				<p class="title">第${groupIndex}组，共${groupSize}组</p>
				<c:forEach items="${quiz.items}" var="questionObj" varStatus="itemStat">
					<c:if test="${questionObj.groupId == (groupIndex - 1)}">
					<div id="question-card-${questionObj.id}" class="row question-card cardinpage-${cardinpage} <c:if test="${itemStat.index % 2 ==0}">light</c:if>">
						<h4>${groupIndex}-${(itemStat.index)%6 + 1}.&nbsp;${questionObj.question}</h4>
						<c:forEach items="${questionObj.options}" var="qOption">
						<div class="radio">
						  <label question="${questionObj.id}">
						    <input type="radio" name="answer[${questionObj.id}]" id="answerOption${questionObj.id}${qOption.index}" value="${qOption.index}" index="${itemStat.index+1}" question="${questionObj.id}" answer="${qOption.index}" score="${qOption.value}"/>
						    <span>${qOption.index}.&nbsp;${qOption.content}</span>
						  </label>
						</div>
						</c:forEach>
					</div>
					</c:if>
				</c:forEach>
			
			</div>
			</c:forEach>

			<div style="text-align: center;height: 60px;line-height: 60px;">
				<a type="button" id="btn-nextpage" class="btn btn-success">下一组</a>
		      	<a type="button" id="btn-submit" class="btn btn-success" style="display:none">提交</a>
		      	<a type="button" id="btn-cancel" class="btn btn-danger" style="float:right;margin-top:10px;margin-right:50px">退出答题</a>
    		</div>

			</div>
		</form>
	</div>
</div>

<%@ include file="/includes/footer_huijia.jsp" %>
</div>

<div id="tip-dialog" class="alert alert-danger">
	<div class="content"></div>
	<button type="button" class="close"><span>&times;</span></button>
</div>

<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/js/bootstrap.min.js"></script>

<script type="text/javascript" src="${base }/js/cupid/core.js"></script>
<script type="text/javascript" src="${base }/js/huijia/forcetest.js"></script>
<script type="text/javascript">
var Pager = {
	currentPage : 1,
	total : ${groupSize},
	next : function(){
		var _allAnswered = true;
		$('#quiz-group-' + Pager.currentPage).find('.question-card').each(function(questionIndex){
			var _answered = false;
			
			$(this).find(':radio').each(function(){
				if($(this).prop('checked')){
					_answered = true;
					return false;
				}
			});

			if(!_answered) {
				_allAnswered = false;
				
				// scroll to 
				$("#scrollWrapper").animate({ scrollTop: $(this).height() *  (questionIndex % 6) + 300}, 400);

				TipHandler.show('第&nbsp;<span>' + ( questionIndex + 1 )  + '</span>&nbsp;题尚未回答。');
				
				return false;
			}
		});
		
		if(!_allAnswered) return;
		
		$('#quiz-group-' + this.currentPage).hide();
		$('#quiz-group-' + (this.currentPage + 1)).show();
		
		// scroll to 
		$("#scrollWrapper").animate({ scrollTop: 300}, 400);
		
		this.currentPage++;

		if(this.currentPage == this.total){ // the last page
			$('#btn-nextpage').hide();
			$('#btn-submit').show();
			$('#btn-submit').attr('disabled', false);
		}
	}
}

var TipHandler = {
		init : function(){
			$('#tip-dialog .close').click(function(){
				$('#tip-dialog').hide();
			});
		},
		show : function(htmlContent){
			$('#tip-dialog .content').html(htmlContent);
			$('#tip-dialog').css({
				top : $(window).height()/2 - 100,
				left : $(window).width()/2 - 80
			});
			$('#tip-dialog').show();
			setTimeout(function(){$('#tip-dialog').hide();}, 3000);
		}
	}

var FormHandler = {
	forcedScore : [],
	getResultJson : function(){
		$('.question-card').each(function(questionIndex){
			$(this).find(':radio').each(function(){
				if($(this).prop('checked')) {
					var	qtId = $(this).attr('question');
					
					FormHandler.forcedScore.push({
						questionId : qtId,
						optionIndex : $(this).attr('answer'),
						score : $(this).attr('score')
					});
					return false;
				}
			});
		});
		return $.toJSON(this.forcedScore);
	},
	submit : function(){
		$('#answerJson').val(this.getResultJson());		
		$('#quiz-form').submit();
	}
}

$(document).ready(function(){
	TipHandler.init();
	
	$('#btn-nextpage').click(function(){
		Pager.next();
	});

	$('#btn-submit').click(function(){FormHandler.submit()});

	$(".quiz-group").find(':radio').click(function(){
		ForchChoise.judge();
	});
});
</script>
</body>
</html>
