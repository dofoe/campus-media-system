package com.campus.media.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UploadCompleteRequest {
    
    @NotBlank(message = "上传ID不能为空")
    private String uploadId;
    
    private String thumbnailUrl;
    
    private String description;
    
    private String copyrightInfo;
    
    public UploadCompleteRequest() {
    }
    
    public String getUploadId() {
        return uploadId;
    }
    
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }
    
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
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
}