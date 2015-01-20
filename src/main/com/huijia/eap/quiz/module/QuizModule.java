package com.huijia.eap.quiz.module;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Chain;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import com.huijia.eap.GlobalConfig;
import com.huijia.eap.annotation.AuthBy;
import com.huijia.eap.auth.Auths;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.commons.i18n.Bundle;
import com.huijia.eap.commons.mvc.Pager;
import com.huijia.eap.commons.mvc.view.exhandler.ExceptionWrapper;
import com.huijia.eap.commons.mvc.view.exhandler.ExceptionWrapper.EC;
import com.huijia.eap.quiz.data.Company;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizEvaluation;
import com.huijia.eap.quiz.data.QuizItem;
import com.huijia.eap.quiz.data.QuizItemRelation;
import com.huijia.eap.quiz.service.QuizEvaluationService;
import com.huijia.eap.quiz.service.QuizItemRelationService;
import com.huijia.eap.quiz.service.QuizItemService;
import com.huijia.eap.quiz.service.QuizService;
import com.huijia.eap.quiz.service.handler.QuizImportHandler;

@IocBean
@InjectName
@AuthBy(check = false)
@At("/quiz")
public class QuizModule {
	private final static String OPERATION_ADD = "add";
	private final static String OPERATION_EDIT = "edit";
	private final static String OPERATION_READ = "read";

	@Inject
	private QuizService quizService;

	@Inject
	private QuizItemService quizItemService;

	@Inject
	private QuizItemRelationService quizItemRelationService;

	@Inject
	private QuizEvaluationService quizEvaluationService;

	Bundle bundle = new Bundle("quiz");

	/**
	 * 分页查看试卷列表
	 * 
	 * @param request
	 * @param pager
	 * @return
	 */
	@At
	@Ok("jsp:jsp.quiz.list")
	public Pager<Quiz> list(HttpServletRequest request,
			@Param("..") Pager<Quiz> pager) {
		return quizService.paging(null, pager);
	}

	/**
	 * 答题者看到的试卷列表
	 * 
	 * @param request
	 */
	@At
	@Ok("jsp:jsp.quiz.test.quizlist")
	public void showtest(HttpServletRequest request) {
		List<Quiz> list = quizService.fetchAll();
		request.setAttribute("quizlist", list);

		User currentUser = Auths.getUser(request);
		request.setAttribute("user", currentUser);
	}

	/**
	 * 添加、编辑试卷的入口
	 * 
	 * @param request
	 * @param id
	 * @param operation
	 */
	@At
	@Ok("jsp:jsp.quiz.edit")
	public void prepare(HttpServletRequest request, @Param("id") long id,
			@Param("operation") String operation) {
		Quiz quiz = new Quiz();
		if (OPERATION_EDIT.equals(operation)) {
			quiz = quizService.fetch(id);
		}
		request.setAttribute("quiz", quiz);

		// 从images/quiz/icons/目录加载可用的试卷图标
		String iconpath = GlobalConfig.getContextValue("web.dir")
				+ File.separator + "images" + File.separator + "quiz"
				+ File.separator + "icons";
		File[] iconFiles = Files.files(new File(iconpath), null);
		List<String> iconNames = new ArrayList<String>();
		for (File f : iconFiles) {
			iconNames.add(f.getName());
		}
		request.setAttribute("iconNames", iconNames);
	}

	/**
	 * 添加试卷
	 * 
	 * @param request
	 * @param quiz
	 * @param tempFile
	 */
	@At
	@Ok("jsp:jsp.quiz.viewquiz")
	@Fail("forward:/quiz/prepare")
	@AdaptBy(type = UploadAdaptor.class)
	@Chain("validate")
	public void add(HttpServletRequest request, @Param("..") Quiz quiz,
			@Param("quiz_file") TempFile tempFile) {
		if (tempFile == null) {
			EC error = new EC("quiz.add.import.empty.error", bundle);
			throw ExceptionWrapper.wrapError(error);
		} else {

			File quizFile = tempFile.getFile();
			String path = quizFile.getAbsolutePath();

			QuizImportHandler quizImportHandler = new QuizImportHandler();
			if (quizImportHandler.process(path) == 0) {

				quiz.setCategoryJson(quizImportHandler.getCategoryJson());
				quiz.setCategoryNum(quizImportHandler.getCategoryNum());
				quiz = quizService.insert(quiz);
				request.setAttribute("quiz", quiz);

				LinkedList<QuizItem> quizItems = quizImportHandler
						.getQuizItems();
				LinkedList<QuizEvaluation> quizEvaluations = quizImportHandler
						.getQuizEvaluations();

				request.setAttribute("quizItems", quizItems);
				request.setAttribute("quizEvaluationsSingle",
						quizImportHandler.getQuizEvaluationsSingle());
				request.setAttribute("quizEvaluationsTeam",
						quizImportHandler.getQuizEvaluationsTeam());

				for (Iterator<QuizItem> it = quizItems.iterator(); it.hasNext();) {
					QuizItem quizItem = it.next();
					quizItemService.insert(quizItem);
					QuizItemRelation quizItemRelation = new QuizItemRelation();
					quizItemRelation.setQuizId(quiz.getId());
					quizItemRelation.setQuizItemId(quizItem.getId());
					quizItemRelationService.insert(quizItemRelation);
				}
				for (Iterator<QuizEvaluation> it = quizEvaluations.iterator(); it
						.hasNext();) {
					QuizEvaluation quizEvaluation = it.next();
					quizEvaluationService.insert(quizEvaluation, quiz.getId());
				}

			} else {
				// 向页面返回错误信息
				EC error = new EC("quiz.add.import.invalid.error", bundle);
				throw ExceptionWrapper.wrapError(error);
			}

		}

	}

