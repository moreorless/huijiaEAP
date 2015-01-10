package com.huijia.eap.commons.mvc.taglib;

import javax.servlet.jsp.tagext.TagSupport;

import com.huijia.eap.commons.mvc.EscapeXmlELResolver;


public class SetEscapeTag extends TagSupport {

	private static final long serialVersionUID = 1L;

    private static final boolean ESCAPE_XML_DEFAULT = true;
    private boolean escapeXml;
    
    public SetEscapeTag() {
        release();
    }
    
    public void setEscapeXml(boolean escapeXml) {
        this.escapeXml = escapeXml;
    }
    
    @Override
    public int doStartTag() {
        pageContext.setAttribute(
                EscapeXmlELResolver.ESCAPE_XML_ATTRIBUTE, escapeXml);
        return EVAL_BODY_INCLUDE;
    }
    
    @Override
    public int doEndTag() {
        pageContext.setAttribute(
                EscapeXmlELResolver.ESCAPE_XML_ATTRIBUTE, ESCAPE_XML_DEFAULT);
        return EVAL_PAGE;
    }
    
    @Override
    public void release() {
        escapeXml = ESCAPE_XML_DEFAULT;
    }
}
