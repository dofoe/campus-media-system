package com.campus.media.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "media")
public class Media {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "file_name", nullable = false, length = 500)
    private String fileName;
    
    @Column(name = "file_path", nullable = false, length = 1000)
    private String filePath;
    
    @Column(name = "file_size", nullable = false)
    private Long fileSize;
    
    @Column(name = "file_type", nullable = false, length = 50)
    private String fileType;
    
    @Column(name = "thumbnail_url", length = 1000)
    private String thumbnailUrl;
    
    @Column(name = "category_id")
    private Long categoryId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;
    
    @Column(name = "storage_path", nullable = false, length = 1000)
    private String storagePath;
    
    @Column(name = "md5_hash", nullable = false, length = 32)
    private String md5Hash;
    
    @Column(name = "ai_tags", columnDefinition = "TEXT")
    private String aiTags = "[]";
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "copyright_info", columnDefinition = "TEXT")
    private String copyrightInfo;
    
    @Column(name = "usage_count")
    private Integer usageCount = 0;
    
    @Column(name = "download_count")
    private Integer downloadCount = 0;
    
    @Column(name = "status", length = 20)
    private String status = "pending";
    
    @Column(name = "audit_status", length = 20)
    private String auditStatus = "pending";
    
    @Column(name = "audit_comment", columnDefinition = "TEXT")
    private String auditComment;
    
    @Column(name = "audit_user_id")
    private Long auditUserId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audit_user_id", insertable = false, updatable = false)
    private User auditUser;
    
    @Column(name = "audit_time")
    private LocalDateTime auditTime;
    
    @Column(name = "upload_user_id", nullable = false)
    private Long uploadUserId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upload_user_id", insertable = false, updatable = false)
    private User uploadUser;
    
    @Column(name = "upload_time")
    private LocalDateTime uploadTime = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    public Media() {
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getFileType() {
        return fileType;
    }
    
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
    
    public Long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public String getStoragePath() {
        return storagePath;
    }
    
    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }
    
    public String getMd5Hash() {
        return md5Hash;
    }
    
    public void setMd5Hash(String md5Hash) {
        this.md5Hash = md5Hash;
    }
    
    public String getAiTags() {
        return aiTags;
    }
    
    public void setAiTags(String aiTags) {
        this.aiTags = aiTags;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCopyrightInfo() {
        return copyrightInfo;
    }
    
    public void setCopyrightInfo(String copyrightInfo) {
        this.copyrightInfo = copyrightInfo;
    }
    
    public Integer getUsageCount() {
        return usageCount;
    }
    
    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }
    
    public Integer getDownloadCount() {
        return downloadCount;
    }
    
    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getAuditStatus() {
        return auditStatus;
    }
    
    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }
    
    public String getAuditComment() {
        return auditComment;
    }
    
    public void setAuditComment(String auditComment) {
        this.auditComment = auditComment;
    }
    
    public Long getAuditUserId() {
        return auditUserId;
    }
    
    public void setAuditUserId(Long auditUserId) {
        this.auditUserId = auditUserId;
    }
    
    public User getAuditUser() {
        return auditUser;
    }
    
    public void setAuditUser(User auditUser) {
        this.auditUser = auditUser;
    }
    
    public LocalDateTime getAuditTime() {
        return auditTime;
    }
    
    public void setAuditTime(LocalDateTime auditTime) {
        this.auditTime = auditTime;
    }
    
    public Long getUploadUserId() {
        return uploadUserId;
    }
    
    public void setUploadUserId(Long uploadUserId) {
        this.uploadUserId = uploadUserId;
    }
    
    public User getUploadUser() {
        return uploadUser;
    }
    
    public void setUploadUser(User uploadUser) {
        this.uploadUser = uploadUser;
    }
    
    public LocalDateTime getUploadTime() {
        return uploadTime;
    }
    
    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}