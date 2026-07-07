package com.campus.media.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditRule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "type", length = 20)
    private String type = "keyword";
    
    @Column(name = "condition", nullable = false, columnDefinition = "TEXT")
    private String condition;
    
    @Column(name = "action", length = 20)
    private String action = "reject";
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "status")
    private Boolean status = true;
    
    @Column(name = "created_by")
    private Long createdBy;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", insertable = false, updatable = false)
    private User createdByUser;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
