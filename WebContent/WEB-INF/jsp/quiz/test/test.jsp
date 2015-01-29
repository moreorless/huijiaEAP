<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>会佳心语测评系统</title>
	<link type="text/css" rel="stylesheet" href="${base}/css/bootstrap.min.css" />
	<link type="text/css" rel="stylesheet" href="${base}/css/common.css" />
	<link type="text/css" rel="stylesheet" href="${base}/css/quiz/quiz.css" />
	<link type="text/css" rel="stylesheet" href="${base}/css/huijia.css"/>
	<style type="text/css">
		body {background-color: #f3f3f3}
		#scrollWrapper {overflow: auto;height: 100%}
		
		#tip-dialog {display:none;position:absolute;z-index:9999;width: 250px;}
		#tip-dialog .content {float:left}
		#tip-dialog .content span {color: green; font-weight: bold; font-size: 18px;}
		#tip-dialog button {float:right; margin:5px;}
	</style>
</head>
<body>
<div id="scrollWrapper">
<%@ include file="/error/inline.jsp"%>
<c:import url="/includes/header_huijia.jsp"></c:import>
<div class="container quiz_wrapper">
	<p class="quiz-tip">温馨提示：请注意保护个人测评信息。</p>
<div class="quiz_inner_wrapper">
	<form class="form-horizontal" role="form" action="${base}/quiz/answer" method="post" id="quiz-form">
	<input type="hidden" name="quizId" value="${quiz.id}"/>
	<input type="hidden" name="answerJson" id="answerJson" value=""/>
	
	<c:forEach items="${quizlist}" var="_quiz" varStatus="quizstat">
	<div id="quiz-card-${quizstat.index}" class="quiz-page" <c:if test="${quizstat.index != 0}">style="display:none"</c:if>>
	<h3>&nbsp;&nbsp;&nbsp;&nbsp;${_quiz.name }</h3>
	<c:forEach items="${_quiz.items}" var="questionObj" varStatus="itemStat">
		<div id="question-card-${questionObj.id}" class="row question-card <c:if test="${itemStat.index % 2 ==0}">light</c:if>">
			<h4>${itemStat.index + 1}.&nbsp;${questionObj.question}</h4>
			<c:forEach items="${questionObj.options}" var="qOption">
			<div class="radio">
			  <label question="${questionObj.id}">
			    <input type="radio" name="answer[${questionObj.id}]" id="answerOption${questionObj.id}${qOption.index}" value="${qOption.index}" index="${itemStat.index+1}" question="${questionObj.id}" answer="${qOption.index}"/>
			    <span>${qOption.index}.&nbsp;${qOption.content}</span>
			  </label>
			</div>
			</c:forEach>
		</div>
	</c:forEach>
	</div>
	</c:forEach>
	<div class="row">
    	<div style="text-align: center;height: 60px;line-height: 60px;padding-top: 20px;">
    	<c:if test="${fn:length(quizlist) > 1}">
    		<button type="button" id="btn-next" class="btn btn-primary">继续答题</button>
    		<button type="button" id="btn-submit" class="btn btn-primary" style="display:none">提交</button>
    	</c:if>
      	<c:if test="${fn:length(quizlist) == 1}">
      	<button type="button" id="btn-submit" class="btn btn-primary">提交</button>
      	</c:if>
      	<%--
      	<a href="${base}/quiz/enquizlist" class="btn btn-default">返回</a>
      	 --%>
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
<script type="text/javascript" src="${base}/js/huijia/index.js"></script>
<script type="text/javascript">
	var currentPage = 0;
	var quizCount = ${fn:length(quizlist)};
	var answerMap = {};
	
	function checkAnswer(){
		var _allAnswered = true;
		$('#quiz-card-' + currentPage).find('.question-card').each(function(questionIndex){
			var _answered = false;

			$(this).find(':radio').each(function(){
				if($(this).prop('checked')){
					_answered = true;
					var	qtId = $(this).attr('question');
					answerMap[qtId] = $(this).attr('answer');
					return false;
				}
			});
			if(!_answered) {
				_allAnswered = false;
				
				// scroll to 
				$("#scrollWrapper").animate({ scrollTop: $(this).height() *  (questionIndex) + 300}, 400);
				TipHandler.show('第&nbsp;<span>' + ( questionIndex + 1 )  + '</span>&nbsp;题尚未回答。');
								
				return false;
			}
		});
		
		return _allAnswered;
	}
	
	var TipHandler = {
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
	
	var BtnHandler = {
		
		init : function(){
			$('#btn-next').click(function(){
				
				if(checkAnswer() == false) return false;
				
				// 切换试题
				$('#quiz-card-' + currentPage).hide();
				currentPage++;
				$('#quiz-card-' + currentPage).show();
				$("#scrollWrapper").animate({ scrollTop: 0 }, 400);
								
				if(currentPage == (quizCount - 1)){
					$('#btn-next').hide();
					$('#btn-submit').show();
				}
			});
			
			$('#btn-submit').click(function(){
				if(!checkAnswer()) return false;
				
				$('#answerJson').val($.toJSON(answerMap));
				
				$('#quiz-form').submit();
			});
			
			$('#tip-dialog .close').click(function(){
				$('#tip-dialog').hide();
			});
		}
	}
	$(document).ready(function(){
		$('.question-card label').click(function(){
			var questionId = $(this).attr('question');
			$('#question-card-' + questionId).find('span').removeClass('question-span-active');
			$(this).find('span').addClass('question-span-active');
		});
		
		BtnHandler.init();
	});
</script>
</body>
</html>
