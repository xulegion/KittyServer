package com.yc.server2.servlet.http;

import com.yc.server2.servlet.ServletRequest;

/**
 * sun定义的专门针对http协议的请求 接口
 */
public interface HttpServletRequest extends ServletRequest {
    /**
     * 请求的方法
     * @return  GET/POST/HEAD/DELETE
     */
    public String getMethod();

    /**
     * /index.html
     * /login.action?name=zy
     * @return
     */
    public String getRequestURI();
}
