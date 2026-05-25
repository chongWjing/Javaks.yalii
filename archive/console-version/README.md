# 失物招领管理系统 - 项目说明

## 项目状态 ✓ 已配置完成

### 数据库配置
- **数据库**: MySQL (localhost:3306)
- **数据库名**: lost_found_db
- **用户名**: root
- **密码**: 123456
- **状态**: 已创建并初始化完成

### 项目结构
```
src/
├── com/lostfound/
│   ├── LostFoundApp.java          # 主入口类
│   ├── api/
│   │   ├── DatabaseCallback.java   # 数据库回调接口
│   │   ├── ItemMatcher.java       # 物品匹配接口
│   │   ├── MatchResult.java       # 匹配结果类
│   │   └── OperationListener.java # 操作监听器接口
│   ├── callback/
│   │   ├── LoggingCallback.java   # 日志回调实现
│   │   ├── NotificationCallback.java # 通知回调实现
│   │   └── StatisticsCallback.java  # 统计回调实现
│   ├── db/
│   │   ├── Database.java          # 数据库连接管理（单例）
│   │   └── DataManager.java       # 数据操作管理
│   ├── model/
│   │   ├── User.java              # 用户基类
│   │   ├── RegularUser.java       # 普通用户
│   │   ├── Admin.java             # 管理员
│   │   ├── Item.java              # 物品基类
│   │   ├── LostItem.java          # 遗失物品
│   │   ├── FoundItem.java         # 拾到物品
│   │   ├── ClaimRecord.java       # 认领记录
│   │   └── Notification.java      # 通知
│   ├── service/
│   │   ├── ItemMatchService.java  # 物品匹配服务
│   │   ├── SimpleItemMatcher.java # 简单匹配策略
│   │   └── AdvancedItemMatcher.java # 高级匹配策略
│   └── ui/
│       └── MainUI.java            # 控制台UI界面
```

### 数据库表结构
1. **users** - 用户表
   - id, username, password, phone, email, role, create_time, post_count, manage_count

2. **items** - 物品表
   - id, name, description, category, location, time, publisher_id, publisher_name, status, item_type, lost_time, reward, found_time, status_description

3. **claim_records** - 认领记录表
   - id, item_id, item_name, claimer_id, claimer_name, claim_reason, status, claim_time, process_time

4. **notifications** - 通知表
   - id, user_id, title, content, type, is_read, create_time

### 功能特性
- ✓ 继承: User -> RegularUser/Admin, Item -> LostItem/FoundItem
- ✓ 多态: 重写父类方法，运行时动态绑定
- ✓ 抽象类: User, Item 定义抽象方法
- ✓ 接口回调: DatabaseCallback, OperationListener
- ✓ 设计模式: 策略模式(ItemMatcher), 单例模式(Database), 观察者模式(OperationListener)
- ✓ 面向接口编程: ItemMatcher接口的多种实现

### 如何运行
1. **初始化数据库（已执行）**
   ```bash
   java -cp ".;lib/mysql-connector-java-8.0.33.jar" InitDB
   ```

2. **编译项目（已执行）**
   ```bash
   find src -name "*.java" | xargs javac -encoding UTF-8 -cp "lib/mysql-connector-java-8.0.33.jar" -d out
   ```

3. **运行应用**
   ```bash
   java -cp "out;lib/mysql-connector-java-8.0.33.jar" com.lostfound.LostFoundApp
   ```
   或双击 `运行.bat` 文件

### 应用功能
- **用户系统**: 注册、登录、角色管理（普通用户/管理员）
- **物品管理**: 发布遗失物品、发布拾到物品
- **智能匹配**: 使用策略模式进行物品匹配
- **认领流程**: 提交认领申请、审核处理
- **通知系统**: 通知消息管理
- **数据管理**: 查询、统计、管理功能

### 依赖项
- MySQL Connector/J 8.0.33
- MySQL Server (需要本地运行)
- Java 8+

### 调试信息
- 数据库连接: ✓ 成功
- 表初始化: ✓ 成功（users, items, claim_records, notifications）
- 编译: ✓ 成功
- 运行: ✓ 成功启动（支持交互式输入）

## 注意事项
1. 确保MySQL服务正在运行在端口3306
2. 控制台应用需要从控制台输入，直接双击.bat文件运行效果最佳
3. 应用支持H2内存数据库作为备选方案（如果MySQL不可用）
