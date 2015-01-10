/**

 * Author: liunan
 * Created: 2011-4-15
 */
package com.huijia.eap.demo.module;

import gauge.GaugeChart;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import jofc2.model.Chart;
import jofc2.model.Text;
import jofc2.model.axis.Label;
import jofc2.model.axis.RadarAxis;
import jofc2.model.axis.XAxis;
import jofc2.model.axis.XAxisLabels;
import jofc2.model.axis.YAxis;
import jofc2.model.axis.YLabel;
import jofc2.model.elements.AbstractDot;
import jofc2.model.elements.AnimatedElement.OnShow;
import jofc2.model.elements.AreaChart;
import jofc2.model.elements.BarChart;
import jofc2.model.elements.BarChart.Style;
import jofc2.model.elements.HorizontalBarChart;
import jofc2.model.elements.HorizontalStackedBarChart;
import jofc2.model.elements.HorizontalStackedBarChart.HorizontalStack;
import jofc2.model.elements.Legend;
import jofc2.model.elements.LineChart;
import jofc2.model.elements.LineChart.Dot;
import jofc2.model.elements.NullElement;
import jofc2.model.elements.PieChart;
import jofc2.model.elements.ScatterChart;
import jofc2.model.elements.ScatterChart.Point;
import jofc2.model.elements.StackedBarChart;
import jofc2.model.elements.StackedBarChart.Key;
import jofc2.model.elements.StackedBarChart.Stack;
import jofc2.model.elements.StackedBarChart.StackValue;
import jofc2.model.elements.Tooltip;

import org.apache.log4j.Logger;
import org.nutz.lang.Files;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.huijia.eap.annotation.AuthBy;
import com.huijia.eap.commons.mvc.adaptor.ChartSaveAdaptor;

@AuthBy(module="demo")
@Ok("chart") //!!!Important!!!，必须使用此视图
@Fail("json")
@At("/demo/chart")
public class ChartHelloWorld {
	
	private static final Logger logger = Logger.getLogger(ChartHelloWorld.class);
	
	@At
	@Ok("jsp:jsp.demo.charts")
	public void view() {
		logger.debug("showing chart demo page");
	}

	/**
	 * 柱图范例
	 * @return
	 */
	@At("/barChart")
	public Object viewBarChart() {
		BarChart bc = new BarChart(Style.GLASS/* Glass样式 */).addValues(1, 2, 3, 4, 5/* 添加5个值，从左到右 */);
		bc.setTooltip(""); //不显示tooltip
		bc.setText("测试（次）"); //这个系列的名称
		OnShow onshow = new OnShow(OnShow.TYPE_FADEIN /* 淡入 */, 0.2f /* 每个值之间间隔0.2秒显示 */, 0.5f /* 矩阵显示后0.5秒后开始显示值 */); //设置动画显示效果
		bc.setOnShow(onshow);
		bc.setId("1"); //设置这个系列的ID，用于在图例的显隐控制时识别，如果不需要图例显隐控制则可不设置
		
		BarChart bc2 = new BarChart(Style.GLASS); //.addValues(5, 4, 3, 2, 1); //创建第二个系列
		for (int i = 0; i < 5; i++) {
			bc2.addBars(new BarChart.Bar(5 - i).setOnClickText("label:" + i));
		}
		bc2.setText("测试2（个数）");
		bc2.setColour("#ff9900"); //设置显示值的颜色
		bc2.setOnClick("click_handler"); //设置点击值时，执行js脚本的函数名称
//		bc2.setOnClickText("{value: #val#, x_lable: '#x_label#', anything: 'anything'}"); //设置点击时，传递给js脚本函数的额外（第二个）参数。第一个参数是OFC自动控制传入的，是点击的值的顺序号，从0开始计算
		bc2.setTooltip("Hello<br>val = #val#"); //设置Tooltip
		bc.setId("2"); //设置这个系列的ID
		
		Chart chart = new Chart("柱图样例" /* 图标题 */).setXLegend(new Text("X轴"))
				.setXAxis(new XAxis().addLabels("一月", "二月", "三月", "四月", "五月" /* 横轴坐标显示标示，非必须 */))
				.setYAxis(new YAxis().setLabels("", "1", "2", "3", "4", "5")).addElements(bc, bc2 /* 将两个系列添加到图中 */);
		Legend legend = new Legend();
		chart.setLegend(legend); //设置图例样式，缺省显示在右侧
		
//		YAxisLabels label = chart.getYAxis().getLabels();
//		label.setText("#val# 次");
		
		return chart; //直接返回Chart对象即可
	}
	