	/**
	 * 添加试卷后，跳转到的试卷查看页面
	 * 
	 * @param request
	 * @param id
	 */
	@At
	@Ok("jsp:jsp.quiz.viewquiz")
	public void viewquiz(HttpServletRequest request, @Param("id") long id) {
		Quiz quiz = quizService.fetch(id);
		request.setAttribute("quiz", quiz);
		
		List<QuizItemRelation> quizItemRelationList = quizItemRelationService
				.fetchListByQuizId(quiz.getId());
		LinkedList<QuizItem> quizItems = new LinkedList<QuizItem>();
		for (Iterator<QuizItemRelation> it = quizItemRelationList
				.iterator(); it.hasNext();) {
			QuizItemRelation quizItemRelation = it.next();
			QuizItem quizItem = quizItemService.fetch(quizItemRelation
					.getQuizItemId());
			quizItems.add(quizItem);
		}
		LinkedList<QuizEvaluation> quizEvaluationsSingle = new LinkedList<QuizEvaluation>();
		LinkedList<QuizEvaluation> quizEvaluationsTeam = new LinkedList<QuizEvaluation>();

		for (Iterator<QuizEvaluation> it = this.quizEvaluationService
				.fetchListByQuizId(quiz.getId()).iterator(); it.hasNext();) {
			QuizEvaluation quizEvaluation = it.next();
			if (quizEvaluation.getType().equals("single"))
				quizEvaluationsSingle.add(quizEvaluation);
			if (quizEvaluation.getType().equals("team"))
				quizEvaluationsTeam.add(quizEvaluation);
		}
		// this.quizEvaluationService.

		request.setAttribute("quizItems", quizItems);
		request.setAttribute("quizEvaluationsSingle", quizEvaluationsSingle);
		request.setAttribute("quizEvaluationsTeam", quizEvaluationsTeam);
	}

