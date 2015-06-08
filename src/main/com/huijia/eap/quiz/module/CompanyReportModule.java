package com.huijia.eap.quiz.module;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.huijia.eap.annotation.AuthBy;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.user.service.UserService;
import com.huijia.eap.quiz.data.Company;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizResult;
import com.huijia.eap.quiz.data.Segment;
import com.huijia.eap.quiz.data.TeamReportCommonParamSet;
import com.huijia.eap.quiz.service.CompanyService;
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

	/**
	 * 当前查询操作对应的quizresult结果
	 */
	private List<QuizResult> currentQuizResultList = new LinkedList<QuizResult>();
	/**
	 * 当前查询操作对应的有效quizresult结果
	 */
	private List<QuizResult> currentValidQuizResultList = new LinkedList<QuizResult>();

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
	 * 预处理报告中使用的一些通用数据，例如年龄构成等信息
	 */
	private void preProcess(HttpServletRequest request, long segmentId,
			long quizId) {
		Segment segment = segmentService.fetch(segmentId);
		Company company = companyService.fetch(segment.getCompanyId());
		Quiz quiz = quizService.fetch(quizId);
		request.setAttribute("segment", segment);
		request.setAttribute("company", company);
		request.setAttribute("quiz", quiz);

		currentQuizResultList = quizResultService.getTestedQuizResultList(
				segmentId, quizId);
		currentValidQuizResultList = quizResultService.getValidQuizResultList(
				segmentId, quizId); // 这个貌似没有用
		List<User> registeredUserList = userService.fetchBySegmentId(segmentId);

		for (QuizResult quizResult : currentQuizResultList) {
			for (User user : registeredUserList) {
				if (user.getUserId() == quizResult.getUserId()) {
					currentTestedUserList.add(user);
					if (quizResult.isValid())
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
		double ratio;
		int precision = 2;
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
		currentCommonParameterSet.workageComment = generateWorkageComment();// 此样本中，员工普遍工作经验较少,工作1-3年以上的员工接近七成

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
	private String generateWorkageComment() {
		String result = "";
		// TODO
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

	/**
	 * 个人心理分析团体报告
	 */
	@At
	@Ok("jsp:jsp.report.company.mental_checkup")
	public void mentalCheckup(HttpServletRequest request,
			@Param("segmentId") long segementId, @Param("quizId") long quizId) {
		preProcess(request, segementId, quizId);		
		request.setAttribute("commonParamSet", currentCommonParameterSet);
	}

	/**
	 * 沟通风格与冲突处理
	 */
	@At
	@Ok("jsp:jsp.report.company.communicate_conflict")
	public void communicateConflict(HttpServletRequest request,
			@Param("segmentId") long segementId, @Param("quizId") long quizId) {
		preProcess(request, segementId, quizId);		
		request.setAttribute("commonParamSet", currentCommonParameterSet);
	}

	/**
	 * 企业员工调查
	 */
	@At
	@Ok("jsp:jsp.report.company.employee_survey")
	public void employeeSurvey(HttpServletRequest request,
			@Param("segmentId") long segementId, @Param("quizId") long quizId) {
		preProcess(request, segementId, quizId);
		request.setAttribute("commonParamSet", currentCommonParameterSet);
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
