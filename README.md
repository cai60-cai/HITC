## 💡 简介

工大圈子是一个专为工大校友和在校生打造的社交平台。随着社交网络的兴起，人们需要一个专属的平台来交流、分享和互动，而工大圈子应运而生。该平台旨在为工大人提供一个便捷的交流平台，让校友之间能够保持联系、共享资源、展示成果，让在校学生能够更好地了解校友的经验和资源。



##  ✨ 功能

##### 普通用户

- 用户可进行注册和登录，新用户实行邮箱绑定制，一个邮箱最多绑定三个用户

- 游客形式只能浏览帖子

- 用户可以在圈子中发布贴子，同时可以上传附件

- 用户可评论收藏点赞和分享好友的帖子，并可以下载文件

- 用户中心可修改查看个人资料、修改登录密码、修改头像、查询登录日志、个性化设置等等

- 用户可向管理员举报帖子、反馈系统相关bug等功能

- 帖子搜索和分类功能，用户可以基于标题搜索感兴趣的帖子

- 热搜榜单功能，用户可以快速找到热门内容

- 用户可在聊天室添加其他人为好友，并且可以私信

- 使用了当下很火的markdown编辑器

- 使用Token进行身份验证，防止未授权的访问

##### 管理员权限

- 设置单个用户的权限,包括文件上传、发言评论、打赏、文章发布、申请友链、反馈、举报等权限,以便于精细化管理用户权限

- 查看活跃用户数量、新用户数量

- 对角色授权，权力下放，比如博客审核，评论审核

- 管理员可以禁用某些用户或者删除无效账户，维护系统秩序

- 重置用户登录密码,以便于帮助用户找回密码

- 管理员可以查看日志，以及设置标签栏、系统logo以及简介等等

- 处理用户的举报以及反馈

- 审核发布的帖子，并可进行推荐，审核通过方可发布，也可自行修改



##  ✨ 运行环境及技术介绍

**编程语言**：Java/JavaScript

**开发环境**：IDEA 2023.1

**运行环境**：Node.js 18.18.0、MySQL 8.3.0、Redis

**主要技术**：

​	•**服务端**：SpringBoot框架搭建，Mybatis—Plus做持久层框架

​	•**数据库**：MySQL 8.3.1

​	•**前端**：Vue2.x、Element-UI、axios、echarts，管理系统使用vue-element-admin

​	•**构建工具**：Maven 3.8.1

## ✨ 部署教程

1、进入cxs-currency-sys-client、chat-app、cxs-currency-sys-client-admin目录

打开cmd执行如下命令，等待依赖下载完

~~~sh
npm install
~~~

2、连接数据库

依次执行config目录下的两个SQL文件，先执行init.sql，在执行cxs_currency_sys.sql

3、在sys-controller模块下找到application-dev.yml配置文件，修改以下部分

1)file-upload位置配置，修改为upload的全路径

2)修改数据库连接信息

3)配置邮件发送，邮箱自行申请配置，暂时使用的是163邮箱

4)修改redis连接配置

4、启动redis服务端

5、启动服务端项目

进入sys-contorller目录下运行CurrencySysApplication

进入chat-app目录下运行ChatAppApplication

6、启动前端项目

进入sys-client\cxs-currency-sys-client目录打开cmd，执行如下命令

~~~sh
npm run serve
~~~

进入sys-client\cxs-currency-sys-client-admin目录打开cmd，执行如下命令

~~~sh
npm run dev
~~~

进入chat-app\chat-app-frontend目录打开cmd，执行如下命令

~~~sh
npm run serve
~~~

前台系统访问：http://127.0.0.1:3000/index

管理系统访问：http://127.0.0.1:9527/

## ✨ 提供账号

超级管理员：admin/123456

用户：user/1234567890!

























