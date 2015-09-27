package com.huijia.eap.quiz.data.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TeamReportEmotionManagementSet {

	// 3.2 四大维度整体概述
	// 存放柱状图所使用的数据
	// 格式如下(List中存放4个数据)：
	// 情绪感知,5.43
	// 情绪控制,4,80
	// 社交技巧,3.38
	// 情绪利用,5.51
	// 以上数据在jsp中依次读取，利用split函数进行分割，分别存入绘制柱状图所需的names和datas中
	public List<String> topLevelAverageScoreList = new ArrayList<String>();
	public List<String> topLevelStandardScoreList = new ArrayList<String>();
	// 存放整体维度得分的评价信息
	public String topLevelAverageScoreEvaluation;

	// 存放图所雷达图使用的数据
	public List<String> subLevelAverageScoreList = new ArrayList<String>();
	public List<String> subLevelStandardScoreList = new ArrayList<String>();
	// 存放9个子维度得分的评价信息
	public String subLevelAverageScoreEvaluation;

	// 3.3 九个子维度的对比分析
	// 超高分是指个人报告中此维度常模得分大于等于9分的人数百分比，
	// 高分为7、8分人数百分比，
	// 中等为5、6分人数百分比，
	// 其余为低分。

	// 5%员工的得分超高，属于情绪自我感知能力很强的人，15%的员工得分属于高分，60%的员工得分属于中等；10%员工的自我情绪感知能力比较差。
	public String evaluationZiwoqingxuganzhi;
	public String evaluationTarenqingxuganzhi;
	public String evaluationKongzhili;
	public String evaluationWendingxing;
	public String evaluationZiwojili;
	public String evaluationBiaoda;
	public String evaluationShiyingli;
	public String evaluationGanranli;
	public String evaluationWentijiejue;

	// var datas_ideal = [ 70,86,90,68 ];
	// 雷达图数据，依次为 [超高分,高分,中等,较差]
	public String dataZiwoqingxuganzhi;
	public String dataTarenqingxuganzhi;
	public String dataKongzhili;
	public String dataWendingxing;
	public String dataZiwojili;
	public String dataBiaoda;
	public String dataShiyingli;
	public String dataGanranli;
	public String dataWentijiejue;

	public void init() {
		topLevelAverageScoreList = new ArrayList<String>();
		topLevelStandardScoreList = new ArrayList<String>();
		topLevelAverageScoreEvaluation = "";

		subLevelAverageScoreList = new ArrayList<String>();
		subLevelStandardScoreList = new ArrayList<String>();
		subLevelAverageScoreEvaluation = "";

		dataZiwoqingxuganzhi = "";
		dataTarenqingxuganzhi = "";
		dataKongzhili = "";
		dataWendingxing = "";
		dataZiwojili = "";
		dataBiaoda = "";
		dataShiyingli = "";
		dataGanranli = "";
		dataWentijiejue = "";

		evaluationZiwoqingxuganzhi = "";
		evaluationTarenqingxuganzhi = "";
		evaluationKongzhili = "";
		evaluationWendingxing = "";
		evaluationZiwojili = "";
		evaluationBiaoda = "";
		evaluationShiyingli = "";
		evaluationGanranli = "";
		evaluationWentijiejue = "";

	}

	public List<String> getTopLevelAverageScoreList() {
		return topLevelAverageScoreList;
	}

	public void setTopLevelAverageScoreList(
			List<String> topLevelAverageScoreList) {
		this.topLevelAverageScoreList = topLevelAverageScoreList;
	}

	public List<String> getTopLevelStandardScoreList() {
		return topLevelStandardScoreList;
	}

	public void setTopLevelStandardScoreList(
			List<String> topLevelStandardScoreList) {
		this.topLevelStandardScoreList = topLevelStandardScoreList;
	}

	public String getTopLevelAverageScoreEvaluation() {
		return topLevelAverageScoreEvaluation;
	}

	public void setTopLevelAverageScoreEvaluation(
			String topLevelAverageScoreEvaluation) {
		this.topLevelAverageScoreEvaluation = topLevelAverageScoreEvaluation;
	}

	public List<String> getSubLevelAverageScoreList() {
		return subLevelAverageScoreList;
	}

	public void setSubLevelAverageScoreList(
			List<String> subLevelAverageScoreList) {
		this.subLevelAverageScoreList = subLevelAverageScoreList;
	}

	public List<String> getSubLevelStandardScoreList() {
		return subLevelStandardScoreList;
	}

	public void setSubLevelStandardScoreList(
			List<String> subLevelStandardScoreList) {
		this.subLevelStandardScoreList = subLevelStandardScoreList;
	}

	public String getSubLevelAverageScoreEvaluation() {
		return subLevelAverageScoreEvaluation;
	}

	public void setSubLevelAverageScoreEvaluation(
			String subLevelAverageScoreEvaluation) {
		this.subLevelAverageScoreEvaluation = subLevelAverageScoreEvaluation;
	}

	public String getEvaluationZiwoqingxuganzhi() {
		return evaluationZiwoqingxuganzhi;
	}

	public void setEvaluationZiwoqingxuganzhi(String evaluationZiwoqingxuganzhi) {
		this.evaluationZiwoqingxuganzhi = evaluationZiwoqingxuganzhi;
	}

	public String getEvaluationTarenqingxuganzhi() {
		return evaluationTarenqingxuganzhi;
	}

	public void setEvaluationTarenqingxuganzhi(
			String evaluationTarenqingxuganzhi) {
		this.evaluationTarenqingxuganzhi = evaluationTarenqingxuganzhi;
	}

	public String getEvaluationKongzhili() {
		return evaluationKongzhili;
	}

	public void setEvaluationKongzhili(String evaluationKongzhili) {
		this.evaluationKongzhili = evaluationKongzhili;
	}

	public String getEvaluationWendingxing() {
		return evaluationWendingxing;
	}

	public void setEvaluationWendingxing(String evaluationWendingxing) {
		this.evaluationWendingxing = evaluationWendingxing;
	}

	public String getEvaluationZiwojili() {
		return evaluationZiwojili;
	}

	public void setEvaluationZiwojili(String evaluationZiwojili) {
		this.evaluationZiwojili = evaluationZiwojili;
	}

	public String getEvaluationBiaoda() {
		return evaluationBiaoda;
	}

	public void setEvaluationBiaoda(String evaluationBiaoda) {
		this.evaluationBiaoda = evaluationBiaoda;
	}

	public String getEvaluationShiyingli() {
		return evaluationShiyingli;
	}

	public void setEvaluationShiyingli(String evaluationShiyingli) {
		this.evaluationShiyingli = evaluationShiyingli;
	}

	public String getEvaluationGanranli() {
		return evaluationGanranli;
	}

	public void setEvaluationGanranli(String evaluationGanranli) {
		this.evaluationGanranli = evaluationGanranli;
	}

	public String getEvaluationWentijiejue() {
		return evaluationWentijiejue;
	}

	public void setEvaluationWentijiejue(String evaluationWentijiejue) {
		this.evaluationWentijiejue = evaluationWentijiejue;
	}

	public String getDataZiwoqingxuganzhi() {
		return dataZiwoqingxuganzhi;
	}

	public void setDataZiwoqingxuganzhi(String dataZiwoqingxuganzhi) {
		this.dataZiwoqingxuganzhi = dataZiwoqingxuganzhi;
	}

	public String getDataTarenqingxuganzhi() {
		return dataTarenqingxuganzhi;
	}

	public void setDataTarenqingxuganzhi(String dataTarenqingxuganzhi) {
		this.dataTarenqingxuganzhi = dataTarenqingxuganzhi;
	}

	public String getDataKongzhili() {
		return dataKongzhili;
	}

	public void setDataKongzhili(String dataKongzhili) {
		this.dataKongzhili = dataKongzhili;
	}

	public String getDataWendingxing() {
		return dataWendingxing;
	}

	public void setDataWendingxing(String dataWendingxing) {
		this.dataWendingxing = dataWendingxing;
	}

	public String getDataZiwojili() {
		return dataZiwojili;
	}

	public void setDataZiwojili(String dataZiwojili) {
		this.dataZiwojili = dataZiwojili;
	}

	public String getDataBiaoda() {
		return dataBiaoda;
	}

	public void setDataBiaoda(String dataBiaoda) {
		this.dataBiaoda = dataBiaoda;
	}

	public String getDataShiyingli() {
		return dataShiyingli;
	}

	public void setDataShiyingli(String dataShiyingli) {
		this.dataShiyingli = dataShiyingli;
	}

	public String getDataGanranli() {
		return dataGanranli;
	}

	public void setDataGanranli(String dataGanranli) {
		this.dataGanranli = dataGanranli;
	}

	public String getDataWentijiejue() {
		return dataWentijiejue;
	}

	public void setDataWentijiejue(String dataWentijiejue) {
		this.dataWentijiejue = dataWentijiejue;
	}

}
