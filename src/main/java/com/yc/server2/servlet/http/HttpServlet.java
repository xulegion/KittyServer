package com.yc.server2.servlet.http;

import com.yc.server2.servlet.Servlet;
import com.yc.server2.servlet.ServletRequest;
import com.yc.server2.servlet.ServletResponse;

public abstract class HttpServlet implements Servlet {
    @Override
    public void init(){
    }

    @Override
    public void destroy(){
    }

    @Override
    public void service(ServletRequest request, ServletResponse response){
        this.service((HttpServletRequest) request,(HttpServletResponse) response);
    }

    public void service(HttpServletRequest request,HttpServletResponse response){
        //取出request中的method
        String method = request.getMethod();
        //判断是get/post
        if ("get".equalsIgnoreCase(method)){
            doGet(request,response);
        }else if ("post".equalsIgnoreCase(method)){
            doPost(request,response);
        }else if ("head".equalsIgnoreCase(method)){
            doHead(request,response);
        }else if ("delete".equalsIgnoreCase(method)){
            doDelete(request,response);
        }

        //调用doGet/doPost
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response){};

    protected void doHead(HttpServletRequest request, HttpServletResponse response){};

    protected void doPost(HttpServletRequest request, HttpServletResponse response){};

    protected void doGet(HttpServletRequest request, HttpServletResponse response){};
}
