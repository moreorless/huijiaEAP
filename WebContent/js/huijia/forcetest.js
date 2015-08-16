
var ForchChoise = {
	currentPageAnswer : {},
	judge : function(){
		if(!this.answerAll()) {
			return;  // 若未答完题，返回
		}
		
		// 进入迫选模式


	},
	answerAll : function(){
		var _allAnswered = true;
		$('#quiz-group-' + Pager.currentPage).find('.question-card').each(function(questionIndex){
			var _answered = false;

			$(this).find(':radio').each(function(){
				if($(this).prop('checked')){
					_answered = true;
					var	qtId = $(this).attr('question');
					
					ForchChoise.currentPageAnswer[qtId] = $(this).attr('answer');
					return false;
				}
			});

			if(!_answered) {
				_allAnswered = false;
				return false;
			}
		});
		return _allAnswered;
	},
	mode : function(){

	}
}