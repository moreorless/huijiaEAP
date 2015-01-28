
require.config({
    paths: {
        echarts: '../echarts'
    }
});
require([
         'echarts',
         'echarts/chart/bar',
         'echarts/theme/green'
         ]);

$(document).ready(function(){
	
	$('.eap-report').each(function(){
		var data = $(this).attr('data');
		if(data.indexOf('report:column_h') != -1){
			var quizIndex = data.substr(data.lastIndexOf(':') + 1);
			quizIndex = parseInt(quizIndex);
			var myChart = echarts.init($(this).get(0), 'green');
			myChart.setOption(column_h_Options[quizIndex]);
		}
	});
});