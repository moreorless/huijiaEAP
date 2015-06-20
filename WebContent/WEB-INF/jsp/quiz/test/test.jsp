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
	<form class="form-horizontal" role="form" action="${base}/quiz/answer" method="post" id="quiz-form" data-validate-type="client">
	<input type="hidden" name="quizId" value="${quiz.id}"/>
	<input type="hidden" name="answerJson" id="answerJson" value=""/>
	
	<c:forEach items="${quizlist}" var="_quiz" varStatus="quizstat">
	<div id="quiz-card-${quizstat.index}" class="quiz-page" itemcount=${fn:length(_quiz.items)} <c:if test="${quizstat.index != 0}">style="display:none"</c:if>>
	<h3>&nbsp;&nbsp;&nbsp;&nbsp;${_quiz.name }</h3>
	<c:forEach items="${_quiz.items}" var="questionObj" varStatus="itemStat">
		<fmt:formatNumber var="cardinpage" value="${(itemStat.index-itemStat.index%10)/10+1}" maxFractionDigits="0" />
		<div id="question-card-${questionObj.id}" class="row question-card cardinpage-${cardinpage} <c:if test="${itemStat.index % 2 ==0}">light</c:if>">
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


	<div style="text-align: center;height: 60px;line-height: 60px;">
		<p id="pager">第&nbsp;<span id="pager-page"></span>&nbsp;页，共&nbsp;<span id="pager-total"></span>&nbsp;页</p>
		<a type="button" id="btn-pre-page" class="btn btn-primary">上一页</a>
    	<a type="button" id="btn-next-page" class="btn btn-primary">下一页</a>

    	<c:if test="${fn:length(quizlist) > 1}">
    		<a type="button" id="btn-next-quiz" class="btn btn-primary" disabled>继续答题</a>
    		<a type="button" id="btn-submit" class="btn btn-success" style="display:none" disabled>提交</a>
    	</c:if>
      	<c:if test="${fn:length(quizlist) == 1}">
      	<a type="button" id="btn-submit" class="btn btn-success" disabled style="display:none">提交</a>
      	</c:if>
      	
      	<a type="button" id="btn-cancel" class="btn btn-danger" style="float:right;margin-top:10px;margin-right:50px">退出答题</a>
      	
    </div>

	<!--div style="position:absolute;right:100px;top:300px;margin:20px;">
		<p id="pager">第&nbsp;<span id="pager-page"></span>&nbsp;页，共&nbsp;<span id="pager-total"></span>&nbsp;页</p>
    	<div style="text-align: center;height: 60px;line-height: 60px;margin-top: 20px; width:100px;">
    		<a type="button" id="btn-pre-page" class="btn btn-primary btn-block">上一页</a>
    		<a type="button" id="btn-next-page" class="btn btn-primary btn-block" style="margin-bottom:50px;">下一页</a>
    	<c:if test="${fn:length(quizlist) > 1}">
    		<a type="button" id="btn-next-quiz" class="btn btn-primary btn-block" disabled>继续答题</a>
    		<a type="button" id="btn-submit" class="btn btn-success btn-block" style="display:none" disabled>提交</a>
    	</c:if>
      	<c:if test="${fn:length(quizlist) == 1}">
      	<a type="button" id="btn-submit" class="btn btn-success btn-block" disabled>提交</a>
      	</c:if>

      	<a type="button" id="btn-cancel" class="btn btn-danger btn-block">退出答题</a>
    	</div>
  	</div -->
	</form>

</div>
</div>

<%@ include file="/includes/footer_huijia.jsp" %>
</div>

<div id="tip-dialog" class="alert alert-danger">
	<div class="content"></div>
	<button type="button" class="close"><span>&times;</span></button>
</div>

<div class="modal" id="redo-dialog" role="dialog">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title">重新答题</h4>
      </div>
      <div class="modal-body">
        <p><fmt:message key="quiz.test.error.redo" bundle="${i18n_quiz}" /></p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">确定</button>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/js/bootstrap.min.js"></script>

<script type="text/javascript" src="${base }/js/cupid/core.js"></script>

