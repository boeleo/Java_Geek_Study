# Week3 homework

1.（必做）整合你上次作业的 httpclient/okhttp；
3.（必做）实现过滤器。

本次作业框架来自老师的 https://github.com/JavaCourse00/JavaCourseCodes 02nio/nio02 文件下， 自己新添加的文件位于`Q1_3/src/main/java/io/github/baojia/homework/`, 有：
* BackendServer.java: 后端server，发布在 http://localhost:8088/；
* OkHttpUtils.java: 上周使用okhttp访问后端server的程序；
* ProxyBizUriFilter.java: 本周实现的过滤request uri的filter，只有uri以“hello”开始的才能通过。

`Q1_3/src/main/java/io/github/kimmking/gateway/` 部分拷贝于老师的nio02，稍作整理之后，重点修改的部分有：
* pom.xml: 添加了okhttp所需的dependency；
* NettyServerApplication.java: 改成单后端；
* HttpInboundHandler.java: 添加了ProxyBizUriFilter的调用。