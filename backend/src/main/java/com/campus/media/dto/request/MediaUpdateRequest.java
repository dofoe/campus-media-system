package com.campus.media.dto.request;

import lombok.Data;

@Data
public class MediaUpdateRequest {
    
    private String fileName;
    
    private Long categoryId;
    
    private String description;
    
    private String copyrightInfo;
    
    private String status;
}
