package com.campus.media.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
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
}
