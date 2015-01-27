package com.huijia.eap.quiz.module;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
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
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.Segment;
import com.huijia.eap.quiz.service.CompanyService;
import com.huijia.eap.quiz.service.QuizService;
import com.huijia.eap.quiz.service.SegmentQuizRelationService;
import com.huijia.eap.quiz.service.SegmentService;

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
	private SegmentQuizRelationService segmentQuizRelationService;

	@At
	@Ok("jsp:jsp.segment.list")
	public Pager<Segment> list(HttpServletRequest request,
			@Param("..") Pager<Segment> pager,
			@Param("companyId") long companyId) {
		return segmentService.paging(Cnd.where("companyId", "=", companyId),
				pager);
	}

	@At
	@Ok("raw")
	public File exportUsers(HttpServletRequest request,
			@Param("..") Segment segment, @Param("id") long id) {
		File file = new File(id + ".txt");
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
			out.write("测试java 文件操作\r\n".getBytes());
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

		List<User> userList = userService.fetchBySegmentId(id);
		request.setAttribute("userList", userList);
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
			if (q.getType() != QuizService.getQuizTypeChild())
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
	@Ok("forward:/company/list")
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
}
