package com.lostfound.api.model.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @JsonIgnore
    @Column(nullable = false, length = 100)
    private String password;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String role;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "post_count")
    private Integer postCount;

    @Column(name = "manage_count")
    private Integer manageCount;

    @JsonIgnore
    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> publishedItems;

    @JsonIgnore
    @OneToMany(mappedBy = "claimer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ClaimRecord> claimRecords;

    public User() {
        this.postCount = 0;
        this.manageCount = 0;
        this.role = "REGULAR";
        this.createTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public Integer getPostCount() { return postCount; }
    public void setPostCount(Integer postCount) { this.postCount = postCount; }

    public Integer getManageCount() { return manageCount; }
    public void setManageCount(Integer manageCount) { this.manageCount = manageCount; }

    public List<Item> getPublishedItems() { return publishedItems; }
    public void setPublishedItems(List<Item> publishedItems) { this.publishedItems = publishedItems; }

    public List<ClaimRecord> getClaimRecords() { return claimRecords; }
    public void setClaimRecords(List<ClaimRecord> claimRecords) { this.claimRecords = claimRecords; }

    public boolean isAdmin() {
        return "ADMIN".equals(this.role);
    }
}
