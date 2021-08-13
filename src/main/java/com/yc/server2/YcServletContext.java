package com.yc.server2;

import com.yc.server2.servlet.Servlet;
import com.yc.server2.servlet.ServletContext;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class YcServletContext implements ServletContext {

    private Map<String,Servlet> servlets=new Hashtable<>();

    private Map<String,Object> attributes=new HashMap<>();

    private static YcServletContext ycServletContext;

    private YcServletContext() {
    }

     //单例: ->DLC
    public synchronized static YcServletContext getInstance(){
        if (ycServletContext==null){
            ycServletContext=new YcServletContext();
        }
        return ycServletContext;
    }

    @Override
    public Map<String, Servlet> getServlets() {
        return this.servlets;
    }

    @Override
    public Servlet getServlet(String servletName) {
        return this.servlets.get(servletName);
    }

    @Override
    public void setServlet(String key, Servlet servlet) {
        this.servlets.put(key,servlet);
    }

    @Override
    public void setAttribute(String key, Object obj) {
        this.attributes.put(key,obj);
    }

    @Override
    public Object getAttribute(String key) {
        return this.attributes.get(key);
    }
}
