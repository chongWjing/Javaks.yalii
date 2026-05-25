# 失物招领管理系统 - Vue + Spring Boot 版

## 架构概述

- **后端**: Spring Boot REST API (端口 8080)
- **前端**: Vue.js 3 + Element Plus (端口 3000)
- **数据库**: MySQL (localhost:3306)

## 启动指南

### 1. 启动后端

```bash
cd lost-found-api

# 如果还没有安装 Maven，需要先安装
# 然后编译并运行
mvn clean install
mvn spring-boot:run
```

后端将在 http://localhost:8080 启动

### 2. 启动前端

```bash
cd lost-found-frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端将在 http://localhost:3000 启动

### 3. 测试流程

1. 访问 http://localhost:3000
2. 注册新用户
3. 登录系统
4. 发布失物/招领信息
5. 浏览物品列表
6. 认领物品
7. 管理员认领审批

## 项目结构

### 后端 (Spring Boot)
```
lost-found-api/
├── src/main/java/com/lostfound/api/
│   ├── controller/    (REST API 控制器)
│   ├── service/       (业务逻辑层)
│   ├── repository/    (数据访问层)
│   ├── model/         (实体类和 DTO)
│   ├── security/      (JWT 认证)
│   └── config/        (配置类)
└── src/main/resources/
    └── application.yml
```

### 前端 (Vue.js)
```
lost-found-frontend/
├── src/
│   ├── views/         (页面组件)
│   ├── components/    (可复用组件)
│   ├── router/        (路由配置)
│   ├── store/         (Pinia 状态管理)
│   ├── services/      (API 服务)
│   └── utils/         (工具函数)
└── vite.config.js
```

## API 端点

### 认证
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `GET /api/auth/me` - 获取当前用户

### 物品
- `GET /api/items` - 获取物品列表
- `POST /api/items` - 发布物品
- `GET /api/items/:id` - 获取物品详情
- `PUT /api/items/:id` - 更新物品
- `DELETE /api/items/:id` - 删除物品

### 认领
- `POST /api/claims` - 创建认领
- `GET /api/claims` - 获取认领列表
- `PUT /api/claims/:id/approve` - 批准认领
- `PUT /api/claims/:id/reject` - 拒绝认领

### 通知
- `GET /api/notifications/user/:userId` - 获取通知
- `PUT /api/notifications/:id/read` - 标记已读

## 技术栈

**后端:**
- Spring Boot 3.2
- Spring Data JPA
- Spring Security + JWT
- MySQL Connector/J 8.0

**前端:**
- Vue.js 3.4
- Vue Router 4
- Pinia 2
- Element Plus 2.4
- Axios 1.6

## 数据库配置

MySQL 连接信息:
- 主机: localhost
- 端口: 3306
- 数据库: lost_found_db
- 用户名: root
- 密码: 123456

## 功能特性

- [x] 用户注册和登录
- [x] JWT Token 认证
- [x] 发布失物/招领信息
- [x] 浏览物品列表（支持过滤和搜索）
- [x] 物品详情查看
- [x] 认领物品
- [x] 管理员认领审批
- [x] 通知消息管理
- [x] 响应式界面设计
- [x] 权限控制（普通用户/管理员）

## 常见问题

### Q: 启动失败怎么办？

1. 检查 MySQL 是否运行在端口 3306
2. 检查数据库连接信息是否正确
3. 查看日志文件定位问题

### Q: 如何创建管理员用户？

注册时选择 "管理员" 角色，或者直接修改数据库中用户角色字段为 "ADMIN"

### Q: 前端无法连接后端？

1. 确认后端已启动在端口 8080
2. 检查浏览器控制台是否有 CORS 错误
3. 检查网络请求是否正确

## 开发建议

- 后端 API 返回统一格式: `{ success: boolean, data: object, message: string }`
- 使用 JWT Token 进行用户认证
- 所有敏感操作需要验证用户身份
- 前端使用 Pinia 进行状态管理
- 使用 Element Plus 组件库构建界面

## 下一步改进

- [ ] 添加数据库迁移脚本
- [ ] 实现图片上传功能
- [ ] 添加搜索和分页优化
- [ ] 实现邮件通知
- [ ] 添加单元测试
- [ ] 优化性能和安全性
