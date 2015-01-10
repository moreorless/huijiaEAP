package com.huijia.eap.commons.mvc;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.jsp.JspFactory;

/**
 * Registers ELResolver that escapes XML in EL expression String values.
 */
public class EscapeXmlELResolverListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
        JspFactory.getDefaultFactory()
                .getJspApplicationContext(event.getServletContext())
                .addELResolver(new EscapeXmlELResolver());
    }

    public void contextDestroyed(ServletContextEvent event) {
    }
}
