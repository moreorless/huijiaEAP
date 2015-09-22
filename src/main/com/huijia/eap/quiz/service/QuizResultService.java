package com.huijia.eap.quiz.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;

import com.huijia.eap.auth.bean.User;
import com.huijia.eap.auth.user.service.UserService;
import com.huijia.eap.commons.service.TblIdsEntityService;
import com.huijia.eap.quiz.cache.QuizCache;
import com.huijia.eap.quiz.dao.QuizResultDao;
import com.huijia.eap.quiz.data.Quiz;
import com.huijia.eap.quiz.data.QuizCategory;
import com.huijia.eap.quiz.data.QuizConstant;
import com.huijia.eap.quiz.data.QuizItem;
import com.huijia.eap.quiz.data.QuizItemOption;
import com.huijia.eap.quiz.data.QuizResult;

@IocBean
public class QuizResultService extends TblIdsEntityService<QuizResult> {

	@Inject
	private UserService userService;

	@Inject
	private QuizCategoryService quizCategoryService;

	@Inject
	private QuizService quizService;

	@Inject("refer:quizResultDao")
	public void setQuizResultDao(Dao dao) {
		setDao(dao);
	}

	public QuizResult insert(QuizResult quizResult) {
		quizResult.setId(getTblMaxIdWithUpdate());
		return this.dao().insert(quizResult);
	}

	/**
	 * 根据试卷id删除
	 * 
	 * @param quizId
	 * @return
	 */
	public int deleteByQuizId(long quizId) {
		Quiz quiz = QuizCache.me().getQuiz(quizId);

		if (quiz != null) {
			if (quiz.getType() == QuizConstant.QUIZ_TYPE_PARENT) {
				for (Quiz _quiz : quiz.getChildList()) {
					this.deleteByQuizId(_quiz.getId());
				}
			}
		}

		return this.dao().clear(getEntityClass(),
				Cnd.where("quizId", "=", quizId));
	}

	/**
	 * 根据用户id, 试卷id删除
	 * 
	 * @param userId
	 * @param quizId
	 * @return
	 */
	public int deleteByX(long userId, long quizId) {
		return this.dao().clear(getEntityClass(),
				Cnd.where("quizId", "=", quizId).and("userId", "=", userId));
	}

	/**
	 * 根据用户id, 试卷id, 答题时间删除
	 * 
	 * @param userId
	 * @param quizId
	 * @param timestamp
	 * @return
	 */
	public int deleteByX(long userId, long quizId, long timestamp) {
		Quiz quiz = QuizCache.me().getQuiz(quizId);

		if (quiz.getType() == QuizConstant.QUIZ_TYPE_PARENT) {
			for (Quiz _quiz : quiz.getChildList()) {
				this.deleteByX(userId, _quiz.getId(), timestamp);
			}
		}

		return this.dao().clear(
				getEntityClass(),
				Cnd.where("quizId", "=", quizId).and("userId", "=", userId)
						.and("timestamp", "=", timestamp));
	}

	public List<QuizResult> getTestedQuizResultList(long segmentId, long quizId) {
		List<QuizResult> resultList = new LinkedList<QuizResult>();
		Quiz quiz = QuizCache.me().getQuiz(quizId);

		if (quiz.getType() == QuizConstant.QUIZ_TYPE_PARENT) {
			for (Quiz _quiz : quiz.getChildList()) {
				List<QuizResult> tmpList = this.dao().query(
						getEntityClass(),
						Cnd.where("quizId", "=", _quiz.getId()).and(
								"segmentId", "=", segmentId));
				resultList.addAll(tmpList);
			}
			return resultList;
		}

		// 如果同一用户 对同一问卷，回答了多次，取最近一次
		List<QuizResult> tmpList = this.dao().query(
				getEntityClass(),
				Cnd.where("quizId", "=", quizId).and("segmentId", "=",
						segmentId));
		resultList.addAll(tmpList);
		return resultList;
	}

	public List<QuizResult> getValidQuizResultList(long segmentId, long quizId) {
		List<QuizResult> resultList = new LinkedList<QuizResult>();
		Quiz quiz = QuizCache.me().getQuiz(quizId);

		if (quiz.getType() == QuizConstant.QUIZ_TYPE_PARENT) {
			for (Quiz _quiz : quiz.getChildList()) {
				List<QuizResult> tmpList = this.dao().query(
						getEntityClass(),
						Cnd.where("quizId", "=", _quiz.getId())
								.and("segmentId", "=", segmentId)
								.and("valid", "=", 1));
				resultList.addAll(tmpList);
			}
			return resultList;
		}

		// 如果同一用户 对同一问卷，回答了多次，取最近一次
		List<QuizResult> tmpList = this.dao().query(
				getEntityClass(),
				Cnd.where("quizId", "=", quizId)
						.and("segmentId", "=", segmentId).and("valid", "=", 1));
		resultList.addAll(tmpList);
		return resultList;
	}

