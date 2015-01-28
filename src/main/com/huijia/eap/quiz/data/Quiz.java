package com.huijia.eap.quiz.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.json.Json;

import com.huijia.eap.commons.mvc.validate.annotation.ValidateType;
import com.huijia.eap.commons.mvc.validate.annotation.Validations;
import com.huijia.eap.commons.mvc.validate.annotation.ValidateType.Type;

@Table("quiz")
public class Quiz {

	

	@Column
	@Id(auto = false)
	private long id;

	/**
	 * 名称
	 */
	@Column
	@Validations(rules = {
			@ValidateType(type = Type.required, errorMsg = "quiz.add.name.span", resource = true, bundle = "quiz"),
			@ValidateType(type = Type.minlength, parameters = { "1" }, errorMsg = "quiz.add.name.span", resource = true, bundle = "quiz"),
			@ValidateType(type = Type.maxlength, parameters = { "512" }, errorMsg = "quiz.add.name.span", resource = true, bundle = "quiz") })
	private String name;

	@Column
	private short type;

	@Column
	private long parentId;

	@Column
	private String children;

	/**
	 * 描述
	 */
	@Column
	private String description;

	@Column
	private String icon;

	@Column
	private long createBy;

	@Column
	private long createAt;

	@Column
	private long updateBy;

	@Column
	private long updateAt;

	@Column
	private String categoryJson;

	@Column
	private long categoryNum;

	@Column
	private long itemNum;

	/**
	 * 测谎分数线
	 */
	@Column
	private long lieBorder;

	/**
	 * 指导语
	 */
	@Column
	private String guideline;
	
	/**
	 * 报告模板
	 */
	@Column
	private String reporttpl;

	public List<Quiz> getChildList() {
		return childList;
	}

	public void setChildList(List<Quiz> childList) {
		this.childList = childList;
	}

	/**
	 * 题目
	 */
	private List<Quiz> childList = new LinkedList<Quiz>();

	public String getGuideline() {
		return guideline;
	}

	public void setGuideline(String guideline) {
		this.guideline = guideline;
	}

	public String getCategoryJson() {
		return categoryJson;
	}

	public void setCategoryJson(String categoryJson) {
		this.categoryJson = categoryJson;
	}

	public long getCategoryNum() {
		return categoryNum;
	}

	public void setCategoryNum(long categoryNum) {
		this.categoryNum = categoryNum;
	}

	/**
	 * 题目
	 */
	private List<QuizItem> items;

	public long getItemNum() {
		return itemNum;
	}

	public void setItemNum(long itemNum) {
		this.itemNum = itemNum;
	}

	public long getLieBorder() {
		return lieBorder;
	}

	public void setLieBorder(long lieBorder) {
		this.lieBorder = lieBorder;
	}

	/**
	 * 评价体系
	 */
	private List<QuizEvaluation> evaluations;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getChildren() {
		return children;
	}

	public void setChildren(String children) {
		this.children = children;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<QuizItem> getItems() {
		return items;
	}

	public void setItems(List<QuizItem> items) {
		this.items = items;
	}

	public long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(long createBy) {
		this.createBy = createBy;
	}

	public long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(long createAt) {
		this.createAt = createAt;
	}

	public long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(long updateBy) {
		this.updateBy = updateBy;
	}

	public long getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(long updateAt) {
		this.updateAt = updateAt;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<QuizEvaluation> getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(List<QuizEvaluation> evaluations) {
		this.evaluations = evaluations;
	}
	
	

	public String getReporttpl() {
		return reporttpl;
	}

	public void setReporttpl(String reporttpl) {
		this.reporttpl = reporttpl;
	}

	public void setCategoryList(List<QuizCategory> categoryList) {
		this.categoryList = categoryList;
	}

	private List<QuizCategory> categoryList = null;
	/**
	 * 获取试卷分类
	 * @return
	 */
	public List<QuizCategory> getCategoryList(){
		if(categoryJson == null) {
			return null;
		}
		
		if(categoryList != null) return categoryList;
		
		categoryList = new ArrayList<QuizCategory>();
		List<Map> _list = (List)Json.fromJson(categoryJson);
		Iterator<Map> iter = _list.iterator();
		while(iter.hasNext()){
			Map _obj = iter.next();
			int id = (int)_obj.get("id");
			String name = (String)_obj.get("name");
			QuizCategory category = new QuizCategory(id, name);
			categoryList.add(category);
		}
		
		return categoryList;
	}
	public QuizCategory getCategoryByName(String categoryName){
		List<QuizCategory> categoryList = this.getCategoryList();
		Iterator<QuizCategory> iter = categoryList.iterator();
		while(iter.hasNext()){
			QuizCategory category = iter.next();
			if(category.getName().equals(categoryName)){
				return category;
			}
		}
		return null;
	}
	public QuizCategory getCategoryById(int categoryId){
		List<QuizCategory> categoryList = this.getCategoryList();
		Iterator<QuizCategory> iter = categoryList.iterator();
		while(iter.hasNext()){
			QuizCategory category = iter.next();
			if(category.getId() == categoryId){
				return category;
			}
		}
		return null;
	}
}
