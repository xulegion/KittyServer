package com.yc.server2;

import com.yc.server2.servlet.ServletRequest;
import com.yc.server2.servlet.ServletResponse;

public class StaticProcessor implements Processor {
    @Override
    public void process(ServletRequest request, ServletResponse response) {
        ((YcHttpServletResponse)response).sendRedirect();  //重定向要发生在 302的响应头 3xx属于http协议
    }
}
