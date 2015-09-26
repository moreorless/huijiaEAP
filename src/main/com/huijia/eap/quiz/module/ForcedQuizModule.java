package com.huijia.eap.quiz.module;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.log4j.Logger;
import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.huijia.eap.GlobalConfig;
import com.huijia.eap.annotation.AuthBy;
import com.huijia.eap.auth.Auths;
import com.huijia.eap.auth.bean.User;
import com.huijia.eap.quiz.data.AnswerBean;
import com.huijia.eap.quiz.data.NormResultBean;
import com.huijia.eap.quiz.data.NormalScoreConfig;
import com.huijia.eap.quiz.data.QuizAnswerLog;
import com.huijia.eap.quiz.data.QuizCategory;
import com.huijia.eap.quiz.data.QuizItem;
import com.huijia.eap.quiz.data.QuizResult;
import com.huijia.eap.quiz.service.ForcedQuizService;
import com.huijia.eap.quiz.service.NormalScoreConfigService;
import com.huijia.eap.quiz.service.QuizAnswerLogService;
import com.huijia.eap.quiz.service.QuizCategoryService;
import com.huijia.eap.quiz.service.QuizItemService;
import com.huijia.eap.quiz.service.QuizResultService;
import com.huijia.eap.quiz.service.QuizService;

/**
 * 迫选题答题处理
 * @author leon
 *
 */
@IocBean
@InjectName
@AuthBy(check = false)
@At("/quiz/forced")
public class ForcedQuizModule {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Inject
	private QuizService quizService;

	@Inject
	private QuizItemService quizItemService;
	
	@Inject
	private QuizCategoryService quizCategoryService;
	
	@Inject
	private NormalScoreConfigService normalScoreConfigService;
	
	@Inject
	private QuizResultService quizResultService;
	
	@Inject
	private ForcedQuizService forcedQuizService;
	
	@Inject
	private QuizAnswerLogService quizAnswerLogService;
	
