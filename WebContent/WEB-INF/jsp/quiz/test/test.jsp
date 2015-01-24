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
		body {overflow: auto;background-color: #f3f3f3}
		#tip-content {height: 60px; line-height: 60px;}
		#tip-content span {color: red; font-weight: bold; font-size: 18px;}
	</style>
</head>
<body>
<c:import url="/includes/header_huijia.jsp"></c:import>
<div class="container quiz_wrapper">
	<p class="quiz-tip">温馨提示：请注意保护个人测评信息。</p>
<div class="quiz_inner_wrapper">
	<form class="form-horizontal" role="form" action="${base}/quiz/answer" method="post" id="quiz-form">
	<input type="hidden" name="quizId" value="${quiz.id}"/>
	<input type="hidden" name="answerJson" id="answerJson" value=""/>
	<c:forEach items="${quiz.items}" var="quizitem" varStatus="stat">
		<div id="question-card-${stat.index+1}" class="row question-card <c:if test="${stat.index % 2 ==0}">light</c:if>">
			<h4>${stat.index + 1}.&nbsp;${quizitem.question}</h4>
			<c:forEach items="${quizitem.options}" var="qOption">
			<div class="radio">
			  <label index="${stat.index+1}">
			    <input type="radio" name="answer[${quizitem.id}]" id="answerOption${quizitem.id}${qOption.index}" value="${qOption.index}" index="${stat.index+1}" question="${quizitem.id}" answer="${qOption.index}"/>
			    <span class="quizitem-span-${stat.index+1}">${qOption.index}.&nbsp;${qOption.content}</span>
			  </label>
			</div>
			</c:forEach>
		</div>
	</c:forEach>
	
	<div class="row">
    	<div style="text-align: center;height: 60px;line-height: 60px;padding-top: 20px;">
      	<button type="button" id="btn-submit" class="btn btn-primary">提交</button>
      	<%--
      	<a href="${base}/quiz/enquizlist" class="btn btn-default">返回</a>
      	 --%>
    	</div>
  	</div>
	</form>
</div>
</div>

<div class="modal" tabindex="-1" role="dialog" id="tip-dialog">
<div class="modal-dialog modal-sm">
  <div class="modal-content">
    <div class="modal-header">
      <h4>提示</h4>
    </div>
    <div class="modal-body">
      <p id="tip-content"></p>
    </div>
    <div class="modal-footer">
      <button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
    </div>
  </div><!-- /.modal-content -->
</div><!-- /.modal-dialog -->
</div>

<%@ include file="/includes/footer_huijia.jsp" %>

<script type="text/javascript" src="${base}/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${base}/js/bootstrap-modalmanager.js"></script>
<script type="text/javascript" src="${base}/js/bootstrap-modal.js"></script>

<script type="text/javascript" src="${base }/js/cupid/core.js"></script>
<script type="text/javascript" src="${base}/js/huijia/index.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$('.question-card label').click(function(){
			var index = $(this).attr('index');
			$('#question-card-' + index).find('span').removeClass('question-span-active');
			$(this).find('span').addClass('question-span-active');
		});
		
		function checkAnswer(){
			var qtLen = ${fn:length(quiz.items)};
			var answers = [];
			var answerMap = {};
			// get all checked radios
			$('input:radio:checked').each(function(){
				var index = parseInt($(this).attr('index'));
				answers[index] = true;
				var	qtId = $(this).attr('question');
				answerMap[qtId] = $(this).attr('answer')
			});
			
			for(var i = 1; i <= qtLen; i++){
				if(!answers[i]) {
					$('#tip-content').html('第&nbsp;<span>' + i + '</span>&nbsp;题尚未作答，请填写完整。');
					$('#tip-dialog').modal();
					return false;
				}
			}
			
			$('#answerJson').val($.toJSON(answerMap));
			return true;
		}
		
		$('#btn-submit').click(function(){
			if(!checkAnswer()) return false;
			$('#quiz-form').submit();
		});
	});
</script>
</body>
</html>
