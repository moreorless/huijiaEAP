package com.huijia.eap.quiz.data.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 问卷1的私有变量集合
 * 
 * @author jianglei
 * 
 */
public class TeamReportMentalCheckupParamSet {

	// （二）心理健康体检结果分析
	// 1. 员工心理健康总体概况
	public double wholeScore;// 整体心理健康得分
	public String wholeEvaluation; // 处于心理健康中等水平

	public long jiaoChaUserCount; // 心理体检结果显示，[1]名员工心理健康水平较差
	public String jiaoChaRatio; // 占有效样本的[10%]
	public long jiaoChaLowestScore; // 其中最低得分为[133]分

	public long buLiangUserCount;
	public String buLiangRatio;
	// public long buLiangLowestScore;

	public long zhongDengUserCount;
	public String zhongDengRatio;
	// public long zhongDengLowestScore;

	public long jiaoHaoUserCount;
	public String jiaoHaoRatio;
	// public long jiaoHaoLowestScore;

	public long henHaoUserCount;
	public String henHaoRatio;
	// public long henHaoLowestScore;

	public long needJingTiRatioUserCount;
	public String needJingTiRatio; // 需要特别引起警惕的员工比例为[30%]（此处比例为“较差”+“不良”）
	public long needTiGaoUserCount;
	public String needTiGaoRatio; // 心理健康 水平有待提高的员工为[20%]（此处比例为“中等”百分比）。

	// 2. 心理健康各维度结果分析
	// (1) 各维度得分的平均分
	// public String categoryEvaluation;
	// //认知维度、情绪维度、意识行为维度、生理症状维度、社会交往维度和自我防御维度的各维度得分都处于中等范围，总体差异程度不大。

	// (2) 各维度得分高低程度的人数比较
	public String lowScoreCategories; // 所选样本员工在6个维度都是低分人数多的是[行为表现维度]，而高分人数最多的是积极心态维度
	public String highScoreCategories; // 所选样本员工在6个维度都是低分人数多的是行为表现维度，而高分人数最多的是[积极心态维度]
	public String percent30Categories; // [行为表现维度]的低分人群均超过总有效样本的30%，
	public String percent30CategoryCount; // 这说明这[一]个维度是企业特别需要企业关注的部分。
	public int percent30CategoryNum; // 用于判断界面要不要显示上一句话

	// 图表使用
	public Map<String, String> chartDataWholePie = new HashMap<String, String>();
	public List<String> chartDataWholeBar = new LinkedList<String>();
	public List<Long> chartDataWholeGauss = new ArrayList<Long>();
	public List<String> chartDataCategoryAverage = new ArrayList<String>();
	public List<Long> chartDataCategoryNumLow = new ArrayList<Long>();
	public List<Long> chartDataCategoryNumHigh = new ArrayList<Long>();
	public List<Long> chartDataCategoryNumMiddle = new ArrayList<Long>();

	public void init() {
		wholeScore = 0.0;// 整体心理健康得分
		jiaoChaUserCount = 0; // 心理体检结果显示，[1]名员工心理健康水平较差
		jiaoChaLowestScore = 0; // 其中最低得分为[133]分
		buLiangUserCount = 0;
		zhongDengUserCount = 0;
		jiaoHaoUserCount = 0;
		henHaoUserCount = 0;
		needJingTiRatioUserCount = 0;
		needTiGaoUserCount = 0;
		percent30CategoryNum = 0; // 用于判断界面要不要显示上一句话

		wholeEvaluation = ""; // 处于心理健康中等水平
		jiaoChaRatio = ""; // 占有效样本的[10%]
		buLiangRatio = "";
		zhongDengRatio = "";
		jiaoHaoRatio = "";
		henHaoRatio = "";
		needJingTiRatio = ""; // 需要特别引起警惕的员工比例为[30%]（此处比例为“较差”+“不良”）
		needTiGaoRatio = ""; // 心理健康 水平有待提高的员工为[20%]（此处比例为“中等”百分比）。
		lowScoreCategories = ""; // 所选样本员工在6个维度都是低分人数多的是[行为表现维度]，而高分人数最多的是积极心态维度
		highScoreCategories = ""; // 所选样本员工在6个维度都是低分人数多的是行为表现维度，而高分人数最多的是[积极心态维度]
		percent30Categories = ""; // [行为表现维度]的低分人群均超过总有效样本的30%，
		percent30CategoryCount = ""; // 这说明这[一]个维度是企业特别需要企业关注的部分。

		chartDataWholePie = new HashMap<String, String>();
		chartDataWholeBar = new LinkedList<String>();
		chartDataWholeGauss = new ArrayList<Long>();
		for (int i = 0; i < 25; i++)
			chartDataWholeGauss.add((long) 0);
		chartDataCategoryAverage = new ArrayList<String>();
		chartDataCategoryNumLow = new ArrayList<Long>();
		chartDataCategoryNumHigh = new ArrayList<Long>();
		chartDataCategoryNumMiddle = new ArrayList<Long>();
	}

	public double getWholeScore() {
		return wholeScore;
	}

	public void setWholeScore(double wholeScore) {
		this.wholeScore = wholeScore;
	}

	public String getWholeEvaluation() {
		return wholeEvaluation;
	}

	public void setWholeEvaluation(String wholeEvaluation) {
		this.wholeEvaluation = wholeEvaluation;
	}

	public long getJiaoChaUserCount() {
		return jiaoChaUserCount;
	}

	public void setJiaoChaUserCount(long jiaoChaUserCount) {
		this.jiaoChaUserCount = jiaoChaUserCount;
	}

