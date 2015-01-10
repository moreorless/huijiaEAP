package com.huijia.eap.commons.mvc.taglib;

import java.util.Date;

import javax.servlet.jsp.JspException;

import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;
import org.apache.taglibs.standard.tag.common.fmt.FormatDateSupport;

/**
 * <p>
 * 格式化日期标签，将long型的日期转换为指定的格式
 * </p>
 * 
 * 
 * @author liunan
 *
 */
public class FormatDateTag extends FormatDateSupport
{
    private String value_;
    private String type_;
    private String dateStyle_;
    private String timeStyle_;
    private String pattern_;
    private String timeZone_;
    
    public int doStartTag() throws JspException
	{
		evaluateExpressions();
		return super.doStartTag();
	}

	public void release()
	{
		super.release();
		init();
	}

	private void init()
    {
        value_ = type_ = dateStyle_ = timeStyle_ = pattern_ = timeZone_ = null;
    }

    private void evaluateExpressions()
        throws JspException
    {
    	long date = (Long) ExpressionEvaluatorManager.evaluate("value", value_, java.lang.Long.class, this, pageContext);
    	if (date != 0)
    		value = new Date(date);
    	else
    		value = null;
        if(type_ != null)
            type = (String)ExpressionEvaluatorManager.evaluate("type", type_, java.lang.String.class, this, pageContext);
        if(dateStyle_ != null)
            dateStyle = (String)ExpressionEvaluatorManager.evaluate("dateStyle", dateStyle_, java.lang.String.class, this, pageContext);
        if(timeStyle_ != null)
            timeStyle = (String)ExpressionEvaluatorManager.evaluate("timeStyle", timeStyle_, java.lang.String.class, this, pageContext);
        if(pattern_ != null)
            pattern = (String)ExpressionEvaluatorManager.evaluate("pattern", pattern_, java.lang.String.class, this, pageContext);
        if(timeZone_ != null)
            timeZone = ExpressionEvaluatorManager.evaluate("timeZone", timeZone_, java.lang.Object.class, this, pageContext);
    }

    public String getDateStyle()
	{
		return dateStyle_;
	}
	public void setDateStyle(String dateStyle_)
	{
		this.dateStyle_ = dateStyle_;
	}
	public String getPattern()
	{
		return pattern_;
	}
	public void setPattern(String pattern_)
	{
		this.pattern_ = pattern_;
	}
	public String getTimeStyle()
	{
		return timeStyle_;
	}
	public void setTimeStyle(String timeStyle_)
	{
		this.timeStyle_ = timeStyle_;
	}
	public String getTimeZone()
	{
		return timeZone_;
	}
	public void setTimeZone(String timeZone_)
	{
		this.timeZone_ = timeZone_;
	}
	public String getType()
	{
		return type_;
	}
	public void setType(String type_)
	{
		this.type_ = type_;
	}
	public String getValue()
	{
		return value_;
	}
	public void setValue(String value_)
	{
		this.value_ = value_;
	}

}
