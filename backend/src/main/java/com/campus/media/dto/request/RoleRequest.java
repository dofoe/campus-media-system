package com.campus.media.dto.request;

import jakarta.validation.constraints.NotBlank;

public class RoleRequest {
    
    @NotBlank(message = "角色名称不能为空")
    private String name;
    
    @NotBlank(message = "角色编码不能为空")
    private String code;
    
    private String description;
    
    private String dataScope = "self";
    
    private Boolean status = true;
    
    public RoleRequest() {
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDataScope() {
        return dataScope;
    }
    
    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }
    
    public Boolean getStatus() {
        return status;
    }
    
    public void setStatus(Boolean status) {
        this.status = status;
    }
}