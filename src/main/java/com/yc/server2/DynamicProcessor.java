package com.yc.server2;

import com.yc.server2.servlet.Servlet;
import com.yc.server2.servlet.ServletContext;
import com.yc.server2.servlet.ServletRequest;
import com.yc.server2.servlet.ServletResponse;
import com.yc.server2.servlet.http.HttpServlet;
import com.yc.server2.servlet.http.HttpServletRequest;
import com.yc.server2.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class DynamicProcessor implements Processor {
    @Override
    // request的 requestURI => /res/HelloServlet.do
    public void process(ServletRequest request, ServletResponse response) {
        try {
            //1.取出uri，从uri中取出请求的servlet名字
            String uri = ((HttpServletRequest) request).getRequestURI();  //  /HelloServlet.action
            String servletName = uri.substring(uri.lastIndexOf("/") + 1, uri.lastIndexOf("."));
            Servlet servlet=null;

            //从application中判断是否有这个servletName
            ServletContext application = YcServletContext.getInstance();
            if (application.getServlet(servletName)!=null){
                servlet=application.getServlet(servletName);
            }else {
                //2.动态字节码加载到 res/找servlet.class文件
                URL[] urls = new URL[1];
                urls[0] = new URL("file", null, System.getProperty("user.dir"));
                System.out.println("要加载的 class的路径为:" + urls[0]);
                //自动根据 class的地址加载字节码
                URLClassLoader ucl = new URLClassLoader(urls);
                //3. URL地址  => file:\\\
                //4. Class.urlclassloader.loadClass(类的名字);
                Class c = ucl.loadClass(servletName);
                //5.以反射的形式newInstance()创建servlet实例
                Object o = c.newInstance();  //-->调用构造方法
                if (o != null && o instanceof Servlet) {
                    servlet = (Servlet) o;
                    application.setServlet(servletName,servlet);
                    servlet.init();
                }
            }
            //6.再以生命周期的方式 来调用servlet中的各方法
            if (servlet!=null && servlet instanceof Servlet){
                //父类引用只能调用子类重写父类的方法而不能调用子类所特有的方法
                ((HttpServlet)servlet).service((HttpServletRequest)request,(HttpServletResponse) response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String bodyentity = e.toString();
            String protocal = gen500(bodyentity.getBytes().length);
            PrintWriter pw = response.getWriter();
            pw.println(protocal);
            pw.println(bodyentity);
            pw.flush();
        }

    }

    private String gen500(int length) {
        String protocal500="HTTP/1.1 500 Internal Server Error\r\nContent-Type: text/html;charset=utf-8\r\nContent-Length: "+length+"\r\n\r\n";
        return protocal500;
    }
}
