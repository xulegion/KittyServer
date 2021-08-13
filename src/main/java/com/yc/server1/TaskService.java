package com.yc.server1;

import com.yc.threadpool.Taskable;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TaskService implements Taskable {
    private Socket socket;
    private InputStream iis;
    private OutputStream oos;
    private boolean flag;

    private Logger logger=Logger.getLogger(TaskService.class.getName());

    public TaskService(Socket socket) {
        this.socket = socket;
        try {
            this.iis=this.socket.getInputStream();
            this.oos=this.socket.getOutputStream();
            flag=true;
        }catch (Exception e){
            logger.error(" failed to get stream ",e);
            flag=false;
            throw new RuntimeException(e);
        }
    }

    @Override    //HTTP协议是一次请求和响应，http是无状态(一次请求和响应就会断开连接)
    public Object doTask() throws IOException {
        if (flag){
            //包装一个HttpServletRequest对象，功能是从 iis中取数据，解析请求信息，保存信息
            HttpServletRequest request = new HttpServletRequest(this.iis);  //uri
            //包装一个HttpServletResponse对象: 从request中取信息(文件的资源),读取资源，构建响应头，回给客户端
            HttpServletResponse response = new HttpServletResponse( this.oos,request);

            response.sendRedirect();

        }

        try {
            this.socket.close();   //http协议是一个基于请求/响应模式的无状态  -->引入cookie
        }catch (Exception e){
            logger.error(" failed to close connection to client ",e);
        }

        return null;
    }
}
