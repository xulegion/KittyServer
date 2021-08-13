package com.yc.server2.servlet.http;

import com.yc.server2.servlet.ServletResponse;

public interface HttpServletResponse extends ServletResponse {

    /**
     * 输出重定向方法: 重定向要发出302的响应码，这个码是http特有的，
     * 所以，此方法是http特有的
     */
    public void sendRedirect();
}
