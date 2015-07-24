package com.huijia.eap.quiz.data.report;

import java.util.HashMap;
import java.util.Map;

/**
 * 报告页面显示的通用变量集合
 * 
 * Note: 元素数量较多，且仅用于显示，为编写代码方便，本对象元素均设为public
 */
public class TeamReportCommonParamSet {
	public long quizId;
	public long segmentId;

	public String companyName; // 公司名称
	public long segmentUserCount = 0; // 号段总人数
	public long registeredUserCount = 0; // 该号段中已注册用户数
	public long testedUserCount = 0; // 该号段中已完成测试用户数
	public long liedUserCount = 0; // 测谎题没过的用户数/答案数
	public long validUserCount = 0; // 测谎题通过的用户数/答案数
	public String validUserRatio;

	/********** 以下统计数据均针对通过测试的用户 *****************/
	/********* 1．性别构成 *************/
	public long genderFemaleUserCount = 0; // 测谎题通过的女性用户数量
	public long genderMaleUserCount = 0; // 测谎题通过的男性用户数量
	public String genderFemaleRatio;
	public String genderMaleRatio;
	public String genderRatio; // 男女比例1:ratio

	/********* 2．年龄构成 *************/
	public long age0to30UserCount = 0; // 0到30用户数量
	public long age31to40UserCount = 0; // 31到40用户数量
	public long age41to50UserCount = 0; // 41到50用户数量
	public long age51to99UserCount = 0; // 50以上用户数量
	public String age0to30Ratio; // 0到30用户比例
	public String age31to40Ratio; // 31到40用户比例
	public String age41to50Ratio; // 41到50用户比例
	public String age51to99Ratio; // 50以上用户比例
	public String ageMaxName;

	/*********** 3.教育程度构成 ********************/
	public long educationDazhuanUserCount = 0;
	public long educationBenkeUserCount = 0;
	public long educationShuoshiUserCount = 0;
	public String educationDazhuanRatio;
	public String educationBenkeRatio;
	public String educationShuoshiRatio;
	public String educationBenkeShuoshiRatio;

	/****************** 4.工作年限构成 *******************/
	public long workage1to3UserCount = 0;
	public long workage3to5UserCount = 0;
	public long workage5to10UserCount = 0;
	public long workage10to99UserCount = 0;
	public String workage1to3Ratio;
	public String workage3to5Ratio;
	public String workage5to10Ratio;
	public String workage10to99Ratio;
	public String workageComment; // 此样本中，员工普遍工作经验较少,工作1-3年以上的员工接近七成

	/************** 5．职位状况构成 ****************/
	public long jobtitleGeneralUserCount = 0; // 普通员工数量
	public long jobtitleMiddleUserCount = 0; // 中层领导数量
	public long jobtitleSeniorUserCount = 0; // 高级领导数量
	public String jobtitleGeneralRatio;
	public String jobtitleMiddleRatio;
	public String jobtitleSeniorRatio;
	
	/************** 报表生成  ****************/
	public Map<String, String> chartDataGender = new HashMap<String, String>();
	public Map<String, String> chartDataAge = new HashMap<String, String>();
	public Map<String, String> chartDataEducation = new HashMap<String, String>();
	public Map<String, String> chartDataWorkage = new HashMap<String, String>();
	public Map<String, String> chartDataJobtitle = new HashMap<String, String>();

