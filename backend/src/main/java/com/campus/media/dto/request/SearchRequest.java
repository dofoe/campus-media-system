package com.campus.media.dto.request;

import lombok.Data;

@Data
public class SearchRequest {
    
    private String keyword;
    
    private String category;
    
    private Long categoryId;
    
    private String fileType;
    
    private String tag;
    
    private Integer pageNum = 1;
    
    private Integer pageSize = 20;
}
