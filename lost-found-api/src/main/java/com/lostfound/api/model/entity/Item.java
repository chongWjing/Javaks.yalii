package com.lostfound.api.model.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lostfound.api.model.converter.StringListConverter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 50)
    private String category;

    @Column(length = 200)
    private String location;

    @Column
    private LocalDateTime time;

    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private User publisher;

    @Column(name = "publisher_name", length = 50)
    private String publisherName;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "item_type", length = 20)
    private String itemType;

    @Column(name = "lost_time", length = 100)
    private String lostTime;

    @Column
    private Double reward;

    @Column(name = "found_time", length = 100)
    private String foundTime;

    @Column(name = "status_description", length = 200)
    private String statusDescription;

    @Convert(converter = StringListConverter.class)
    @Column(name = "image_urls", columnDefinition = "TEXT")
    private List<String> imageUrls;

    public Item() {
        this.status = "ACTIVE";
        this.time = LocalDateTime.now();
        this.reward = 0.0;
        this.imageUrls = new ArrayList<>();
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }

    public User getPublisher() { return publisher; }
    public void setPublisher(User publisher) { this.publisher = publisher; }

    public String getPublisherName() { return publisherName; }
    public void setPublisherName(String publisherName) { this.publisherName = publisherName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }

    public String getLostTime() { return lostTime; }
    public void setLostTime(String lostTime) { this.lostTime = lostTime; }

    public Double getReward() { return reward; }
    public void setReward(Double reward) { this.reward = reward; }

    public String getFoundTime() { return foundTime; }
    public void setFoundTime(String foundTime) { this.foundTime = foundTime; }

    public String getStatusDescription() { return statusDescription; }
    public void setStatusDescription(String statusDescription) { this.statusDescription = statusDescription; }

    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
}