	@At
	@Ok("jsp:jsp.demo.chart-reload-bug")
	public void viewReloadBug() {
		
	}
	
	@At("/reloadBug")
	public Chart getReloadBug() {
		BarChart bc = new BarChart(Style.GLASS/* Glass样式 */);
		for (int i = 0; i < 5; i++) {
			bc.addValues(Math.random() * 10);
		}
		bc.setTooltip(""); //不显示tooltip
		bc.setText("测试（次）"); //这个系列的名称
		OnShow onshow = new OnShow(OnShow.TYPE_FADEIN /* 淡入 */, 0.2f /* 每个值之间间隔0.2秒显示 */, 0.5f /* 矩阵显示后0.5秒后开始显示值 */); //设置动画显示效果
		bc.setOnShow(onshow);
		bc.setOnClick("tab_switch_handler");
		
		Chart chart = new Chart("柱图样例" /* 图标题 */)
							.setXAxis(new XAxis().addLabels("一月", "二月", "三月", "四月", "五月" /* 横轴坐标显示标示，非必须 */))
							.setYAxis(new YAxis()).addElements(bc);
		return chart;
	}
	
	@At("/hBarChart")
	public Chart viewHBarChart() {
		HorizontalBarChart hbar = new HorizontalBarChart();
		for (int i = 0; i < 5; i++) {
			hbar.addBars(new HorizontalBarChart.Bar(i + 20).setOnClickText("label:" + (i + 1)));
		}
		hbar.setOnClick("hbar_click_handler");
		hbar.setTooltip("#y_label#(#index#): #val#");
		Chart chart = new Chart("水平柱图").addElements(hbar);
		chart.getYAxis().setOffset(true);
		for (int i = 0; i < 6; i++) {
			if (i > 0)
				chart.getYAxis().getYAxisLabels().addLabels(i + "月");
		}
		return chart;
	}
	
	/**
	 * 饼图
	 * @return
	 */
	@At("/pieChart")
	public Chart viewPieChart() {
		String[] months = new String[] {
			"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"
		};
		PieChart pc = new PieChart(); //.addSlice(4, "一月").addSlice(9, "二月").addSlice(2, "三月").addSlice(4, "四月").addSlice(6, "五月"); //添加五块扇区
		for (int i = 0; i < months.length; i++) {
			PieChart.Slice slice = new PieChart.Slice(Math.random() * 10, months[i]);
			slice.setOnClickText("{\"value\":#val#, \"total\":#total#, \"percent\":\"#percent#\", \"label\":\"#label#\"}");
			pc.addSlices(slice);
		}
		pc.setOnClick("pie_click_handler");
		
		pc.setAnimate(true); //设置使用动画效果
		
		Chart chart = new Chart("饼图").addElements(pc);
		
		return chart;
	}
	