	/**
	 * 获取答题结果，只获取最近一次记录 如果是复合问卷，返回每个自问卷的结果集合
	 * 
	 * @param userId
	 * @param quizId
	 * @param timestamp
	 * @return
	 */
	public List<QuizResult> getQuizResult(long userId, long quizId) {
		List<QuizResult> resultList = new LinkedList<QuizResult>();
		Quiz quiz = QuizCache.me().getQuiz(quizId);

		if (quiz.getType() == QuizConstant.QUIZ_TYPE_PARENT) {
			for (Quiz _quiz : quiz.getChildList()) {
				List<QuizResult> _rList = getQuizResult(userId, _quiz.getId());
				resultList.addAll(_rList);
			}
			return resultList;
		}

		// 如果同一用户 对同一问卷，回答了多次，取最近一次
		List<QuizResult> tmpList = this.dao().query(
				getEntityClass(),
				Cnd.where("quizId", "=", quizId).and("userId", "=", userId)
						.desc("timestamp"));
		if (tmpList.size() > 0) {
			resultList.add(tmpList.get(0));
		}
		return resultList;
	}

	/**
	 * 获取答题结果，指定答题时间
	 * 
	 * @param userId
	 * @param quizId
	 * @param timestamp
	 * @return
	 */
	public List<QuizResult> getQuizResult(long userId, long quizId,
			long timestamp) {
		List<QuizResult> resultList = new LinkedList<QuizResult>();
		Quiz quiz = QuizCache.me().getQuiz(quizId);

		if (quiz.getType() == QuizConstant.QUIZ_TYPE_PARENT) {
			for (Quiz _quiz : quiz.getChildList()) {
				List<QuizResult> _rList = getQuizResult(userId, _quiz.getId());
				resultList.addAll(_rList);
			}
			return resultList;
		}

		List<QuizResult> tmpList = this.dao().query(
				getEntityClass(),
				Cnd.where("quizId", "=", quizId).and("userId", "=", userId)
						.and("timestamp", "=", timestamp));
		if (tmpList.size() > 0) {
			resultList.add(tmpList.get(0));
		}
		return resultList;
	}

	/**
	 * 保存答案
	 * 
	 * @param userId
	 * @param quiz
	 * @param answerMap
	 * @return
	 */
	public List<QuizResult> storeResult(long userId, Quiz quiz,
			Map<Long, String> answerMap) {
		long timestamp = System.currentTimeMillis();
		return storeResult(userId, quiz, answerMap, timestamp);
	}

	/**
	 * 根据号段和问卷号获取答案列表
	 * 
	 * 用于团体报告
	 * 
	 * @param segmentId
	 * @param quizId
	 * @return
	 */
	public List<QuizResult> getQuizResultList(long segmentId, long quizId) {

		List<QuizResult> tmpList = this.dao().query(
				getEntityClass(),
				Cnd.where("quizId", "=", quizId).and("segmentId", "=",
						segmentId));
		return tmpList;
	}

	/**
	 * 获取某号段答完某套题完毕的用户数量
	 * 
	 * @param segmentId
	 *            quizId
	 * @return
	 */
	public int userFinishedCount(long segmentId, long quizId) {

		Quiz quiz = quizService.fetch(quizId);
		int ret = 0;
		if (quiz.getType() == 0 || quiz.getType() == 2) // 独立问卷 或 子问卷
			ret = this.count(Cnd.where("segmentid", "=", segmentId).and(
					"quizid", "=", quizId));
		else if (quiz.getType() == 1) { // 父问卷
			List<Quiz> quizList = quizService.fetchListByParentId(quizId);

			boolean isFirst = true;
			HashSet<Integer> userIDsFinished = new HashSet<Integer>();
			for (Quiz q : quizList) {
				List<QuizResult> resultList = this.getQuizResultList(segmentId,
						q.getId());
				if (isFirst) { // 首先将第一个子问卷中所有答完题的用户都插入hashset
					isFirst = false;
					for (QuizResult qr : resultList) {
						userIDsFinished.add((int) qr.getUserId());
					}
				} else { // 遍历后面的子问卷答题完毕用户，如果元素没有出现，即在hashset中删除用户Id
					HashSet<Integer> newUserIDsFinished = new HashSet<Integer>();

					for (QuizResult qr : resultList) {
						int userId = (int) qr.getUserId();
						if (userIDsFinished.contains(userId)) {
							newUserIDsFinished.add(userId);
						}
					}
					userIDsFinished = newUserIDsFinished;
				}
			}
			ret = userIDsFinished.size();
		} else
			ret = 0;
		return ret;

	}

	/**
	 * 保存答案
	 * 
	 * @param userId
	 * @param quiz
	 * @param answerMap
	 * @param timestamp
	 * @return
	 */
	public List<QuizResult> storeResult(long userId, Quiz quiz,
			Map<Long, String> answerMap, long timestamp) {
		List<QuizResult> resultList = new ArrayList<QuizResult>();
		if (quiz.getType() == QuizConstant.QUIZ_TYPE_PARENT) {
			for (Quiz _quiz : quiz.getChildList()) {

				// 写入结果前，删除历史答题结果
				deleteByX(userId, _quiz.getId());

				QuizResult _result = _storeResult(userId, _quiz, answerMap,
						timestamp);
				resultList.add(_result);
			}
		} else {
			// 写入结果前，删除历史答题结果
			deleteByX(userId, quiz.getId());

			QuizResult _result = _storeResult(userId, quiz, answerMap,
					timestamp);
			resultList.add(_result);
		}
		return resultList;

	}

