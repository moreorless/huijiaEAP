package com.huijia.eap.quiz.module;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.huijia.eap.annotation.AuthBy;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.user.service.UserService;
import com.huijia.eap.quiz.data.Company;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizCategory;
import com.huijia.eap.quiz.data.QuizItem;
import com.huijia.eap.quiz.data.QuizItemOption;
import com.huijia.eap.quiz.data.QuizResult;
import com.huijia.eap.quiz.data.Segment;
import com.huijia.eap.quiz.data.report.TeamReportCommonParamSet;
import com.huijia.eap.quiz.data.report.TeamReportCommunicationSet;
import com.huijia.eap.quiz.data.report.TeamReportConflictSet;
import com.huijia.eap.quiz.data.report.TeamReportEmployeeSurveySet;
import com.huijia.eap.quiz.data.report.TeamReportMentalCheckupParamSet;
import com.huijia.eap.quiz.service.CompanyService;
import com.huijia.eap.quiz.service.QuizCategoryService;
import com.huijia.eap.quiz.service.QuizItemService;
import com.huijia.eap.quiz.service.QuizResultService;
import com.huijia.eap.quiz.service.QuizService;
import com.huijia.eap.quiz.service.SegmentService;

@IocBean
@InjectName
@AuthBy(check = false)
@At("/report/company")
public class CompanyReportModule {

	@Inject
	private CompanyService companyService;

	@Inject
	private SegmentService segmentService;

	@Inject
	private QuizService quizService;

	@Inject
	private UserService userService;

	@Inject
	private QuizResultService quizResultService;

	@Inject
	private QuizCategoryService quizCategoryService;

	@Inject
	private QuizItemService quizItemService;

	/**
	 * 当前查询操作对应的quizresult结果
	 */
	private List<QuizResult> currentQuizResultList = new LinkedList<QuizResult>();
	/**
	 * 当前查询操作对应的有效quizresult结果
	 */
	// private List<QuizResult> currentValidQuizResultList = new
	// LinkedList<QuizResult>();

	/**
	 * 当前已完成测试的用户信息列表
	 */
	private List<User> currentTestedUserList = new LinkedList<User>();

	/**
	 * 当前有效测试的用户信息列表(满足测谎线要求)
	 */
	private List<User> currentValidUserList = new LinkedList<User>();

	/**
	 * 报告页面显示的通用变量集合
	 */
	private TeamReportCommonParamSet currentCommonParameterSet = new TeamReportCommonParamSet();

	/**
	 * 第一份问卷 心理健康评测报告页面显示变量集合
	 */
	private TeamReportMentalCheckupParamSet currentMentalCheckupParamSet = new TeamReportMentalCheckupParamSet();

	/**
	 * 第二套问卷 子问卷1 沟通风格子问卷
	 */
	private TeamReportCommunicationSet currentCommunicationParamSet = new TeamReportCommunicationSet();

	/**
	 * 第二套问卷 子问卷2 冲突处理子问卷
	 */
	private TeamReportConflictSet currentConflictParamSet = new TeamReportConflictSet();

	/**
	 * 第三套问卷
	 */
	private TeamReportEmployeeSurveySet currentEmployeeSurveySet = new TeamReportEmployeeSurveySet();

	private int _getScoreByAnswer(String answer, String quizName) {
		int ret = 0;

		if (quizName.equals("employeeSurvey")) {
			if (answer.equals("A"))
				ret = 0;
			else if (answer.equals("B"))
				ret = 1;
			else if (answer.equals("C"))
				ret = 2;
			else if (answer.equals("D"))
				ret = 3;
			else if (answer.equals("E"))
				ret = 4;
			else if (answer.equals("F"))
				ret = 5;
		}

		return ret;
	}

	/**
	 * 预处理报告中使用的一些通用数据，例如年龄构成等信息
	 */
	private void preProcess(HttpServletRequest request, long segmentId,
			long quizId) {

		currentCommonParameterSet.init();

		Segment segment = segmentService.fetch(segmentId);
		Company company = companyService.fetch(segment.getCompanyId());
		Quiz quiz = quizService.fetch(quizId);
		request.setAttribute("segment", segment);
		request.setAttribute("company", company);
		request.setAttribute("quiz", quiz);

		currentQuizResultList = quizResultService.getTestedQuizResultList(
				segmentId, quizId);
		// currentValidQuizResultList =
		// quizResultService.getValidQuizResultList(
		// segmentId, quizId); // 这个貌似没有用
		List<User> registeredUserList = userService.fetchBySegmentId(segmentId);
		double ratio;
		int precision = 2;

		for (QuizResult quizResult : currentQuizResultList) {
			for (User user : registeredUserList) {
				if (user.getUserId() == quizResult.getUserId()) {
					if (currentTestedUserList.contains(user) == false)
						currentTestedUserList.add(user);
					if (quizResult.isValid()
							&& currentValidUserList.contains(user) == false)
						currentValidUserList.add(user);
					break;
				}
			}
		} // end of for
		
		/**************************** 开始计算数量 ******************************/
		currentCommonParameterSet.segmentId = segmentId;
		currentCommonParameterSet.quizId = quizId;

		currentCommonParameterSet.companyName = company.getName();
		currentCommonParameterSet.segmentUserCount = segment.getSize();
		currentCommonParameterSet.registeredUserCount = registeredUserList
				.size();
		currentCommonParameterSet.testedUserCount = currentTestedUserList
				.size();
		currentCommonParameterSet.validUserCount = currentValidUserList.size();

		ratio = currentCommonParameterSet.validUserCount * 1.0
				/ currentCommonParameterSet.segmentUserCount;

		currentCommonParameterSet.validUserRatio = formatRatio(ratio * 100,
				precision) + "%";

		for (User user : currentValidUserList) {

			// 1．性别构成 ******************************/
			if (user.getGender() == 0)
				currentCommonParameterSet.genderFemaleUserCount++;
			else
				currentCommonParameterSet.genderMaleUserCount++;

			// 2.年龄构成 ******************************/
			if (user.getAge() < 31)
				currentCommonParameterSet.age0to30UserCount++;
			else if (user.getAge() < 41)
				currentCommonParameterSet.age31to40UserCount++;
			else if (user.getAge() < 51)
				currentCommonParameterSet.age41to50UserCount++;
			else
				currentCommonParameterSet.age51to99UserCount++;

			// 3.教育程度构成 ********************/
			if (user.getEducation() == 0)
				currentCommonParameterSet.educationDazhuanUserCount++;
			else if (user.getEducation() == 1)
				currentCommonParameterSet.educationBenkeUserCount++;
			else
				currentCommonParameterSet.educationShuoshiUserCount++;

			// 4.工作年龄构成 ********************/
			if (user.getWorkage() < 3)
				currentCommonParameterSet.workage1to3UserCount++;
			else if (user.getWorkage() < 5)
				currentCommonParameterSet.workage3to5UserCount++;
			else if (user.getWorkage() < 10)
				currentCommonParameterSet.workage5to10UserCount++;
			else
				currentCommonParameterSet.workage10to99UserCount++;

			// 5．职位状况构成 ****************/
			if (user.getJobtitle() == 0)
				currentCommonParameterSet.jobtitleGeneralUserCount++;
			else if (user.getJobtitle() == 1)
				currentCommonParameterSet.jobtitleMiddleUserCount++;
			else
				currentCommonParameterSet.jobtitleSeniorUserCount++;
		}

		/************ 开始计算比例 *****************/

		long totalUserCount = currentValidUserList.size();
		// 1．性别构成
		ratio = currentCommonParameterSet.genderMaleUserCount * 1.0
				/ currentCommonParameterSet.genderFemaleUserCount;
		currentCommonParameterSet.genderRatio = "1:"
				+ formatRatio(ratio, precision);
		ratio = currentCommonParameterSet.genderMaleUserCount * 1.0
				/ totalUserCount;
		currentCommonParameterSet.genderMaleRatio = formatRatio(ratio * 100,
				precision) + "%";
		ratio = currentCommonParameterSet.genderFemaleUserCount * 1.0
				/ totalUserCount;
		currentCommonParameterSet.genderFemaleRatio = formatRatio(ratio * 100,
				precision) + "%";

		// 2.年龄构成 ******************************/
		ratio = currentCommonParameterSet.age0to30UserCount * 1.0
				/ totalUserCount;
		currentCommonParameterSet.age0to30Ratio = formatRatio(ratio * 100,
				precision) + "%";
		ratio = currentCommonParameterSet.age31to40UserCount * 1.0
				/ totalUserCount;
		currentCommonParameterSet.age31to40Ratio = formatRatio(ratio * 100,
				precision) + "%";
		ratio = currentCommonParameterSet.age41to50UserCount * 1.0
				/ totalUserCount;
		currentCommonParameterSet.age41to50Ratio = formatRatio(ratio * 100,
				precision) + "%";
		ratio = currentCommonParameterSet.age51to99UserCount * 1.0
				/ totalUserCount;
		currentCommonParameterSet.age51to99Ratio = formatRatio(ratio * 100,
				precision) + "%";
		currentCommonParameterSet.ageMaxName = generateAgeMaxName();

		// 3.教育程度构成 ********************/
		ratio = currentCommonParameterSet.educationDazhuanUserCount * 1.0
				/ totalUserCount;
		currentCommonParameterSet.educationDazhuanRatio = formatRatio(
				ratio * 100, precision) + "%";
		ratio = currentCommonParameterSet.educationBenkeUserCount * 1.0
				/ totalUserCount;
		currentCommonParameterSet.educationBenkeRatio = formatRatio(
				ratio * 100, precision) + "%";
		ratio = currentCommonParameterSet.educationShuoshiUserCount * 1.0
				/ totalUserCount;
		currentCommonParameterSet.educationShuoshiRatio = formatRatio(
				ratio * 100, precision) + "%";
		ratio = (currentCommonParameterSet.educationDazhuanUserCount + currentCommonParameterSet.educationBenkeUserCount)
				* 1.0 / totalUserCount;
		currentCommonParameterSet.educationBenkeShuoshiRatio = formatRatio(
				ratio * 100, precision) + "%";

		// 4.工作年龄构成 ********************/
		ratio = currentCommonParameterSet.workage1to3UserCount * 1.0
				/ totalUserCount;
		currentCommonParameterSet.workage1to3Ratio = formatRatio(ratio * 100,
				precision) + "%";

		currentCommonParameterSet.workageComment = generateWorkageComment(ratio);// 此样本中，员工普遍工作经验较少,工作1-3年以上的员工接近七成

		ratio = currentCommonParameterSet.workage3to5UserCount * 1.0
				/ totalUserCount;
		currentCommonParameterSet.workage3to5Ratio = formatRatio(ratio * 100,
				precision) + "%";
		ratio = currentCommonParameterSet.workage5to10UserCount * 1.0
				/ totalUserCount;
		currentCommonParameterSet.workage5to10Ratio = formatRatio(ratio * 100,
				precision) + "%";
		ratio = currentCommonParameterSet.workage10to99UserCount * 1.0
				/ totalUserCount;
		currentCommonParameterSet.workage10to99Ratio = formatRatio(ratio * 100,
				precision) + "%";

		// 5．职位状况构成 ****************/
		ratio = currentCommonParameterSet.jobtitleGeneralUserCount * 1.0
				/ totalUserCount;
		currentCommonParameterSet.jobtitleGeneralRatio = formatRatio(
				ratio * 100, precision) + "%";
		ratio = currentCommonParameterSet.jobtitleMiddleUserCount * 1.0
				/ totalUserCount;
		currentCommonParameterSet.jobtitleMiddleRatio = formatRatio(
				ratio * 100, precision) + "%";
		ratio = currentCommonParameterSet.jobtitleSeniorUserCount * 1.0
				/ totalUserCount;
		currentCommonParameterSet.jobtitleSeniorRatio = formatRatio(
				ratio * 100, precision) + "%";

	}

	/********************
	 * 获取人数最多的年龄部分
	 * 
	 * @return
	 */
	private String generateAgeMaxName() {
		long max = currentCommonParameterSet.age0to30UserCount;
		String result = "30岁以内";
		if (max < currentCommonParameterSet.age31to40UserCount) {
			max = currentCommonParameterSet.age31to40UserCount;
			result = "31到40岁";
		}
		if (max < currentCommonParameterSet.age41to50UserCount) {
			max = currentCommonParameterSet.age41to50UserCount;
			result = "41到50岁";
		}
		if (max < currentCommonParameterSet.age51to99UserCount) {
			max = currentCommonParameterSet.age51to99UserCount;
			result = "50岁以上";
		}

		return result;
	}

	/**
	 * 
	 * @return
	 */
	private String generateWorkageComment(double ratio) {
		String result = "";
		BigDecimal r = new BigDecimal(10 * ratio).setScale(0,
				BigDecimal.ROUND_HALF_UP); // 四舍五入取整
		if (ratio >= 0.55)
			result = "员工普遍工作经验较少，工作1-3年以上的员工接近" + r + "成";
		else if (ratio >= 0.45)
			result = "员工普遍工作经验还不错，工作3年及以上的员工接近5成";
		else
			result = "员工普遍工作经验比较高，工作3年及其以上的员工接近"
					+ new BigDecimal(10 - 10 * ratio).setScale(0,
							BigDecimal.ROUND_HALF_UP) + "成";
		return result;
	}

