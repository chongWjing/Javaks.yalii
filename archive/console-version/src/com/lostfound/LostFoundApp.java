package com.lostfound;

import com.lostfound.ui.MainUI;
import com.lostfound.db.Database;

/**
 * 失物招领管理系统 - 主入口类
 *
 * 项目特点：
 * 1. 继承：User -> RegularUser/Admin, Item -> LostItem/FoundItem
 * 2. 多态：重写父类方法，运行时动态绑定
 * 3. 抽象类：User, Item 定义抽象方法
 * 4. 接口回调：DatabaseCallback, OperationListener
 * 5. 设计模式：策略模式(ItemMatcher), 单例模式(Database), 观察者模式(OperationListener)
 * 6. 面向接口编程：ItemMatcher接口的多种实现
 */
public class LostFoundApp {

    public static void main(String[] args) {
        System.out.println("正在初始化失物招领管理系统...");

        // 初始化数据库
        Database.getInstance();

        // 创建并启动主界面
        MainUI mainUI = new MainUI();
        mainUI.start();

        // 退出时关闭数据库
        Database.getInstance().close();
    }
}
