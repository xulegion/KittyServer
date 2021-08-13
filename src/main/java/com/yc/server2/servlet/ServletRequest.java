package com.yc.server2.servlet;

import java.util.Map;

/**
 * 与协议无关的请求接口(一种规范，就比如J2EE指定一套接口规范，apache tomcat去实现)
 */
public interface ServletRequest {

    public String getRealPath();

    public Object getAttribute(String key);

    public void setAttribute(String key,Object value);

    /**
     *  获取参数值 通过  http://localhost:8080/xxx.jsp?name=a&age=3
     * @param key
     * @return
     */
    public String getParameter(String key);

    public Map<String,String> getParameterMap();

    /**
     * 解析请求: 1.解析出uri   2.解析出参数name,age  3.解析出请求的方式 get/post/head
     */
    public void parse();

    public String getServerName();    //

    public int getServerPort();
}
