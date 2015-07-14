package com.huijia.eap.quiz.data.report;

public class TeamReportConflictSet {
	public long userCountZhipei = 0;
	public long userCountZhezhong = 0;
	public long userCountHuibi = 0;
	public long userCountQianrang = 0;
	public long userCountZhenghe = 0;

	public String userRatioZhipei = "";
	public String userRatioZhezhong = "";
	public String userRatioHuibi = "";
	public String userRatioQianrang = "";
	public String userRatioZhenghe = "";

	public String evaluation = "";
	public String topCategory = "";

	public void init() {
		userCountZhipei = 0;
		userCountZhezhong = 0;
		userCountHuibi = 0;
		userCountQianrang = 0;
		userCountZhenghe = 0;

		userRatioZhipei = "";
		userRatioZhezhong = "";
		userRatioHuibi = "";
		userRatioQianrang = "";
		userRatioZhenghe = "";

		evaluation = "";
		topCategory = "";
	}

	public long getUserCountZhipei() {
		return userCountZhipei;
	}

	public void setUserCountZhipei(long userCountZhipei) {
		this.userCountZhipei = userCountZhipei;
	}

	public long getUserCountZhezhong() {
		return userCountZhezhong;
	}

	public void setUserCountZhezhong(long userCountZhezhong) {
		this.userCountZhezhong = userCountZhezhong;
	}

	public long getUserCountHuibi() {
		return userCountHuibi;
	}

	public void setUserCountHuibi(long userCountHuibi) {
		this.userCountHuibi = userCountHuibi;
	}

	public long getUserCountQianrang() {
		return userCountQianrang;
	}

	public void setUserCountQianrang(long userCountQianrang) {
		this.userCountQianrang = userCountQianrang;
	}

	public long getUserCountZhenghe() {
		return userCountZhenghe;
	}

	public void setUserCountZhenghe(long userCountZhenghe) {
		this.userCountZhenghe = userCountZhenghe;
	}

	public String getUserRatioZhipei() {
		return userRatioZhipei;
	}

	public void setUserRatioZhipei(String userRatioZhipei) {
		this.userRatioZhipei = userRatioZhipei;
	}

	public String getUserRatioZhezhong() {
		return userRatioZhezhong;
	}

	public void setUserRatioZhezhong(String userRatioZhezhong) {
		this.userRatioZhezhong = userRatioZhezhong;
	}

	public String getUserRatioHuibi() {
		return userRatioHuibi;
	}

	public void setUserRatioHuibi(String userRatioHuibi) {
		this.userRatioHuibi = userRatioHuibi;
	}

	public String getUserRatioQianrang() {
		return userRatioQianrang;
	}

	public void setUserRatioQianrang(String userRatioQianrang) {
		this.userRatioQianrang = userRatioQianrang;
	}

	public String getUserRatioZhenghe() {
		return userRatioZhenghe;
	}

	public void setUserRatioZhenghe(String userRatioZhenghe) {
		this.userRatioZhenghe = userRatioZhenghe;
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
