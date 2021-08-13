import com.yc.server2.servlet.http.HttpServlet;
import com.yc.server2.servlet.http.HttpServletRequest;
import com.yc.server2.servlet.http.HttpServletResponse;

import java.io.PrintWriter;

public class HelloServlet extends HttpServlet {

    public HelloServlet(){
        System.out.println("HelloServlet的构造");
    }

    @Override
    public void init() {
        super.init();
        System.out.println("init()");
    }

    @Override
    public void destroy() {
        super.destroy();
        System.out.println("destroy()");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
       doGet(request,response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
       String content="hello world";

        String name = request.getParameter("name");
        String age = request.getParameter("age");
        content+="name:"+name+"\tage:"+age;

        PrintWriter out = response.getWriter();
        //在标准servlet中是由 tomcat完成这些响应头
        out.print("HTTP/1.1 200 OK\r\n");
        out.print("Content-Type: text/html\r\n");
        out.print("Content-Length: "+content.getBytes().length+"\r\n");
        out.print("\r\n");

        out.println(content);

        out.close();



    }
}
