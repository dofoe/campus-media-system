package com.campus.media.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UploadCompleteRequest {
    
    @NotBlank(message = "上传ID不能为空")
    private String uploadId;
    
    private String thumbnailUrl;
    
    private String description;
    
    private String copyrightInfo;
}
