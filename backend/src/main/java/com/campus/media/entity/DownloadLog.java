package com.campus.media.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "download_logs")
public class DownloadLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "media_id", nullable = false)
    private Long mediaId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id", insertable = false, updatable = false)
    private Media media;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    
    @Column(name = "download_time")
    private LocalDateTime downloadTime = LocalDateTime.now();
    
    @Column(name = "client_ip", length = 50)
    private String clientIp;
    
    @Column(name = "user_agent", columnDefinition = "VARCHAR(4000)")
    private String userAgent;
    
    public DownloadLog() {
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getMediaId() {
        return mediaId;
    }
    
    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }
    
    public Media getMedia() {
        return media;
    }
    
    public void setMedia(Media media) {
        this.media = media;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public LocalDateTime getDownloadTime() {
        return downloadTime;
    }
    
    public void setDownloadTime(LocalDateTime downloadTime) {
        this.downloadTime = downloadTime;
    }
    
    public String getClientIp() {
        return clientIp;
    }
    
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}