package com.yc.server2.servlet;

import java.util.Map;

/**
 * application容器
 */
public interface ServletContext {

    //Servlet的类名  Servlet实例
    public Map<String,Servlet> getServlets();

    public Servlet getServlet(String servletName);

    public void setServlet(String key,Servlet servlet);

    public void setAttribute(String key,Object obj);

    public Object getAttribute(String key);
}
