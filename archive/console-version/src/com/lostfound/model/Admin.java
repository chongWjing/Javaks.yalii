package com.lostfound.model;

import java.time.LocalDateTime;

/**
 * 管理员类 - 继承User抽象类，展示继承和多态
 */
public class Admin extends User {

    private int manageCount;

    public Admin(String username, String password, String phone, String email) {
        super(username, password, phone, email);
        this.manageCount = 0;
    }

    public Admin(int id, String username, String password, String phone,
                 String email, LocalDateTime createTime, int postCount, int manageCount) {
        super(id, username, password, phone, email, createTime, postCount);
        this.manageCount = manageCount;
    }

    @Override
    public String getRole() {
        return "管理员";
    }

    @Override
    public boolean canDeleteItem(int itemId) {
        return true; // 管理员可以删除任何物品
    }

    @Override
    public String getPermissionDescription() {
        return "管理所有物品信息，审核认领申请，管理用户";
    }

    // 管理员特有方法 - 重写父类方法体现多态
    @Override
    public void incrementPostCount() {
        super.incrementPostCount();
        this.manageCount++;
    }

    public int getManageCount() {
        return manageCount;
    }

    @Override
    public String toString() {
        return String.format("Admin{id=%d, name='%s', phone='%s', posts=%d, managed=%d}",
                id, username, phone, postCount, manageCount);
    }
}
