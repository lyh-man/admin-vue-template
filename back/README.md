项目说明：
+ 此项目为 后台管理项目模板 -- 后台代码

项目特点：
+ 引入 Swagger 生成接口文档
+ 引入 MyBatis-plus 进行持久层操作
+ 引入 JSR 303 进行数据校验
+ 自定义国际化处理操作


博客地址：

[SpringBoot + Vue + ElementUI 实现后台管理系统模板 -- 后端篇（一）： 搭建基本环境、整合 Swagger、MyBatisPlus、JSR303 以及国际化操作](https://www.cnblogs.com/l-y-h/p/13083375.html)

[SpringBoot + Vue + ElementUI 实现后台管理系统模板 -- 后端篇（二）： 整合 Redis（常用工具类、缓存）、整合邮件发送功能](https://www.cnblogs.com/l-y-h/p/13163653.html)

[SpringBoot + Vue + ElementUI 实现后台管理系统模板 -- 后端篇（三）： 整合阿里云 OSS 服务 -- 上传、下载文件、图片](https://www.cnblogs.com/l-y-h/p/13202746.html)

[SpringBoot + Vue + ElementUI 实现后台管理系统模板 -- 后端篇（四）： 整合阿里云 短信服务、整合 JWT 单点登录](https://www.cnblogs.com/l-y-h/p/13214493.html)

[SpringBoot + Vue + ElementUI 实现后台管理系统模板 -- 后端篇（五）： 数据表设计、使用 jwt、redis、sms 工具类完善注册登录逻辑](https://www.cnblogs.com/l-y-h/p/13264307.html)

代码结构：
```javascript
back
|--- src
|   |--- main         保存源代码
|   |   |--- java                     代码目录
|   |   |   |--- common               保存公共操作
|   |   |   |   |--- config           保存配置类
|   |   |   |   |--- exception        保存异常处理操作
|   |   |   |   |--- utils            保存工具类
|   |   |   |   |--- validator        保存 JSR303 校验相关操作
|   |   |   |--- controller          保存控制层代码
|   |   |   |--- entity              保存实体类代码
|   |   |   |--- handler             保存数据处理相关操作
|   |   |   |--- mapper              保存 sql 相关映射操作
|   |   |   |--- service             保存业务层代码
|
|   |   |--- resources                 资源目录
|   |   |   |--- static                用于保存项目静态文件
|   |   |   |--- application.yml       用于保存项目的配置信息
|   |   |   |--- logback-spring.xml    用于保存日志的配置信息
|
|   |--- test         保存测试代码
|   |   |--- java
|
|--- pom.xml    用于保存项目依赖信息
```
