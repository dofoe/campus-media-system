package com.campus.media.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "media")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
