package com.campus.media.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "media_tags", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"media_id", "tag_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaTag {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "media_id", nullable = false)
    private Long mediaId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id", insertable = false, updatable = false)
    private Media media;
    
    @Column(name = "tag_id", nullable = false)
    private Long tagId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", insertable = false, updatable = false)
    private Tag tag;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
