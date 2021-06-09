#### 项目模块说明

```text
galaxy
├── galaxy-ui -- 前端部分
├── galaxy-web -- web服务部分
├── galaxy-eureka -- 注册中心
├── galaxy-config -- 配置中心
├── galaxy-gateway -- Spring Cloud Gateway网关
├── galaxy-common -- 系统基础模块
├── galaxy-metadata -- 元数据管理模块
├── galaxy-scheduler -- 工作流调度模块
├── galaxy-dqc -- 数据质量中心模块
├── galaxy-monitor -- 监控模块

```

#### 基础技术栈
|名称|版本|备注|
|---|---|---|
|SpringBoot|2.2.6|使用Web,Rest服务,解耦合等|
|SpringCloud|2.2.6|注册中心功能,配置中心,网关服务|
|SpringSecurity|2.2.6|权限验证|
|SpringOAuth|2.0|权限验证|
|Redis|5.x|缓存|
|Mysql|5.7.24|数据存储|
|ElasticSearch|7.x|搜索服务|

#### 中间件选型
|名称|版本|备注|
|---|---|---|
|Apache Calcite|1.1.9|sql解析|
|Apache Griffin|0.7.0|质量中心管理|
|Prometheus||指标监控|
|Grafana||监控指标展示|
|Kafka|2.4.0|集成ksql, 实时计算的消息总线|

#### 基本设计规范
##### 数据库设计规范

https://developer.aliyun.com/article/709387

##### 缓存设计规范

https://juejin.cn/post/6844903665845665805