	/**
	 * 线图/时序图
	 * @return
	 */
	@At("/lineChart")
	public Chart viewLineChart() {
		Calendar cal = Calendar.getInstance();
		long current = cal.getTimeInMillis();
		cal.add(Calendar.SECOND, -10);
		long start = cal.getTimeInMillis();

		LineChart lc = new LineChart().setDotStyle(
				new AbstractDot.Style(AbstractDot.Style.Type.H0LLOW_DOT /* 使用Hollow点样式 */, null, 3 /* 数据点半径 */, 2 /* 数据点与连线间的间隔 */)
				).setWidth(3 /* 连线粗度 */);
//		ScatterChart lc = new ScatterChart(ScatterChart.Style.LINE).setDotStyle(new AbstractDot.Style(AbstractDot.Style.Type.H0LLOW_DOT, null, 3, 2));
		lc.setOnShow(new OnShow(OnShow.TYPE_POPUP)); //设置显示动画效果
		for (long i = start; i <= current; i += 1000l) {
			if ((i - start) / 1000l != 7) {
				Double value = Math.random() * 10;
				lc.addDots(new LineChart.ScatterDot(i / 1000, value)); //添加随机数据
//				lc.addPoint(i / 1000, Math.random() * 10);
			} else //添加一个空值点，以便产生不连续的线图
				lc.getValues().add(new NullElement()); //TODO:目前不支持断续线图
		}
		
		Chart chart = new Chart().addElements(lc);
		XAxis xaxis = new XAxis();
		xaxis.setRange(start / 1000, current / 1000, 1l); //设置时间轴的范围和间隔，单位为秒
		XAxisLabels xlabels = new XAxisLabels();
		xlabels.setText("#date:H:i:s#" /* 设置时间显示格式 */).setRotation(70);//.setRotation(Label.Rotation.DIAGONAL /* 设置横轴文字旋转角度 */);
		xaxis.setXAxisLabels(xlabels);
		chart.setXAxis(xaxis);
		
		int step = 2;
		chart.computeYAxisRange(step);
		for (int i = chart.getYAxis().getMin().intValue(); i < chart.getYAxis().getMax().intValue() + 1; i+=step) {
			chart.getYAxis().addLabels(new YLabel(String.valueOf(i), i));
		}
		return chart;
	}
	
	@At("/areaChart")
	public Chart viewAreaChart() {
		AreaChart ac = new AreaChart();
		ac.setTooltip("#val# K");
		Chart chart = new Chart("面积图").addElements(ac);
		XAxis axis = new XAxis();
		axis.setOffset(false);
		axis.setSteps(15);
		chart.setXAxis(axis);
		for (int i = 0; i < 10; i++) {
			ac.addValues(i + 1);
			axis.addLabels(new Label("数字" + i));
		}
		chart.setYAxisAutoRange(false);
		YAxis yaxis = new YAxis();
		yaxis.setMax(20);
		yaxis.setMin(1);
		chart.setYAxis(yaxis);
		
		return chart;
	}
	
	/**
	 * 堆积图
	 * @return
	 */
	@At("/stackbarChart")
	public Chart viewStackbarChart() {
		
		//称横轴的划分维度（条件）为第一统计条件
		//称第一统计条件中，再进行划分的维度为第二统计条件
		
		StackedBarChart sbc = new StackedBarChart();
		for (int i = 0; i < 4; i++) {
			Stack value = new Stack(); //每第一统计条件中的数据
			StackValue sv = null;
			for (int j = 0; j < 3; j++) {
				//value.addValues(Math.random() * 10); //每第二统计条件的数据
				sv = new StackValue(Math.random() * 10, null, null, "stackedClick", "{\"key\":\"#key#\", \"x_label\":\"#x_label#\", \"value\":#val#}");
				value.addStackValues(sv);
			}
			value.addStackValues(sv);
			sbc.addStack(value);
		}
		sbc.addKeys(new Key(null, "甲", null), new Key(null, "乙", null), new Key(null, "丙", null)); //添加第二统计条件
		//sbc.addColours("#C4D318", "#50284A", "#7D7B6A"); //设置第二统计条件的颜色序列
		sbc.setOnShow(new OnShow(OnShow.TYPE_POPUP)); 
		sbc.setTooltip("#key#(#x_label#): #val#"); //设置Tooltip
		
		YAxis yaxis = new YAxis();
		yaxis.setMax(30 /* 设置Y轴最大值 */).setSteps(3 /* 设置步进值 */);
		Chart chart = new Chart("堆积图").setXAxis(new XAxis().addLabels("春", "夏", "秋", "冬" /* 设置X轴第一统计条件的各名称 */)).setYAxis(yaxis);
		chart.addElements(sbc).setTooltip(new Tooltip().setHover(/* 设置tooltip的显示方式为只有当鼠标悬停时才显示。默认为当离开后，tooltip依然显示 */));
		chart.setTitle(new Text("测试")).setXLegend(new Text("横轴图例"));
		return chart;
	}
	
