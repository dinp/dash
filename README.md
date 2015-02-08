Dashboard
=========

这是DINP中的dashboard，是用户操作的入口。我们没有提供命令行的入口，只提供了web的入口，有三个原因：

- 我们发现很多用户还是习惯web界面
- 命令行工具需要安装，而浏览器无需安装
- 人力问题

dashboard采用 [UIC](http://ulricqin.com/project/uic/) 作为单点登录系统，故而，要run dashboard需要提前部署UIC。

# 部署

和UIC一样，这也是一个java项目，也是采用JFinal框架，也是使用Ant来打包编译，也是采用JDK7+Tomcat7来run，请参考前面UIC的链接，里边介绍了UIC的部署方式

# 手册

http://dinp.qiniudn.com/manual.pdf

# 传送门

国内用户使用github可能比较慢，我也push了一份到gitcafe： https://gitcafe.com/dinp/dash ，不用谢
