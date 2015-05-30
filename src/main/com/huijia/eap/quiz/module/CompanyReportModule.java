package com.huijia.eap.quiz.module;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.huijia.eap.annotation.AuthBy;
import com.huijia.eap.auth.user.service.UserService;
import com.huijia.eap.quiz.data.Company;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.Segment;
import com.huijia.eap.quiz.service.CompanyService;
import com.huijia.eap.quiz.service.QuizService;
import com.huijia.eap.quiz.service.SegmentService;

@IocBean
@InjectName
@AuthBy(check=false)
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
	
	/**
	 * 个人心理分析团体报告
	 */
	@At
	@Ok("jsp:jsp.report.company.mental_checkup")
	public void mentalCheckup(HttpServletRequest request, @Param("segmentId") long segementId, @Param("quizId") long quizId){
		preProcess(request, segementId, quizId);
	}
	
	/**
	 * 沟通风格与冲突处理
	 */
	@At
	@Ok("jsp:jsp.report.company.communicate_conflict")
	public void communicateConflict(HttpServletRequest request, @Param("segmentId") long segementId, @Param("quizId") long quizId){
		preProcess(request, segementId, quizId);
	}
	
	/**
	 * 企业员工调查
	 */
	@At
	@Ok("jsp:jsp.report.company.employee_survey")
	public void employeeSurvey(HttpServletRequest request, @Param("segmentId") long segementId, @Param("quizId") long quizId){
		preProcess(request, segementId, quizId);
	}
	
	/**
	 * 情绪管理倾向
	 */
	@At
	@Ok("jsp:jsp.report.company.emotion_management")
	public void emotionManagement(HttpServletRequest request, @Param("segmentId") long segementId, @Param("quizId") long quizId){
		preProcess(request, segementId, quizId);
	}
	/**
	 * 预处理报告中使用的一些通用数据，例如年龄构成等信息
	 */
	private void preProcess(HttpServletRequest request, long segementId, long quizId){
		Segment segment = segmentService.fetch(segementId);
		Company company = companyService.fetch(segment.getCompanyId());
		Quiz quiz = quizService.fetch(quizId);
		request.setAttribute("segment", segment);
		request.setAttribute("company", company);
		request.setAttribute("quiz", quiz);
		
		request.setAttribute("usercount", getUserCountInCompany(company.getId()));
		
	}
	
	/**
	 * 按公司获取用户数量
	 * @param companyId
	 * @return
	 */
	private int getUserCountInCompany(long companyId){
		return userService.count(Cnd.where("companyid", "=", companyId));
	}
	
}
