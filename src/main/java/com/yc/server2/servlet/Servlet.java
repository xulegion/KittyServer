package com.yc.server2.servlet;

/**
 * sun定义的Servlet接口用来定义生命周期的回调方法
 */
public interface Servlet {
    public void init();

    public void destroy();

    public void service(ServletRequest request,ServletResponse response);
}
