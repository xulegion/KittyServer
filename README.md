# KittyServer

仿写Tomcat服务器(实现资源访问功能)

    server1：实现静态资源访问
  
    server2：实现静态资源访问+servlet(单例)

项目架构：

    分为三层：
    
        1.模拟的sun公司规定的无协议的ServletRequest、ServletResponse接口及针对Http协议的HttpServletRequest、HttpServletResponse等接口
        
        2.服务器端--->KittyServer，针对接口开发服务器(Tomcat是http服务器，基于请求与响应，且无状态)
        
        3.客户端(静态资源+servlet)
        
 运用技术:
    
    基于Http协议
    1.线程池(处理多个请求访问资源)
    2.Socket(建立联接)
    3.门面模式+单例模式
    
    静态资源处理:
        定位资源+IO流的处理
    servlet处理:
        定位servlet+动态加载class字节码+流的处理
        
具体内容，请分析代码
