package com.lostfound.api.model.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

@Entity
@Table(name = "claim_records")
public class ClaimRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "item_name", length = 100)
    private String itemName;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claimer_id")
    private User claimer;

    @Column(name = "claimer_name", length = 50)
    private String claimerName;

    @Column(name = "claim_reason", columnDefinition = "TEXT")
    private String claimReason;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "claim_time")
    private LocalDateTime claimTime;

    @Column(name = "process_time")
    private LocalDateTime processTime;

    @Column(name = "contact_info", length = 200)
    private String contactInfo;

    @Column(name = "evidence_urls", columnDefinition = "TEXT")
    private String evidenceUrls;

    @Column(name = "review_note", columnDefinition = "TEXT")
    private String reviewNote;

    @Column(name = "reject_reason", length = 500)
    private String rejectReason;

    public ClaimRecord() {
        this.status = "PENDING";
        this.claimTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public User getClaimer() { return claimer; }
    public void setClaimer(User claimer) { this.claimer = claimer; }

    public String getClaimerName() { return claimerName; }
    public void setClaimerName(String claimerName) { this.claimerName = claimerName; }

    public String getClaimReason() { return claimReason; }
    public void setClaimReason(String claimReason) { this.claimReason = claimReason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getClaimTime() { return claimTime; }
    public void setClaimTime(LocalDateTime claimTime) { this.claimTime = claimTime; }

    public LocalDateTime getProcessTime() { return processTime; }
    public void setProcessTime(LocalDateTime processTime) { this.processTime = processTime; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public String getEvidenceUrls() { return evidenceUrls; }
    public void setEvidenceUrls(String evidenceUrls) { this.evidenceUrls = evidenceUrls; }

    public String getReviewNote() { return reviewNote; }
    public void setReviewNote(String reviewNote) { this.reviewNote = reviewNote; }

    public String getRejectReason() { return rejectReason; }
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }
}