	/**
	 * 水平堆积图
	 * @return
	 */
	@At("hStackbarChart")
	public Chart viewHStackbarChart() {
		
		//称横轴的划分维度（条件）为第一统计条件
		//称第一统计条件中，再进行划分的维度为第二统计条件
		
		HorizontalStackedBarChart sbc = new HorizontalStackedBarChart();
		for (int i = 0; i < 4; i++) {
			HorizontalStack value = new HorizontalStack(); //每第一统计条件中的数据
			for (int j = 0; j < 3; j++) {
				//value.addSequenceValues(Math.random() * 10); //每第二统计条件的数据
				HorizontalStackedBarChart.HorizontalStackValue sv = new HorizontalStackedBarChart.HorizontalStackValue(Math.random() * 10);
				if (j == 1) {
					sv.setColor("#ff0000");
				}
				sv.setOnClickText("{\"key\":\"#key#\", \"y_label\":\"#y_label#\", \"left\":#left#, \"right\":#right#, \"value\":#val#}");
				value.addStackValues(sv);
			}
			sbc.addStack(value);
		}
		sbc.addKeys(new Key(null, "甲", null), new Key(null, "乙", null), new Key(null, "丙", null)); //添加第二统计条件
		sbc.setOnClick("hstack_click_handler");
		sbc.setTooltip("#key#(#y_label#):#val#");
		
		YAxis yaxis = new YAxis();
		yaxis.addLabels((YLabel) new YLabel("春").setColour("#ffffff"), (YLabel) new YLabel("夏").setColour("#ff0000"), (YLabel) new YLabel("秋").setColour("#ff9900"), (YLabel) new YLabel("冬").setColour("#00ff00")) /* 设置X轴第一统计条件的各名称 */;
		Chart chart = new Chart("水平堆积图").setYAxis(yaxis);
		chart.setXAxisAutoRange(true/* 可以设置水平坐标范围自适应 */).addElements(sbc).setTooltip(new Tooltip().setHover(/* 设置tooltip的显示方式为只有当鼠标悬停时才显示。默认为当离开后，tooltip依然显示 */));
		return chart;
	}

	/**
	 * 雷达图
	 * @return
	 */
	@At("/radarChart")
	public Chart viewRadarChart() {
		AreaChart ac = new AreaChart();
		ac.setWidth(1).addValues(3, 4, 5, 4, 3, 3, 2.5 /* 添加7个值 */).setLoop(true /* 必须设置 */);
		
		RadarAxis axis = new RadarAxis();
		axis.addSpokeLabels("星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日" /* 设置雷达的每个方向的显示名称 */).addLabels("低", "中低", "中", "中高", "高", "5").setMax(5);
		
		Chart chart = new Chart("雷达图").setRadarAxis(axis).addElements(ac);
		return chart;
	}
	
	@At
	public Chart viewColors(@Param("param1") String param1, @Param("param2") int param2) {
		BarChart bc = new BarChart(Style.GLASS).addValues(9,9,9,9,9,9,9,9,9,9);
		bc.setTooltip(""); //不显示tooltip
		
		Chart chart = new Chart("显示颜色序列").addElements(bc);
		chart.getYAxis().setLabels("1", "2", "3", "4", "5", "6", "7", "8", "9");
		chart.setYAxisAutoRange(true);
		return chart;
	}
	
