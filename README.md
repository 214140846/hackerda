hackerda
======

![](https://img.shields.io/badge/language-Java-orange.svg)
![](https://img.shields.io/badge/mother_fucker-v0.0.0-519dd9.svg)
![](https://img.shields.io/badge/license-MIT-000000.svg)
[![使用IntelliJ IDEA开发维护](https://img.shields.io/badge/IntelliJ%20IDEA-提供支持-blue.svg)](https://www.jetbrains.com/idea/)

#### 背景

为了给黑龙江科技大学的同学提供更加便捷的教务服务，我们基于Spring Boot框架使用maven构建了该项目。

领域驱动设计这个概念对我影响非常深，这个项目的设计思想也是基于领域驱动的(DDD)，用于业余时间对领域驱动设计认知的实践。同时该项目目前也在为黑科大的同学提供在线的服务。


#### 项目结构

该项目主要分为两部分，爬虫部分和web服务部分。

爬虫部分负责模拟教务网抓取的逻辑的，包括登录，验证码下载和预测，登录信息持久化以及数据清洗，向上次web服务提供接口。

web部分目前教务功能包括，成绩，课表，考试时间，空教室，学生个人信息查询。

除教务功能以外，还构建了一套独立的用户体系。基于这个用户体系和学生的身份信息，提供了论坛功能功能。包括发帖评论等。



![Image text](https://raw.githubusercontent.com/JR--Chen/hkxj/master/picture/framework.jpg)


开源许可证 License MIT
---

