package com.yc.server2;


import com.yc.server2.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 由服务器实现的http请求的类，主要是解析 http请求头 取出参数(  /login.action?name=xxn,post请求时写在请求实体中的参数name=xxn)
 * 存到Map中，由request.getParameter()
 */
public class YcHttpServletRequest implements HttpServletRequest {
    //请求方法
    private String method;
    //协议的版本
    private String protocal;
    //服务器名
    private String serverName;
    //端口
    private int serverPort;
    //资源的地址
    private String requestURI;
    private String requestURL;
    //上下文路径  项目名/
    private String contextPath;
    //项目在服务器中的真实路径 webapps
    private String realPath = System.getProperty("user.dir") + File.separator + "webapps";

    private InputStream iis;
    //请求字符串  地址栏中 地址的后面那一部分 /login.action?name=xxn
    private String queryString;
    //请求参数，值 都是String类型
    private Map<String, String> parameters = new HashMap<>();
    //值为Object类型
    private Map<String, Object> attributes = new HashMap<>();

    public YcHttpServletRequest(InputStream iis) {
        this.iis = iis;
        parse();
    }

    @Override
    public String getRealPath() {
        return realPath;
    }

    @Override
    public void parse() {
        String requestInfoString = readFromInputStream();   //从输入流中读取请求头
        if (requestInfoString == null || "".equals(requestInfoString)) {
            return;
        }

        //解析requestInfoString: method,  URI,  键:值   参数
        parseRequestInfoString(requestInfoString);
    }

    private void parseRequestInfoString(String requestInfoString) {
        StringTokenizer st = new StringTokenizer(requestInfoString);
        //请求头   GET /xxx/xxx.jpg?key=value HTTP/1.1
        if (st.hasMoreTokens()) {
            this.method = st.nextToken();
            this.requestURI = st.nextToken();  //    /keaiwu/index.html
            this.protocal = st.nextToken();
            this.contextPath = "/" + this.requestURI.split("/")[1];   //contextPath应用上下文路径=>/wowotuan

        }
        //TODO:后面的请求，键值对，请求实体 暂时不管到时候再加
        parseParameter(requestInfoString);
        //TODO: 解析Header
        //TODO: 文件上传
    }

    public String getQueryString(){
        return this.queryString;
    }

    public String getProtocal() {
        return protocal;
    }

    @Override
    public String getServerName() {
        return serverName;
    }

    @Override
    public int getServerPort() {
        return serverPort;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public String getContextPath() {
        return contextPath;
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    @Override
    public String getParameter(String key) {
        return parameters.get(key);
    }

    @Override
    public Map<String, String> getParameterMap() {
        return this.parameters;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getRequestURI() {
        return requestURI;
    }

    //从输入流中读取数据
    private String readFromInputStream() {
        //1.从input中读取所有的内容(http请求协议==>protocal)
        String protocal = null;
        //TODO:从流中取protocal
        StringBuffer sb = new StringBuffer(1024 * 20);//20K-->避免循环读取
        int length = -1;
        byte[] bs = new byte[1024 * 20];
        try {
            length = this.iis.read(bs);
        } catch (Exception e) {
            e.printStackTrace();
            length = -1;
        }

        for (int j = 0; j < length; j++) {
            sb.append((char) bs[j]);
        }
        protocal = sb.toString();
        return protocal;
    }

    //解析参数
    private void parseParameter(String requestInfoString) {
        // /wowotuan/xxx.do?name=a&age=20
        //1.判断是否有?,有则要取?前面当作 requuestURI
        //以下解决了地址栏后面的参数解析问题
        if (requestURI.indexOf("?")>=0){
            this.queryString=requestURI.substring(requestURI.indexOf("?")+1);  //查询字符串 name=a&age=20
            this.requestURI=requestURI.substring(0,requestURI.indexOf("?"));   //资源地址
            //从queryString中解析出参数   name=a&pwd=a
            String[] ss = this.queryString.split("&");
            if (ss!=null && ss.length>0){
                for (String s:ss){
                    String[] paire = s.split("=");
                    if (paire!=null && paire.length>0){
                        this.parameters.put(paire[0],paire[1]);
                    }
                }
            }
        }

        if (this.method.equals("POST")){
            //post实体中的参数
            String ps = requestInfoString.substring(requestInfoString.indexOf("\r\n\r\n") + 4);
            if (ps!=null && ps.length()>0){
                String[] ss = ps.split("&");
                if (ss!=null && ss.length>0) {
                    for (String s : ss) {
                        String[] paire = s.split("=");
                        if (paire != null && paire.length > 0) {
                            this.parameters.put(paire[0], paire[1]);
                        }
                    }
                }
            }
        }
    }

}
