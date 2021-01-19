## 快速开发平台（微服务架构版本）
这是一个快速开发的脚手架，内置了许多通用的功能，可节约项目开发30%-70%的时间。   
配套的后台管理界面项目请见：[通用后台管理](https://github.com/QQ794763733/common-backend)  
作者集成了许多常见的应用开发的框架，编写了一些常用的模块与接口及实现类。    
例如RBAC权限管理、分布式认证中心、Oauth2.0、WebSocket、图形验证码、代码生成器……  
**单体架构版本请见：[单体架构版本](https://github.com/QQ794763733/machine-geek)**
## 配套前端界面预览
如果图片加载不出来是缓存的问题，请谅解。
![预览图1](https://store.machine-geek.cn/0042.png)
![预览图2](https://store.machine-geek.cn/0043.png)
## 环境需要
这是作者的开发环境，请参考再选择自己的版本。
1. `MySQL`8
2. `Redis`
3. `Maven`
4. `JDK` 1.8
5. `Zipkin`
6. `Nacos`
7. `Sentinel-Dashboard`
8. `RabbitMQ`
9. `SeataServer`
## Docker快速启动
```terminal
// Docker创建自定义桥接网络mynet
docker network create --driver bridge --subnet 172.100.0.0/24 --gateway 172.100.0.1 mynet

// 启动MySQL容器（无数据卷）
docker run -d --name dev-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 --net mynet --ip 172.100.0.2 --restart=always mysql:latest

// 启动Redis容器
docker run -d --name dev-redis -p 6379:6379 --net mynet --ip 172.100.0.3 --restart=always redis:latest --requirepass 123456

// 启动nacos容器（独立mysql版本，需要在mysql中运行好nacos官方提供的DDL语句）
docker run -d --name dev-nacos -e MODE=standalone -e MYSQL_SERVICE_DB_NAME=nacos -e MYSQL_SERVICE_USER=root -e MYSQL_SERVICE_PASSWORD=123456 -e SPRING_DATASOURCE_PLATFORM=mysql -e MYSQL_SERVICE_HOST=172.100.0.2 -e MYSQL_DATABASE_NUM=1 -p 8848:8848 --net mynet --ip 172.100.0.4 --restart=always nacos/nacos-server:latest

// 启动Sentinel容器
docker run -d --name dev-sentinel -p 8858:8858 -p 8719:8719 --net mynet --ip 172.100.0.5 --restart=always bladex/sentinel-dashboard:latest

// 启动zipkin容器
docker run -d --name dev-zipkin -p 9411:9411 --net mynet --ip 172.100.0.6 --restart=always --restart=always openzipkin/zipkin:latest
```
## RSA生成
```terminal
// 生成私钥
keytool -genkeypair -alias MachineGeek -keyalg RSA -keypass MachineGeek -keystore MachineGeek.jks -validity 365 -storepass MachineGeek

// 根据私钥生成公钥
keytool -list -rfc --keystore MachineGeek.jks | openssl x509 -inform pem -pubkey
```
## 特性
1. 项目结构清晰的分层，每个模块目标明确，代码注释详细，可以清晰的明白每个模块和类的作用。
2. 集成了常见的应用开发的框架，加入了许多的常见配置，并注册到了`IOC`容器中，使用时直接`@Autowired`即可。
3. 提供了常见的Service接口与实现类，如文件上传、图形验证码、邮件发送……等，无需再次开发。
4. `分布式认证中心`：已经编写好的认证中心、`RBAC`已经集成、只需要加相应的注解即可，集成`OAuth2.0`做开放平台。
5. `Alibaba`一站式的微服务解决方案集成并封装成了模块。
6. `WebSocket`集成，提供了接口可以注入轻松发送，搭配了`Redis`的订阅发布，集群状态下也无需担心连接发送不到的问题。
7. `代码生成器`可以一键生成`实体类`、`映射类`、`xml文件`、`服务接口`、`服务实现类`、`控制器`、`前端数据表页面`、`API文件`等等，使用的模板引擎，可以自定义自己的代码生成工具。
## 说明
`Spring Cloud Alibaba Nacos`：采用Nacos作为服务注册中心、配置中心、消息总线。  
`Spring Cloud OpenFeign、Spring Cloud Alibaba Dubbo`:采用OpenFeign、Dubbo作为服务调用。  
`Spring Cloud Alibaba Sentinel`: 采用Sentinel对服务做`限流`、`熔断`、`降级`。  
`Spring Cloud Zipkin`: 采用Zipkin作为服务调用的链路追踪。  
`Spring Cloud Alibaba Seata`: 使用Seata做分布式事务管理。  
`Spring Cloud Stream`: 使用Stream做消息驱动。  
`Spring Cloud Gateway`: 使用Gateway做微服务网关。
## 项目结构
* `micro-service-api`:这个模块是用来放置一些通用的依赖实体、接口。
* `micro-service-auth`:这个模块是分布式的认证中心，包含Oauth2.0以及RBAC的权限控制。
* `micro-service-framework`:这个模块是用来封装通用依赖以及一些常用的框架配置的。  
  * `micro-service-core`:这个模块包含了常用的Web开发的依赖与配置，包含Web Starter、Nacos、Sentinel、OpenFeign、Dubbo、zipkin平时开发服务只需要依赖这个模块即可。
  * `micro-service-data`:这个模块包含了常用的数据开发的依赖与配置，包含Redis、RabbitMQ、MySQL、操作数据依赖这个模块即可。
* `micro-service-gateway`:这个模块是Gateway网关。
* `micro-service-instance`:这个模块是具体的业务服务实例。  
  * `……`:业务代码的模块请在这里添加。

## 关于作者
喜欢的话就**Star**一下吧！  
也可以逛逛[作者博客](http://blog.machine-geek.cn/)  
和其他的项目[作者Github](https://github.com/QQ794763733)

**如果这个项目有帮助到您就请作者喝一杯咖啡吧！**

<img src="https://store.machine-geek.cn/0012.jpg" width="20%"/>