	/**
	 * 提交答案(迫选题)
	 * 
	 * @param request
	 */
	@At
	@Ok("jsp:jsp.quiz.test.answerok")
	public void answer(HttpServletRequest request,
			@Param("quizId") long quizId, @Param("answerJson") String answerJson) {
		User user = Auths.getUser(request);

//		boolean _debug = false;
//		// 加载调试数据
//		if(_debug){
//			answerJson = Files.read(GlobalConfig.getContextValue("conf.dir")
//					+ File.separator + "test" + File.separator + "forcedanswer.txt");
//		}
		
		
		// 计算并存储答题结果
		List answerlist = (List)Json.fromJson(answerJson);
		
		// 各维度得分情况
		Map<Long, NormResultBean> categoryScore = new HashMap<Long, NormResultBean>();
		Map<Long, List<Float>> categoryScoreList = new HashMap<Long, List<Float>>();
		
		// 遍历各题目答案
		Iterator<Map> iter = answerlist.iterator();
		while(iter.hasNext()){
			Map _map = iter.next();
			AnswerBean answerBean = new AnswerBean();
			answerBean.setQuestionId(Long.parseLong((String)_map.get("questionId")));
			answerBean.setOptionIndex((String)_map.get("optionIndex"));
			answerBean.setScore(Float.parseFloat((String)_map.get("score")));
			
			long quizItemId = answerBean.getQuestionId();
			QuizItem quizItem = quizItemService.fetch(quizItemId);
			long categoryId = quizItem.getOption(answerBean.getOptionIndex()).getCategoryId();
			
			if(categoryScoreList.containsKey(categoryId)){
				categoryScoreList.get(categoryId).add(answerBean.getScore());
			}else{
				List<Float> scoreList = new ArrayList<>();
				scoreList.add(answerBean.getScore());
				categoryScoreList.put(categoryId, scoreList);
			}
		}
		
		// 常模得分配置
		List<NormalScoreConfig> normalConfigList = normalScoreConfigService.getConfigList();
		// to Map
		Map<String, NormalScoreConfig> normalConfigMap = new HashMap<>();
		for(NormalScoreConfig _config : normalConfigList){
			normalConfigMap.put(_config.getCategoryName(), _config);
		}
		
		logger.debug("迫选题-情绪管理倾向 得分计算： 用户 " + user.getName());
		// 计算各维度分数（平均分*10，精确到个位数）
		for(long categoryId : categoryScoreList.keySet()){
			QuizCategory category = quizCategoryService.fetch(categoryId);
			
			NormResultBean resultBean = new NormResultBean();
			resultBean.setCategoryId(categoryId);
			resultBean.setCategoryName(category.getName());
			
			// 计算平均分
			List<Float> scoreList = categoryScoreList.get(categoryId);
			float total = 0.0f;
			double[] values = new double[scoreList.size()];
			for(int i = 0; i < scoreList.size(); i++){
				float _score = scoreList.get(i);
				total += _score;
				
				values[i] = _score;
			}
			double _avg = total / scoreList.size();
			
			// 保留4位小数
			BigDecimal bd1 = new BigDecimal(_avg);
			_avg = bd1.setScale(4,  BigDecimal.ROUND_HALF_UP).doubleValue();
			
			resultBean.setOriginalAver(_avg);
			resultBean.setAverageScore((int)Math.round(_avg * 10));
			
			// 计算总体标准差
			Variance variance = new Variance();
			variance.setBiasCorrected(false);	// 计算总体方差，设置为true时，表示计算抽样方差
			double varp = variance.evaluate(values);
			resultBean.setVarp(varp);
			
			// 计算常模得分
			NormalScoreConfig _config = normalConfigMap.get(category.getName());
			if(_config == null) {
				logger.error("常模数据未配置， 维度 " + category.getName());
				continue;
			}
			
			// 计算该维度的倾向指数
			// 计算方法 1分(<=2各标准差) 2分(<=1个标准差) 3分(正负1个标准差) 4分(>=1个标准差) 5分(>=2个标准差)
			int level = 0;
			double dValue = _avg - _config.getAverageScore(); 
			if(dValue <= -2 * _config.getSd()){
				level = 1;
			}else if(dValue > -2 * _config.getSd() && dValue <= -_config.getSd()){
				level = 2;
			}else if(dValue > -_config.getSd() && dValue < _config.getSd()){
				level = 3;
			}else if(dValue >= _config.getSd() && dValue < 2 * _config.getSd()){
				level = 4;
			}else {
				level = 5;
			}
			resultBean.setIndex(level);
			
			NormalDistribution normdist = new NormalDistribution(_config.getAverageScore(), _config.getSd());
			// 计算累积概率值
			double normdistValue = normdist.cumulativeProbability(_avg);
			
			BigDecimal bd2 = new BigDecimal(normdistValue);
			// 保留4位小数
			normdistValue = bd2.setScale(4,  BigDecimal.ROUND_HALF_UP).doubleValue();
			resultBean.setNormdist(normdistValue);
			
			// 根据概率分布配置文件，转换成常模得分
			int[] thresholdArray = new int[]{
				1, 5, 10, 25, 40, 60, 75, 90, 95, 99
			};
			int normalScore = 0;
			for(int i = 0; i < thresholdArray.length; i++){
				if(normdistValue * 100 > thresholdArray[i]){
					normalScore++;
				}else{
					break;
				}
			}
			resultBean.setNormalScore(normalScore);
			resultBean.setNormalAver((int)Math.round(_config.getAverageScore() * 10));
			
			// 输出得分信息
			logger.debug(resultBean);
			
			categoryScore.put(categoryId, resultBean);
		}
		
		// 删除历史数据
		quizResultService.deleteByX(user.getUserId(), quizId);
		
		
		long currentTime = System.currentTimeMillis();
		// 存储计算结果 (存入quiz_result表)
		QuizResult quizResult = new QuizResult();
		quizResult.setQuizId(quizId);
		quizResult.setUserId(user.getUserId());
		quizResult.setValid(true);
		quizResult.setCompanyId(user.getCompanyId());
		quizResult.setSegmentId(user.getSegmentId());
		quizResult.setAnswer(answerJson);
		quizResult.setScoreJson(Json.toJson(categoryScore));
		quizResult.setTimestamp(currentTime);
		quizResultService.insert(quizResult);
		
		QuizAnswerLog history = new QuizAnswerLog();
		history.setQuizId(quizId);
		history.setUserId(user.getUserId());
		history.setCompanyId(user.getCompanyId());
		history.setTimestamp(currentTime);
		quizAnswerLogService.insert(history);
	}
	
	@At
	@Ok("json")
	@Fail("json")
	public List<NormResultBean> getResult(@Param("userId") long userId, @Param("quizId") long quizId, HttpServletRequest request){
		List<NormResultBean> result = forcedQuizService.getAnswerResult(userId, quizId);
		return result;
	}
	
}