	public void init() {
		segmentUserCount = 0; // 号段总人数
		registeredUserCount = 0; // 该号段中已注册用户数
		testedUserCount = 0; // 该号段中已完成测试用户数
		liedUserCount = 0; // 测谎题没过的用户数/答案数
		validUserCount = 0; // 测谎题通过的用户数/答案数
		genderFemaleUserCount = 0; // 测谎题通过的女性用户数量
		genderMaleUserCount = 0; // 测谎题通过的男性用户数量
		age0to30UserCount = 0; // 0到30用户数量
		age31to40UserCount = 0; // 31到40用户数量
		age41to50UserCount = 0; // 41到50用户数量
		age51to99UserCount = 0; // 50以上用户数量
		educationDazhuanUserCount = 0;
		educationBenkeUserCount = 0;
		educationShuoshiUserCount = 0;
		workage1to3UserCount = 0;
		workage3to5UserCount = 0;
		workage5to10UserCount = 0;
		workage10to99UserCount = 0;
		jobtitleGeneralUserCount = 0; // 普通员工数量
		jobtitleMiddleUserCount = 0; // 中层领导数量
		jobtitleSeniorUserCount = 0; // 高级领导数量

		companyName = ""; // 公司名称
		validUserRatio = "";
		genderFemaleRatio = "";
		genderMaleRatio = "";
		genderRatio = ""; // 男女比例1:ratio
		age0to30Ratio = ""; // 0到30用户比例
		age31to40Ratio = ""; // 31到40用户比例
		age41to50Ratio = ""; // 41到50用户比例
		age51to99Ratio = ""; // 50以上用户比例
		ageMaxName = "";
		educationDazhuanRatio = "";
		educationBenkeRatio = "";
		educationShuoshiRatio = "";
		educationBenkeShuoshiRatio = "";
		workage1to3Ratio = "";
		workage3to5Ratio = "";
		workage5to10Ratio = "";
		workage10to99Ratio = "";
		workageComment = ""; // 此样本中，员工普遍工作经验较少,工作1-3年以上的员工接近七成
		jobtitleGeneralRatio = "";
		jobtitleMiddleRatio = "";
		jobtitleSeniorRatio = "";
		
		chartDataGender = new HashMap<String, String>();
		chartDataAge = new HashMap<String, String>();
		chartDataEducation = new HashMap<String, String>();
		chartDataWorkage = new HashMap<String, String>();
		chartDataJobtitle = new HashMap<String, String>();
	}

	public Map<String, String> getChartDataGender() {
		return chartDataGender;
	}

	public void setChartDataGender(Map<String, String> chartDataGender) {
		this.chartDataGender = chartDataGender;
	}

	public long getQuizId() {
		return quizId;
	}

	public void setQuizId(long quizId) {
		this.quizId = quizId;
	}

