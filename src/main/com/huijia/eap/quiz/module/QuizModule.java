package com.huijia.eap.quiz.module;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.mvc.View;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Chain;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;
import org.nutz.mvc.view.ForwardView;
import org.nutz.mvc.view.JspView;
import org.nutz.mvc.view.ServerRedirectView;
import org.nutz.mvc.view.ViewWrapper;

import com.huijia.eap.GlobalConfig;
import com.huijia.eap.annotation.AuthBy;
import com.huijia.eap.auth.Auths;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.user.service.UserService;
import com.huijia.eap.commons.i18n.Bundle;
import com.huijia.eap.commons.mvc.Pager;
import com.huijia.eap.commons.mvc.view.exhandler.ExceptionWrapper;
import com.huijia.eap.commons.mvc.view.exhandler.ExceptionWrapper.EC;
import com.huijia.eap.quiz.cache.QuizCache;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizAnswerLog;
import com.huijia.eap.quiz.data.QuizConstant;
import com.huijia.eap.quiz.data.QuizEvaluation;
import com.huijia.eap.quiz.data.QuizItem;
import com.huijia.eap.quiz.data.QuizResult;
import com.huijia.eap.quiz.data.Segment;
import com.huijia.eap.quiz.service.QuizAnswerLogService;
import com.huijia.eap.quiz.service.QuizEvaluationService;
import com.huijia.eap.quiz.service.QuizItemService;
import com.huijia.eap.quiz.service.QuizResultService;
import com.huijia.eap.quiz.service.QuizService;
import com.huijia.eap.quiz.service.SegmentQuizRelationService;
import com.huijia.eap.quiz.service.SegmentService;
import com.huijia.eap.quiz.service.handler.QuizImportHandler;

@IocBean
@InjectName
@AuthBy(check = false)
@At("/quiz")
public class QuizModule {
	private final static String OPERATION_ADD = "add";
	private final static String OPERATION_EDIT = "edit";
	private final static String OPERATION_ADD_CHILD = "addSubquiz";
	private final static String OPERATION_READ = "read";

	@Inject
	private QuizService quizService;

	@Inject
	private QuizItemService quizItemService;

	@Inject
	private QuizEvaluationService quizEvaluationService;

	@Inject
	private QuizResultService quizResultService;

	@Inject
	private SegmentQuizRelationService segmentQuizRelationService;

	@Inject
	private UserService userService;
	
	@Inject
	private SegmentService segmentService;
	
	@Inject
	private QuizAnswerLogService quizAnswerLogService;

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

