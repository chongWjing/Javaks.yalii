package com.lostfound.model;

import java.time.LocalDateTime;

/**
 * 普通用户类 - 继承User抽象类，展示继承和多态
 */
public class RegularUser extends User {

    public RegularUser(String username, String password, String phone, String email) {
        super(username, password, phone, email);
    }

    public RegularUser(int id, String username, String password, String phone,
                       String email, LocalDateTime createTime, int postCount) {
        super(id, username, password, phone, email, createTime, postCount);
    }

    @Override
    public String getRole() {
        return "普通用户";
    }

    @Override
    public boolean canDeleteItem(int itemId) {
        return false;
    }

    @Override
    public String getPermissionDescription() {
        return "发布和查询失物招领信息，认领物品";
    }

    @Override
    public String toString() {
        return String.format("RegularUser{id=%d, name='%s', phone='%s', posts=%d}",
                id, username, phone, postCount);
    }
}