<script type="text/javascript" src="${base }/js/ui/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${base }/js/ui/validate/messages_cn.js"></script>
<script type="text/javascript" src="${base }/js/ui/validate/jquery.validate.ext.methods.js"></script>

<script type="text/javascript" src="${base}/js/huijia/index.js"></script>
<script type="text/javascript">
	var currentQuizPage = 0;
	var quizCount = ${fn:length(quizlist)};
	var answerMap = {};
	
	function checkAnswer(){
		var _allAnswered = true;
		$('#quiz-card-' + currentQuizPage).find('.question-card').each(function(questionIndex){
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
				
				// turn to page
				var targetPage = parseInt((questionIndex + 1) / 10) + 1;
				if(targetPage != Pager.currentPage){
					Pager.showPage(targetPage);	
				}
				
				// scroll to 
				$("#scrollWrapper").animate({ scrollTop: $(this).height() *  (questionIndex % 10) + 300}, 400);

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
			$('#btn-pre-page').click(function(){
				Pager.prePage();
			});
			$('#btn-next-page').click(function(){
				Pager.nextPage();
			});
			$('#btn-next-quiz').click(function(){
				
				if(checkAnswer() == false) return false;
				
				// 切换试题
				$('#quiz-card-' + currentQuizPage).hide();
				currentQuizPage++;
				$('#quiz-card-' + currentQuizPage).show();
				$("#scrollWrapper").animate({ scrollTop: 0 }, 400);
								
				Pager.init();

				if(currentQuizPage < (quizCount - 1)){
					$('#btn-next-quiz').show();
					$('#btn-submit').hide();
				}else{
					$('#btn-next-quiz').hide();
					$('#btn-submit').show();
				}

			});
			
			$('#btn-submit').click(function(){
				if(!checkAnswer()) return false;
				
				$('#answerJson').val($.toJSON(answerMap));
				
				$('#quiz-form').submit();
			});
			
			$('#btn-cancel').click(function(){
				if(confirm("退出后，本次答题结果不会被保存。\n确定退出本次答题？")){
					window.location = "${base}/quiz/enquizlist";
				}
			});
			
			$('#tip-dialog .close').click(function(){
				$('#tip-dialog').hide();
			});
		}
	}

	var Pager = {
		currentPage : 1,
		pagesize : 10,
		total : 0,
		/**
		* 输入参数为quiz-card的ID
		*/
		init : function(quizcard){
			this.total = parseInt($('#quiz-card-' + currentQuizPage).attr('itemcount') / this.pagesize + 1);
			$('#pager-total').text(this.total);

			$('#btn-pre-page').attr('disabled', true);
			this.showPage(1);

		},
		nextPage : function(){
			this.showPage(++this.currentPage);
		},
		prePage : function(){
			this.showPage(--this.currentPage);
		},
		showPage : function(page){
			this.currentPage = page;

			$('#pager-page').text(this.currentPage);

			if(page == 1){
				$('#btn-pre-page').attr('disabled', true);
			}else{
				$('#btn-pre-page').attr('disabled', false);
			}

			if(page == this.total){
				$('#btn-next-page').attr('disabled', true);

				$('#btn-next-quiz').attr('disabled', false);
				$('#btn-submit').attr('disabled', false);
				$('#btn-submit').show();

			}else{
				$('#btn-next-page').attr('disabled', false);
			}
			$('#quiz-card-' + currentQuizPage).find('.question-card').hide();
			$('#quiz-card-' + currentQuizPage).find('.cardinpage-' + page).show();

			$("#scrollWrapper").animate({ scrollTop: 0 }, 400);
		}
	}

	$(document).ready(function(){
		$('.question-card label').click(function(){
			var questionId = $(this).attr('question');
			$('#question-card-' + questionId).find('span').removeClass('question-span-active');
			$(this).find('span').addClass('question-span-active');
		});
		
		BtnHandler.init();
		Pager.init();

		<c:if test="${param.redo == 'true'}">
			$('#redo-dialog').modal();
		</c:if>
	});
</script>
</body>
</html>
