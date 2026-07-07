package com.campus.media.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RuleRequest {
    
    @NotBlank(message = "规则名称不能为空")
    private String name;
    
    private String type = "keyword";
    
    @NotBlank(message = "规则条件不能为空")
    private String condition;
    
    private String action = "reject";
    
    private String description;
    
    private Boolean status = true;
}
