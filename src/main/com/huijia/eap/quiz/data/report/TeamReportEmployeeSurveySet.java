package com.huijia.eap.quiz.data.report;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class TeamReportEmployeeSurveySet {

	// 3.2 优势劣势问题
	/**
	 * 满意度最高的三道题目序号及对应的因素
	 */
	public String top1QuestionIds;
	public String top1QuestionFactors;
	public String top2QuestionIds;
	public String top2QuestionFactors;
	public String top3QuestionIds;
	public String top3QuestionFactors;
	public String topEvaluation;

	/**
	 * 满意度最低的三道题目序号及对应的因素
	 */
	public String low1QuestionIds;
	public String low1QuestionFactors;
	public String low2QuestionIds;
	public String low2QuestionFactors;
	public String low3QuestionIds;
	public String low3QuestionFactors;

	// 3.3 满意度整体分析
	// （1）整体选择频率分析
	public long satisfactionRank1Count = 0; // 非常同意
	public long satisfactionRank2Count = 0; // 同意
	public long satisfactionRank3Count = 0; // 比较同意
	public long satisfactionRank4Count = 0; // 比较不同意
	public long satisfactionRank5Count = 0; // 不同意
	public long satisfactionRank6Count = 0; // 非常不同意
	public String satisfactionRank1Ratio;
	public String satisfactionRank2Ratio;
	public String satisfactionRank3Ratio;
	public String satisfactionRank4Ratio;
	public String satisfactionRank5Ratio;
	public String satisfactionRank6Ratio;
	public String satisfactionRank1Evaluation;
	public String satisfactionRank2Evaluation;
	public String satisfactionRank6Evaluation;

	// 3.3整体满意度分析
	// （2）整体满意度分析
	public long wholeSatisfactionScoreXinchoufuli = 0; // 薪酬福利整体满意度
	public long wholeSatisfactionScoreZhiyefazhan = 0; // 职业发展
	public long wholeSatisfactionScoreGongzuozhize = 0;// 工作职责
	public long wholeSatisfactionScoreGuanli = 0; // 管理
	public long wholeSatisfactionScoreHuanjing = 0; // 环境
	public String wholeSatisfactionEvaluation = "";

	// 3.3整体满意度分析
	// （3）子维度分析
	public Map<String, Double> subSatisfactionFactorScores = new HashMap<String, Double>();
	public String subSatisfactionEvaluation = "";

	// 3.4 员工忠诚度分析
	// 整体忠诚度
	public long wholeLoyaltyPraiseCount = 0;
	public long wholeLoyaltyNoPraiseCount = 0;
	public long wholeLoyaltyRemainCount = 0;
	public long wholeLoyaltyNoRemainCount = 0;
	public long wholeLoyaltyLeaveCount = 0;
	public String wholeLoyaltyPraiseRatio = "";
	public String wholeLoyaltyNoPraiseRatio = "";
	public String wholeLoyaltyRemainRatio = "";
	public String wholeLoyaltyNoRemainRatio = "";
	public LinkedList<String> wholeLoyaltyLeaveUserList = new LinkedList<String>();
	public String wholeLoyaltyEvaluation = "";

	// 3.4 员工忠诚度分析
	// （2）分类分析
	public double classifyLoyaltyPraiseWholeSatisfictionScore = 0.0;
	public double classifyLoyaltyPraiseAverageSatisfictionScore = 0.0;
	public double classifyLoyaltyRemainWholeSatisfictionScore = 0.0;
	public double classifyLoyaltyRemainAverageSatisfictionScore = 0.0;
	public String classifyLoyaltyEvaluation = "";

	// 3.4 员工忠诚度分析
	// （3）员工忠诚度驱力因素分析
	public String drivingLoyaltyPraiseRatioGuanli = "";
	public String drivingLoyaltyPraiseRatioGongzuozhize = "";
	public String drivingLoyaltyPraiseRatioHuanjing = "";
	public String drivingLoyaltyPraiseRatioXinchoufuli = "";
	public String drivingLoyaltyPraiseRatioZhiyefazhan = "";
	public String drivingLoyaltyRemainRatioGuanli = "";
	public String drivingLoyaltyRemainRatioGongzuozhize = "";
	public String drivingLoyaltyRemainRatioHuanjing = "";
	public String drivingLoyaltyRemainRatioXinchoufuli = "";
	public String drivingLoyaltyRemainRatioZhiyefazhan = "";
	public String drivingLoyaltyEvaluation = "";

	// 3.4 员工忠诚度分析
	// （4）员工忠诚度驱力因素细分
	public Map<String, Double> detailDrivingLoyaltyPraiseRatioMap = new HashMap<String, Double>();
	public Map<String, Double> detailDrivingLoyaltyRemainRatioMap = new HashMap<String, Double>();
	public String detailDrivingLoyaltyEvaluation = "";

	public void init() {
		top1QuestionIds = "";
		top1QuestionFactors = "";
		top2QuestionIds = "";
		top2QuestionFactors = "";
		top3QuestionIds = "";
		top3QuestionFactors = "";
		topEvaluation = "";

		/**
		 * 满意度最低的三道题目序号及对应的因素
		 */
		low1QuestionIds = "";
		low1QuestionFactors = "";
		low2QuestionIds = "";
		low2QuestionFactors = "";
		low3QuestionIds = "";
		low3QuestionFactors = "";

		// 3.3 满意度整体分析
		// （1）整体选择频率分析
		satisfactionRank1Count = 0; // 非常同意
		satisfactionRank2Count = 0; // 同意
		satisfactionRank3Count = 0; // 比较同意
		satisfactionRank4Count = 0; // 比较不同意
		satisfactionRank5Count = 0; // 不同意
		satisfactionRank6Count = 0; // 非常不同意
		satisfactionRank1Ratio = "";
		satisfactionRank2Ratio = "";
		satisfactionRank3Ratio = "";
		satisfactionRank4Ratio = "";
		satisfactionRank5Ratio = "";
		satisfactionRank6Ratio = "";
		satisfactionRank1Evaluation = "";
		satisfactionRank2Evaluation = "";
		satisfactionRank6Evaluation = "";

		// 3.3整体满意度分析
		// （2）整体满意度分析
		wholeSatisfactionScoreXinchoufuli = 0; // 薪酬福利整体满意度
		wholeSatisfactionScoreZhiyefazhan = 0; // 职业发展
		wholeSatisfactionScoreGongzuozhize = 0;// 工作职责
		wholeSatisfactionScoreGuanli = 0; // 管理
		wholeSatisfactionScoreHuanjing = 0; // 环境
		wholeSatisfactionEvaluation = "";

		subSatisfactionFactorScores.clear();
		subSatisfactionFactorScores.put("薪酬福利/薪酬", 0.0);
		subSatisfactionFactorScores.put("薪酬福利/福利", 0.0);
		subSatisfactionFactorScores.put("薪酬福利/绩效", 0.0);
		subSatisfactionFactorScores.put("职业发展/能力", 0.0);
		subSatisfactionFactorScores.put("职业发展/机会", 0.0);
		subSatisfactionFactorScores.put("职业发展/前景", 0.0);
		subSatisfactionFactorScores.put("工作职责/明确", 0.0);
		subSatisfactionFactorScores.put("工作职责/意义", 0.0);
		subSatisfactionFactorScores.put("管理/培养", 0.0);
		subSatisfactionFactorScores.put("管理/认可", 0.0);
		subSatisfactionFactorScores.put("管理/信任", 0.0);
		subSatisfactionFactorScores.put("管理/制度流程", 0.0);
		subSatisfactionFactorScores.put("管理/倾听", 0.0);
		subSatisfactionFactorScores.put("环境/资源", 0.0);
		subSatisfactionFactorScores.put("环境/企业文化", 0.0);
		subSatisfactionFactorScores.put("环境/同事关系", 0.0);
		subSatisfactionFactorScores.put("环境/工作场所", 0.0);
		subSatisfactionEvaluation = "";

		wholeLoyaltyPraiseCount = 0;
		wholeLoyaltyNoPraiseCount = 0;
		wholeLoyaltyRemainCount = 0;
		wholeLoyaltyNoRemainCount = 0;
		wholeLoyaltyPraiseRatio = "";
		wholeLoyaltyNoPraiseRatio = "";
		wholeLoyaltyRemainRatio = "";
		wholeLoyaltyNoRemainRatio = "";
		wholeLoyaltyLeaveCount = 0;
		wholeLoyaltyLeaveUserList = new LinkedList<String>();
		wholeLoyaltyEvaluation = "";

		classifyLoyaltyPraiseWholeSatisfictionScore = 0.0;
		classifyLoyaltyPraiseAverageSatisfictionScore = 0.0;
		classifyLoyaltyRemainWholeSatisfictionScore = 0.0;
		classifyLoyaltyRemainAverageSatisfictionScore = 0.0;
		classifyLoyaltyEvaluation = "";

		// 3.4 员工忠诚度分析
		// （3）员工忠诚度驱力因素分析
		drivingLoyaltyPraiseRatioGuanli = "";
		drivingLoyaltyPraiseRatioGongzuozhize = "";
		drivingLoyaltyPraiseRatioHuanjing = "";
		drivingLoyaltyPraiseRatioXinchoufuli = "";
		drivingLoyaltyPraiseRatioZhiyefazhan = "";
		drivingLoyaltyRemainRatioGuanli = "";
		drivingLoyaltyRemainRatioGongzuozhize = "";
		drivingLoyaltyRemainRatioHuanjing = "";
		drivingLoyaltyRemainRatioXinchoufuli = "";
		drivingLoyaltyRemainRatioZhiyefazhan = "";
		drivingLoyaltyEvaluation = "";
		
		detailDrivingLoyaltyPraiseRatioMap = new HashMap<String, Double>();
		detailDrivingLoyaltyRemainRatioMap = new HashMap<String, Double>();
		detailDrivingLoyaltyEvaluation = "";
	}

	public String getTop1QuestionIds() {
		return top1QuestionIds;
	}

	public void setTop1QuestionIds(String top1QuestionIds) {
		this.top1QuestionIds = top1QuestionIds;
	}

	public String getTop1QuestionFactors() {
		return top1QuestionFactors;
	}

	public void setTop1QuestionFactors(String top1QuestionFactors) {
		this.top1QuestionFactors = top1QuestionFactors;
	}

	public String getTop2QuestionIds() {
		return top2QuestionIds;
	}

	public void setTop2QuestionIds(String top2QuestionIds) {
		this.top2QuestionIds = top2QuestionIds;
	}

	public String getTop2QuestionFactors() {
		return top2QuestionFactors;
	}

	public void setTop2QuestionFactors(String top2QuestionFactors) {
		this.top2QuestionFactors = top2QuestionFactors;
	}

	public String getTop3QuestionIds() {
		return top3QuestionIds;
	}

	public void setTop3QuestionIds(String top3QuestionIds) {
		this.top3QuestionIds = top3QuestionIds;
	}

	public String getTop3QuestionFactors() {
		return top3QuestionFactors;
	}

	public void setTop3QuestionFactors(String top3QuestionFactors) {
		this.top3QuestionFactors = top3QuestionFactors;
	}

	public String getTopEvaluation() {
		return topEvaluation;
	}

	public void setTopEvaluation(String topEvaluation) {
		this.topEvaluation = topEvaluation;
	}

	public String getLow1QuestionIds() {
		return low1QuestionIds;
	}

	public void setLow1QuestionIds(String low1QuestionIds) {
		this.low1QuestionIds = low1QuestionIds;
	}

	public String getLow1QuestionFactors() {
		return low1QuestionFactors;
	}

	public void setLow1QuestionFactors(String low1QuestionFactors) {
		this.low1QuestionFactors = low1QuestionFactors;
	}

	public String getLow2QuestionIds() {
		return low2QuestionIds;
	}

	public void setLow2QuestionIds(String low2QuestionIds) {
		this.low2QuestionIds = low2QuestionIds;
	}

	public String getLow2QuestionFactors() {
		return low2QuestionFactors;
	}

	public void setLow2QuestionFactors(String low2QuestionFactors) {
		this.low2QuestionFactors = low2QuestionFactors;
	}

	public String getLow3QuestionIds() {
		return low3QuestionIds;
	}

	public void setLow3QuestionIds(String low3QuestionIds) {
		this.low3QuestionIds = low3QuestionIds;
	}

	public String getLow3QuestionFactors() {
		return low3QuestionFactors;
	}

	public void setLow3QuestionFactors(String low3QuestionFactors) {
		this.low3QuestionFactors = low3QuestionFactors;
	}

	public long getSatisfactionRank1Count() {
		return satisfactionRank1Count;
	}

	public void setSatisfactionRank1Count(long satisfactionRank1Count) {
		this.satisfactionRank1Count = satisfactionRank1Count;
	}

	public long getSatisfactionRank2Count() {
		return satisfactionRank2Count;
	}

	public void setSatisfactionRank2Count(long satisfactionRank2Count) {
		this.satisfactionRank2Count = satisfactionRank2Count;
	}

	public long getSatisfactionRank3Count() {
		return satisfactionRank3Count;
	}

	public void setSatisfactionRank3Count(long satisfactionRank3Count) {
		this.satisfactionRank3Count = satisfactionRank3Count;
	}

	public long getSatisfactionRank4Count() {
		return satisfactionRank4Count;
	}

	public void setSatisfactionRank4Count(long satisfactionRank4Count) {
		this.satisfactionRank4Count = satisfactionRank4Count;
	}

	public long getSatisfactionRank5Count() {
		return satisfactionRank5Count;
	}

	public void setSatisfactionRank5Count(long satisfactionRank5Count) {
		this.satisfactionRank5Count = satisfactionRank5Count;
	}

	public long getSatisfactionRank6Count() {
		return satisfactionRank6Count;
	}

	public void setSatisfactionRank6Count(long satisfactionRank6Count) {
		this.satisfactionRank6Count = satisfactionRank6Count;
	}

	public String getSatisfactionRank1Ratio() {
		return satisfactionRank1Ratio;
	}

	public void setSatisfactionRank1Ratio(String satisfactionRank1Ratio) {
		this.satisfactionRank1Ratio = satisfactionRank1Ratio;
	}

	public String getSatisfactionRank2Ratio() {
		return satisfactionRank2Ratio;
	}

	public void setSatisfactionRank2Ratio(String satisfactionRank2Ratio) {
		this.satisfactionRank2Ratio = satisfactionRank2Ratio;
	}

	public String getSatisfactionRank3Ratio() {
		return satisfactionRank3Ratio;
	}

	public void setSatisfactionRank3Ratio(String satisfactionRank3Ratio) {
		this.satisfactionRank3Ratio = satisfactionRank3Ratio;
	}

	public String getSatisfactionRank4Ratio() {
		return satisfactionRank4Ratio;
	}

	public void setSatisfactionRank4Ratio(String satisfactionRank4Ratio) {
		this.satisfactionRank4Ratio = satisfactionRank4Ratio;
	}

	public String getSatisfactionRank5Ratio() {
		return satisfactionRank5Ratio;
	}

	public void setSatisfactionRank5Ratio(String satisfactionRank5Ratio) {
		this.satisfactionRank5Ratio = satisfactionRank5Ratio;
	}

	public String getSatisfactionRank6Ratio() {
		return satisfactionRank6Ratio;
	}

	public void setSatisfactionRank6Ratio(String satisfactionRank6Ratio) {
		this.satisfactionRank6Ratio = satisfactionRank6Ratio;
	}

	public String getSatisfactionRank1Evaluation() {
		return satisfactionRank1Evaluation;
	}

	public void setSatisfactionRank1Evaluation(
			String satisfactionRank1Evaluation) {
		this.satisfactionRank1Evaluation = satisfactionRank1Evaluation;
	}

	public String getSatisfactionRank2Evaluation() {
		return satisfactionRank2Evaluation;
	}

	public void setSatisfactionRank2Evaluation(
			String satisfactionRank2Evaluation) {
		this.satisfactionRank2Evaluation = satisfactionRank2Evaluation;
	}

	public String getSatisfactionRank6Evaluation() {
		return satisfactionRank6Evaluation;
	}

	public void setSatisfactionRank6Evaluation(
			String satisfactionRank6Evaluation) {
		this.satisfactionRank6Evaluation = satisfactionRank6Evaluation;
	}

	public long getWholeSatisfactionScoreXinchoufuli() {
		return wholeSatisfactionScoreXinchoufuli;
	}

	public void setWholeSatisfactionScoreXinchoufuli(
			long wholeSatisfactionScoreXinchoufuli) {
		this.wholeSatisfactionScoreXinchoufuli = wholeSatisfactionScoreXinchoufuli;
	}

	public long getWholeSatisfactionScoreZhiyefazhan() {
		return wholeSatisfactionScoreZhiyefazhan;
	}

	public void setWholeSatisfactionScoreZhiyefazhan(
			long wholeSatisfactionScoreZhiyefazhan) {
		this.wholeSatisfactionScoreZhiyefazhan = wholeSatisfactionScoreZhiyefazhan;
	}

	public long getWholeSatisfactionScoreGongzuozhize() {
		return wholeSatisfactionScoreGongzuozhize;
	}

	public void setWholeSatisfactionScoreGongzuozhize(
			long wholeSatisfactionScoreGongzuozhize) {
		this.wholeSatisfactionScoreGongzuozhize = wholeSatisfactionScoreGongzuozhize;
	}

	public long getWholeSatisfactionScoreGuanli() {
		return wholeSatisfactionScoreGuanli;
	}

	public void setWholeSatisfactionScoreGuanli(
			long wholeSatisfactionScoreGuanli) {
		this.wholeSatisfactionScoreGuanli = wholeSatisfactionScoreGuanli;
	}

	public long getWholeSatisfactionScoreHuanjing() {
		return wholeSatisfactionScoreHuanjing;
	}

	public void setWholeSatisfactionScoreHuanjing(
			long wholeSatisfactionScoreHuanjing) {
		this.wholeSatisfactionScoreHuanjing = wholeSatisfactionScoreHuanjing;
	}

	public String getWholeSatisfactionEvaluation() {
		return wholeSatisfactionEvaluation;
	}

	public void setWholeSatisfactionEvaluation(
			String wholeSatisfactionEvaluation) {
		this.wholeSatisfactionEvaluation = wholeSatisfactionEvaluation;
	}

	public Map<String, Double> getSubSatisfactionFactorScores() {
		return subSatisfactionFactorScores;
	}

	public void setSubSatisfactionFactorScores(
			Map<String, Double> subSatisfactionFactorScores) {
		this.subSatisfactionFactorScores = subSatisfactionFactorScores;
	}

	public String getSubSatisfactionEvaluation() {
		return subSatisfactionEvaluation;
	}

	public void setSubSatisfactionEvaluation(String subSatisfactionEvaluation) {
		this.subSatisfactionEvaluation = subSatisfactionEvaluation;
	}

	public long getWholeLoyaltyPraiseCount() {
		return wholeLoyaltyPraiseCount;
	}

	public void setWholeLoyaltyPraiseCount(long wholeLoyaltyPraiseCount) {
		this.wholeLoyaltyPraiseCount = wholeLoyaltyPraiseCount;
	}

	public long getWholeLoyaltyNoPraiseCount() {
		return wholeLoyaltyNoPraiseCount;
	}

	public void setWholeLoyaltyNoPraiseCount(long wholeLoyaltyNoPraiseCount) {
		this.wholeLoyaltyNoPraiseCount = wholeLoyaltyNoPraiseCount;
	}

	public long getWholeLoyaltyRemainCount() {
		return wholeLoyaltyRemainCount;
	}

	public void setWholeLoyaltyRemainCount(long wholeLoyaltyRemainCount) {
		this.wholeLoyaltyRemainCount = wholeLoyaltyRemainCount;
	}

	public long getWholeLoyaltyNoRemainCount() {
		return wholeLoyaltyNoRemainCount;
	}

	public void setWholeLoyaltyNoRemainCount(long wholeLoyaltyNoRemainCount) {
		this.wholeLoyaltyNoRemainCount = wholeLoyaltyNoRemainCount;
	}

	public long getWholeLoyaltyLeaveCount() {
		return wholeLoyaltyLeaveCount;
	}

	public void setWholeLoyaltyLeaveCount(long wholeLoyaltyLeaveCount) {
		this.wholeLoyaltyLeaveCount = wholeLoyaltyLeaveCount;
	}

	public String getWholeLoyaltyPraiseRatio() {
		return wholeLoyaltyPraiseRatio;
	}

	public void setWholeLoyaltyPraiseRatio(String wholeLoyaltyPraiseRatio) {
		this.wholeLoyaltyPraiseRatio = wholeLoyaltyPraiseRatio;
	}

	public String getWholeLoyaltyNoPraiseRatio() {
		return wholeLoyaltyNoPraiseRatio;
	}

	public void setWholeLoyaltyNoPraiseRatio(String wholeLoyaltyNoPraiseRatio) {
		this.wholeLoyaltyNoPraiseRatio = wholeLoyaltyNoPraiseRatio;
	}

	public String getWholeLoyaltyRemainRatio() {
		return wholeLoyaltyRemainRatio;
	}

	public void setWholeLoyaltyRemainRatio(String wholeLoyaltyRemainRatio) {
		this.wholeLoyaltyRemainRatio = wholeLoyaltyRemainRatio;
	}

	public String getWholeLoyaltyNoRemainRatio() {
		return wholeLoyaltyNoRemainRatio;
	}

	public void setWholeLoyaltyNoRemainRatio(String wholeLoyaltyNoRemainRatio) {
		this.wholeLoyaltyNoRemainRatio = wholeLoyaltyNoRemainRatio;
	}

	public LinkedList<String> getWholeLoyaltyLeaveUserList() {
		return wholeLoyaltyLeaveUserList;
	}

	public void setWholeLoyaltyLeaveUserList(
			LinkedList<String> wholeLoyaltyLeaveUserList) {
		this.wholeLoyaltyLeaveUserList = wholeLoyaltyLeaveUserList;
	}

	public String getWholeLoyaltyEvaluation() {
		return wholeLoyaltyEvaluation;
	}

	public void setWholeLoyaltyEvaluation(String wholeLoyaltyEvaluation) {
		this.wholeLoyaltyEvaluation = wholeLoyaltyEvaluation;
	}

	public double getClassifyLoyaltyPraiseWholeSatisfictionScore() {
		return classifyLoyaltyPraiseWholeSatisfictionScore;
	}

	public void setClassifyLoyaltyPraiseWholeSatisfictionScore(
			double classifyLoyaltyPraiseWholeSatisfictionScore) {
		this.classifyLoyaltyPraiseWholeSatisfictionScore = classifyLoyaltyPraiseWholeSatisfictionScore;
	}

	public double getClassifyLoyaltyPraiseAverageSatisfictionScore() {
		return classifyLoyaltyPraiseAverageSatisfictionScore;
	}

	public void setClassifyLoyaltyPraiseAverageSatisfictionScore(
			double classifyLoyaltyPraiseAverageSatisfictionScore) {
		this.classifyLoyaltyPraiseAverageSatisfictionScore = classifyLoyaltyPraiseAverageSatisfictionScore;
	}

	public double getClassifyLoyaltyRemainWholeSatisfictionScore() {
		return classifyLoyaltyRemainWholeSatisfictionScore;
	}

	public void setClassifyLoyaltyRemainWholeSatisfictionScore(
			double classifyLoyaltyRemainWholeSatisfictionScore) {
		this.classifyLoyaltyRemainWholeSatisfictionScore = classifyLoyaltyRemainWholeSatisfictionScore;
	}

	public double getClassifyLoyaltyRemainAverageSatisfictionScore() {
		return classifyLoyaltyRemainAverageSatisfictionScore;
	}

	public void setClassifyLoyaltyRemainAverageSatisfictionScore(
			double classifyLoyaltyRemainAverageSatisfictionScore) {
		this.classifyLoyaltyRemainAverageSatisfictionScore = classifyLoyaltyRemainAverageSatisfictionScore;
	}

	public String getClassifyLoyaltyEvaluation() {
		return classifyLoyaltyEvaluation;
	}

	public void setClassifyLoyaltyEvaluation(String classifyLoyaltyEvaluation) {
		this.classifyLoyaltyEvaluation = classifyLoyaltyEvaluation;
	}

	public String getDrivingLoyaltyPraiseRatioGuanli() {
		return drivingLoyaltyPraiseRatioGuanli;
	}

	public void setDrivingLoyaltyPraiseRatioGuanli(
			String drivingLoyaltyPraiseRatioGuanli) {
		this.drivingLoyaltyPraiseRatioGuanli = drivingLoyaltyPraiseRatioGuanli;
	}

	public String getDrivingLoyaltyPraiseRatioGongzuozhize() {
		return drivingLoyaltyPraiseRatioGongzuozhize;
	}

	public void setDrivingLoyaltyPraiseRatioGongzuozhize(
			String drivingLoyaltyPraiseRatioGongzuozhize) {
		this.drivingLoyaltyPraiseRatioGongzuozhize = drivingLoyaltyPraiseRatioGongzuozhize;
	}

	public String getDrivingLoyaltyPraiseRatioHuanjing() {
		return drivingLoyaltyPraiseRatioHuanjing;
	}

	public void setDrivingLoyaltyPraiseRatioHuanjing(
			String drivingLoyaltyPraiseRatioHuanjing) {
		this.drivingLoyaltyPraiseRatioHuanjing = drivingLoyaltyPraiseRatioHuanjing;
	}

	public String getDrivingLoyaltyPraiseRatioXinchoufuli() {
		return drivingLoyaltyPraiseRatioXinchoufuli;
	}

	public void setDrivingLoyaltyPraiseRatioXinchoufuli(
			String drivingLoyaltyPraiseRatioXinchoufuli) {
		this.drivingLoyaltyPraiseRatioXinchoufuli = drivingLoyaltyPraiseRatioXinchoufuli;
	}

	public String getDrivingLoyaltyPraiseRatioZhiyefazhan() {
		return drivingLoyaltyPraiseRatioZhiyefazhan;
	}

	public void setDrivingLoyaltyPraiseRatioZhiyefazhan(
			String drivingLoyaltyPraiseRatioZhiyefazhan) {
		this.drivingLoyaltyPraiseRatioZhiyefazhan = drivingLoyaltyPraiseRatioZhiyefazhan;
	}

	public String getDrivingLoyaltyRemainRatioGuanli() {
		return drivingLoyaltyRemainRatioGuanli;
	}

	public void setDrivingLoyaltyRemainRatioGuanli(
			String drivingLoyaltyRemainRatioGuanli) {
		this.drivingLoyaltyRemainRatioGuanli = drivingLoyaltyRemainRatioGuanli;
	}

	public String getDrivingLoyaltyRemainRatioGongzuozhize() {
		return drivingLoyaltyRemainRatioGongzuozhize;
	}

	public void setDrivingLoyaltyRemainRatioGongzuozhize(
			String drivingLoyaltyRemainRatioGongzuozhize) {
		this.drivingLoyaltyRemainRatioGongzuozhize = drivingLoyaltyRemainRatioGongzuozhize;
	}

	public String getDrivingLoyaltyRemainRatioHuanjing() {
		return drivingLoyaltyRemainRatioHuanjing;
	}

	public void setDrivingLoyaltyRemainRatioHuanjing(
			String drivingLoyaltyRemainRatioHuanjing) {
		this.drivingLoyaltyRemainRatioHuanjing = drivingLoyaltyRemainRatioHuanjing;
	}

	public String getDrivingLoyaltyRemainRatioXinchoufuli() {
		return drivingLoyaltyRemainRatioXinchoufuli;
	}

	public void setDrivingLoyaltyRemainRatioXinchoufuli(
			String drivingLoyaltyRemainRatioXinchoufuli) {
		this.drivingLoyaltyRemainRatioXinchoufuli = drivingLoyaltyRemainRatioXinchoufuli;
	}

	public String getDrivingLoyaltyRemainRatioZhiyefazhan() {
		return drivingLoyaltyRemainRatioZhiyefazhan;
	}

	public void setDrivingLoyaltyRemainRatioZhiyefazhan(
			String drivingLoyaltyRemainRatioZhiyefazhan) {
		this.drivingLoyaltyRemainRatioZhiyefazhan = drivingLoyaltyRemainRatioZhiyefazhan;
	}

	public String getDrivingLoyaltyEvaluation() {
		return drivingLoyaltyEvaluation;
	}

	public void setDrivingLoyaltyEvaluation(String drivingLoyaltyEvaluation) {
		this.drivingLoyaltyEvaluation = drivingLoyaltyEvaluation;
	}

	public Map<String, Double> getDetailDrivingLoyaltyPraiseRatioMap() {
		return detailDrivingLoyaltyPraiseRatioMap;
	}

	public void setDetailDrivingLoyaltyPraiseRatioMap(
			Map<String, Double> detailDrivingLoyaltyPraiseRatioMap) {
		this.detailDrivingLoyaltyPraiseRatioMap = detailDrivingLoyaltyPraiseRatioMap;
	}

	public String getDetailDrivingLoyaltyEvaluation() {
		return detailDrivingLoyaltyEvaluation;
	}

	public void setDetailDrivingLoyaltyEvaluation(
			String detailDrivingLoyaltyEvaluation) {
		this.detailDrivingLoyaltyEvaluation = detailDrivingLoyaltyEvaluation;
	}

}
