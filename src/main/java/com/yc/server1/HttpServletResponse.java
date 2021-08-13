package com.yc.server1;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

public class HttpServletResponse {
    
    private OutputStream oos;
    private HttpServletRequest request;

    public HttpServletResponse(OutputStream oos, HttpServletRequest request) {
        this.oos = oos;
        this.request = request;
    }

    /**
     *   1.从request中取出uri 
     *   2.判断是否在本地存在这个uri指代的文件，没有 404
     *   3.有的话，以输入流读取这个文件
     *   4.以输入流将文件写到客户端，要加入响应的协议
     */
    
    public void sendRedirect(){
        String responseprotocal = null;  //响应协议头
        byte[] fileContent =null;   //响应的内容
        String uri=request.getRequestURI();  //请求资源的地址  /keaiwu/index.html

        File f = new File(request.getRealPath(), uri);//请求文件的绝对路径
        if (!f.exists()){   //404.html
            fileContent=readFile(new File(request.getRealPath(),"/404.html"));
            responseprotocal=gen404(fileContent.length);
        }else {
            fileContent=readFile(f);
            responseprotocal=gen200(fileContent.length);
        }
        
        try {
            oos.write(responseprotocal.getBytes());   //写协议，字节数组，不能以字符串 防止图片乱码
            oos.flush();
            oos.write(fileContent);  //写出文件
            oos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (oos!=null){
                try {
                    oos.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 要考虑静态文件的类型
     * @param length  内容的长度
     * @return
     */
    private String gen200(int length) {
        //TODO:  更安全是要看魔术字
        String uri = this.request.getRequestURI();   //取出要访问的文件的地址
        int index = uri.lastIndexOf(".");
        if (index>=0){
            index = index+1;
        }
        String fileExtension = uri.substring(index);//文件的后缀名
        String protocal200="";
        //TODO: 参考 tomcat的web.xml用xml文件记录这种配置
        if ("JPG".equalsIgnoreCase(fileExtension) || "JPEG".equalsIgnoreCase(fileExtension)){
            protocal200="HTTP/1.1 200 OK\r\nContent-Type: image/JPEG\r\nContent-Length: "+length+"\r\n\r\n";
        }else if ("PNG".equalsIgnoreCase(fileExtension) ){
            protocal200="HTTP/1.1 200 OK\r\nContent-Type: image/PNG\r\nContent-Length: "+length+"\r\n\r\n";
        }else if ("json".equalsIgnoreCase(fileExtension) ){
            protocal200="HTTP/1.1 200 OK\r\nContent-Type: application/json\r\nContent-Length: "+length+"\r\n\r\n";
        }else if ("css".equalsIgnoreCase(fileExtension) ){
            protocal200="HTTP/1.1 200 OK\r\nContent-Type: text/css\r\nContent-Length: "+length+"\r\n\r\n";
        }else if ("js".equalsIgnoreCase(fileExtension) ){
            protocal200="HTTP/1.1 200 OK\r\nContent-Type: text/javascript\r\nContent-Length: "+length+"\r\n\r\n";
        }else {
            protocal200="HTTP/1.0 200 OK\r\nContent-Type: text/html\r\nContent-Length: "+length+"\r\n\r\n";
        }
        return protocal200;
    }

    /**
     * 产生404响应
     * @param length
     * @return
     */
    private String gen404(int length) {
        String protocal404="HTTP/1.1 404 File Not Found\r\nContent-Type: text/html;charset=utf-8\r\nContent-Length: "+length+"\r\n\r\n";
        return protocal404;
    }

    private byte[] readFile(File f) {
        FileInputStream fis=null;
        //字节数组输出流： 读取到字节数组后，存到内存
        ByteArrayOutputStream boas = new ByteArrayOutputStream();  //字节数组输出流
        //将字节数组数据保存到内存
        try{
            //读取这个文件
            fis=new FileInputStream(f);
            byte[] bs = new byte[10 * 1024];
            int length=-1;
            while ((length=fis.read(bs,0,bs.length))!=-1){
                boas.write(bs,0,length);   //写道内存数组
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (fis!=null){
                try {
                    fis.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        return boas.toByteArray();  //一次性地从内存中读取所有地字节数组返回
    }
}
