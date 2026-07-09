package com.campus.media.dto.request;

public class MediaUpdateRequest {
    
    private String fileName;
    
    private Long categoryId;
    
    private String description;
    
    private String copyrightInfo;
    
    private String status;
    
    public MediaUpdateRequest() {
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public Long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}