	public long getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(long segmentId) {
		this.segmentId = segmentId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public long getSegmentUserCount() {
		return segmentUserCount;
	}

	public void setSegmentUserCount(long segmentUserCount) {
		this.segmentUserCount = segmentUserCount;
	}

	public long getRegisteredUserCount() {
		return registeredUserCount;
	}

	public void setRegisteredUserCount(long registeredUserCount) {
		this.registeredUserCount = registeredUserCount;
	}

	public long getTestedUserCount() {
		return testedUserCount;
	}

	public void setTestedUserCount(long testedUserCount) {
		this.testedUserCount = testedUserCount;
	}

	public long getLiedUserCount() {
		return liedUserCount;
	}

	public void setLiedUserCount(long liedUserCount) {
		this.liedUserCount = liedUserCount;
	}

	public long getValidUserCount() {
		return validUserCount;
	}

	public void setValidUserCount(long validUserCount) {
		this.validUserCount = validUserCount;
	}

	public String getValidUserRatio() {
		return validUserRatio;
	}

	public void setValidUserRatio(String validUserRatio) {
		this.validUserRatio = validUserRatio;
	}

	public long getGenderFemaleUserCount() {
		return genderFemaleUserCount;
	}

	public void setGenderFemaleUserCount(long genderFemaleUserCount) {
		this.genderFemaleUserCount = genderFemaleUserCount;
	}

	public long getGenderMaleUserCount() {
		return genderMaleUserCount;
	}

	public void setGenderMaleUserCount(long genderMaleUserCount) {
		this.genderMaleUserCount = genderMaleUserCount;
	}

	public String getGenderFemaleRatio() {
		return genderFemaleRatio;
	}

	public void setGenderFemaleRatio(String genderFemaleRatio) {
		this.genderFemaleRatio = genderFemaleRatio;
	}

	public String getGenderMaleRatio() {
		return genderMaleRatio;
	}

	public void setGenderMaleRatio(String genderMaleRatio) {
		this.genderMaleRatio = genderMaleRatio;
	}

	public String getGenderRatio() {
		return genderRatio;
	}

	public void setGenderRatio(String genderRatio) {
		this.genderRatio = genderRatio;
	}

	public long getAge0to30UserCount() {
		return age0to30UserCount;
	}

	public void setAge0to30UserCount(long age0to30UserCount) {
		this.age0to30UserCount = age0to30UserCount;
	}

	public long getAge31to40UserCount() {
		return age31to40UserCount;
	}

	public void setAge31to40UserCount(long age31to40UserCount) {
		this.age31to40UserCount = age31to40UserCount;
	}

	public long getAge41to50UserCount() {
		return age41to50UserCount;
	}

	public void setAge41to50UserCount(long age41to50UserCount) {
		this.age41to50UserCount = age41to50UserCount;
	}

	public long getAge51to99UserCount() {
		return age51to99UserCount;
	}

	public void setAge51to99UserCount(long age51to99UserCount) {
		this.age51to99UserCount = age51to99UserCount;
	}

	public String getAge0to30Ratio() {
		return age0to30Ratio;
	}

	public void setAge0to30Ratio(String age0to30Ratio) {
		this.age0to30Ratio = age0to30Ratio;
	}

	public String getAge31to40Ratio() {
		return age31to40Ratio;
	}

	public void setAge31to40Ratio(String age31to40Ratio) {
		this.age31to40Ratio = age31to40Ratio;
	}

	public String getAge41to50Ratio() {
		return age41to50Ratio;
	}

	public void setAge41to50Ratio(String age41to50Ratio) {
		this.age41to50Ratio = age41to50Ratio;
	}

	public String getAge51to99Ratio() {
		return age51to99Ratio;
	}

	public void setAge51to99Ratio(String age51to99Ratio) {
		this.age51to99Ratio = age51to99Ratio;
	}

	public String getAgeMaxName() {
		return ageMaxName;
	}

	public void setAgeMaxName(String ageMaxName) {
		this.ageMaxName = ageMaxName;
	}

	public long getEducationDazhuanUserCount() {
		return educationDazhuanUserCount;
	}

	public void setEducationDazhuanUserCount(long educationDazhuanUserCount) {
		this.educationDazhuanUserCount = educationDazhuanUserCount;
	}

	public long getEducationBenkeUserCount() {
		return educationBenkeUserCount;
	}

	public void setEducationBenkeUserCount(long educationBenkeUserCount) {
		this.educationBenkeUserCount = educationBenkeUserCount;
	}

	public long getEducationShuoshiUserCount() {
		return educationShuoshiUserCount;
	}

	public void setEducationShuoshiUserCount(long educationShuoshiUserCount) {
		this.educationShuoshiUserCount = educationShuoshiUserCount;
	}

	public String getEducationDazhuanRatio() {
		return educationDazhuanRatio;
	}

	public void setEducationDazhuanRatio(String educationDazhuanRatio) {
		this.educationDazhuanRatio = educationDazhuanRatio;
	}

	public String getEducationBenkeRatio() {
		return educationBenkeRatio;
	}

	public void setEducationBenkeRatio(String educationBenkeRatio) {
		this.educationBenkeRatio = educationBenkeRatio;
	}

	public String getEducationShuoshiRatio() {
		return educationShuoshiRatio;
	}

	public void setEducationShuoshiRatio(String educationShuoshiRatio) {
		this.educationShuoshiRatio = educationShuoshiRatio;
	}

	public String getEducationBenkeShuoshiRatio() {
		return educationBenkeShuoshiRatio;
	}

	public void setEducationBenkeShuoshiRatio(String educationBenkeShuoshiRatio) {
		this.educationBenkeShuoshiRatio = educationBenkeShuoshiRatio;
	}

	public long getWorkage1to3UserCount() {
		return workage1to3UserCount;
	}

	public void setWorkage1to3UserCount(long workage1to3UserCount) {
		this.workage1to3UserCount = workage1to3UserCount;
	}

	public long getWorkage3to5UserCount() {
		return workage3to5UserCount;
	}

	public void setWorkage3to5UserCount(long workage3to5UserCount) {
		this.workage3to5UserCount = workage3to5UserCount;
	}

	public long getWorkage5to10UserCount() {
		return workage5to10UserCount;
	}

	public void setWorkage5to10UserCount(long workage5to10UserCount) {
		this.workage5to10UserCount = workage5to10UserCount;
	}

	public long getWorkage10to99UserCount() {
		return workage10to99UserCount;
	}

	public void setWorkage10to99UserCount(long workage10to99UserCount) {
		this.workage10to99UserCount = workage10to99UserCount;
	}

	public String getWorkage1to3Ratio() {
		return workage1to3Ratio;
	}

	public void setWorkage1to3Ratio(String workage1to3Ratio) {
		this.workage1to3Ratio = workage1to3Ratio;
	}

	public String getWorkage3to5Ratio() {
		return workage3to5Ratio;
	}

	public void setWorkage3to5Ratio(String workage3to5Ratio) {
		this.workage3to5Ratio = workage3to5Ratio;
	}

	public String getWorkage5to10Ratio() {
		return workage5to10Ratio;
	}

	public void setWorkage5to10Ratio(String workage5to10Ratio) {
		this.workage5to10Ratio = workage5to10Ratio;
	}

	public String getWorkage10to99Ratio() {
		return workage10to99Ratio;
	}

	public void setWorkage10to99Ratio(String workage10to99Ratio) {
		this.workage10to99Ratio = workage10to99Ratio;
	}

	public String getWorkageComment() {
		return workageComment;
	}

	public void setWorkageComment(String workageComment) {
		this.workageComment = workageComment;
	}

	public long getJobtitleGeneralUserCount() {
		return jobtitleGeneralUserCount;
	}

	public void setJobtitleGeneralUserCount(long jobtitleGeneralUserCount) {
		this.jobtitleGeneralUserCount = jobtitleGeneralUserCount;
	}

	public long getJobtitleMiddleUserCount() {
		return jobtitleMiddleUserCount;
	}

	public void setJobtitleMiddleUserCount(long jobtitleMiddleUserCount) {
		this.jobtitleMiddleUserCount = jobtitleMiddleUserCount;
	}

	public long getJobtitleSeniorUserCount() {
		return jobtitleSeniorUserCount;
	}

	public void setJobtitleSeniorUserCount(long jobtitleSeniorUserCount) {
		this.jobtitleSeniorUserCount = jobtitleSeniorUserCount;
	}

	public String getJobtitleGeneralRatio() {
		return jobtitleGeneralRatio;
	}

	public void setJobtitleGeneralRatio(String jobtitleGeneralRatio) {
		this.jobtitleGeneralRatio = jobtitleGeneralRatio;
	}

	public String getJobtitleMiddleRatio() {
		return jobtitleMiddleRatio;
	}

	public void setJobtitleMiddleRatio(String jobtitleMiddleRatio) {
		this.jobtitleMiddleRatio = jobtitleMiddleRatio;
	}

	public String getJobtitleSeniorRatio() {
		return jobtitleSeniorRatio;
	}

	public void setJobtitleSeniorRatio(String jobtitleSeniorRatio) {
		this.jobtitleSeniorRatio = jobtitleSeniorRatio;
	}

	public Map<String, String> getChartDataAge() {
		return chartDataAge;
	}

	public void setChartDataAge(Map<String, String> chartDataAge) {
		this.chartDataAge = chartDataAge;
	}

	public Map<String, String> getChartDataEducation() {
		return chartDataEducation;
	}

	public void setChartDataEducation(Map<String, String> chartDataEducation) {
		this.chartDataEducation = chartDataEducation;
	}

	public Map<String, String> getChartDataWorkage() {
		return chartDataWorkage;
	}

	public void setChartDataWorkage(Map<String, String> chartDataWorkage) {
		this.chartDataWorkage = chartDataWorkage;
	}

	public Map<String, String> getChartDataJobtitle() {
		return chartDataJobtitle;
	}

	public void setChartDataJobtitle(Map<String, String> chartDataJobtitle) {
		this.chartDataJobtitle = chartDataJobtitle;
	}
}