	/*************************
	 * 按位数四舍五入格式化double
	 */
	private String formatRatio(double ratio, int size) {

		NumberFormat ddf = NumberFormat.getNumberInstance();

		ddf.setMaximumFractionDigits(size);
		String s = ddf.format(ratio);
		return s;
	}

	private void mentalCheckupProcess(long segmentId, long quizId) {

		currentMentalCheckupParamSet.init();

		Quiz quiz = quizService.fetch(quizId);
		List<QuizResult> allResultList = quizResultService
				.getTestedQuizResultList(segmentId, quizId);
		List<QuizResult> resultList = new LinkedList<QuizResult>();
		for (QuizResult result : allResultList) {
			if (result.getLieScore() <= quiz.getLieBorder())
				resultList.add(result);
		}

		double score = 0.0;
		double ratio = 0.0;
		String evaluation = "";
		long totalUserCount = resultList.size();
		int precision = 2;

		for (QuizResult result : resultList) {
			score += result.getScore();
		}
		score = score * 1.0 / resultList.size();
		currentMentalCheckupParamSet.wholeScore = score;

		int level = getMentalCheckupScoreLevel(score);

		if (level == 1) {
			evaluation += "较差水平";
		} else if (level == 2) {
			evaluation += "不良水平";
		} else if (level == 3) {
			evaluation += "中等水平";
		} else if (level == 4) {
			evaluation += "较好水平";
		} else
			evaluation += "非常好水平";
		currentMentalCheckupParamSet.wholeEvaluation = evaluation;

		score = 0.0;
		evaluation = "";

		currentMentalCheckupParamSet.jiaoChaUserCount = 0;
		currentMentalCheckupParamSet.jiaoChaRatio = "";
		currentMentalCheckupParamSet.jiaoChaLowestScore = 0;
		currentMentalCheckupParamSet.buLiangUserCount = 0;
		currentMentalCheckupParamSet.buLiangRatio = "";
		currentMentalCheckupParamSet.zhongDengRatio = "";
		currentMentalCheckupParamSet.zhongDengUserCount = 0;
		currentMentalCheckupParamSet.jiaoHaoRatio = "";
		currentMentalCheckupParamSet.jiaoHaoUserCount = 0;
		currentMentalCheckupParamSet.henHaoUserCount = 0;
		currentMentalCheckupParamSet.henHaoRatio = "";

		for (QuizResult result : resultList) {
			level = getMentalCheckupScoreLevel(result.getScore());
			if (currentMentalCheckupParamSet.jiaoChaLowestScore > result
					.getScore())
				currentMentalCheckupParamSet.jiaoChaLowestScore = result
						.getScore();

			if (level == 1) {
				currentMentalCheckupParamSet.jiaoChaUserCount++;
			}
			if (level == 2) {
				currentMentalCheckupParamSet.buLiangUserCount++;
			}
			if (level == 3) {
				currentMentalCheckupParamSet.zhongDengUserCount++;
			}
			if (level == 4) {
				currentMentalCheckupParamSet.jiaoHaoUserCount++;
			}
			if (level == 5) {
				currentMentalCheckupParamSet.henHaoUserCount++;
			}
		}

		ratio = currentMentalCheckupParamSet.jiaoChaUserCount * 1.0
				/ totalUserCount;
		currentMentalCheckupParamSet.jiaoChaRatio = formatRatio(ratio * 100,
				precision) + "%";

		ratio = currentMentalCheckupParamSet.buLiangUserCount * 1.0
				/ totalUserCount;
		currentMentalCheckupParamSet.buLiangRatio = formatRatio(ratio * 100,
				precision) + "%";

		ratio = currentMentalCheckupParamSet.zhongDengUserCount * 1.0
				/ totalUserCount;
		currentMentalCheckupParamSet.zhongDengRatio = formatRatio(ratio * 100,
				precision) + "%";

		ratio = currentMentalCheckupParamSet.jiaoHaoUserCount * 1.0
				/ totalUserCount;
		currentMentalCheckupParamSet.jiaoHaoRatio = formatRatio(ratio * 100,
				precision) + "%";

		ratio = currentMentalCheckupParamSet.henHaoUserCount * 1.0
				/ totalUserCount;
		currentMentalCheckupParamSet.henHaoRatio = formatRatio(ratio * 100,
				precision) + "%";

		currentMentalCheckupParamSet.needJingTiRatioUserCount = currentMentalCheckupParamSet.jiaoChaUserCount
				+ currentMentalCheckupParamSet.buLiangUserCount;
		ratio = currentMentalCheckupParamSet.needJingTiRatioUserCount * 1.0
				/ totalUserCount;
		currentMentalCheckupParamSet.needJingTiRatio = formatRatio(ratio * 100,
				precision) + "%";
		currentMentalCheckupParamSet.needTiGaoUserCount = currentMentalCheckupParamSet.zhongDengUserCount;
		currentMentalCheckupParamSet.needTiGaoRatio = currentMentalCheckupParamSet.zhongDengRatio;

		/**
		 * 维度顺序 0: 积极心态 1: 情绪管理 2: 行为表现 3: 生理症状 4: 社会支持 5: 自我防御
		 */
		int[] categoryLowBorder = { 31, 24, 24, 26, 25, 24 };
		int[] categoryHighBorder = { 37, 35, 34, 37, 35, 33 };

		List<QuizCategory> categoryList = quizCategoryService
				.getByQuizId(quizId);
		int[] categoryIds = new int[categoryList.size()];
		String[] categoryNames = { "积极心态", "情绪管理", "行为表现", "生理症状", "社会支持",
				"自我防御" };
		for (QuizCategory category : categoryList) {
			if (category.getName().equals("积极心态"))
				categoryIds[0] = category.getId();
			if (category.getName().equals("情绪管理"))
				categoryIds[1] = category.getId();
			if (category.getName().equals("行为表现"))
				categoryIds[2] = category.getId();
			if (category.getName().equals("生理症状"))
				categoryIds[3] = category.getId();
			if (category.getName().equals("社会支持"))
				categoryIds[4] = category.getId();
			if (category.getName().equals("自我防御"))
				categoryIds[5] = category.getId();
		}

		// List <Integer> lowScoreCategories = new ArrayList<Integer>();
		// List <Integer> highScoreCategories = new ArrayList<Integer>();

		int[] lowScoreCategories = { 0, 0, 0, 0, 0, 0 };
		int[] highScoreCategories = { 0, 0, 0, 0, 0, 0 };

		for (QuizResult result : resultList) {
			Map<String, Integer> scoreMap = result.getScoreMap();
			for (int i = 0; i < 6; i++) {
				if (scoreMap.get(Integer.toString(categoryIds[i])) < categoryLowBorder[i])
					lowScoreCategories[i]++;
				if (scoreMap.get(Integer.toString(categoryIds[i])) > categoryHighBorder[i])
					highScoreCategories[i]++;
			}
		}

		int lowestCategoryId = 0;
		int highestCategoryId = 0;
		int lowestScore = 40;
		int highestScore = 0;
		for (int i = 0; i < 6; i++) {
			if (lowScoreCategories[i] < lowestScore) {
				lowestCategoryId = i;
				lowestScore = lowScoreCategories[i];
			}
			if (highScoreCategories[i] < highestScore) {
				highestCategoryId = i;
				highestScore = highScoreCategories[i];
			}
		}
		currentMentalCheckupParamSet.lowScoreCategories = categoryNames[lowestCategoryId]
				+ "维度";
		currentMentalCheckupParamSet.highScoreCategories = categoryNames[highestCategoryId]
				+ "维度";

		List<Integer> percent30CategoryIds = new ArrayList<Integer>();
		for (int i = 0; i < 6; i++) {
			ratio = lowScoreCategories[i] * 1.0 / totalUserCount;
			if (ratio > 0.3)
				percent30CategoryIds.add(i);
		}

		String[] numNames = { "零", "一", "二", "三", "四", "五", "六" };

		currentMentalCheckupParamSet.percent30CategoryNum = percent30CategoryIds
				.size();
		currentMentalCheckupParamSet.percent30CategoryCount = numNames[percent30CategoryIds
				.size()];
		currentMentalCheckupParamSet.percent30Categories = "";
		for (int i = 0; i < percent30CategoryIds.size(); i++) {
			if (i > 0)
				currentMentalCheckupParamSet.percent30Categories += "、";
			currentMentalCheckupParamSet.percent30Categories += categoryNames[percent30CategoryIds
					.get(i)] + "维度";
		}

	}

	/*
	 * 根据分数返回等级：1:较差水平; 2: 不良水平; 3:中等; 4:较好; 5:非常好
	 */
	private int getMentalCheckupScoreLevel(double score) {
		int ret = 0;
		if (score <= 144) {
			ret = 1;
		} else if (score < 168) {
			ret = 2;
		} else if (score <= 192) {
			ret = 3;
		} else if (score < 216) {
			ret = 4;
		} else
			ret = 5;
		return ret;
	}

	/**
	 * 个人心理分析团体报告
	 */
	@At
	@Ok("jsp:jsp.report.company.mental_checkup")
	public void mentalCheckup(HttpServletRequest request,
			@Param("segmentId") long segmentId, @Param("quizId") long quizId) {
		preProcess(request, segmentId, quizId);
		request.setAttribute("commonParamSet", currentCommonParameterSet);
		mentalCheckupProcess(segmentId, quizId);
		request.setAttribute("mentalParamSet", currentMentalCheckupParamSet);
	}

	private void communicationProcess(long segmentId, long quizId) {
		currentCommunicationParamSet.init();

		Quiz quiz = quizService.fetch(quizId);
		List<QuizResult> allResultList = quizResultService
				.getTestedQuizResultList(segmentId, quizId);
		List<QuizResult> resultList = new LinkedList<QuizResult>();
		for (QuizResult result : allResultList) {
			Quiz q = quizService.fetch(result.getQuizId());
			if (result.getLieScore() <= quiz.getLieBorder()
					&& q.getTag().equals("communicate_style"))
				resultList.add(result);
		}

		int precision = 2;
		double ratio = 0.0;

		long totalUserCount = resultList.size();

		// 排序表格显示
		Collections
				.sort(currentEmployeeSurveySet.detailDrivingLoyaltyRatioList);
		Map<String, Long> userCountCategoryMap = new HashMap<String, Long>();
		userCountCategoryMap.put("支配型", (long) 0);
		userCountCategoryMap.put("表达型", (long) 0);
		userCountCategoryMap.put("和蔼型", (long) 0);
		userCountCategoryMap.put("分析型", (long) 0);

		for (QuizResult result : resultList) {
			if (result.getCategoryName().indexOf("支配") >= 0) {
				userCountCategoryMap.put("支配型",
						userCountCategoryMap.get("支配型") + 1);
			}
			if (result.getCategoryName().indexOf("表达") >= 0) {
				userCountCategoryMap.put("表达型",
						userCountCategoryMap.get("表达型") + 1);
			}
			if (result.getCategoryName().indexOf("和蔼") >= 0) {
				userCountCategoryMap.put("和蔼型",
						userCountCategoryMap.get("和蔼型") + 1);
			}
			if (result.getCategoryName().indexOf("分析") >= 0) {
				userCountCategoryMap.put("分析型",
						userCountCategoryMap.get("分析型") + 1);
			}
		}

		// 排序
		List<Map.Entry<String, Long>> tmpList = new ArrayList<Map.Entry<String, Long>>(
				userCountCategoryMap.entrySet());
		Collections.sort(tmpList, new Comparator<Map.Entry<String, Long>>() {
			public int compare(Map.Entry<String, Long> o1,
					Map.Entry<String, Long> o2) {
				BigDecimal data1 = new BigDecimal(o1.getValue());
				BigDecimal data2 = new BigDecimal(o2.getValue());
				return data2.compareTo(data1);
			}
		});
		// 排序结束，顺序结果存放在tmpList中

		currentCommunicationParamSet.userCountBiaoda = userCountCategoryMap
				.get("表达型");
		currentCommunicationParamSet.userCountZhipei = userCountCategoryMap
				.get("支配型");
		currentCommunicationParamSet.userCountHeai = userCountCategoryMap
				.get("和蔼型");
		currentCommunicationParamSet.userCountFenxi = userCountCategoryMap
				.get("分析型");

		ratio = currentCommunicationParamSet.userCountBiaoda * 1.0
				/ totalUserCount;
		currentCommunicationParamSet.userRatioBiaoda = formatRatio(ratio * 100,
				precision) + "%";

		ratio = currentCommunicationParamSet.userCountZhipei * 1.0
				/ totalUserCount;
		currentCommunicationParamSet.userRatioZhipei = formatRatio(ratio * 100,
				precision) + "%";

		ratio = currentCommunicationParamSet.userCountHeai * 1.0
				/ totalUserCount;
		currentCommunicationParamSet.userRatioHeai = formatRatio(ratio * 100,
				precision) + "%";

		ratio = currentCommunicationParamSet.userCountFenxi * 1.0
				/ totalUserCount;
		currentCommunicationParamSet.userRatioFenxi = formatRatio(ratio * 100,
				precision) + "%";

		if (tmpList.get(0).getKey().equals("表达型")) {
			currentCommunicationParamSet.evaluation = "在这个团体中，大部分人是比较友好、活泼和热情的，喜欢在沟通中配合动作和手势。另外，赞扬和肯定对他们很重要，这个团队成员大部分会很快抓住问题的实质，或者说主要问题，但可能容易忽略细节。所以，在与这样的团队沟通时，说话有直接，还需要叮嘱其保留沟通的材料，且留意细节问题。另外，多给与肯定和表扬也是很重要的。";
		}
		if (tmpList.get(0).getKey().equals("支配型")) {
			currentCommunicationParamSet.evaluation = "在这个团体中，大部分人沟通时比较果断，不会很热情，他们看重沟通的结果，比较讲究信息的准确性和直接性，不太喜欢拐弯抹角的沟通。所以，在与这样的团体打交道中，尽量不要有太多寒暄和发散，直奔主题会更有效。这样的团体比较喜欢声音洪亮语速较快的谈话，也喜欢目光接触。所以，与这样的团体沟通的人，需要注意自己的外在表现，在沟通中尽量声音洪亮且自信满满会比较容易达成更好的沟通效果。";
		}
		if (tmpList.get(0).getKey().equals("和蔼型")) {
			currentCommunicationParamSet.evaluation = "这个团体中，大部分人是比较和蔼、亲切、很好说话的。他们比较注意沟通对方的情绪。在沟通中，他们比较看重彼此间的情感沟通，所以可能会忽视沟通的效率和沟通的结果。所以，在与这样的团队沟通时，说话的语速要比较慢，多面带微笑，多眼神接触，还要多鼓励。如果想知道个别人的建议，需要点名提问，不然他们不会主动发表意见。";
		}
		if (tmpList.get(0).getKey().equals("分析型")) {
			currentCommunicationParamSet.evaluation = "这个团体中，大部分人是比较偏爱逻辑性强、分析型强的沟通，如专业术语、数据、计划书、图表等。他们一般很认真、严肃，不太喜欢太多的眼神交流，也不喜欢太多的身体接触。所以，在与这样的团队沟通时，如果你也边说边写，表现得更他们一样认真，如果还能列举一二三四，体现你的逻辑思维能力，再配上图表，那么你们的沟通会更加顺畅。";
		}

		currentCommunicationParamSet.topCategory = tmpList.get(0).getKey();
	}