		return quizService.paging(Cnd.wrap("type <> 2"), pager);
	}

	/**
	 * 答题者看到的试卷列表
	 * 
	 * @param request
	 */
	@At
	@Ok("jsp:jsp.quiz.test.quizlist")
	public void enquizlist(HttpServletRequest request) {
		// List<Quiz> list = quizService.fetchDisplayQuizs();
		// request.setAttribute("quizlist", list);

		// User currentUser = Auths.getUser(request);
		User currentUser = userService.fetchByName(Auths.getUser(request)
				.getName());
		currentUser.setPassword(User.PASSWORD_FADE);
		request.setAttribute("user", currentUser);
		request.setAttribute("current_user", currentUser);
		

		//按照用户拥有的问卷权限显示问卷列表
		List<Quiz> list = quizService.fetchQuizListBySegmentId(currentUser
				.getSegmentId());
		request.setAttribute("quizlist", list);
		
		// 答题历史记录
		Map<Long, List<QuizAnswerLog>> quizHistoryMap = new HashMap<Long, List<QuizAnswerLog>>();
		Iterator<Quiz> iter = list.iterator();
		while(iter.hasNext()){
			Quiz _quiz = iter.next();
			List<QuizAnswerLog> history = quizAnswerLogService.getHistory(currentUser.getUserId(), _quiz.getId());
			quizHistoryMap.put(_quiz.getId(), history);
		}
		request.setAttribute("quizHistoryMap", quizHistoryMap);
		
		//加载用户所在的号段
		Segment segment = segmentService.fetch(currentUser.getSegmentId());
		request.setAttribute("segment", segment);
	}

	/**
	 * 添加、编辑试卷的入口
	 * 
	 * @param request
	 * @param id
	 * @param operation
	 */
	@At
	// @Ok("jsp:jsp.quiz.edit")
	public View prepare(HttpServletRequest request, @Param("id") long id,
			@Param("parentId") long parentId,
			@Param("operation") String operation) {

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

		Quiz quiz = new Quiz();

		if (OPERATION_ADD_CHILD.equals(operation)) {
			quiz.setParentId(parentId);
			request.setAttribute("quiz", quiz);
			return new ViewWrapper(new JspView("jsp.quiz.addSubquiz"), null);

		}

		if (OPERATION_EDIT.equals(operation)) {
			quiz = quizService.fetch(id);
			quiz.setChildList(quizService.fetchListByParentId(id));
		}
		request.setAttribute("quiz", quiz);
		return new ViewWrapper(new JspView("jsp.quiz.edit"), null);

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
	@Fail("forward:/quiz/list")
	@AdaptBy(type = UploadAdaptor.class)
	@Chain("validate")
	public View add(HttpServletRequest request, @Param("..") Quiz quiz,
			@Param("quiz_file") TempFile tempFile) {
		if (quiz.getType() == QuizConstant.QUIZ_TYPE_PARENT) {
			quiz = quizService.insert(quiz);
			request.setAttribute("quiz", quiz);
			return new ViewWrapper(new ForwardView("/quiz/list"), null);
		} else if (tempFile == null) {
			EC error = new EC("quiz.add.import.empty.error", bundle);
			throw ExceptionWrapper.wrapError(error);
		} else {

			File quizFile = tempFile.getFile();
			String path = quizFile.getAbsolutePath();

			QuizImportHandler quizImportHandler = new QuizImportHandler();
			if (quizImportHandler.process(path) == 0) {
				quiz.setItemNum(quizImportHandler.getItemNum());
				quiz.setLieBorder(quizImportHandler.getLieBorder());
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
					quizItem.setQuizId(quiz.getId());
					quizItemService.insert(quizItem);
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
		return new ViewWrapper(new JspView("jsp.quiz.viewquiz"), null);

	}

	/**
	 * 添加试卷后，跳转到的试卷查看页面
	 * 
	 * @param request
	 * @param id
	 */
	@At
	// @Ok("jsp:jsp.quiz.viewquiz")
	public View viewquiz(HttpServletRequest request, @Param("id") long id) {
		Quiz quiz = quizService.fetch(id);
		request.setAttribute("quiz", quiz);

		LinkedList<QuizItem> quizItems = (LinkedList<QuizItem>) quizItemService
				.fetchListByQuizId(id);
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

		return new ViewWrapper(new JspView("jsp.quiz.viewquiz"), null);
	}

	/**
	 * 编辑试卷
	 * 
	 * @param quiz
	 */
	@At
	// @Ok("jsp:jsp.quiz.viewquiz")
	@Fail("forward:/quiz/list")
	@AdaptBy(type = UploadAdaptor.class)
	@Chain("validate")
	public View edit(HttpServletRequest request, @Param("..") Quiz quiz,
			@Param("quiz_file") TempFile tempFile) {

		if (tempFile != null) {

			File quizFile = tempFile.getFile();
			String path = quizFile.getAbsolutePath();

			QuizImportHandler quizImportHandler = new QuizImportHandler();
			if (quizImportHandler.process(path) == 0) {

				// 清空item表、evaluation
				long quizId = quiz.getId();

				// Table: quiz_item
				quizItemService.deleteByQuizId(quizId);

				// Table: quiz_evaluation
				quizEvaluationService.deleteByQuizId(quizId);

				quiz.setItemNum(quizImportHandler.getItemNum());
				quiz.setLieBorder(quizImportHandler.getLieBorder());
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
					quizItem.setQuizId(quizId);
					quizItemService.insert(quizItem);

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
			quiz.setCategoryJson(quizService.fetch(quiz.getId())
					.getCategoryJson());
			quiz.setCategoryNum(quizService.fetch(quiz.getId())
					.getCategoryNum());
			quiz.setItemNum(quizService.fetch(quiz.getId()).getItemNum());
			quiz.setLieBorder(quizService.fetch(quiz.getId()).getLieBorder());
			quiz.setChildList(quizService.fetchListByParentId(quiz.getId()));

			quizService.update(quiz);
			request.setAttribute("quiz", quiz);

			LinkedList<QuizItem> quizItems = (LinkedList<QuizItem>) quizItemService
					.fetchListByQuizId(quiz.getId());
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

			request.setAttribute("quizItems", quizItems);
			request.setAttribute("quizEvaluationsSingle", quizEvaluationsSingle);
			request.setAttribute("quizEvaluationsTeam", quizEvaluationsTeam);
		}

		// 更新缓存
		QuizCache.me().update(quiz);
		
		if (quiz.getType() == QuizConstant.QUIZ_TYPE_PARENT) {
			return new ViewWrapper(new ForwardView("/quiz/list"), null);
		}
		return new ViewWrapper(new JspView("jsp.quiz.viewquiz"), null);

	}

	/**
	 * 删除试卷
	 * 
	 * @param id
	 */
	@At
	// @Ok("forward:/quiz/list")
	public View delete(@Param("id") long id) {

		Quiz quiz = quizService.fetch(id);
		if (quiz == null)
			return new ViewWrapper(new ForwardView("/quiz/list"), null);

		quizService.deleteByQuizId(id);
		
		// 更新缓存
		QuizCache.me().delete(id);

		if (quiz.getType() == QuizConstant.QUIZ_TYPE_CHILD) {
			String s = "/quiz/prepare?operation=edit&id=" + quiz.getParentId();
			return new ViewWrapper(new ServerRedirectView(s), null);
		}
		return new ViewWrapper(new ForwardView("/quiz/list"), null);
	}

	/**
	 * 添加子试卷
	 * 
	 * @param request
	 * @param quiz
	 * @param tempFile
	 */
	@At
	@Ok("jsp:jsp.quiz.viewquiz")
	@Fail("forward:/quiz/list")
	@AdaptBy(type = UploadAdaptor.class)
	@Chain("validate")
	public void addSubquiz(HttpServletRequest request, @Param("..") Quiz quiz,
			@Param("quiz_file") TempFile tempFile,
			@Param("parentId") long parentId) {
		request.setAttribute("operation", "addSubquiz");
		if (tempFile == null) {
			EC error = new EC("quiz.add.import.empty.error", bundle);
			throw ExceptionWrapper.wrapError(error);
		} else {

			File quizFile = tempFile.getFile();
			String path = quizFile.getAbsolutePath();

			QuizImportHandler quizImportHandler = new QuizImportHandler();
			if (quizImportHandler.process(path) == 0) {
				quiz.setItemNum(quizImportHandler.getItemNum());
				quiz.setLieBorder(quizImportHandler.getLieBorder());
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
					quizItem.setQuizId(quiz.getId());
					quizItemService.insert(quizItem);
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
	 * 答题测验页面
	 * 
	 * @param request
	 * @param id
	 */
	@At
	@Ok("jsp:jsp.quiz.test.test")
	public void test(HttpServletRequest request, @Param("quizId") long quizId) {
		Quiz quiz = QuizCache.me().getQuiz(quizId);
		request.setAttribute("quiz", quiz);

		List<Quiz> quizList = new LinkedList<Quiz>();
		if (quiz.getType() == QuizConstant.QUIZ_TYPE_STANDALONE) {
			quizList.add(quiz);
		} else if (quiz.getType() == QuizConstant.QUIZ_TYPE_PARENT) {
			quizList.addAll(quiz.getChildList());
		}
		request.setAttribute("quizlist", quizList);
	}

	/**
	 * 提交答案
	 * 
	 * @param request
	 */
	@At
	@Ok("jsp:jsp.quiz.test.report")
	@Fail("forward:/quiz/test")
	public void answer(HttpServletRequest request,
			@Param("quizId") long quizId, @Param("answerJson") String answerJson) {
		Quiz quiz = QuizCache.me().getQuiz(quizId);

		User user = Auths.getUser(request);

		// 计算并存储答题结果
		Map<String, String> _answer = (Map<String, String>) Json
				.fromJson(answerJson);
		Map<Long, String> answerMap = new HashMap<Long, String>();
		for (String key : _answer.keySet()) {
			long questionId = Long.parseLong(key);
			answerMap.put(questionId, _answer.get(key));
		}
		
		// 当前时间
		long currentTime = System.currentTimeMillis();
		
		List<QuizResult> resultList = quizResultService.storeResult(user.getUserId(), quiz, answerMap, currentTime);
		
		// 检测结果有效性
		boolean isValid = true;
		for(QuizResult result : resultList){
			if(result.isValid() == false){
				isValid = false;
				break;
			}
		}
		if(!isValid){
			// 无效作答，重新答题
			// 回滚数据
			quizResultService.deleteByX(user.getUserId(), quizId, currentTime);
			
			// 向页面返回错误信息
			EC error = new EC("quiz.test.answer.invalid", bundle);
			throw ExceptionWrapper.wrapError(error);
		}
		
		QuizAnswerLog history = new QuizAnswerLog();
		history.setQuizId(quizId);
		history.setUserId(user.getUserId());
		history.setCompanyId(user.getCompanyId());
		history.setTimestamp(currentTime);
		quizAnswerLogService.insert(history);
		
		request.setAttribute("resultlist", resultList);
		
		List<Quiz> quizList = new ArrayList<Quiz>();
		if(quiz.getType() == QuizConstant.QUIZ_TYPE_STANDALONE){
			quizList.add(quiz);
		}else if(quiz.getType() == QuizConstant.QUIZ_TYPE_PARENT){
			quizList.addAll(quiz.getChildList());
		}
		request.setAttribute("quizlist", quizList);
		
		request.setAttribute("quiz", quiz);
	}

	/**
	 * 报表页面
	 */
	@At
	@Ok("jsp:jsp.quiz.test.report")
	public void report(HttpServletRequest request, @Param("quizId") long quizId) {
		Quiz quiz = QuizCache.me().getQuiz(quizId);

		User user = Auths.getUser(request);

		List<QuizResult> resultList = quizResultService.getQuizResult(
				user.getUserId(), quizId);
		request.setAttribute("resultlist", resultList);

		List<Quiz> quizList = new ArrayList<Quiz>();
		if (quiz.getType() == QuizConstant.QUIZ_TYPE_STANDALONE) {
			quizList.add(quiz);
		} else if (quiz.getType() == QuizConstant.QUIZ_TYPE_PARENT) {
			quizList.addAll(quiz.getChildList());
		}
		request.setAttribute("quizlist", quizList);

		request.setAttribute("quiz", quiz);
	}

}
