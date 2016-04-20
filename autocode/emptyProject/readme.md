###项目介绍
durcframework-core对应的demo,基础框架已搭建完成,可以当作空项目使用.
采用SpringMvc + mybatis + mysql + maven

![输入图片说明](http://git.oschina.net/uploads/images/2016/0412/153813_4476b879_332975.png "在这里输入图片标题")

![输入图片说明](http://git.oschina.net/uploads/images/2016/0412/153542_3a85a3d2_332975.png "在这里输入图片标题")

###项目部署
1. 下载demo,进入项目根目录,运行mvn eclipse:eclipse,然后导入到eclipse中;
2. 导入数据(Mysql),运行database.sql即可,数据库连接配置在config.properties中;
3. 启动tomcat或者输入maven命令mvn jetty:run (默认端口8088),浏览器输入http://localhost:your_port/your_project_name/index.jsp

4. app.controller.demo包下含有更多例子,请自行体验.

[代码生成器](https://git.oschina.net/durcframework/autoCode)