	@At("/barChartWithLegend")
	public Chart viewBarChartWithLegend() {
		Chart chart = new Chart("可以显示图例的柱图");
		for (int i = 0; i < 10; i++) {
			BarChart bc = new BarChart(Style.GLASS).addValues(9);
			bc.setTooltip("");
			bc.setText("这里可以是很长很长的名字-" + i);
			bc.setBarwidth(0.95f).setOverlap(-0.33f);
			chart.addElements(bc);
		}
		chart.getXAxis().setLabels("");
		return chart;
	}
	
	@At("/gaugeChart")
	public GaugeChart viewGaugeChart() {
		GaugeChart chart = new GaugeChart();
		chart.setValue(Double.valueOf(Math.random() * 100).floatValue());
		chart.setValueUnit("K");
		
		return chart;
	}
	
	@At("/anotherGaugeChart")
	public GaugeChart viewAnotherGaugeChart() {
		GaugeChart chart = new GaugeChart();
		chart.setMajorTickMarkNumber(5);
		chart.setMajorTickLabel("正常", "一般", "轻微", "严重", "致命");
		chart.setValue(25f);
		chart.setShowValue(false);
		chart.setShowAlerts(true);
		chart.setAlertColors(new int[] {
				0xFF0000, 0xFFD700, 0x00FF00
		});
		chart.setAlertAlphas(new float[] {1.0f, 1.0f, 1.0f});
		
		return chart;
	}
	
	@At("/sonarChart")
	public Chart viewSonarChart() {
		Chart chart = new Chart("");
		for (int i = 0; i < 5; i++) {
			AreaChart ac = new AreaChart();
			double value = ((int) (Math.random() * 50)) / 10;
			ac.setWidth(1).setDotStyle(new AbstractDot.Style(AbstractDot.Style.Type.ANCHOR).setDotSize(5)).setLoop(true);
			for (int j = i; j > 0; j--)
				ac.addDots((Dot) null);
			ac.addDots(
					(Dot) new Dot(value).setOnClick("sonar_click_handler").setOnClickText("{value:" + value + "}")); 
			ac.setTooltip("#y#");
			chart.addElements(ac);
		}
		
		RadarAxis axis = new RadarAxis();
		axis.setMax(5);
		
		chart.setRadarAxis(axis);
		return chart;
	}
	
	@At("/dualAxis")
	public Chart viewDualAxis() {
		Chart chart = new Chart("双纵轴多样式统计图");
		chart.getYAxis().setMax(10);
		YAxis rAxis = new YAxis();
		rAxis.setRange(0, 120);
		rAxis.setSteps(30);
		chart.setYAxisRight(rAxis);
		chart.setYAxisAutoRange(false);
		
		BarChart bc = new BarChart();
		bc.addValues(4, 2, 6, 9, 5);
		chart.addElements(bc);
		
		ScatterChart sc = new ScatterChart();
		sc.addPoints(new Point(0, 2), new Point(1, 5), new Point(2, 4), new Point(3, 8), new Point(4, 3));
		sc.setDotStyle(new AbstractDot.Style(AbstractDot.Style.Type.ANCHOR).setSides(4).setDotSize(10).setHollow(false).setRotation(45));
		chart.addElements(sc);
		
		return chart;
	}

	/**
	 * 下载保存成图片
	 * @TODO: 由于Data URI Scheme(data:image/png)方式不被IE7支持，暂仅提供后台方式
	 * @param image
	 * @return
	 */
	@At
	@Ok("dl:chart.png")
	@AdaptBy(type=ChartSaveAdaptor.class)
	public File save(@Param("chart") byte[] image) {
		
		try {
			File file = File.createTempFile(UUID.randomUUID().toString(), "png");
			Files.write(file, image);
			return file;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
//		return image;
	}
}