	public String getJiaoChaRatio() {
		return jiaoChaRatio;
	}

	public void setJiaoChaRatio(String jiaoChaRatio) {
		this.jiaoChaRatio = jiaoChaRatio;
	}

	public long getJiaoChaLowestScore() {
		return jiaoChaLowestScore;
	}

	public void setJiaoChaLowestScore(long jiaoChaLowestScore) {
		this.jiaoChaLowestScore = jiaoChaLowestScore;
	}

	public long getBuLiangUserCount() {
		return buLiangUserCount;
	}

	public void setBuLiangUserCount(long buLiangUserCount) {
		this.buLiangUserCount = buLiangUserCount;
	}

	public String getBuLiangRatio() {
		return buLiangRatio;
	}

	public void setBuLiangRatio(String buLiangRatio) {
		this.buLiangRatio = buLiangRatio;
	}

	public long getZhongDengUserCount() {
		return zhongDengUserCount;
	}

	public void setZhongDengUserCount(long zhongDengUserCount) {
		this.zhongDengUserCount = zhongDengUserCount;
	}

	public String getZhongDengRatio() {
		return zhongDengRatio;
	}

	public void setZhongDengRatio(String zhongDengRatio) {
		this.zhongDengRatio = zhongDengRatio;
	}

	public long getJiaoHaoUserCount() {
		return jiaoHaoUserCount;
	}

	public void setJiaoHaoUserCount(long jiaoHaoUserCount) {
		this.jiaoHaoUserCount = jiaoHaoUserCount;
	}

	public String getJiaoHaoRatio() {
		return jiaoHaoRatio;
	}

	public void setJiaoHaoRatio(String jiaoHaoRatio) {
		this.jiaoHaoRatio = jiaoHaoRatio;
	}

	public long getHenHaoUserCount() {
		return henHaoUserCount;
	}

	public void setHenHaoUserCount(long henHaoUserCount) {
		this.henHaoUserCount = henHaoUserCount;
	}

	public String getHenHaoRatio() {
		return henHaoRatio;
	}

	public void setHenHaoRatio(String henHaoRatio) {
		this.henHaoRatio = henHaoRatio;
	}

	public String getNeedJingTiRatio() {
		return needJingTiRatio;
	}

	public void setNeedJingTiRatio(String needJingTiRatio) {
		this.needJingTiRatio = needJingTiRatio;
	}

	public String getNeedTiGaoRatio() {
		return needTiGaoRatio;
	}

	public void setNeedTiGaoRatio(String needTiGaoRatio) {
		this.needTiGaoRatio = needTiGaoRatio;
	}

	public String getLowScoreCategories() {
		return lowScoreCategories;
	}

	public void setLowScoreCategories(String lowScoreCategories) {
		this.lowScoreCategories = lowScoreCategories;
	}

	public String getHighScoreCategories() {
		return highScoreCategories;
	}

	public void setHighScoreCategories(String highScoreCategories) {
		this.highScoreCategories = highScoreCategories;
	}

	public String getPercent30Categories() {
		return percent30Categories;
	}

	public void setPercent30Categories(String percent30Categories) {
		this.percent30Categories = percent30Categories;
	}

	public String getPercent30CategoryCount() {
		return percent30CategoryCount;
	}

	public void setPercent30CategoryCount(String percent30CategoryCount) {
		this.percent30CategoryCount = percent30CategoryCount;
	}

	public int getPercent30CategoryNum() {
		return percent30CategoryNum;
	}

	public void setPercent30CategoryNum(int percent30CategoryNum) {
		this.percent30CategoryNum = percent30CategoryNum;
	}

	public Map<String, String> getChartDataWholePie() {
		return chartDataWholePie;
	}

	public void setChartDataWholePie(Map<String, String> chartDataWholePie) {
		this.chartDataWholePie = chartDataWholePie;
	}

	public List<String> getCharDataWholeBar() {
		return chartDataWholeBar;
	}

	public void setCharDataWholeBar(List<String> charDataWholeBar) {
		this.chartDataWholeBar = charDataWholeBar;
	}

	public List<Long> getCharDataWholeGauss() {
		return chartDataWholeGauss;
	}

	public void setCharDataWholeGauss(List<Long> charDataWholeGauss) {
		this.chartDataWholeGauss = charDataWholeGauss;
	}

	public List<String> getChartDataCategoryAverage() {
		return chartDataCategoryAverage;
	}

	public void setChartDataCategoryAverage(
			List<String> chartDataCategoryAverage) {
		this.chartDataCategoryAverage = chartDataCategoryAverage;
	}

	public List<Long> getChartDataCategoryNumLow() {
		return chartDataCategoryNumLow;
	}

	public void setChartDataCategoryNumLow(List<Long> chartDataCategoryNumLow) {
		this.chartDataCategoryNumLow = chartDataCategoryNumLow;
	}

	public List<Long> getChartDataCategoryNumHigh() {
		return chartDataCategoryNumHigh;
	}

	public void setChartDataCategoryNumHigh(List<Long> chartDataCategoryNumHigh) {
		this.chartDataCategoryNumHigh = chartDataCategoryNumHigh;
	}

	public List<Long> getChartDataCategoryNumMiddle() {
		return chartDataCategoryNumMiddle;
	}

	public void setChartDataCategoryNumMiddle(List<Long> chartDataCategoryNumMiddle) {
		this.chartDataCategoryNumMiddle = chartDataCategoryNumMiddle;
	}

}
