package com.lostfound.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 用户抽象类 - 展示抽象类的使用
 * 定义所有用户的公共属性和抽象方法，子类必须实现具体行为
 */
public abstract class User {
    protected int id;
    protected String username;
    protected String password;
    protected String phone;
    protected String email;
    protected LocalDateTime createTime;
    protected int postCount;

    public User(String username, String password, String phone, String email) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.createTime = LocalDateTime.now();
        this.postCount = 0;
    }

    public User(int id, String username, String password, String phone, String email,
                LocalDateTime createTime, int postCount) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.createTime = createTime;
        this.postCount = postCount;
    }

    // 抽象方法 - 子类必须实现（展示抽象类）
    public abstract String getRole();
    public abstract boolean canDeleteItem(int itemId);
    public abstract String getPermissionDescription();

    // 普通方法 - 子类可选择性重写（展示多态）
    public void incrementPostCount() {
        this.postCount++;
    }

    public boolean verifyPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public String getFormattedCreateTime() {
        return createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String toString() {
        return String.format("User{id=%d, name='%s', role='%s', phone='%s'}",
                id, username, getRole(), phone);
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDateTime getCreateTime() { return createTime; }
    public int getPostCount() { return postCount; }
}
