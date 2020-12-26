# AdminSystem

## 一、简介

该项目来自于一个比赛，因为后续没有继续参赛所以将这个项目开源，供学习交流。AdminSystem在功能上拥有一个社区模块和一个交易平台模块，两个模块相对独立。

社区模块类似于朋友圈，提供了发布，评论和点赞等功能。交易平台模块提供发布项目，候选人竞选项目，发布人选择候选人和达成订单等功能。

> 目前项目提供了全部Java后台代码和使用Vue写的管理页前端，无前台展示页面。

![管理页面截图](http://image.marwin.cn/uPic/screencapture-localhost-8080-2020-10-01-19_49_34.png)

## 二、安装环境

1. 使用`java -version`确定已安装Java 8以上环境。

2. 安装Mysql Server，创建一个名为`admin_system`的数据库，导入`resource/sql/admin_system.sql`文件完成数据库的配置。在mysql命令行中执行：
    ```shell script
    use admin_system
   
    source  path   # sql文件的绝对路径
    ```

3. 修改`application-dev.properties`中的数据库账号密码为自己的账号密码，`file.location`的值是文件上传路径，指定一个自己电脑上的路径。

    > mysql 8以上版本使用的mysql驱动为`com.mysql.cj.jdbc.Driver`，如果你的mysql是之前版本的请修改为`com.mysql.jdbc.Driver`。
  
4. 要运行Vue前端需要node环境，安装方法请参考搜索引擎。

## 三、运行项目

### 命令行运行后台                                                                                      
项目打包
```shell script
./mvnw -DskipTests clean package
```

运行Jar包
```shell script
java -jar target/adminsystem-dev.jar
```

### IDE运行后台
等待IDE下载所有Maven依赖，然后直接在IDE中右键`Application`类，点击run运行。

### 运行前端
frontend目录下执行`npm run dev`，其他命令参考前端部分[README](https://github.com/marwincn/AdminSystem/tree/master/frontend#frontend)

## 四、更新计划
* ~~网站的实时浏览记录记录在redis中，增加了运行项目的环境要求，需要删除对redis的依赖~~

* 后台public接口重构成RESTful风格接口

* Spring Security过于复杂，而且没有适配Restful接口，后续重构成JWT认证 + 自定义Filter鉴权

* 因为之前项目是有前台的，所以管理页没有提供新建记录的接口，现在需要实现这部分功能

* 用户密码暂时使用明文存储，后续要加密加盐存储

* 异常处理机制需要调整：原来异常依然返回HTTP 200，自定义响应体结构中体现错误信息。需要改为正常响应返回结果，异常由Service抛到Control层再由上层转换成具体的HTTP错误响应码

* 增加日志
