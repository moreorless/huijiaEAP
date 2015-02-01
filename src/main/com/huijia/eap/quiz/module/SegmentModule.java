package com.huijia.eap.quiz.module;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Chain;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.huijia.eap.annotation.AuthBy;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.user.service.UserService;
import com.huijia.eap.commons.mvc.Pager;
import com.huijia.eap.quiz.data.Company;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizConstant;
import com.huijia.eap.quiz.data.Segment;
import com.huijia.eap.quiz.data.UserTemp;
import com.huijia.eap.quiz.service.CompanyService;
import com.huijia.eap.quiz.service.QuizService;
import com.huijia.eap.quiz.service.SegmentQuizRelationService;
import com.huijia.eap.quiz.service.SegmentService;
import com.huijia.eap.quiz.service.UserTempService;

@IocBean
@InjectName
@AuthBy(check = false)
@At("/segment")
public class SegmentModule {

	private final static String OPERATION_ADD = "add";
	private final static String OPERATION_EDIT = "edit";

	private final static short SEGMENT_VALID = 1;
	private final static short SEGMENT_INVALID = 0;

	@Inject
	private SegmentService segmentService;

	@Inject
	private CompanyService companyService;

	@Inject
	private QuizService quizService;

	@Inject
	private UserService userService;

	@Inject
	private UserTempService userTempService;

	@Inject
	private SegmentQuizRelationService segmentQuizRelationService;

	@At
	@Ok("jsp:jsp.segment.list")
	public Pager<Segment> list(HttpServletRequest request,
			@Param("..") Pager<Segment> pager,
			@Param("companyId") long companyId) {
		Company company = companyService.fetch(companyId);
		request.setAttribute("company", company);
		return segmentService.paging(Cnd.where("companyId", "=", companyId),
				pager);
	}

	@At
	@Ok("raw")
	public File exportUsers(HttpServletRequest request, @Param("id") long id) {
		Segment segment = segmentService.fetch(id);
		File file = new File(companyService.fetch(segment.getCompanyId())
				.getName() + id + ".segment.txt");
		if (file.exists())
			file.delete();
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			List<UserTemp> list = userTempService.fetchListBySegmentId(id);
			out.write(("企业名称: "
					+ companyService.fetch(segment.getCompanyId()).getName() + "\r\n")
					.getBytes());
			out.write("号段编码\t\t\t初始密码\r\n".getBytes());
			for (UserTemp userTemp : list) {
				out.write((userTemp.getCode() + "\t\t\t"
						+ segment.getInitPassword() + "\r\n").getBytes());
			}
			out.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return file;
	}

	@At
	@Ok("jsp:jsp.segment.listuser")
	public void listUsers(HttpServletRequest request, @Param("id") long id) {

		Segment segment = segmentService.fetch(id);
		Company company = companyService.fetch(segment.getCompanyId());
		List<User> userList = userService.fetchBySegmentId(id);
		request.setAttribute("userList", userList);
		request.setAttribute("segment", segment);
		request.setAttribute("company", company);
	}

	@At
	@Ok("jsp:jsp.segment.edit")
	public void prepare(HttpServletRequest request, @Param("id") long id,
			@Param("companyId") long companyId,
			@Param("operation") String operation) {
		Segment segment = new Segment();
		List<Quiz> all = quizService.fetchAll();
		LinkedList<Quiz> list = new LinkedList<Quiz>();
		for (Quiz q : all) {
			if (q.getType() != QuizConstant.QUIZ_TYPE_CHILD)
				list.add(q);
		}
		request.setAttribute("allQuizList", list);
		if (OPERATION_EDIT.equals(operation)) {
			segment = segmentService.fetchSegmentById(id);
		}
		segment.setCompanyId(companyId);
		request.setAttribute("segment", segment);
	}

	@At
	@Ok("forward:/segment/list")
	@Chain("validate")
	public void edit(HttpServletRequest request, @Param("..") Segment segment) {
		segmentService.update(segment);
	}

	@At
	@Ok("forward:/segment/list")
	@Chain("validate")
	public void add(HttpServletRequest request, @Param("..") Segment segment) {

		// 组合问卷1,个人性格分析
		segmentService.insert(segment);
	}

	@At
	@Ok("forward:/segment/list")
	public void delete(HttpServletRequest request, @Param("id") long id,
			@Param("companyId") long companyId) {

		// 组合问卷1,个人性格分析
		segmentService.deleteBySegmentId(id);
	}
}
