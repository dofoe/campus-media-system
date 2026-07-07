package com.campus.media.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserUpdateRequest {
    
    @NotBlank(message = "姓名不能为空")
    private String name;
    
    private Long deptId;
    
    @NotNull(message = "角色ID不能为空")
    private Long roleId;
    
    private String email;
    
    private String phone;
    
    private String dataScope;
    
    private Boolean status;
}
