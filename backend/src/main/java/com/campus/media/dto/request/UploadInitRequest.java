package com.campus.media.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UploadInitRequest {
    
    @NotBlank(message = "文件名不能为空")
    private String fileName;
    
    @NotNull(message = "文件大小不能为空")
    private Long fileSize;
    
    @NotBlank(message = "文件类型不能为空")
    private String fileType;
    
    @NotBlank(message = "MD5哈希值不能为空")
    private String md5Hash;
    
    private Long categoryId;
    
    private String description;
    
    public UploadInitRequest() {
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
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
    
    public String getMd5Hash() {
        return md5Hash;
    }
    
    public void setMd5Hash(String md5Hash) {
        this.md5Hash = md5Hash;
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
}