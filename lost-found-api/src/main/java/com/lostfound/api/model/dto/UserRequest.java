package com.lostfound.api.model.dto;

import jakarta.validation.constraints.Size;

public class UserRequest {
    @Size(min = 6, max = 100, message = "密码长度必须在6-100之间")
    private String password;

    private String phone;
    private String email;
    private String role;

    public UserRequest() {}

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
