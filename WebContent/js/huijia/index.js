var UserDialog = {
	show : function(){
		$("#user-dialog").modal('show');
	}
}
$(document).ready(function(){
	$("#nav-items li").click(function(){
		$("#nav-items").find('li').removeClass('active');
		$(this).addClass('active');
		$("#main-iframe").attr('src', '${base}' + $(this).attr('data'));
	});
	
	$("#nav-items li").first().click();
	
	$('#btn-save').click(function(){
		if(!$('#userForm').valid()) return;
		$.ajax({
			url : '${base}/user/editAjax',
			dataType : 'json',
			type : 'post',
			data : $('#userForm').serialize(),
			success : function(){
				$('#save-message').show();
				$('#save-message strong').text('保存成功');
				
				setTimeout(function(){$('#save-message').hide();}, 5000);
			}
		});
	});
	
	$('#btn-user').click(function(){
		UserDialog.show();
	});
});