package com.yc.server1;

import java.io.File;
import java.io.InputStream;
import java.net.ServerSocket;
import java.util.StringTokenizer;

public class HttpServletRequest {

    private String method;  //请求方法
    private String protocal;  //协议版本
    private String serverName;  //服务器名
    private int serverPort;    //端口
    private String requestURI;  //资源的地址
    private String requestURL;  //绝对路径
    private String contextPath;  //项目上下文路径

    private String realPath=System.getProperty("user.dir")+ File.separator+"webapps";
    //user.dir  -->  取到的是当前的class的路径

    private InputStream iis;

    public HttpServletRequest(InputStream iis) {
        this.iis = iis;
        parseRequest();   //解析请求中的信息
        System.out.println("******************"+requestURI);
    }

    private void parseRequest() {
        String requestInfoString=readFromInputStream();   //从输入流中读取请求头
        if (requestInfoString==null || "".equals(requestInfoString)){
            return;
        }

        //解析requestInfoString: method,  URI,  键:值   参数
        parseRequestInfoString(requestInfoString);



    }

    private void parseRequestInfoString(String requestInfoString) {
        StringTokenizer st = new StringTokenizer(requestInfoString);
        //请求头   GET /xxx/xxx.jpg?key=value HTTP/1.1
        if (st.hasMoreTokens()){
            this.method=st.nextToken();
            this.requestURI=st.nextToken();  //    /keaiwu/index.html
            this.protocal=st.nextToken();
            this.contextPath="/"+this.requestURI.split("/")[1];   //contextPath应用上下文路径=>/wowotuan

        }

        //TODO:后面的请求，键值对，请求实体 暂时不管到时候再加
    }


    //从输入流中读取数据
    private String readFromInputStream() {
        //1.从input中读取所有的内容(http请求协议==>protocal)
        String protocal=null;
        //TODO:从流中取protocal
        StringBuffer sb = new StringBuffer(1024 * 10);//10K-->避免循环读取
        int length=-1;
        byte[] bs = new byte[1024 * 10];
        try {
            length=this.iis.read(bs);
        }catch (Exception e){
            e.printStackTrace();
            length=-1;
        }

        for (int j=0;j<length;j++){
            sb.append((char)bs[j]);
        }
        protocal=sb.toString();
        return protocal;
    }

    public String getMethod() {
        return method;
    }

    public String getProtocal() {
        return protocal;
    }

    public String getServerName() {
        return serverName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getRealPath() {
        return realPath;
    }
}