	/**
	 * 保存一个独立的试题
	 * 
	 * @param userId
	 * @param quiz
	 * @param answerMap
	 * @param timestamp
	 *            // 控制一组试题的答题时间一致
	 * @return
	 */
	private QuizResult _storeResult(long userId, Quiz quiz,
			Map<Long, String> answerMap, long timestamp) {

		// 总分
		int totalScore = 0;
		// 测谎题总分
		int lieScore = 0;

		List<QuizItem> quizItems = quiz.getItems();
		Map<Long, String> quizAnswer = new HashMap<Long, String>();
		Iterator<QuizItem> qIter = quizItems.iterator();

		Map<Integer, Integer> scoreByCategory = new HashMap<>();
		// 计算每个题目的分数
		while (qIter.hasNext()) {
			QuizItem qItem = qIter.next();

			// 该题的答案序号
			String answerIndex = answerMap.get(qItem.getId());
			quizAnswer.put(qItem.getId(), answerIndex);
			QuizItemOption qOption = qItem.getOption(answerIndex);
			// 该答案对应的分值
			int optScore = qOption.getValue();

			String categoryName = qOption.getCategoryName();
			int categoryId = qOption.getCategoryId();

			// 如果是测谎题目
			if (qItem.isLieFlag()) {
				lieScore += optScore;
				continue;
			}

			// 按分类统计分数
			int _score = optScore;
			if (scoreByCategory.containsKey(categoryId)) {
				_score = scoreByCategory.get(categoryId) + optScore;
			}
			scoreByCategory.put(categoryId, _score);

			// 加入总分
			totalScore += optScore;
		}

		// 读取维度的最高分
		int maxCategoryScore = 0;
		for (int categoryId : scoreByCategory.keySet()) {
			int _score = scoreByCategory.get(categoryId);
			if (_score > maxCategoryScore) {
				maxCategoryScore = _score;
			}
		}
		// 取分数最高的维度，可能有多个维度等级相同
		List<QuizCategory> _maxCategories = new ArrayList<QuizCategory>();
		for (int categoryId : scoreByCategory.keySet()) {
			int _score = scoreByCategory.get(categoryId);
			if (_score == maxCategoryScore) {
				_maxCategories.add(quizCategoryService.fetch(categoryId));
			}
		}

		int myCategoryId = 0;
		boolean isValid = true;

		if (_maxCategories.size() == 1) {
			myCategoryId = _maxCategories.get(0).getId();
		} else {
			// 如果多个维度分数相同，则根据优先级判断
			Iterator<QuizCategory> _qcIter = _maxCategories.iterator();
			int maxPrioriy = 0;
			while (_qcIter.hasNext()) {
				QuizCategory _cate = _qcIter.next();
				if (_cate.getPriority() > maxPrioriy) {
					maxPrioriy = _cate.getPriority();
				}
			}

			if (maxPrioriy == 0) { // 无优先级，取第一个值返回
				myCategoryId = _maxCategories.get(0).getId();
			} else {
				// 删除小于maxPrioriy的项目
				_qcIter = _maxCategories.iterator();
				while (_qcIter.hasNext()) {
					QuizCategory _cate = _qcIter.next();
					if (_cate.getPriority() < maxPrioriy) {
						_qcIter.remove();
					}
				}

				if (_maxCategories.size() == 1) {
					myCategoryId = _maxCategories.get(0).getId();
				} else {
					// 两个或多个同级别的维度，得分相同，则认为答题无效
					isValid = false;
				}

			}
		}

		// 存在测谎题，校验有效性
		if (lieScore > 0 || quiz.getLieBorder() > 0) {
			if (lieScore > quiz.getLieBorder()) {
				isValid = false;
			}
		}

		User user = userService.fetch(userId);
		QuizResult quizResult = new QuizResult();
		quizResult.setId(getTblMaxIdWithUpdate());
		quizResult.setQuizId(quiz.getId());
		quizResult.setUserId(userId);
		quizResult.setSegmentId(user.getSegmentId());
		quizResult.setCompanyId(user.getCompanyId());
		quizResult.setTimestamp(timestamp);
		quizResult.setAnswer(Json.toJson(quizAnswer));
		quizResult.setLieScore(lieScore);
		quizResult.setScore(totalScore);
		quizResult.setScoreJson(Json.toJson(scoreByCategory));
		quizResult.setCategoryId(myCategoryId);
		quizResult.setCategoryName(quizCategoryService.fetch(myCategoryId)
				.getName());
		quizResult.setValid(isValid);

		quizResult = this.dao().insert(quizResult);

		return quizResult;
	}

}
