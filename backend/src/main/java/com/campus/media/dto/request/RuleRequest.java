package com.campus.media.dto.request;

import jakarta.validation.constraints.NotBlank;

public class RuleRequest {
    
    @NotBlank(message = "规则名称不能为空")
    private String name;
    
    private String type = "keyword";
    
    @NotBlank(message = "规则条件不能为空")
    private String condition;
    
    private String action = "reject";
    
    private String description;
    
    private Boolean status = true;
    
    public RuleRequest() {
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getCondition() {
        return condition;
    }
    
    public void setCondition(String condition) {
        this.condition = condition;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getStatus() {
        return status;
    }
    
    public void setStatus(Boolean status) {
        this.status = status;
    }
}