	/**
	 * 编辑试卷
	 * 
	 * @param quiz
	 */
	@At
	@Ok("jsp:jsp.quiz.viewquiz")
	@Fail("forward:/quiz/prepare")
	@AdaptBy(type = UploadAdaptor.class)
	@Chain("validate")
	public void edit(HttpServletRequest request, @Param("..") Quiz quiz,
			@Param("quiz_file") TempFile tempFile) {

		quiz.setCategoryJson(quizService.fetch(quiz.getId()).getCategoryJson());
		quiz.setCategoryNum(quizService.fetch(quiz.getId()).getCategoryNum());

		if (tempFile != null) {

			File quizFile = tempFile.getFile();
			String path = quizFile.getAbsolutePath();

			QuizImportHandler quizImportHandler = new QuizImportHandler();
			if (quizImportHandler.process(path) == 0) {

				// 清空item表、evaluation、关联表
				long quizId = quiz.getId();
				// Table: quiz_item
				List<QuizItemRelation> quizItemRelationList = quizItemRelationService
						.fetchListByQuizId(quizId);
				for (Iterator<QuizItemRelation> it = quizItemRelationList
						.iterator(); it.hasNext();) {
					QuizItemRelation quizItemRelation = it.next();
					quizItemService.delete(quizItemRelation.getQuizItemId());
				}
				// Table: quiz_evaluation
				quizEvaluationService.deleteByQuizId(quizId);

				// Table: quiz_item_relation
				quizItemRelationService.deleteByQuizId(quizId);

				quiz.setCategoryJson(quizImportHandler.getCategoryJson());
				quiz.setCategoryNum(quizImportHandler.getCategoryNum());

				quizService.update(quiz);
				request.setAttribute("quiz", quiz);

				LinkedList<QuizItem> quizItems = quizImportHandler
						.getQuizItems();
				LinkedList<QuizEvaluation> quizEvaluations = quizImportHandler
						.getQuizEvaluations();

				request.setAttribute("quizItems", quizItems);
				request.setAttribute("quizEvaluationsSingle",
						quizImportHandler.getQuizEvaluationsSingle());
				request.setAttribute("quizEvaluationsTeam",
						quizImportHandler.getQuizEvaluationsTeam());

				for (Iterator<QuizItem> it = quizItems.iterator(); it.hasNext();) {
					QuizItem quizItem = it.next();
					quizItemService.insert(quizItem);
					QuizItemRelation quizItemRelation = new QuizItemRelation();
					quizItemRelation.setQuizId(quiz.getId());
					quizItemRelation.setQuizItemId(quizItem.getId());
					quizItemRelationService.insert(quizItemRelation);
				}
				for (Iterator<QuizEvaluation> it = quizEvaluations.iterator(); it
						.hasNext();) {
					QuizEvaluation quizEvaluation = it.next();
					quizEvaluationService.insert(quizEvaluation, quiz.getId());
				}

			} else {
				// 向页面返回错误信息
				EC error = new EC("quiz.add.import.invalid.error", bundle);
				throw ExceptionWrapper.wrapError(error);
			}

		} else {
			request.setAttribute("quiz", quiz);
			List<QuizItemRelation> quizItemRelationList = quizItemRelationService
					.fetchListByQuizId(quiz.getId());
			LinkedList<QuizItem> quizItems = new LinkedList<QuizItem>();
			for (Iterator<QuizItemRelation> it = quizItemRelationList
					.iterator(); it.hasNext();) {
				QuizItemRelation quizItemRelation = it.next();
				QuizItem quizItem = quizItemService.fetch(quizItemRelation
						.getQuizItemId());
				quizItems.add(quizItem);
			}
			LinkedList<QuizEvaluation> quizEvaluationsSingle = new LinkedList<QuizEvaluation>();
			LinkedList<QuizEvaluation> quizEvaluationsTeam = new LinkedList<QuizEvaluation>();

			for (Iterator<QuizEvaluation> it = this.quizEvaluationService
					.fetchListByQuizId(quiz.getId()).iterator(); it.hasNext();) {
				QuizEvaluation quizEvaluation = it.next();
				if (quizEvaluation.getType().equals("single"))
					quizEvaluationsSingle.add(quizEvaluation);
				if (quizEvaluation.getType().equals("team"))
					quizEvaluationsTeam.add(quizEvaluation);
			}
			// this.quizEvaluationService.

			request.setAttribute("quizItems", quizItems);
			request.setAttribute("quizEvaluationsSingle", quizEvaluationsSingle);
			request.setAttribute("quizEvaluationsTeam", quizEvaluationsTeam);
		}

	}

	/**
	 * 删除试卷
	 * 
	 * @param id
	 */
	@At
	@Ok("forward:/quiz/list")
	public void delete(@Param("id") long id) {

		List<QuizItemRelation> quizItemRelationList = quizItemRelationService
				.fetchListByQuizId(id);

		// Table: quiz
		quizService.delete(id);

		// Table: quiz_item_relation
		quizItemRelationService.deleteByQuizId(id);

		// Table: quiz_item
		for (Iterator<QuizItemRelation> it = quizItemRelationList.iterator(); it
				.hasNext();) {
			QuizItemRelation quizItemRelation = it.next();
			quizItemService.delete(quizItemRelation.getQuizItemId());
		}

		// Table: quiz_evaluation
		quizEvaluationService.deleteByQuizId(id);
	}

	/**
	 * 答题测验页面
	 * 
	 * @param request
	 * @param id
	 */
	@At
	@Ok("jsp:jsp.quiz.test.test")
	public void test(HttpServletRequest request, @Param("id") long id) {
		Quiz quiz = quizService.genSampleQuiz();
		request.setAttribute("quiz", quiz);
	}

	/**
	 * 提交答案
	 * 
	 * @param request
	 */
	@At
	@Ok("jsp:jsp.quiz.test.report")
	public void answer(HttpServletRequest request) {

	}

	/**
	 * 报表页面
	 */
	@At
	@Ok("jsp:jsp.quiz.test.report")
	public void report() {

	}

}