	private void conflictProcess(long segmentId, long quizId) {
		currentConflictParamSet.init();

		Quiz quiz = quizService.fetch(quizId);
		List<QuizResult> allResultList = quizResultService
				.getTestedQuizResultList(segmentId, quizId);
		List<QuizResult> resultList = new LinkedList<QuizResult>();
		for (QuizResult result : allResultList) {
			Quiz q = quizService.fetch(result.getQuizId());
			if (result.getLieScore() <= quiz.getLieBorder()
					&& q.getTag().equals("conflict_style"))
				resultList.add(result);
		}

		int precision = 2;
		double ratio = 0.0;

		long totalUserCount = resultList.size();

		// 排序表格显示
		Collections
				.sort(currentEmployeeSurveySet.detailDrivingLoyaltyRatioList);
		Map<String, Long> userCountCategoryMap = new HashMap<String, Long>();
		userCountCategoryMap.put("支配型", (long) 0);
		userCountCategoryMap.put("折中型", (long) 0);
		userCountCategoryMap.put("回避型", (long) 0);
		userCountCategoryMap.put("谦让型", (long) 0);
		userCountCategoryMap.put("整合型", (long) 0);

		for (QuizResult result : resultList) {
			if (result.getCategoryName().indexOf("支配") >= 0) {
				userCountCategoryMap.put("支配型",
						userCountCategoryMap.get("支配型") + 1);
			}
			if (result.getCategoryName().indexOf("折中") >= 0) {
				userCountCategoryMap.put("折中型",
						userCountCategoryMap.get("折中型") + 1);
			}
			if (result.getCategoryName().indexOf("回避") >= 0) {
				userCountCategoryMap.put("回避型",
						userCountCategoryMap.get("回避型") + 1);
			}
			if (result.getCategoryName().indexOf("谦让") >= 0) {
				userCountCategoryMap.put("谦让型",
						userCountCategoryMap.get("谦让型") + 1);
			}
			if (result.getCategoryName().indexOf("整合") >= 0) {
				userCountCategoryMap.put("整合型",
						userCountCategoryMap.get("整合型") + 1);
			}
		}

		// 排序
		List<Map.Entry<String, Long>> tmpList = new ArrayList<Map.Entry<String, Long>>(
				userCountCategoryMap.entrySet());
		Collections.sort(tmpList, new Comparator<Map.Entry<String, Long>>() {
			public int compare(Map.Entry<String, Long> o1,
					Map.Entry<String, Long> o2) {
				BigDecimal data1 = new BigDecimal(o1.getValue());
				BigDecimal data2 = new BigDecimal(o2.getValue());
				return data2.compareTo(data1);
			}
		});
		// 排序结束，顺序结果存放在tmpList中

		currentConflictParamSet.userCountZhipei = userCountCategoryMap
				.get("支配型");
		currentConflictParamSet.userCountZhezhong = userCountCategoryMap
				.get("折中型");
		currentConflictParamSet.userCountHuibi = userCountCategoryMap
				.get("回避型");
		currentConflictParamSet.userCountQianrang = userCountCategoryMap
				.get("谦让型");
		currentConflictParamSet.userCountZhenghe = userCountCategoryMap
				.get("整合型");

		ratio = currentConflictParamSet.userCountZhipei * 1.0 / totalUserCount;
		currentConflictParamSet.userRatioZhipei = formatRatio(ratio * 100,
				precision) + "%";
		ratio = currentConflictParamSet.userCountZhezhong * 1.0
				/ totalUserCount;
		currentConflictParamSet.userRatioZhezhong = formatRatio(ratio * 100,
				precision) + "%";
		ratio = currentConflictParamSet.userCountHuibi * 1.0 / totalUserCount;
		currentConflictParamSet.userRatioHuibi = formatRatio(ratio * 100,
				precision) + "%";
		ratio = currentConflictParamSet.userCountQianrang * 1.0
				/ totalUserCount;
		currentConflictParamSet.userRatioQianrang = formatRatio(ratio * 100,
				precision) + "%";
		ratio = currentConflictParamSet.userCountZhenghe * 1.0 / totalUserCount;
		currentConflictParamSet.userRatioZhenghe = formatRatio(ratio * 100,
				precision) + "%";

		if (tmpList.get(0).getKey().equals("支配型")) {
			currentConflictParamSet.evaluation = "在冲突处理和问题解决中，这个团队的大部分人是比较有想法、有主见的。当问题来临时，不会逃避，不会妥协，会积极主动地解决冲突，虽然解决冲突的方法可能会比较粗暴。因为团队中大部分人喜欢采用支配型冲突处理方式，所以达成一致意见的时间会比较长。这种团队比较适合由整合型或者折中型的冲突处理风格的人带领。";
		}
		if (tmpList.get(0).getKey().equals("折中型")) {
			currentConflictParamSet.evaluation = "在冲突处理和问题解决中，这个团队的大部分人不会选择逃避问题，而是主动寻求问题的解决方案。在寻找方案的过程中，他们也会尽量做到公平，会考虑到所有人的利益，会尽量选择一个大家都能接受的方案，虽然这个方案可能不是最佳的那一个。因为团队中大部分人喜欢折中型的冲突处理方式，所以在某些问题的处理上不够完善。如果大家再往前进一步，再多做一些研究，多做一些探索和发掘，效果可能更好。这种团队比较适合由整合型冲突处理方式的人带领。";
		}
		if (tmpList.get(0).getKey().equals("回避型")) {
			currentConflictParamSet.evaluation = "在冲突处理和问题解决中，这个团队的大部分人会选择漠视、逃避等态度，不够积极，也不太爱出风头，不太愿意发言，也不表态。因为团队中大部分人喜欢采用逃避性的冲突处理方式，所以虽然可能存在很多问题，但这些问题都深埋在人群之下。这种团队比较适合由整合型或者支配型的冲突处理方式的人带领。";
		}
		if (tmpList.get(0).getKey().equals("谦让型")) {
			currentConflictParamSet.evaluation = "在冲突处理和问题解决中，这个团队的大部分人重视团队，乐于助人，有很强的协作精神，而且会把别人的需求放在自己的需求之前，能够很好地顾及他人感受。但是因为团队中大部分人都喜欢采用谦让的冲突处理方式，所以很多时会忽视自己的角度，看问题不够全面。这种团队比较适合由整合型的冲突处理方式的人带领。";
		}
		if (tmpList.get(0).getKey().equals("整合型")) {
			currentConflictParamSet.evaluation = "在冲突处理和问题解决中，这个团队的大部分重视团队，乐于助人，有很强的协作精神。因为团队中大部分人喜欢采用整合型的冲突处理方式，所以该团队团队凝聚力比较高，气氛也比较好。但是因为这种团体某些极端情况下，会有点偏向完美主义，从而影响问题解决的效率，所以给他们安排时间截止点，并适时地督促他们解决问题会更好。";
		}

		currentConflictParamSet.topCategory = tmpList.get(0).getKey();
	}

	/**
	 * 沟通风格与冲突处理
	 */
	@At
	@Ok("jsp:jsp.report.company.communicate_conflict")
	public void communicateConflict(HttpServletRequest request,
			@Param("segmentId") long segmentId, @Param("quizId") long quizId) {
		preProcess(request, segmentId, quizId);
		request.setAttribute("commonParamSet", currentCommonParameterSet);
		communicationProcess(segmentId, quizId);
		request.setAttribute("communicationParamSet",
				currentCommunicationParamSet);
		conflictProcess(segmentId, quizId);
		request.setAttribute("conflictParamSet", currentConflictParamSet);

	}

	private static void sortQuestionListById(List<QuizItem> data) {
		Collections.sort(data, new Comparator<QuizItem>() {
			public int compare(QuizItem o1, QuizItem o2) {
				// 降序
				return (int) (o1.getId() - o2.getId());
			}
		});
	}

	private static void sortQuestionListByTotalScore(List<QuizItem> data) {
		Collections.sort(data, new Comparator<QuizItem>() {
			public int compare(QuizItem o1, QuizItem o2) {
				// 降序
				return (int) (o2.getTotalScoresAllResults() - o1
						.getTotalScoresAllResults());
			}
		});
	}

	class BestEmployerData { // 最佳雇主数据类
		// 3.3 满意度整体分析
		// （1）整体选择频率分析
		public double satisfactionRank1Ratio = 0.341;; // 非常同意
		public double satisfactionRank2Ratio = 0.452;
		public double satisfactionRank3Ratio = 0.129;
		public double satisfactionRank4Ratio = 0.036;
		public double satisfactionRank5Ratio = 0.018;
		public double satisfactionRank6Ratio = 0.023; // 非常不同意

		// 3.3整体满意度分析
		// （2）整体满意度分析
		public long wholeSatisfactionScoreXinchoufuli = 68; // 薪酬福利整体满意度
		public long wholeSatisfactionScoreZhiyefazhan = 76; // 职业发展
		public long wholeSatisfactionScoreGongzuozhize = 86;// 工作职责
		public long wholeSatisfactionScoreGuanli = 70; // 管理
		public long wholeSatisfactionScoreHuanjing = 90; // 环境
	}

