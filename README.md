# 失物招领管理系统

一个基于 Spring Boot + Vue 3 的全栈失物招领管理系统，支持物品发布、认领、举报、实时通知等功能。

## 功能特性

### 用户功能
- 用户注册/登录（JWT 认证）
- 发布失物/招领信息（支持图片上传）
- 搜索筛选（关键词、类型、分类、时间范围）
- 在线认领（填写联系方式、证据材料）
- 站内通知（WebSocket 实时推送）
- 举报不当信息

### 管理员功能
- 认领审核（批准/拒绝，填写审核备注）
- 用户管理（编辑/删除用户）
- 举报处理（处理/驳回举报）
- 统计报表（热门地点、月度趋势、平均找回时长）

## 技术栈

### 后端
- Java 17
- Spring Boot 3.2.0
- Spring Security + JWT
- Spring Data JPA + Hibernate
- WebSocket（实时通知）
- MySQL 8.0
- Maven

### 前端
- Vue 3（Composition API）
- Vue Router 4
- Pinia（状态管理）
- Element Plus（UI 组件库）
- Axios（HTTP 请求）
- Vite（构建工具）

## 项目结构

```
├── lost-found-api/                 # Spring Boot 后端
│   ├── src/main/java/com/lostfound/api/
│   │   ├── config/                 # 配置类（CORS、Security、WebSocket）
│   │   ├── controller/             # REST 控制器
│   │   ├── exception/              # 全局异常处理
│   │   ├── model/
│   │   │   ├── dto/                # 数据传输对象
│   │   │   └── entity/             # 实体类
│   │   ├── repository/             # JPA Repository
│   │   ├── security/               # JWT 认证
│   │   └── service/                # 业务逻辑
│   └── src/main/resources/
│       ├── application.yml         # 应用配置
│       ├── schema.sql              # 数据库初始化脚本
│       └── db/migration/           # 数据库迁移脚本
│
├── lost-found-frontend/            # Vue 3 前端
│   ├── src/
│   │   ├── components/             # 公共组件
│   │   ├── router/                 # 路由配置
│   │   ├── services/               # API 服务
│   │   ├── store/                  # Pinia 状态管理
│   │   └── views/                  # 页面视图
│   ├── package.json
│   └── vite.config.js
│
└── archive/                        # 归档的控制台版本
```

## 快速开始

### 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Node.js 18+

### 数据库配置
```sql
CREATE DATABASE javaks DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 后端启动
```bash
cd lost-found-api
mvn spring-boot:run
```

后端将在 http://localhost:8080 启动

### 前端启动
```bash
cd lost-found-frontend
npm install
npm run dev
```

前端将在 http://localhost:3000 启动

### 测试账号
| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | 123456 | 管理员 |
| 张三 | 123456 | 普通用户 |
| 李四 | 123456 | 普通用户 |

## API 接口

### 认证
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `GET /api/auth/me` - 获取当前用户

### 物品
- `GET /api/items` - 获取物品列表（支持筛选）
- `GET /api/items/paged` - 分页获取物品
- `GET /api/items/{id}` - 获取物品详情
- `POST /api/items` - 发布物品
- `PUT /api/items/{id}` - 更新物品
- `DELETE /api/items/{id}` - 删除物品

### 认领
- `GET /api/claims` - 获取认领列表
- `POST /api/claims` - 提交认领申请
- `PUT /api/claims/{id}/approve` - 批准认领
- `PUT /api/claims/{id}/reject` - 拒绝认领
- `DELETE /api/claims/{id}` - 撤回认领

### 通知
- `GET /api/notifications/user/{userId}` - 获取用户通知
- `PUT /api/notifications/{id}/read` - 标记已读
- `PUT /api/notifications/user/{userId}/read-all` - 全部标记已读

### 举报
- `POST /api/reports` - 提交举报
- `GET /api/reports` - 获取举报列表
- `PUT /api/reports/{id}/resolve` - 处理举报
- `PUT /api/reports/{id}/dismiss` - 驳回举报

### 统计
- `GET /api/stats` - 系统概览
- `GET /api/items/stats/top-locations` - 热门地点
- `GET /api/items/stats/monthly-trend` - 月度趋势
- `GET /api/items/stats/avg-recovery` - 平均找回时长

## 数据库表结构

| 表名 | 说明 |
|------|------|
| users | 用户表 |
| items | 物品表 |
| claim_records | 认领记录表 |
| notifications | 通知表 |
| reports | 举报表 |

## 许可证

MIT License
