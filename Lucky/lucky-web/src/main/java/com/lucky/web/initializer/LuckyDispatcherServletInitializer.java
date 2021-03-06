package com.lucky.web.initializer;

import com.lucky.web.servlet.LuckyDispatcherServlet;
import com.lucky.web.servlet.LuckyServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * @author fk7075
 * @version 1.0.0
 * @date 2020/12/16 上午2:19
 */
public class LuckyDispatcherServletInitializer implements WebApplicationInitializer{

    private static final Logger log= LoggerFactory.getLogger("c.l.w.i.LuckyDispatcherServletInitializer");

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.addListener(new LuckyServletContextListener());
        log.info("WebApplicationInitialize Add Listener `class = com.lucky.web.servlet.LuckyServletContextListener`");
        ServletRegistration.Dynamic luckyDispatcherServlet = servletContext.addServlet("LuckyDispatcherServlet", new LuckyDispatcherServlet());
        luckyDispatcherServlet.setLoadOnStartup(0);
        luckyDispatcherServlet.addMapping("/");
        luckyDispatcherServlet.setAsyncSupported(true);
        log.info("WebApplicationInitialize Add Servlet `name = LuckyDispatcherServlet mapping = [/] class = com.lucky.web.servlet.LuckyDispatcherServlet`");
    }
}