	private void employeeSurveyProcess(long segmentId, long quizId) {

		currentEmployeeSurveySet.init();

		Quiz quiz = quizService.fetch(quizId);
		List<QuizResult> allResultList = quizResultService
				.getTestedQuizResultList(segmentId, quizId);
		List<QuizResult> resultList = new LinkedList<QuizResult>();
		for (QuizResult result : allResultList) {
			if (result.getLieScore() <= quiz.getLieBorder())
				resultList.add(result);
		}

		int precision = 2;
		double ratio;

		// 3.2优势劣势问题

		ArrayList<QuizItem> questionList = new ArrayList<QuizItem>();

		// 遍历所有答案，计算所有题目获得的总分
		for (QuizResult result : resultList) {
			Map<String, String> answerMap = (Map<String, String>) Json
					.fromJson(result.getAnswer());
			for (Entry<String, String> answer : answerMap.entrySet()) {
				int qiId = Integer.valueOf(answer.getKey());
				QuizItem item = quizItemService.fetch(qiId);
				String qiValue = answer.getValue();
				QuizItemOption qiOption = item.getOption(qiValue);

				int score = qiOption.getValue();
				boolean _flag = false;
				for (QuizItem qi : questionList) {
					if (qi.getId() == qiId) {
						qi.setTotalScoresAllResults(qi
								.getTotalScoresAllResults() + score);
						_flag = true;
						break;
					}
				}
				if (_flag == false) {
					item.setTotalScoresAllResults(score);
					questionList.add(item);
				}
			}
		}

		sortQuestionListById(questionList);

		/**
		 * 添加题目编号
		 */
		int index = 1;
		for (QuizItem qi : questionList) {
			qi.setIndex(index);
			index++;
		}

		ArrayList<QuizItem> sortedQuestionList = new ArrayList<QuizItem>(
				questionList);

		sortQuestionListByTotalScore(sortedQuestionList);

		// 移除 "表现"和“满意度”的相关题目
		Iterator<QuizItem> iter = sortedQuestionList.iterator();
		while (iter.hasNext()) {
			QuizItem qi = iter.next();
			if (qi.getOptions().getFirst().getCategoryName().indexOf("表现") >= 0
					|| qi.getOptions().getFirst().getCategoryName()
							.indexOf("满意度") >= 0) {
				iter.remove();
			}
		}

		List<QuizItem> top1QuizItemList = new LinkedList<QuizItem>();
		List<QuizItem> top2QuizItemList = new LinkedList<QuizItem>();
		List<QuizItem> top3QuizItemList = new LinkedList<QuizItem>();
		int flag = 0;
		long currentScore = 0;

		for (QuizItem qi : sortedQuestionList) {
			if (flag == 0) {// 添加第一个元素的题目列表
				currentScore = qi.getTotalScoresAllResults();
				top1QuizItemList.add(qi);
				flag = 1;
			} else if (flag == 1) {
				if (qi.getTotalScoresAllResults() == currentScore) {
					top1QuizItemList.add(qi);
					flag = 1;
				} else {
					currentScore = qi.getTotalScoresAllResults();
					top2QuizItemList.add(qi);
					flag = 2;
				}
			} else if (flag == 2) {
				if (qi.getTotalScoresAllResults() == currentScore) {
					top2QuizItemList.add(qi);
					flag = 2;
				} else {
					currentScore = qi.getTotalScoresAllResults();
					top3QuizItemList.add(qi);
					flag = 3;
				}
			} else if (flag == 3) {
				if (qi.getTotalScoresAllResults() == currentScore) {
					top3QuizItemList.add(qi);
					flag = 3;
				} else {
					break;
				}
			} else
				break;
		}
		Set<String> questionIds = new HashSet<String>();
		Set<String> questionFactors = new HashSet<String>();
		for (QuizItem qi : top1QuizItemList) {
			questionIds.add(Integer.toString((int) qi.getIndex()));
			questionFactors.add(qi.getOptions().getFirst().getCategoryName()
					.split("/")[0]);
		}
		currentEmployeeSurveySet.top1QuestionIds = questionIds.toString()
				.substring(1, questionIds.toString().length() - 1);
		currentEmployeeSurveySet.top1QuestionFactors = questionFactors
				.toString().substring(1,
						questionFactors.toString().length() - 1);

		questionIds.clear();
		questionFactors.clear();
		for (QuizItem qi : top2QuizItemList) {
			questionIds.add(Integer.toString((int) qi.getIndex()));
			questionFactors.add(qi.getOptions().getFirst().getCategoryName()
					.split("/")[0]);
		}
		currentEmployeeSurveySet.top2QuestionIds = questionIds.toString()
				.substring(1, questionIds.toString().length() - 1);
		currentEmployeeSurveySet.top2QuestionFactors = questionFactors
				.toString().substring(1,
						questionFactors.toString().length() - 1);

		questionIds.clear();
		questionFactors.clear();
		for (QuizItem qi : top3QuizItemList) {
			questionIds.add(Integer.toString((int) qi.getIndex()));
			questionFactors.add(qi.getOptions().getFirst().getCategoryName()
					.split("/")[0]);
		}
		currentEmployeeSurveySet.top3QuestionIds = questionIds.toString()
				.substring(1, questionIds.toString().length() - 1);
		currentEmployeeSurveySet.top3QuestionFactors = questionFactors
				.toString().substring(1,
						questionFactors.toString().length() - 1);

		Collections.reverse(sortedQuestionList);

		flag = 0;
		currentScore = 0;

		List<QuizItem> low1QuizItemList = new LinkedList<QuizItem>();
		List<QuizItem> low2QuizItemList = new LinkedList<QuizItem>();
		List<QuizItem> low3QuizItemList = new LinkedList<QuizItem>();

		for (QuizItem qi : sortedQuestionList) {
			if (flag == 0) {// 添加第一个元素的题目列表
				currentScore = qi.getTotalScoresAllResults();
				low1QuizItemList.add(qi);
				flag = 1;
			} else if (flag == 1) {
				if (qi.getTotalScoresAllResults() == currentScore) {
					low1QuizItemList.add(qi);
					flag = 1;
				} else {
					currentScore = qi.getTotalScoresAllResults();
					low2QuizItemList.add(qi);
					flag = 2;
				}
			} else if (flag == 2) {
				if (qi.getTotalScoresAllResults() == currentScore) {
					low2QuizItemList.add(qi);
					flag = 2;
				} else {
					currentScore = qi.getTotalScoresAllResults();
					low3QuizItemList.add(qi);
					flag = 3;
				}
			} else if (flag == 3) {
				if (qi.getTotalScoresAllResults() == currentScore) {
					low3QuizItemList.add(qi);
					flag = 3;
				} else {
					break;
				}
			} else
				break;
		}
		questionIds.clear();
		questionFactors.clear();
		for (QuizItem qi : low1QuizItemList) {
			questionIds.add(Integer.toString((int) qi.getIndex()));
			questionFactors.add(qi.getOptions().getFirst().getCategoryName()
					.split("/")[0]);
		}
		currentEmployeeSurveySet.low1QuestionIds = questionIds.toString()
				.substring(1, questionIds.toString().length() - 1);
		currentEmployeeSurveySet.low1QuestionFactors = questionFactors
				.toString().substring(1,
						questionFactors.toString().length() - 1);

		questionIds.clear();
		questionFactors.clear();
		for (QuizItem qi : low2QuizItemList) {
			questionIds.add(Integer.toString((int) qi.getIndex()));
			questionFactors.add(qi.getOptions().getFirst().getCategoryName()
					.split("/")[0]);
		}
		currentEmployeeSurveySet.low2QuestionIds = questionIds.toString()
				.substring(1, questionIds.toString().length() - 1);
		currentEmployeeSurveySet.low2QuestionFactors = questionFactors
				.toString().substring(1,
						questionFactors.toString().length() - 1);

		questionIds.clear();
		questionFactors.clear();
		for (QuizItem qi : low3QuizItemList) {
			questionIds.add(Integer.toString((int) qi.getIndex()));
			questionFactors.add(qi.getOptions().getFirst().getCategoryName()
					.split("/")[0]);
		}
		currentEmployeeSurveySet.low3QuestionIds = questionIds.toString()
				.substring(1, questionIds.toString().length() - 1);
		currentEmployeeSurveySet.low3QuestionFactors = questionFactors
				.toString().substring(1,
						questionFactors.toString().length() - 1);

		String subFactors[] = new String[5];
		for (QuizItem qi : top1QuizItemList) {
			if (qi.getOptions().getFirst().getCategoryName().indexOf("工作职责") >= 0) {
				subFactors[0] = qi.getOptions().getFirst().getCategoryName()
						.split("/")[1];
			}
			if (qi.getOptions().getFirst().getCategoryName().indexOf("管理因素") >= 0) {
				subFactors[1] = qi.getOptions().getFirst().getCategoryName()
						.split("/")[1];
			}
			if (qi.getOptions().getFirst().getCategoryName().indexOf("环境因素") >= 0) {
				subFactors[2] = qi.getOptions().getFirst().getCategoryName()
						.split("/")[1];
			}
			if (qi.getOptions().getFirst().getCategoryName().indexOf("薪酬福利") >= 0) {
				subFactors[3] = qi.getOptions().getFirst().getCategoryName()
						.split("/")[1];
			}
			if (qi.getOptions().getFirst().getCategoryName().indexOf("职业发展") >= 0) {
				subFactors[4] = qi.getOptions().getFirst().getCategoryName()
						.split("/")[1];
			}
		}

		String evaluation = "";
		if (top1QuizItemList.size() == 1) {
			evaluation += "优势比较集中：在五大因素中，优势主要集中在了"
					+ currentEmployeeSurveySet.top1QuestionFactors + "这个因素上";
		} else if (top1QuizItemList.size() == 2) {
			evaluation += "优势主要集中在了"
					+ currentEmployeeSurveySet.top1QuestionFactors + "这两个因素中";
		} else if (top1QuizItemList.size() == 3) {
			evaluation += "优势比较平均：在五大因素中，"
					+ currentEmployeeSurveySet.top1QuestionFactors
					+ "这三个因素中都体现出了优势";
		} else if (top1QuizItemList.size() == 4) {
			evaluation += "优势比较平均：在五大因素中，"
					+ currentEmployeeSurveySet.top1QuestionFactors
					+ "这四个因素中都体现出了优势";
		} else if (top1QuizItemList.size() == 5) {
			evaluation += "优势非常全面：在工作职责、管理、环境、薪酬福利和职业发展五大因素中，员工在每一个因素上都有高满意度的项目,分别是工作职责因素中的"
					+ subFactors[0]
					+ "维度，管理因素中的"
					+ subFactors[1]
					+ "维度，环境因素中的"
					+ subFactors[2]
					+ "维度，薪酬福利中的"
					+ subFactors[3]
					+ "维度，以及职业发展中的" + subFactors[4] + "维度";
		}
		currentEmployeeSurveySet.topEvaluation = evaluation;

		// 3.3整体满意度分析
		// （1）整体选择频率分析
		/*
		 * 遍历所有答案，统计所有题目的选项个数
		 */
		BestEmployerData bestEmployerData = new BestEmployerData();

		int totalScore = 0;

		for (QuizResult result : resultList) {
			Map<String, String> answerMap = (Map<String, String>) Json
					.fromJson(result.getAnswer());
			for (Entry<String, String> answer : answerMap.entrySet()) {
				int qiId = Integer.valueOf(answer.getKey());
				QuizItem item = quizItemService.fetch(qiId);
				if (item.getOptions().getFirst().getCategoryName()
						.indexOf("表现") >= 0)
					continue;
				if (answer.getValue().equals("F"))
					currentEmployeeSurveySet.satisfactionRank1Count++;
				if (answer.getValue().equals("E"))
					currentEmployeeSurveySet.satisfactionRank2Count++;
				if (answer.getValue().equals("D"))
					currentEmployeeSurveySet.satisfactionRank3Count++;
				if (answer.getValue().equals("C"))
					currentEmployeeSurveySet.satisfactionRank4Count++;
				if (answer.getValue().equals("B"))
					currentEmployeeSurveySet.satisfactionRank5Count++;
				if (answer.getValue().equals("A"))
					currentEmployeeSurveySet.satisfactionRank6Count++;
				totalScore++;
			}
		}

		ratio = currentEmployeeSurveySet.satisfactionRank1Count * 1.0
				/ totalScore;
		currentEmployeeSurveySet.satisfactionRank1Ratio = formatRatio(
				ratio * 100, precision) + "%";
		if (ratio < bestEmployerData.satisfactionRank1Ratio) {
			currentEmployeeSurveySet.satisfactionRank1Evaluation = "低"
					+ formatRatio(
							(bestEmployerData.satisfactionRank1Ratio - ratio) * 100,
							precision) + "个百分点";
		} else {
			currentEmployeeSurveySet.satisfactionRank1Evaluation = "高"
					+ formatRatio(
							(ratio - bestEmployerData.satisfactionRank1Ratio) * 100,
							precision) + "个百分点";
		}

		ratio = currentEmployeeSurveySet.satisfactionRank2Count * 1.0
				/ totalScore;
		currentEmployeeSurveySet.satisfactionRank2Ratio = formatRatio(
				ratio * 100, precision) + "%";
		if (ratio < bestEmployerData.satisfactionRank2Ratio) {
			currentEmployeeSurveySet.satisfactionRank2Evaluation = "低"
					+ formatRatio(
							(bestEmployerData.satisfactionRank2Ratio - ratio) * 100,
							precision) + "个百分点";
		} else {
			currentEmployeeSurveySet.satisfactionRank2Evaluation = "高"
					+ formatRatio(
							(ratio - bestEmployerData.satisfactionRank2Ratio) * 100,
							precision) + "个百分点";
		}

		ratio = currentEmployeeSurveySet.satisfactionRank3Count * 1.0
				/ totalScore;
		currentEmployeeSurveySet.satisfactionRank3Ratio = formatRatio(
				ratio * 100, precision) + "%";

		ratio = currentEmployeeSurveySet.satisfactionRank4Count * 1.0
				/ totalScore;
		currentEmployeeSurveySet.satisfactionRank4Ratio = formatRatio(
				ratio * 100, precision) + "%";

		ratio = currentEmployeeSurveySet.satisfactionRank5Count * 1.0
				/ totalScore;
		currentEmployeeSurveySet.satisfactionRank5Ratio = formatRatio(
				ratio * 100, precision) + "%";

		ratio = currentEmployeeSurveySet.satisfactionRank6Count * 1.0
				/ totalScore;
		currentEmployeeSurveySet.satisfactionRank6Ratio = formatRatio(
				ratio * 100, precision) + "%";
		if (ratio < bestEmployerData.satisfactionRank6Ratio) {
			currentEmployeeSurveySet.satisfactionRank6Evaluation = "低"
					+ formatRatio(
							(bestEmployerData.satisfactionRank6Ratio - ratio) * 100,
							precision) + "个百分点";
		} else {
			currentEmployeeSurveySet.satisfactionRank6Evaluation = "高"
					+ formatRatio(
							(ratio - bestEmployerData.satisfactionRank6Ratio) * 100,
							precision) + "个百分点";
		}

		// 3.3整体满意度分析
		// （2）整体满意度分析
		totalScore = 0;

		for (QuizResult result : resultList) {
			Map<String, String> answerMap = (Map<String, String>) Json
					.fromJson(result.getAnswer());
			for (Entry<String, String> answer : answerMap.entrySet()) {
				int qiId = Integer.valueOf(answer.getKey());
				QuizItem item = quizItemService.fetch(qiId);
				if (item.getOptions().getFirst().getCategoryName()
						.indexOf("表现") >= 0
						|| item.getOptions().getFirst().getCategoryName()
								.indexOf("满意度") >= 0)
					continue;
				if (item.getOptions().getFirst().getCategoryName()
						.indexOf("管理") >= 0) {
					int score = _getScoreByAnswer(answer.getValue(),
							"employeeSurvey");
					currentEmployeeSurveySet.wholeSatisfactionScoreGuanli += score;
					totalScore += score;
				}
				if (item.getOptions().getFirst().getCategoryName()
						.indexOf("薪酬福利") >= 0) {
					int score = _getScoreByAnswer(answer.getValue(),
							"employeeSurvey");
					currentEmployeeSurveySet.wholeSatisfactionScoreXinchoufuli += score;
					totalScore += score;
				}
				if (item.getOptions().getFirst().getCategoryName()
						.indexOf("职业发展") >= 0) {
					int score = _getScoreByAnswer(answer.getValue(),
							"employeeSurvey");
					currentEmployeeSurveySet.wholeSatisfactionScoreZhiyefazhan += score;
					totalScore += score;
				}
				if (item.getOptions().getFirst().getCategoryName()
						.indexOf("工作职责") >= 0) {
					int score = _getScoreByAnswer(answer.getValue(),
							"employeeSurvey");
					currentEmployeeSurveySet.wholeSatisfactionScoreGongzuozhize += score;
					totalScore += score;
				}
				if (item.getOptions().getFirst().getCategoryName()
						.indexOf("环境") >= 0) {
					int score = _getScoreByAnswer(answer.getValue(),
							"employeeSurvey");
					currentEmployeeSurveySet.wholeSatisfactionScoreHuanjing += score;
					totalScore += score;
				}
			}
		}
		currentEmployeeSurveySet.wholeSatisfactionScoreGuanli = Integer
				.parseInt(formatRatio(
						currentEmployeeSurveySet.wholeSatisfactionScoreGuanli
								* 1.0 / 25 / resultList.size() * 100, 0));
		currentEmployeeSurveySet.wholeSatisfactionScoreXinchoufuli = Integer
				.parseInt(formatRatio(
						currentEmployeeSurveySet.wholeSatisfactionScoreXinchoufuli
								* 1.0 / 15 / resultList.size() * 100, 0));
		currentEmployeeSurveySet.wholeSatisfactionScoreZhiyefazhan = Integer
				.parseInt(formatRatio(
						currentEmployeeSurveySet.wholeSatisfactionScoreZhiyefazhan
								* 1.0 / 15 / resultList.size() * 100, 0));
		currentEmployeeSurveySet.wholeSatisfactionScoreGongzuozhize = Integer
				.parseInt(formatRatio(
						currentEmployeeSurveySet.wholeSatisfactionScoreGongzuozhize
								* 1.0 / 10 / resultList.size() * 100, 0));
		currentEmployeeSurveySet.wholeSatisfactionScoreHuanjing = Integer
				.parseInt(formatRatio(
						currentEmployeeSurveySet.wholeSatisfactionScoreHuanjing
								* 1.0 / 20 / resultList.size() * 100, 0));
		List<String> wholeSatisfactionFactorListOverBestEmployee = new ArrayList<String>();
		List<String> wholeSatisfactionFactorListEqualBestEmployee = new ArrayList<String>();
		List<String> wholeSatisfactionFactorListUnderBestEmployee = new ArrayList<String>();
		long currentGap = 0; // 与最佳雇主之间的差距

		if (currentEmployeeSurveySet.wholeSatisfactionScoreGuanli > bestEmployerData.wholeSatisfactionScoreGuanli)
			wholeSatisfactionFactorListOverBestEmployee.add("管理");
		else {
			long _gap = bestEmployerData.wholeSatisfactionScoreGuanli
					- currentEmployeeSurveySet.wholeSatisfactionScoreGuanli;
			if (_gap < 4)
				wholeSatisfactionFactorListEqualBestEmployee.add("管理");
			else {
				if (currentGap == _gap) {
					if (wholeSatisfactionFactorListEqualBestEmployee
							.contains("管理") == false)
						wholeSatisfactionFactorListUnderBestEmployee.add("管理");
				}
				if (currentGap < _gap) {
					wholeSatisfactionFactorListUnderBestEmployee.clear();
					currentGap = 0;
					if (wholeSatisfactionFactorListEqualBestEmployee
							.contains("管理") == false) {
						wholeSatisfactionFactorListUnderBestEmployee.add("管理");
						currentGap = _gap;
					}
				}
			}
		}

		if (currentEmployeeSurveySet.wholeSatisfactionScoreXinchoufuli > bestEmployerData.wholeSatisfactionScoreXinchoufuli)
			wholeSatisfactionFactorListOverBestEmployee.add("薪酬福利");
		else {
			long _gap = bestEmployerData.wholeSatisfactionScoreXinchoufuli
					- currentEmployeeSurveySet.wholeSatisfactionScoreXinchoufuli;
			if (_gap < 4)
				wholeSatisfactionFactorListEqualBestEmployee.add("薪酬福利");
			else {
				if (currentGap == _gap) {
					if (wholeSatisfactionFactorListEqualBestEmployee
							.contains("薪酬福利") == false)
						wholeSatisfactionFactorListUnderBestEmployee
								.add("薪酬福利");
				}
				if (currentGap < _gap) {
					wholeSatisfactionFactorListUnderBestEmployee.clear();
					currentGap = 0;
					if (wholeSatisfactionFactorListEqualBestEmployee
							.contains("薪酬福利") == false) {
						wholeSatisfactionFactorListUnderBestEmployee
								.add("薪酬福利");
						currentGap = _gap;
					}
				}

			}
		}

		if (currentEmployeeSurveySet.wholeSatisfactionScoreZhiyefazhan > bestEmployerData.wholeSatisfactionScoreZhiyefazhan)
			wholeSatisfactionFactorListOverBestEmployee.add("职业发展");
		else {
			long _gap = bestEmployerData.wholeSatisfactionScoreZhiyefazhan
					- currentEmployeeSurveySet.wholeSatisfactionScoreZhiyefazhan;
			if (_gap < 4)
				wholeSatisfactionFactorListEqualBestEmployee.add("职业发展");
			else {
				if (currentGap == _gap) {
					if (wholeSatisfactionFactorListEqualBestEmployee
							.contains("职业发展") == false)
						wholeSatisfactionFactorListUnderBestEmployee
								.add("职业发展");
				}
				if (currentGap < _gap) {
					wholeSatisfactionFactorListUnderBestEmployee.clear();
					currentGap = 0;
					if (wholeSatisfactionFactorListEqualBestEmployee
							.contains("职业发展") == false) {
						wholeSatisfactionFactorListUnderBestEmployee
								.add("职业发展");
						currentGap = _gap;
					}
				}
			}
		}

		if (currentEmployeeSurveySet.wholeSatisfactionScoreGongzuozhize > bestEmployerData.wholeSatisfactionScoreGongzuozhize)
			wholeSatisfactionFactorListOverBestEmployee.add("工作职责");
		else {
			long _gap = bestEmployerData.wholeSatisfactionScoreGongzuozhize
					- currentEmployeeSurveySet.wholeSatisfactionScoreGongzuozhize;
			if (_gap < 4)
				wholeSatisfactionFactorListEqualBestEmployee.add("工作职责");
			else {
				if (currentGap == _gap) {
					if (wholeSatisfactionFactorListEqualBestEmployee
							.contains("工作职责") == false)
						wholeSatisfactionFactorListUnderBestEmployee
								.add("工作职责");
				}
				if (currentGap < _gap) {
					wholeSatisfactionFactorListUnderBestEmployee.clear();
					currentGap = 0;
					if (wholeSatisfactionFactorListEqualBestEmployee
							.contains("工作职责") == false) {
						wholeSatisfactionFactorListUnderBestEmployee
								.add("工作职责");
						currentGap = _gap;
					}
				}

			}
		}

		if (currentEmployeeSurveySet.wholeSatisfactionScoreHuanjing > bestEmployerData.wholeSatisfactionScoreHuanjing)
			wholeSatisfactionFactorListOverBestEmployee.add("环境");
		else {
			long _gap = bestEmployerData.wholeSatisfactionScoreHuanjing
					- currentEmployeeSurveySet.wholeSatisfactionScoreHuanjing;
			if (_gap < 4)
				wholeSatisfactionFactorListEqualBestEmployee.add("环境");
			else {
				if (currentGap == _gap) {
					if (wholeSatisfactionFactorListEqualBestEmployee
							.contains("环境") == false)
						wholeSatisfactionFactorListUnderBestEmployee.add("环境");
				}
				if (currentGap < _gap) {
					wholeSatisfactionFactorListUnderBestEmployee.clear();
					currentGap = 0;
					if (wholeSatisfactionFactorListEqualBestEmployee
							.contains("环境") == false) {
						wholeSatisfactionFactorListUnderBestEmployee.add("环境");
						currentGap = _gap;
					}
				}
			}
		}
		if (wholeSatisfactionFactorListOverBestEmployee.size() > 0)
			for (int i = 0; i < wholeSatisfactionFactorListOverBestEmployee
					.size(); i++) {
				if (i == wholeSatisfactionFactorListOverBestEmployee.size() - 1)
					currentEmployeeSurveySet.wholeSatisfactionEvaluation += wholeSatisfactionFactorListOverBestEmployee
							.get(i) + "因素上，比最佳雇主还略胜一筹，员工满意度很高；";
				else
					currentEmployeeSurveySet.wholeSatisfactionEvaluation += wholeSatisfactionFactorListOverBestEmployee
							.get(i) + "因素、";
			}
		if (wholeSatisfactionFactorListEqualBestEmployee.size() > 0)
			for (int i = 0; i < wholeSatisfactionFactorListEqualBestEmployee
					.size(); i++) {
				if (i == wholeSatisfactionFactorListEqualBestEmployee.size() - 1)
					currentEmployeeSurveySet.wholeSatisfactionEvaluation += wholeSatisfactionFactorListEqualBestEmployee
							.get(i) + "因素上，与最佳雇主企业几乎持平；";
				else
					currentEmployeeSurveySet.wholeSatisfactionEvaluation += wholeSatisfactionFactorListEqualBestEmployee
							.get(i) + "因素、";
			}
		if (wholeSatisfactionFactorListUnderBestEmployee.size() > 0)
			for (int i = 0; i < wholeSatisfactionFactorListUnderBestEmployee
					.size(); i++) {
				if (i == wholeSatisfactionFactorListUnderBestEmployee.size() - 1)
					currentEmployeeSurveySet.wholeSatisfactionEvaluation += wholeSatisfactionFactorListUnderBestEmployee
							.get(i) + "因素上，与最佳雇主企业差距最大";
				else
					currentEmployeeSurveySet.wholeSatisfactionEvaluation += wholeSatisfactionFactorListUnderBestEmployee
							.get(i) + "因素、";
			}

		// 3.3整体满意度分析
		// （3）子维度分析
		for (QuizResult result : resultList) {
			Map<String, String> answerMap = (Map<String, String>) Json
					.fromJson(result.getAnswer());
			for (Entry<String, String> answer : answerMap.entrySet()) {
				int qiId = Integer.valueOf(answer.getKey());
				QuizItem item = quizItemService.fetch(qiId);
				if (item.getOptions().getFirst().getCategoryName()
						.indexOf("表现") >= 0
						|| item.getOptions().getFirst().getCategoryName()
								.indexOf("满意度") >= 0)
					continue;
				currentEmployeeSurveySet.subSatisfactionFactorScores.put(
						item.getOptions().getFirst().getCategoryName(),
						currentEmployeeSurveySet.subSatisfactionFactorScores
								.get(item.getOptions().getFirst()
										.getCategoryName())
								+ _getScoreByAnswer(answer.getValue(),
										"employeeSurvey"));
			}
		}
		for (Entry<String, Double> factorScore : currentEmployeeSurveySet.subSatisfactionFactorScores
				.entrySet()) {
			currentEmployeeSurveySet.subSatisfactionFactorScores.put(
					factorScore.getKey(),
					Double.parseDouble(formatRatio(factorScore.getValue()
							/ resultList.size() * 20, precision)));
		}
		// 排序
		List<Map.Entry<String, Double>> tmpList = new ArrayList<Map.Entry<String, Double>>(
				currentEmployeeSurveySet.subSatisfactionFactorScores.entrySet());
		Collections.sort(tmpList, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1,
					Map.Entry<String, Double> o2) {
				BigDecimal data1 = new BigDecimal(o1.getValue());
				BigDecimal data2 = new BigDecimal(o2.getValue());
				return data2.compareTo(data1);
			}
		});
		// 排序结束，顺序结果存放在tmpList中
		List<String> subSatisfactionFactorListBest = new ArrayList<String>();
		List<String> subSatisfactionFactorListSecond = new ArrayList<String>();
		List<String> subSatisfactionFactorListWorst = new ArrayList<String>();

		Double subFactorScoreBest = 0.0;
		Double subFactorScoreSecond = 0.0;
		Double subFactorScoreWorst = 0.0;
		subFactorScoreBest = tmpList.get(0).getValue();
		subFactorScoreWorst = tmpList.get(tmpList.size() - 1).getValue();
		for (int i = 0; i < tmpList.size(); i++) {
			if (tmpList.get(i).getValue() < subFactorScoreBest) {
				subFactorScoreSecond = tmpList.get(i).getValue();
				break;
			}
			if (tmpList.get(i).getValue() == subFactorScoreBest
					&& i == tmpList.size() - 1)
				subFactorScoreSecond = -1.0;
		}
		for (Map.Entry<String, Double> e : tmpList) {
			if (e.getValue() == subFactorScoreBest)
				subSatisfactionFactorListBest.add(e.getKey());
			if (e.getValue() == subFactorScoreSecond)
				subSatisfactionFactorListSecond.add(e.getKey());
			if (e.getValue() == subFactorScoreWorst)
				subSatisfactionFactorListWorst.add(e.getKey());
		}

		if (subSatisfactionFactorListBest.size() > 0) {
			currentEmployeeSurveySet.subSatisfactionEvaluation += "最高的是";
			for (int i = 0; i < subSatisfactionFactorListBest.size(); i++) {
				if (i == subSatisfactionFactorListBest.size() - 1) {
					currentEmployeeSurveySet.subSatisfactionEvaluation += "“"
							+ subSatisfactionFactorListBest.get(i)
							+ "”二级因素，满意度为"
							+ formatRatio(subFactorScoreBest, precision) + "%。";
				} else
					currentEmployeeSurveySet.subSatisfactionEvaluation += "“"
							+ subSatisfactionFactorListBest.get(i) + "”、";
			}
		}
		if (subSatisfactionFactorListSecond.size() > 0) {
			currentEmployeeSurveySet.subSatisfactionEvaluation += "其次为";
			for (int i = 0; i < subSatisfactionFactorListSecond.size(); i++) {
				if (i == subSatisfactionFactorListSecond.size() - 1) {
					currentEmployeeSurveySet.subSatisfactionEvaluation += "“"
							+ subSatisfactionFactorListSecond.get(i) + "”。";
				} else
					currentEmployeeSurveySet.subSatisfactionEvaluation += "“"
							+ subSatisfactionFactorListSecond.get(i) + "”、";
			}
		}
		if (subSatisfactionFactorListWorst.size() > 0) {
			currentEmployeeSurveySet.subSatisfactionEvaluation += "满意度最低的二级因素是";
			for (int i = 0; i < subSatisfactionFactorListWorst.size(); i++) {
				if (i == subSatisfactionFactorListWorst.size() - 1) {
					currentEmployeeSurveySet.subSatisfactionEvaluation += "“"
							+ subSatisfactionFactorListWorst.get(i) + "”，满意度为"
							+ formatRatio(subFactorScoreWorst, precision)
							+ "%。";
					;
				} else
					currentEmployeeSurveySet.subSatisfactionEvaluation += "“"
							+ subSatisfactionFactorListWorst.get(i) + "”、";
			}
		}

		// 3.4 员工忠诚度分析
		// 整体忠诚度
		for (QuizResult result : resultList) {
			Map<String, String> answerMap = (Map<String, String>) Json
					.fromJson(result.getAnswer());
			for (Entry<String, String> answer : answerMap.entrySet()) {
				int qiId = Integer.valueOf(answer.getKey());
				QuizItem item = quizItemService.fetch(qiId);
				if (item.getOptions().getFirst().getCategoryName()
						.equals("表现/赞扬")) {// 第一题
					if (answer.getValue().equals("F")) { // 非常同意
						currentEmployeeSurveySet.wholeLoyaltyPraiseCount++;
					}
					if (answer.getValue().equals("E")) { // 同意
						currentEmployeeSurveySet.wholeLoyaltyPraiseCount++;

					}
					if (answer.getValue().equals("D")) { // 比较同意
						currentEmployeeSurveySet.wholeLoyaltyPraiseCount++;

					}
					if (answer.getValue().equals("C")) { // 比较不同意
						currentEmployeeSurveySet.wholeLoyaltyNoPraiseCount++;
					}
					if (answer.getValue().equals("B")) { // 不同意
						currentEmployeeSurveySet.wholeLoyaltyNoPraiseCount++;
					}
					if (answer.getValue().equals("A")) { // 非常不同意
						currentEmployeeSurveySet.wholeLoyaltyNoPraiseCount++;
					}
				}
				if (item.getOptions().getFirst().getCategoryName()
						.equals("表现/留任")) {// 第二题
					if (answer.getValue().equals("F")) { // 非常同意
						currentEmployeeSurveySet.wholeLoyaltyRemainCount++;
					}
					if (answer.getValue().equals("E")) { // 同意
						currentEmployeeSurveySet.wholeLoyaltyRemainCount++;

					}
					if (answer.getValue().equals("D")) { // 比较同意
						currentEmployeeSurveySet.wholeLoyaltyRemainCount++;
					}
					if (answer.getValue().equals("C")) { // 比较不同意
						currentEmployeeSurveySet.wholeLoyaltyNoRemainCount++;
					}
					if (answer.getValue().equals("B")) { // 不同意
						currentEmployeeSurveySet.wholeLoyaltyNoRemainCount++;
					}
					if (answer.getValue().equals("A")) { // 非常不同意
						currentEmployeeSurveySet.wholeLoyaltyNoRemainCount++;
						currentEmployeeSurveySet.wholeLoyaltyLeaveCount++;
						currentEmployeeSurveySet.wholeLoyaltyLeaveUserList
								.add(userService.fetch(result.getUserId())
										.getName());
					}
				}
			}
		}
		ratio = currentEmployeeSurveySet.wholeLoyaltyPraiseCount * 1.0
				/ resultList.size();
		currentEmployeeSurveySet.wholeLoyaltyPraiseRatio = formatRatio(
				ratio * 100, precision) + "%";
		ratio = currentEmployeeSurveySet.wholeLoyaltyNoPraiseCount * 1.0
				/ resultList.size();
		currentEmployeeSurveySet.wholeLoyaltyNoPraiseRatio = formatRatio(
				ratio * 100, precision) + "%";
		ratio = currentEmployeeSurveySet.wholeLoyaltyRemainCount * 1.0
				/ resultList.size();
		currentEmployeeSurveySet.wholeLoyaltyRemainRatio = formatRatio(
				ratio * 100, precision) + "%";
		ratio = currentEmployeeSurveySet.wholeLoyaltyNoRemainCount * 1.0
				/ resultList.size();
		currentEmployeeSurveySet.wholeLoyaltyNoRemainRatio = formatRatio(
				ratio * 100, precision) + "%";

		currentEmployeeSurveySet.wholeLoyaltyEvaluation += "从上图可以看出，经常或很多次表现赞扬的人，比经常考虑留任的人";
		Double dValue = currentEmployeeSurveySet.wholeLoyaltyPraiseCount * 1.0
				/ resultList.size()
				- currentEmployeeSurveySet.wholeLoyaltyRemainCount * 1.0
				/ resultList.size();
		if (dValue > 0.005) {
			currentEmployeeSurveySet.wholeLoyaltyEvaluation += "要多"
					+ formatRatio(dValue * 100, precision)
					+ "%，也就是说，即使有人会在外表扬、夸赞公司，但是他们不会真正留下。";
		} else if (dValue < -0.005) {
			currentEmployeeSurveySet.wholeLoyaltyEvaluation += "要少"
					+ formatRatio(dValue * (-1.0) * 100, precision)
					+ "%，也就是说，即使有人会继续留任，但是他们不会在外夸赞和表扬公司。";
		} else
			currentEmployeeSurveySet.wholeLoyaltyEvaluation += "差不多。";
		currentEmployeeSurveySet.wholeLoyaltyEvaluation += "很少或根本没有表现赞扬的人，比经常考虑留任的人";
		dValue = currentEmployeeSurveySet.wholeLoyaltyNoPraiseCount * 1.0
				/ resultList.size()
				- currentEmployeeSurveySet.wholeLoyaltyRemainCount * 1.0
				/ resultList.size();
		if (dValue > 0.005) {
			currentEmployeeSurveySet.wholeLoyaltyEvaluation += "要多"
					+ formatRatio(dValue * 100, precision) + "%，";
		} else if (dValue < -0.005) {
			currentEmployeeSurveySet.wholeLoyaltyEvaluation += "要少"
					+ formatRatio(dValue * (-1.0) * 100, precision) + "%，";
		} else
			currentEmployeeSurveySet.wholeLoyaltyEvaluation += "差不多。";

		// 3.4 员工忠诚度分析
		// （2）分类分析
		List<Long> praiseUserList = new ArrayList<Long>();
		List<Long> remainUserList = new ArrayList<Long>();
		for (QuizResult result : resultList) {
			Map<String, String> answerMap = (Map<String, String>) Json
					.fromJson(result.getAnswer());
			for (Entry<String, String> answer : answerMap.entrySet()) {
				int qiId = Integer.valueOf(answer.getKey());
				QuizItem item = quizItemService.fetch(qiId);
				if (item.getOptions().getFirst().getCategoryName()
						.equals("表现/赞扬")) {// 第一题
					if (answer.getValue().equals("F")
							|| answer.getValue().equals("E")
							|| answer.getValue().equals("D")) { // 非常同意
						praiseUserList.add(result.getUserId());
					}
				}
				if (item.getOptions().getFirst().getCategoryName()
						.equals("表现/留任")) {// 第二题
					if (answer.getValue().equals("F")
							|| answer.getValue().equals("E")
							|| answer.getValue().equals("D")) { // 非常同意
						remainUserList.add(result.getUserId());
					}
				}
			}
		}
		int praiseWholeCount = 0;
		int praiseAverageCount = 0;
		int remainWholeCount = 0;
		int remainAverageCount = 0;
		for (QuizResult result : resultList) {
			Map<String, String> answerMap = (Map<String, String>) Json
					.fromJson(result.getAnswer());
			for (Entry<String, String> answer : answerMap.entrySet()) {
				int qiId = Integer.valueOf(answer.getKey());
				QuizItem item = quizItemService.fetch(qiId);
				if (item.getOptions().getFirst().getCategoryName()
						.indexOf("表现") >= 0)
					continue;
				else if (item.getOptions().getFirst().getCategoryName()
						.indexOf("满意度") >= 0) {
					if (praiseUserList.contains(result.getUserId())) {
						praiseWholeCount++;
						currentEmployeeSurveySet.classifyLoyaltyPraiseWholeSatisfictionScore += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
					}
					if (remainUserList.contains(result.getUserId())) {
						praiseAverageCount++;
						currentEmployeeSurveySet.classifyLoyaltyPraiseAverageSatisfictionScore += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
					}
				} else {
					if (praiseUserList.contains(result.getUserId())) {
						remainWholeCount++;
						currentEmployeeSurveySet.classifyLoyaltyRemainWholeSatisfictionScore += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
					}
					if (remainUserList.contains(result.getUserId())) {
						remainAverageCount++;
						currentEmployeeSurveySet.classifyLoyaltyRemainAverageSatisfictionScore += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
					}
				}
			}
		}

		currentEmployeeSurveySet.classifyLoyaltyPraiseWholeSatisfictionScore = currentEmployeeSurveySet.classifyLoyaltyPraiseWholeSatisfictionScore
				* 1.0 / praiseWholeCount * 20;
		currentEmployeeSurveySet.classifyLoyaltyPraiseAverageSatisfictionScore = currentEmployeeSurveySet.classifyLoyaltyPraiseAverageSatisfictionScore
				* 1.0 / praiseAverageCount * 20;
		currentEmployeeSurveySet.classifyLoyaltyRemainWholeSatisfictionScore = currentEmployeeSurveySet.classifyLoyaltyRemainWholeSatisfictionScore
				* 1.0 / remainWholeCount * 20;
		currentEmployeeSurveySet.classifyLoyaltyRemainAverageSatisfictionScore = currentEmployeeSurveySet.classifyLoyaltyRemainAverageSatisfictionScore
				* 1.0 / remainAverageCount * 20;
		currentEmployeeSurveySet.classifyLoyaltyEvaluation += "从上图中可以看出，在整体满意度得分上，倾向表现出赞扬的个体比表现出留任的个体";
		if (currentEmployeeSurveySet.classifyLoyaltyPraiseWholeSatisfictionScore > currentEmployeeSurveySet.classifyLoyaltyRemainWholeSatisfictionScore) {
			currentEmployeeSurveySet.classifyLoyaltyEvaluation += "高。";
		} else if (currentEmployeeSurveySet.classifyLoyaltyPraiseWholeSatisfictionScore < currentEmployeeSurveySet.classifyLoyaltyRemainWholeSatisfictionScore) {
			currentEmployeeSurveySet.classifyLoyaltyEvaluation += "低。";
		} else if (currentEmployeeSurveySet.classifyLoyaltyPraiseWholeSatisfictionScore == currentEmployeeSurveySet.classifyLoyaltyRemainWholeSatisfictionScore) {
			currentEmployeeSurveySet.classifyLoyaltyEvaluation += "二者相等。";
		} else
			;

		currentEmployeeSurveySet.classifyLoyaltyEvaluation += "在五大因素的平均满意度得分上，倾向表现出赞扬的个体比表现出留任的个体";
		if (currentEmployeeSurveySet.classifyLoyaltyPraiseAverageSatisfictionScore > currentEmployeeSurveySet.classifyLoyaltyRemainAverageSatisfictionScore) {
			currentEmployeeSurveySet.classifyLoyaltyEvaluation += "高。";
		} else if (currentEmployeeSurveySet.classifyLoyaltyPraiseAverageSatisfictionScore < currentEmployeeSurveySet.classifyLoyaltyRemainAverageSatisfictionScore) {
			currentEmployeeSurveySet.classifyLoyaltyEvaluation += "低。";
		} else if (currentEmployeeSurveySet.classifyLoyaltyPraiseAverageSatisfictionScore == currentEmployeeSurveySet.classifyLoyaltyRemainAverageSatisfictionScore) {
			currentEmployeeSurveySet.classifyLoyaltyEvaluation += "二者相等。";
		} else
			;

		// 3.4 员工忠诚度分析
		// （3）员工忠诚度驱力因素分析

		long totalScorePraise = 0;
		long totalScoreRemain = 0;

		long drivingLoyaltyPraiseScoreGuanli = 0;
		long drivingLoyaltyPraiseScoreGongzuozhize = 0;
		long drivingLoyaltyPraiseScoreHuanjing = 0;
		long drivingLoyaltyPraiseScoreXinchoufuli = 0;
		long drivingLoyaltyPraiseScoreZhiyefazhan = 0;
		long drivingLoyaltyRemainScoreGuanli = 0;
		long drivingLoyaltyRemainScoreGongzuozhize = 0;
		long drivingLoyaltyRemainScoreHuanjing = 0;
		long drivingLoyaltyRemainScoreXinchoufuli = 0;
		long drivingLoyaltyRemainScoreZhiyefazhan = 0;

		for (QuizResult result : resultList) {
			Map<String, String> answerMap = (Map<String, String>) Json
					.fromJson(result.getAnswer());
			for (Entry<String, String> answer : answerMap.entrySet()) {
				int qiId = Integer.valueOf(answer.getKey());
				QuizItem item = quizItemService.fetch(qiId);
				if (item.getOptions().getFirst().getCategoryName()
						.indexOf("表现") >= 0
						|| item.getOptions().getFirst().getCategoryName()
								.indexOf("满意度") >= 0)
					continue;
				if (item.getOptions().getFirst().getCategoryName()
						.indexOf("管理") >= 0) {
					if (praiseUserList.contains(result.getUserId())) {
						drivingLoyaltyPraiseScoreGuanli += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
						totalScorePraise += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
					}
					if (remainUserList.contains(result.getUserId())) {
						drivingLoyaltyRemainScoreGuanli += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
						totalScoreRemain += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
					}
				}
				if (item.getOptions().getFirst().getCategoryName()
						.indexOf("工作职责") >= 0) {
					if (praiseUserList.contains(result.getUserId())) {
						drivingLoyaltyPraiseScoreGongzuozhize += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
						totalScorePraise += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
					}
					if (remainUserList.contains(result.getUserId())) {
						drivingLoyaltyRemainScoreGongzuozhize += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
						totalScoreRemain += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
					}
				}
				if (item.getOptions().getFirst().getCategoryName()
						.indexOf("环境") >= 0) {
					if (praiseUserList.contains(result.getUserId())) {
						drivingLoyaltyPraiseScoreHuanjing += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
						totalScorePraise += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
					}
					if (remainUserList.contains(result.getUserId())) {
						drivingLoyaltyRemainScoreHuanjing += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
						totalScoreRemain += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
					}
				}
				if (item.getOptions().getFirst().getCategoryName()
						.indexOf("薪酬福利") >= 0) {
					if (praiseUserList.contains(result.getUserId())) {
						drivingLoyaltyPraiseScoreXinchoufuli += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
						totalScorePraise += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
					}
					if (remainUserList.contains(result.getUserId())) {
						drivingLoyaltyRemainScoreXinchoufuli += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
						totalScoreRemain += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
					}
				}
				if (item.getOptions().getFirst().getCategoryName()
						.indexOf("职业发展") >= 0) {
					if (praiseUserList.contains(result.getUserId())) {
						drivingLoyaltyPraiseScoreZhiyefazhan += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
						totalScorePraise += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
					}
					if (remainUserList.contains(result.getUserId())) {
						drivingLoyaltyRemainScoreZhiyefazhan += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
						totalScoreRemain += _getScoreByAnswer(
								answer.getValue(), "employeeSurvey");
					}
				}
			}
		}

		List<String> praiseImportantDrivingLoyaltyFactorList = new ArrayList<String>();
		List<String> remainImportantDrivingLoyaltyFactorList = new ArrayList<String>();
		List<Double> praiseImportantDrivingLoyaltyRatioList = new ArrayList<Double>();
		List<Double> remainImportantDrivingLoyaltyRatioList = new ArrayList<Double>();
		Map<String, Double> drivingLoyaltyGapMap = new HashMap<String, Double>();

		double gap = 0.0;
		ratio = drivingLoyaltyPraiseScoreGuanli * 1.0 / totalScorePraise;
		currentEmployeeSurveySet.drivingLoyaltyPraiseRatioGuanli = formatRatio(
				ratio * 100, precision) + "%";
		if (ratio > 0.2) {
			praiseImportantDrivingLoyaltyFactorList.add("管理");
			praiseImportantDrivingLoyaltyRatioList.add(ratio);
		}
		gap = drivingLoyaltyRemainScoreGuanli * 1.0 / totalScoreRemain - ratio;
		ratio = drivingLoyaltyRemainScoreGuanli * 1.0 / totalScoreRemain;
		currentEmployeeSurveySet.drivingLoyaltyRemainRatioGuanli = formatRatio(
				ratio * 100, precision) + "%";
		if (ratio > 0.2) {
			remainImportantDrivingLoyaltyFactorList.add("管理");
			remainImportantDrivingLoyaltyRatioList.add(ratio);
		}
		drivingLoyaltyGapMap.put("管理", Math.abs(gap));

		ratio = drivingLoyaltyPraiseScoreGongzuozhize * 1.0 / totalScorePraise;
		currentEmployeeSurveySet.drivingLoyaltyPraiseRatioGongzuozhize = formatRatio(
				ratio * 100, precision) + "%";
		if (ratio > 0.2) {
			praiseImportantDrivingLoyaltyFactorList.add("工作职责");
			praiseImportantDrivingLoyaltyRatioList.add(ratio);
		}
		gap = drivingLoyaltyRemainScoreGongzuozhize * 1.0 / totalScoreRemain
				- ratio;
		ratio = drivingLoyaltyRemainScoreGongzuozhize * 1.0 / totalScoreRemain;
		currentEmployeeSurveySet.drivingLoyaltyRemainRatioGongzuozhize = formatRatio(
				ratio * 100, precision) + "%";
		if (ratio > 0.2) {
			remainImportantDrivingLoyaltyFactorList.add("工作职责");
			remainImportantDrivingLoyaltyRatioList.add(ratio);
		}
		drivingLoyaltyGapMap.put("工作职责", Math.abs(gap));

		ratio = drivingLoyaltyPraiseScoreHuanjing * 1.0 / totalScorePraise;
		currentEmployeeSurveySet.drivingLoyaltyPraiseRatioHuanjing = formatRatio(
				ratio * 100, precision) + "%";
		if (ratio >= 0.2) {
			praiseImportantDrivingLoyaltyFactorList.add("环境");
			praiseImportantDrivingLoyaltyRatioList.add(ratio);
		}
		gap = drivingLoyaltyRemainScoreHuanjing * 1.0 / totalScoreRemain
				- ratio;
		ratio = drivingLoyaltyRemainScoreHuanjing * 1.0 / totalScoreRemain;
		currentEmployeeSurveySet.drivingLoyaltyRemainRatioHuanjing = formatRatio(
				ratio * 100, precision) + "%";
		if (ratio >= 0.2) {
			remainImportantDrivingLoyaltyFactorList.add("环境");
			remainImportantDrivingLoyaltyRatioList.add(ratio);
		}
		drivingLoyaltyGapMap.put("环境", Math.abs(gap));

		ratio = drivingLoyaltyPraiseScoreXinchoufuli * 1.0 / totalScorePraise;
		currentEmployeeSurveySet.drivingLoyaltyPraiseRatioXinchoufuli = formatRatio(
				ratio * 100, precision) + "%";
		if (ratio >= 0.2) {
			praiseImportantDrivingLoyaltyFactorList.add("薪酬福利");
			praiseImportantDrivingLoyaltyRatioList.add(ratio);
		}
		gap = drivingLoyaltyRemainScoreXinchoufuli * 1.0 / totalScoreRemain
				- ratio;
		ratio = drivingLoyaltyRemainScoreXinchoufuli * 1.0 / totalScoreRemain;
		currentEmployeeSurveySet.drivingLoyaltyRemainRatioXinchoufuli = formatRatio(
				ratio * 100, precision) + "%";
		if (ratio >= 0.2) {
			remainImportantDrivingLoyaltyFactorList.add("薪酬福利");
			remainImportantDrivingLoyaltyRatioList.add(ratio);
		}
		drivingLoyaltyGapMap.put("薪酬福利", Math.abs(gap));

		ratio = drivingLoyaltyPraiseScoreZhiyefazhan * 1.0 / totalScorePraise;
		currentEmployeeSurveySet.drivingLoyaltyPraiseRatioZhiyefazhan = formatRatio(
				ratio * 100, precision) + "%";
		if (ratio >= 0.2) {
			praiseImportantDrivingLoyaltyFactorList.add("职业发展");
			praiseImportantDrivingLoyaltyRatioList.add(ratio);
		}
		gap = drivingLoyaltyRemainScoreZhiyefazhan * 1.0 / totalScoreRemain
				- ratio;
		ratio = drivingLoyaltyRemainScoreZhiyefazhan * 1.0 / totalScoreRemain;
		currentEmployeeSurveySet.drivingLoyaltyRemainRatioZhiyefazhan = formatRatio(
				ratio * 100, precision) + "%";
		if (ratio >= 0.2) {
			remainImportantDrivingLoyaltyFactorList.add("职业发展");
			remainImportantDrivingLoyaltyRatioList.add(ratio);
		}
		drivingLoyaltyGapMap.put("职业发展", Math.abs(gap));

		currentEmployeeSurveySet.drivingLoyaltyEvaluation += "从上图可以看出，对于表现出在公司留任的员工来说，";
		for (int i = 0; i < remainImportantDrivingLoyaltyFactorList.size(); i++) {
			if (i == remainImportantDrivingLoyaltyFactorList.size() - 1) {
				currentEmployeeSurveySet.drivingLoyaltyEvaluation += remainImportantDrivingLoyaltyFactorList
						.get(i) + "因素很重要";
			} else
				currentEmployeeSurveySet.drivingLoyaltyEvaluation += remainImportantDrivingLoyaltyFactorList
						.get(i) + "因素、";
		}
		currentEmployeeSurveySet.drivingLoyaltyEvaluation += "，其重要性为";
		for (int i = 0; i < remainImportantDrivingLoyaltyRatioList.size(); i++) {
			if (i == remainImportantDrivingLoyaltyRatioList.size() - 1) {
				currentEmployeeSurveySet.drivingLoyaltyEvaluation += formatRatio(
						remainImportantDrivingLoyaltyRatioList.get(i) * 100,
						precision)
						+ "%。";
			} else
				currentEmployeeSurveySet.drivingLoyaltyEvaluation += formatRatio(
						remainImportantDrivingLoyaltyRatioList.get(i) * 100,
						precision)
						+ "%、";
		}
		currentEmployeeSurveySet.drivingLoyaltyEvaluation += "对于表现出对公司表扬的员工来说，";
		for (int i = 0; i < praiseImportantDrivingLoyaltyFactorList.size(); i++) {
			if (i == praiseImportantDrivingLoyaltyFactorList.size() - 1) {
				currentEmployeeSurveySet.drivingLoyaltyEvaluation += praiseImportantDrivingLoyaltyFactorList
						.get(i) + "因素很重要";
			} else
				currentEmployeeSurveySet.drivingLoyaltyEvaluation += praiseImportantDrivingLoyaltyFactorList
						.get(i) + "因素、";
		}
		currentEmployeeSurveySet.drivingLoyaltyEvaluation += "，其重要性为";
		for (int i = 0; i < praiseImportantDrivingLoyaltyRatioList.size(); i++) {
			if (i == praiseImportantDrivingLoyaltyRatioList.size() - 1) {
				currentEmployeeSurveySet.drivingLoyaltyEvaluation += formatRatio(
						praiseImportantDrivingLoyaltyRatioList.get(i) * 100,
						precision)
						+ "%。";
			} else
				currentEmployeeSurveySet.drivingLoyaltyEvaluation += formatRatio(
						praiseImportantDrivingLoyaltyRatioList.get(i) * 100,
						precision)
						+ "%、";
		}

		tmpList = new ArrayList<Map.Entry<String, Double>>(
				drivingLoyaltyGapMap.entrySet());
		Collections.sort(tmpList, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1,
					Map.Entry<String, Double> o2) {
				BigDecimal data1 = new BigDecimal(o1.getValue());
				BigDecimal data2 = new BigDecimal(o2.getValue());
				return data2.compareTo(data1);
			}
		});
		// 排序结束，顺序结果存放在tmpList中
		currentEmployeeSurveySet.drivingLoyaltyEvaluation += "对比两行数据可知，在"
				+ tmpList.get(0).getKey() + "因素、" + tmpList.get(1).getKey()
				+ "因素上差异较大，最大的两者相差"
				+ formatRatio(tmpList.get(0).getValue() * 100, precision)
				+ "%；而在";
		Collections.reverse(tmpList); // 反转，按升序排列
		for (int i = 0; i < tmpList.size(); i++) {
			if (i == tmpList.size() - 1) {
				currentEmployeeSurveySet.drivingLoyaltyEvaluation += tmpList
						.get(i).getKey() + "因素";
			} else if (tmpList.get(i).getValue() != tmpList.get(i + 1)
					.getValue()) {
				currentEmployeeSurveySet.drivingLoyaltyEvaluation += tmpList
						.get(i).getKey() + "因素";
				break;
			} else
				currentEmployeeSurveySet.drivingLoyaltyEvaluation += tmpList
						.get(i).getKey() + "因素、";
		}
		currentEmployeeSurveySet.drivingLoyaltyEvaluation += "上差不多。";

		// 3.4 员工忠诚度分析
		// （4）员工忠诚度驱力因素细分
		Map<String, Double> detailDriverGapMap = new HashMap<String, Double>();
		Map<String, Double> praiseScoreMap = new HashMap<String, Double>();
		Map<String, Double> remainScoreMap = new HashMap<String, Double>();
		List<String> categoryNameList = new ArrayList<String>();
		List<Double> praiseRatioList = new ArrayList<Double>();
		List<Double> remainRatioList = new ArrayList<Double>();
		totalScorePraise = 0;
		totalScoreRemain = 0;

		for (QuizResult result : resultList) {
			Map<String, String> answerMap = (Map<String, String>) Json
					.fromJson(result.getAnswer());
			for (Entry<String, String> answer : answerMap.entrySet()) {
				int qiId = Integer.valueOf(answer.getKey());
				QuizItem item = quizItemService.fetch(qiId);
				if (item.getOptions().getFirst().getCategoryName()
						.indexOf("表现") >= 0
						|| item.getOptions().getFirst().getCategoryName()
								.indexOf("满意度") >= 0)
					continue;
				String _name = item.getOptions().getFirst().getCategoryName();
				if (categoryNameList.contains(_name) == false)
					categoryNameList.add(_name);
				if (praiseUserList.contains(result.getUserId())) {
					double _currentValue = 0;
					if (praiseScoreMap.containsKey(_name))
						_currentValue = praiseScoreMap.get(_name);
					long _value = _getScoreByAnswer(answer.getValue(),
							"employeeSurvey");
					praiseScoreMap.put(_name, _currentValue + _value);
					totalScorePraise += _value;
				}
				if (remainUserList.contains(result.getUserId())) {
					double _currentValue = 0;
					if (remainScoreMap.containsKey(_name))
						_currentValue = remainScoreMap.get(_name);
					long _value = _getScoreByAnswer(answer.getValue(),
							"employeeSurvey");
					remainScoreMap.put(_name, _currentValue + _value);
					totalScoreRemain += _value;
				}
			}
		}
		for (int i = 0; i < categoryNameList.size(); i++) {
			String _name = categoryNameList.get(i);
			praiseRatioList.add(praiseScoreMap.get(_name) * 1.0
					/ totalScorePraise);
			remainRatioList.add(remainScoreMap.get(_name) * 1.0
					/ totalScoreRemain);
			double _value = Math.abs(praiseScoreMap.get(_name) * 1.0
					/ totalScorePraise - remainScoreMap.get(_name) * 1.0
					/ totalScoreRemain);
			detailDriverGapMap.put(_name, _value);
			String s = _name
					+ ","
					+ formatRatio(praiseScoreMap.get(_name) * 1.0
							/ totalScorePraise * 100, precision)
					+ "%,"
					+ formatRatio(remainScoreMap.get(_name) * 1.0
							/ totalScoreRemain * 100, precision) + "%,"
					+ formatRatio(_value * 100, precision) + "%";
			currentEmployeeSurveySet.detailDrivingLoyaltyRatioList.add(s);
		}
		currentEmployeeSurveySet.detailDrivingLoyaltyCategoryNameList
				.addAll(categoryNameList);
		currentEmployeeSurveySet.detailDrivingLoyaltyPraiseRatioList
				.addAll(praiseRatioList);
		currentEmployeeSurveySet.detailDrivingLoyaltyRemainRatioList
				.addAll(remainRatioList);

		// 排序表格显示
		Collections
				.sort(currentEmployeeSurveySet.detailDrivingLoyaltyRatioList);
		// 排序
		tmpList = new ArrayList<Map.Entry<String, Double>>(
				detailDriverGapMap.entrySet());
		Collections.sort(tmpList, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1,
					Map.Entry<String, Double> o2) {
				BigDecimal data1 = new BigDecimal(o1.getValue());
				BigDecimal data2 = new BigDecimal(o2.getValue());
				return data2.compareTo(data1);
			}
		});
		// 排序结束，顺序结果存放在tmpList中

		currentEmployeeSurveySet.detailDrivingLoyaltyEvaluation += tmpList.get(
				0).getKey()
				+ "二级因素上，差异最大，为"
				+ formatRatio(tmpList.get(0).getValue() * 100, precision)
				+ "%。";
		currentEmployeeSurveySet.detailDrivingLoyaltyEvaluation += "其次为"
				+ tmpList.get(1).getKey() + "、" + tmpList.get(2).getKey()
				+ "二级因素。";

		Collections.reverse(tmpList);

		currentEmployeeSurveySet.detailDrivingLoyaltyEvaluation += tmpList.get(
				0).getKey()
				+ "二级因素上，差异最小，为";
		if (tmpList.get(0).getValue() == 0)
			currentEmployeeSurveySet.detailDrivingLoyaltyEvaluation += "两者比重完全一致，";
		else
			currentEmployeeSurveySet.detailDrivingLoyaltyEvaluation += formatRatio(
					tmpList.get(0).getValue() * 100, precision) + "%，";
		currentEmployeeSurveySet.detailDrivingLoyaltyEvaluation += "其次为"
				+ tmpList.get(1).getKey() + "、" + tmpList.get(2).getKey()
				+ "二级因素。";

		// 3.5 满意度、忠诚度综合分析
		Map<String, Double> satisfactionScoreMap = new HashMap<String, Double>();
		Map<String, Double> loyaltyScoreMap = new HashMap<String, Double>();
		categoryNameList = new ArrayList<String>();
		List<Double> satisfactionScoreList = new ArrayList<Double>();
		List<Double> loyaltyScoreList = new ArrayList<Double>();
		double totalScoreSatisfaction = 0;
		double totalScoreLoyalty = 0;
		double averageScoreSatisfaction = 0.0;
		double averageScoreLoyalty = 0.0;

		List<String> categoryNameListQiangshi = new ArrayList<String>();
		List<String> categoryNameListYoushi = new ArrayList<String>();
		List<String> categoryNameListGaijin = new ArrayList<String>();
		List<String> categoryNameListRentong = new ArrayList<String>();

		for (QuizResult result : resultList) {
			Map<String, String> answerMap = (Map<String, String>) Json
					.fromJson(result.getAnswer());
			for (Entry<String, String> answer : answerMap.entrySet()) {
				int qiId = Integer.valueOf(answer.getKey());
				QuizItem item = quizItemService.fetch(qiId);
				if (item.getOptions().getFirst().getCategoryName()
						.indexOf("表现") >= 0
						|| item.getOptions().getFirst().getCategoryName()
								.indexOf("满意度") >= 0)
					continue;
				String _name = item.getOptions().getFirst().getCategoryName();

				if (categoryNameList.contains(_name) == false)
					categoryNameList.add(_name);

				double _currentValue = 0;
				long _value = 0;

				// 设置满意度得分
				_currentValue = 0;
				_value = 0;
				if (satisfactionScoreMap.containsKey(_name))
					_currentValue = satisfactionScoreMap.get(_name);
				_value = _getScoreByAnswer(answer.getValue(), "employeeSurvey");
				satisfactionScoreMap.put(_name, _currentValue + _value);
				totalScoreSatisfaction += _value;

				// 设置忠诚度得分
				_currentValue = 0;
				_value = 0;
				if (loyaltyScoreMap.containsKey(_name))
					_currentValue = loyaltyScoreMap.get(_name);
				_value = _getScoreByAnswer(answer.getValue(), "employeeSurvey");
				loyaltyScoreMap.put(_name, _currentValue + _value);
				totalScoreLoyalty += _value;
			}
		}

		averageScoreSatisfaction = totalScoreSatisfaction * 1.0 * 20
				/ resultList.size() / categoryNameList.size();
		averageScoreLoyalty = 1.0 / 17 / 50 * 27;

		for (int i = 0; i < categoryNameList.size(); i++) {
			String _name = categoryNameList.get(i);
			double _satisfactionValue = satisfactionScoreMap.get(_name) * 1.0
					* 20 / resultList.size();
			satisfactionScoreList.add(_satisfactionValue);
			double _loyaltyValue = loyaltyScoreMap.get(_name) * 1.0
					/ totalScoreLoyalty;
			loyaltyScoreList.add(_loyaltyValue);
			String area = "";
			if (_satisfactionValue >= averageScoreSatisfaction
					&& _loyaltyValue >= averageScoreLoyalty) {
				area = "强势区域";
				categoryNameListQiangshi.add(_name);
			}
			if (_satisfactionValue < averageScoreSatisfaction
					&& _loyaltyValue >= averageScoreLoyalty) {
				area = "改进区域";
				categoryNameListGaijin.add(_name);
			}
			if (_satisfactionValue >= averageScoreSatisfaction
					&& _loyaltyValue < averageScoreLoyalty) {
				area = "优势区域";
				categoryNameListYoushi.add(_name);
			}
			if (_satisfactionValue < averageScoreSatisfaction
					&& _loyaltyValue < averageScoreLoyalty) {
				area = "忍痛区域";
				categoryNameListRentong.add(_name);
			}
			String s = _name + "," + formatRatio(_satisfactionValue, precision)
					+ "," + formatRatio(_loyaltyValue * 100, precision) + "%"
					+ "," + area;
			currentEmployeeSurveySet.compositeAnalysisAreaScoreList.add(s);
		}
		currentEmployeeSurveySet.compositeAnalysisCategoryNameListQiangshi
				.addAll(categoryNameListQiangshi);
		currentEmployeeSurveySet.compositeAnalysisCategoryNameListYoushi
				.addAll(categoryNameListYoushi);
		currentEmployeeSurveySet.compositeAnalysisCategoryNameListGaijin
				.addAll(categoryNameListGaijin);
		currentEmployeeSurveySet.compositeAnalysisCategoryNameListRentong
				.addAll(categoryNameListRentong);
		currentEmployeeSurveySet.compositeAnalysisSatisfactionAverageScore = averageScoreSatisfaction;
		currentEmployeeSurveySet.compositeAnalysisLoyaltyAverageScore = averageScoreLoyalty;

		return;

	}

	/**
	 * 企业员工调查
	 */
	@At
	@Ok("jsp:jsp.report.company.employee_survey")
	public void employeeSurvey(HttpServletRequest request,
			@Param("segmentId") long segmentId, @Param("quizId") long quizId) {
		preProcess(request, segmentId, quizId);
		request.setAttribute("commonParamSet", currentCommonParameterSet);
		employeeSurveyProcess(segmentId, quizId);
		request.setAttribute("employeeParamSet", currentEmployeeSurveySet);
	}

	/**
	 * 情绪管理倾向
	 */
	@At
	@Ok("jsp:jsp.report.company.emotion_management")
	public void emotionManagement(HttpServletRequest request,
			@Param("segmentId") long segementId, @Param("quizId") long quizId) {
		preProcess(request, segementId, quizId);
		request.setAttribute("commonParamSet", currentCommonParameterSet);
	}
}
