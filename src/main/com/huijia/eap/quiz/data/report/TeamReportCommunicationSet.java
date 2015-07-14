package com.huijia.eap.quiz.data.report;

public class TeamReportCommunicationSet {

	public long userCountBiaoda = 0;
	public long userCountZhipei = 0;
	public long userCountHeai = 0;
	public long userCountFenxi = 0;

	public String userRatioBiaoda = "";
	public String userRatioZhipei = "";
	public String userRatioHeai = "";
	public String userRatioFenxi = "";

	public String evaluation = "";
	public String topCategory = "";

	public void init() {
		userCountBiaoda = 0;
		userCountZhipei = 0;
		userCountHeai = 0;
		userCountFenxi = 0;

		userRatioBiaoda = "";
		userRatioZhipei = "";
		userRatioHeai = "";
		userRatioFenxi = "";

		evaluation = "";
		topCategory = "";
	}

	public long getUserCountBiaoda() {
		return userCountBiaoda;
	}

	public void setUserCountBiaoda(long userCountBiaoda) {
		this.userCountBiaoda = userCountBiaoda;
	}

	public long getUserCountZhipei() {
		return userCountZhipei;
	}

	public void setUserCountZhipei(long userCountZhipei) {
		this.userCountZhipei = userCountZhipei;
	}

	public long getUserCountHeai() {
		return userCountHeai;
	}

	public void setUserCountHeai(long userCountHeai) {
		this.userCountHeai = userCountHeai;
	}

	public long getUserCountFenxi() {
		return userCountFenxi;
	}

	public void setUserCountFenxi(long userCountFenxi) {
		this.userCountFenxi = userCountFenxi;
	}

	public String getUserRatioBiaoda() {
		return userRatioBiaoda;
	}

	public void setUserRatioBiaoda(String userRatioBiaoda) {
		this.userRatioBiaoda = userRatioBiaoda;
	}

	public String getUserRatioZhipei() {
		return userRatioZhipei;
	}

	public void setUserRatioZhipei(String userRatioZhipei) {
		this.userRatioZhipei = userRatioZhipei;
	}

	public String getUserRatioHeai() {
		return userRatioHeai;
	}

	public void setUserRatioHeai(String userRatioHeai) {
		this.userRatioHeai = userRatioHeai;
	}

	public String getUserRatioFenxi() {
		return userRatioFenxi;
	}

	public void setUserRatioFenxi(String userRatioFenxi) {
		this.userRatioFenxi = userRatioFenxi;
	}

	public String getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}

	public String getTopCategory() {
		return topCategory;
	}

	public void setTopCategory(String topCategory) {
		this.topCategory = topCategory;
	}

}
